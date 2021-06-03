package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTrackingConstants
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductData
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.presentation.adapter.ImageReviewAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.ReviewTemplatesAdapter
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewTemplateListener
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.*
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoBottomSheetBuilder
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CreateReviewBottomSheet : BottomSheetUnify(), IncentiveOvoListener, TextAreaListener,
        ImageClickListener, ReviewTemplateListener {

    companion object {
        const val GOOD_RATING_THRESHOLD = 3
        fun createInstance(rating: Int, productId: Long, reputationId: Long, utmSource: String): CreateReviewBottomSheet {
            return CreateReviewBottomSheet().apply {
                this.rating = rating
                this.productId = productId
                this.reputationId = reputationId
                this.utmSource = utmSource
            }
        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel

    // View Elements
    private var reviewFormCoordinatorLayout: View? = null
    private var productCard: CreateReviewProductCard? = null
    private var ratingStars: AnimatedRatingPickerCreateReviewView? = null
    private var incentivesTicker: Ticker? = null
    private var textAreaTitle: Typography? = null
    private var textArea: CreateReviewTextArea? = null
    private var incentivesHelperText: Typography? = null
    private var templatesRecyclerView: RecyclerView? = null
    private var addPhoto: CreateReviewAddPhoto? = null
    private var photosRecyclerView: RecyclerView? = null
    private var textAreaBottomSheet: CreateReviewTextAreaBottomSheet? = null
    private var anonymousOption: CreateReviewAnonymousOption? = null
    private var progressBar: CreateReviewProgressBar? = null
    private var submitButton: UnifyButton? = null
    private var loadingView: View? = null
    private var ovoIncentiveBottomSheet: BottomSheetUnify? = null
    private var thankYouBottomSheet: BottomSheetUnify? = null

    private var rating: Int = 0
    private var productId: Long = 0L
    private var reputationId: Long = 0L
    private var feedbackId: Long = 0L
    private var isEditMode: Boolean = false
    private var utmSource: String = ""
    private var isReviewIncomplete = false
    private var incentiveHelper = ""
    private var templatesSelectedCount = 0
    private var shouldShowThankYouBottomSheet = false

    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this)
    }

    private val templatesAdapter: ReviewTemplatesAdapter by lazy {
        ReviewTemplatesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_create_review, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        setRatingClickListener()
        setAddPhotoOnClickListener()
        setSubmitButtonOnClickListener()
        setTextAreaListener()
        setDismissBehavior()
        setAnonymousOptionClickListener()
        setPaddings()
        setRatingInitialState()
        setOnTouchListenerToHideKeyboard()
        setOnTouchOutsideListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CreateReviewFragment.REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = ImagePickerResultExtractor.extract(data)
                    val selectedImage = result.imageUrlOrPathList
                    val imagesFedIntoPicker = result.imagesFedIntoPicker
                    createReviewViewModel.clearImageData()

                    CreateReviewTracking.reviewOnImageUploadTracker(
                            getOrderId(),
                            productId.toString(10),
                            true,
                            selectedImage.size.toString(10),
                            isEditMode,
                            feedbackId.toString()
                    )

                    if (!selectedImage.isNullOrEmpty()) {
                        val imageListData = createReviewViewModel.getAfterEditImageList(selectedImage, imagesFedIntoPicker)
                        imageAdapter.setImageReviewData(imageListData)
                        photosRecyclerView?.apply {
                            adapter = imageAdapter
                            show()
                        }
                        addPhoto?.hide()
                        createReviewViewModel.updateProgressBarFromPhotos()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onUrlClicked(url: String): Boolean {
        context?.let {
            return ReviewUtil.routeToWebview(it, ovoIncentiveBottomSheet, url)
        }
        return false
    }

    override fun onClickCloseThankYouBottomSheet() {
        finishIfRoot(true)
    }

    override fun onClickReviewAnother() {
        dismiss()
        goToReviewPending()
    }

    override fun onExpandButtonClicked(text: String) {
        CreateReviewTracking.onExpandTextBoxClicked(getOrderId(), productId.toString())
        if(isUserEligible()) {
            if (incentiveHelper.isBlank()) incentiveHelper = context?.getString(R.string.review_create_text_area_eligible) ?: ""
        }
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(this, text, incentiveHelper, isUserEligible(), getTemplatesForTextArea())
        (textAreaBottomSheet as BottomSheetUnify).setTitle(textAreaTitle?.text.toString())
        fragmentManager?.let { textAreaBottomSheet?.show(it, "") }
    }

    override fun onCollapseButtonClicked(text: String) {
        CreateReviewTracking.onCollapseTextBoxClicked(getOrderId(), productId.toString())
        textAreaBottomSheet?.dismiss()
    }

    override fun onDismissBottomSheet(text: String) {
        textArea?.setText(text)
        clearFocusAndHideSoftInput(view)
    }

    override fun scrollToShowTextArea() {
        // No Op
    }

    override fun trackWhenHasFocus(textLength: Int) {
        CreateReviewTracking.reviewOnMessageChangedTracker(getOrderId(), productId.toString(), textLength == 0, isEditMode, feedbackId.toString())
        setHelperText(textLength)
    }

    override fun onTextChanged(textLength: Int) {
        setHelperText(textLength)
        val isNotEmpty = textLength != 0
        updateButtonState(isGoodRating(), isNotEmpty)
        createReviewViewModel.updateProgressBarFromTextArea(isNotEmpty)
    }

    override fun hideText() {
        incentivesHelperText?.hide()
    }

    override fun onAddImageClick() {
        clearFocusAndHideSoftInput(view)
        goToImagePicker()
    }

    override fun onRemoveImageClick(item: BaseImageReviewUiModel) {
        imageAdapter.setImageReviewData(createReviewViewModel.removeImage(item, isEditMode))
        if (imageAdapter.isEmpty()) {
            photosRecyclerView?.hide()
            addPhoto?.show()
        }
        createReviewViewModel.updateProgressBarFromPhotos()
    }

    override fun onTemplateSelected(template: String) {
        templatesSelectedCount++
        textArea?.append(context?.getString(R.string.review_form_templates_formatting, template)
                ?: template)
        CreateReviewTracking.eventClickReviewTemplate(template, getReputationId(), getOrderId(), productId.toString(), createReviewViewModel.getUserId())
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                handleDismiss()
                true
            } else false
        }
    }

    private fun initInjector() {
        activity?.let {
            DaggerCreateReviewComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    private fun bindViews() {
        reviewFormCoordinatorLayout = view?.findViewById(R.id.review_form_coordinator_layout)
        productCard = view?.findViewById(R.id.review_form_product_card)
        ratingStars = view?.findViewById(R.id.review_form_rating)
        incentivesTicker = view?.findViewById(R.id.review_form_incentives_ticker)
        addPhoto = view?.findViewById(R.id.review_form_add_photo)
        photosRecyclerView = view?.findViewById(R.id.review_form_photos_rv)
        textAreaTitle = view?.findViewById(R.id.review_form_text_area_title)
        textArea = view?.findViewById(R.id.review_form_text_area)
        incentivesHelperText = view?.findViewById(R.id.review_form_incentive_helper_text)
        templatesRecyclerView = view?.findViewById(R.id.review_form_templates_rv)
        anonymousOption = view?.findViewById(R.id.review_form_anonymous_option)
        progressBar = view?.findViewById(R.id.review_form_progress_bar_widget)
        submitButton = view?.findViewById(R.id.review_form_submit_button)
        loadingView = view?.findViewById(R.id.shimmering_create_review_form)
    }

    private fun setRatingClickListener() {
        ratingStars?.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                super.onClick(position)
                trackRatingChanged(position)
                updateTitleBasedOnSelectedRating(position)
                val isGoodRating = isGoodRating()
                updateButtonState(isGoodRating, textArea?.isEmpty()?.not() ?: false)
                createReviewViewModel.updateProgressBarFromRating(isGoodRating)
                if (isGoodRating && !isUserEligible()) {
                    showTemplates()
                } else {
                    hideTemplates()
                }
                clearFocusAndHideSoftInput(view)
            }
        })
    }

    private fun setRatingInitialState() {
        ratingStars?.apply {
            resetStars()
            renderInitialReviewWithData(rating)
        }
        updateTitleBasedOnSelectedRating(rating)
        updateButtonState(isGoodRating(), textArea?.isEmpty()?.not() ?: false)
        createReviewViewModel.updateProgressBarFromRating(isGoodRating())
    }

    private fun getForm() {
        createReviewViewModel.getProductReputation(productId, reputationId)
    }

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo(productId, reputationId)
    }

    private fun getTemplates() {
        createReviewViewModel.getReviewTemplates(productId)
    }

    private fun observeGetForm() {
        createReviewViewModel.getReputationDataForm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetReviewForm(it.data)
                is Fail -> onErrorGetReviewForm(it.throwable)
            }
        })
    }

    private fun observeIncentive() {
        createReviewViewModel.incentiveOvo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetOvoIncentive(it.data)
                is Fail -> onFailGetOvoIncentive(it.throwable)
            }
        })
    }

    private fun observeTemplates() {
        createReviewViewModel.reviewTemplates.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetTemplate(it.data)
                is Fail -> onFailGetTemplate(it.throwable)
            }
        })
    }

    private fun observeSubmitReview() {
        createReviewViewModel.submitReviewResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is com.tokopedia.review.common.data.Success -> {
                    onSuccessSubmitReview()
                }
                is com.tokopedia.review.common.data.Fail -> {
                    onFailSubmitReview(it.fail)
                }
                is com.tokopedia.review.common.data.LoadingView -> {
                    showButtonLoading()
                }
            }
        })
    }

    private fun observeButtonState() {
        createReviewViewModel.submitButtonState.observe(viewLifecycleOwner, Observer {
            submitButton?.isEnabled = it
        })
    }

    private fun observeProgressBarState() {
        createReviewViewModel.progressBarState.observe(viewLifecycleOwner, Observer {
            progressBar?.setProgressBarValue(it)
        })
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        with(data.productrevGetForm) {
            when {
                !validToReview -> {
                    finishIfRoot(false, getString(R.string.review_pending_invalid_to_review))
                    return
                }
                productData.productStatus == 0 -> {
                    finishIfRoot(false, getString(R.string.review_pending_deleted_product_error_toaster))
                    return
                }
            }
            hideLoading()
            updateProductId(productData.productID)
            setProductDetail(productData)
            CreateReviewTracking.reviewOnViewTracker(orderID, productId.toString())
        }
    }

    private fun onSuccessGetOvoIncentive(ovoDomain: ProductRevIncentiveOvoDomain?) {
        ovoDomain?.productrevIncentiveOvo?.let {
            if (shouldShowThankYouBottomSheet) {
                showThankYouBottomSheet(ovoDomain)
                return
            }
            if (ovoIncentiveBottomSheet == null) {
                ovoIncentiveBottomSheet = context?.let { context -> IncentiveOvoBottomSheetBuilder.getTermsAndConditionsBottomSheet(context, ovoDomain, this, "", getTncBottomSheetTrackerData()) }
            }
            hideTemplates()
            incentivesTicker?.apply {
                setHtmlDescription(it.subtitle)
                setOnClickListener { _ ->
                    ovoIncentiveBottomSheet?.let { bottomSheet ->
                        activity?.supportFragmentManager?.let { supportFragmentManager -> bottomSheet.show(supportFragmentManager, bottomSheet.tag) }
                        CreateReviewTracking.eventClickIncentivesTicker(it.subtitle, getReputationId(), getOrderId(), productId.toString(), getUserId())
                    }
                }
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        CreateReviewTracking.eventViewIncentivesTicker(it.subtitle, getReputationId(), getOrderId(), productId.toString(), getUserId())
                        incentivesTicker?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
                show()
            }
            return
        }
        showTemplates()
        incentivesTicker?.hide()
    }

    private fun onFailGetOvoIncentive(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessSubmitReview() {
        stopButtonLoading()
        if (isUserEligible() && !isReviewIncomplete) {
            shouldShowThankYouBottomSheet = true
            getIncentiveOvoData()
            return
        }
        activity?.setResult(Activity.RESULT_OK)
        dismiss()
    }

    private fun onFailSubmitReview(throwable: Throwable) {
        stopButtonLoading()
        showToasterError(throwable.message ?: getString(R.string.review_create_fail_toaster))
        logToCrashlytics(throwable)
    }

    private fun onSuccessGetTemplate(templates: List<String>) {
        templatesRecyclerView?.apply {
            adapter = templatesAdapter
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL)
            viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if(templates.isNotEmpty()) {
                        CreateReviewTracking.eventViewReviewTemplate(templates.size, productId.toString(), getUserId())
                    }
                    templatesRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener (this)
                }
            })
        }
        templatesAdapter.setData(templates)
    }

    private fun onFailGetTemplate(throwable: Throwable) {
        logToCrashlytics(throwable)
        hideTemplates()
    }

    private fun setProductDetail(data: ProductData) {
        productCard?.apply {
            setProduct(data)
        }
    }

    private fun setAddPhotoOnClickListener() {
        addPhoto?.setOnClickListener {
            goToImagePicker()
        }
    }

    private fun setSubmitButtonOnClickListener() {
        submitButton?.setOnClickListener {
            CreateReviewTracking.eventClickSubmitForm(getRating(), getReviewMessageLength(), getNumberOfPictures(), isAnonymous(), isUserEligible(), isTemplateAvailable(), templatesSelectedCount, getOrderId(), productId.toString(), getUserId())
            if (!isReviewComplete() && isUserEligible()) {
                showReviewIncompleteDialog()
            } else {
                submitNewReview()
            }
        }
    }

    private fun setTextAreaListener() {
        textArea?.setListener(this)
    }

    private fun goToImagePicker() {
        context?.let {
            val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .withSimpleEditor()
                    .withSimpleMultipleSelection(initialImagePathList = createReviewViewModel.getSelectedImagesUrl())
                    .apply {
                        title = getString(R.string.image_picker_title)
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            startActivityForResult(intent, CreateReviewFragment.REQUEST_CODE_IMAGE)
        }
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {
        logToCrashlytics(throwable)
        finishIfRoot(false, throwable.message ?: getString(R.string.review_toaster_page_error))
    }

    private fun updateTitleBasedOnSelectedRating(position: Int) {
        when (position) {
            CreateReviewFragment.RATING_1 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_worst_title)
                textArea?.setPlaceHolder(resources.getString(R.string.review_form_worst_helper))
            }
            CreateReviewFragment.RATING_2 -> {
                textAreaTitle?.text = resources.getString(R.string.review_form_bad_title)

                textArea?.setPlaceHolder(resources.getString(R.string.review_form_bad_helper))
            }
            CreateReviewFragment.RATING_3 -> {
                textAreaTitle?.text = resources.getString(R.string.review_form_neutral_title)
                textArea?.setPlaceHolder(resources.getString(R.string.review_form_neutral_helper))
            }
            else -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_best_title)
                textArea?.setPlaceHolder(resources.getString(R.string.review_form_good_helper))
            }
        }
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

    private fun getOrderId(): String {
        return (createReviewViewModel.getReputationDataForm.value as? Success<ProductRevGetForm>)?.data?.productrevGetForm?.orderID
                ?: ""
    }

    private fun clearFocusAndHideSoftInput(view: View?) {
        textArea?.clearFocus()
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun submitNewReview() {
        val reviewMessage = textArea?.getText() ?: ""
        createReviewViewModel.submitReview(getRating(), reviewMessage, 0, isAnonymous(), utmSource)
    }

    private fun isGoodRating(): Boolean {
        return ratingStars?.clickAt ?: 0 > GOOD_RATING_THRESHOLD
    }

    private fun setDismissBehavior() {
        setOnDismissListener {
            CreateReviewTracking.eventDismissForm(getRating(), getReviewMessageLength(), getNumberOfPictures(), isAnonymous(), isUserEligible(), isTemplateAvailable(), templatesSelectedCount, getOrderId(), productId.toString(), getUserId())
            activity?.finish()
        }
    }

    private fun setPaddings() {
        bottomSheetTitle.hide()
    }

    private fun isReviewComplete(): Boolean {
        val reviewMessage = textArea?.getText() ?: ""
        return (reviewMessage.length >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && ratingStars?.getReviewClickAt() != 0 && createReviewViewModel.isImageNotEmpty())
    }

    private fun showReviewIncompleteDialog() {
        val title = getString(R.string.review_create_incomplete_title)
        showDialog(title, getString(R.string.review_form_incentives_incomplete_dialog_body), getString(R.string.review_create_incomplete_cancel), {
            CreateReviewTracking.eventClickCompleteReviewFirst(title)
        }, getString(R.string.review_create_incomplete_send_anyways), {
            isReviewIncomplete = true
            submitNewReview()
            CreateReviewTracking.eventClickSendNow(title)
        })
        CreateReviewTracking.eventViewDialog(title)
    }

    private fun showSendRatingOnlyDialog() {
        val title = getString(R.string.review_form_send_rating_only_dialog_title)
        showDialog(title, getString(R.string.review_form_send_rating_only_body), getString(R.string.review_form_send_rating_only_exit), { dismiss() }, getString(R.string.review_form_send_rating_only), {
            submitNewReview()
            CreateReviewTracking.eventClickDialogOption(CreateReviewDialogType.CreateReviewSendRatingOnlyDialog, title, getReputationId(), getOrderId(), productId.toString(), getUserId())
        })
        CreateReviewTracking.eventViewDialog(CreateReviewDialogType.CreateReviewSendRatingOnlyDialog, title, getReputationId(), getOrderId(), productId.toString(), getUserId())
    }

    private fun showReviewUnsavedWarningDialog() {
        val title = getString(R.string.review_form_dismiss_form_dialog_title)
        showDialog(title, getString(R.string.review_form_dismiss_form_dialog_body), getString(R.string.review_edit_dialog_exit), { dismiss() }, getString(R.string.review_form_dismiss_form_dialog_stay), {
            CreateReviewTracking.eventClickDialogOption(CreateReviewDialogType.CreateReviewUnsavedDialog, title, getReputationId(), getOrderId(), productId.toString(), getUserId())
        })
        CreateReviewTracking.eventViewDialog(CreateReviewDialogType.CreateReviewUnsavedDialog, title, getReputationId(), getOrderId(), productId.toString(), getUserId())
    }

    private fun showIncentivesExitWarningDialog() {
        showDialog(getString(R.string.review_form_incentives_exit_dialog_title), getString(R.string.review_form_incentives_exit_dialog_body), getString(R.string.review_edit_dialog_exit), { dismiss() }, getString(R.string.review_form_dismiss_form_dialog_stay), {})
    }

    private fun showDialog(title: String, description: String, primaryCtaText: String, primaryCtaAction: () -> Unit, secondaryCtaText: String, secondaryCtaAction: () -> Unit) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setPrimaryCTAClickListener {
                    this.dismiss()
                    primaryCtaAction.invoke()
                }
                setSecondaryCTAText(secondaryCtaText)
                setSecondaryCTAClickListener {
                    this.dismiss()
                    secondaryCtaAction.invoke()
                }
                show()
            }
        }
    }

    private fun updateProductId(productId: Long) {
        this.productId = productId
    }

    private fun showTemplates() {
        templatesRecyclerView?.show()
    }

    private fun hideTemplates() {
        templatesRecyclerView?.hide()
    }

    private fun setAnonymousOptionClickListener() {
        anonymousOption?.setOnClickListener {
            if (anonymousOption?.isChecked() == true) {
                CreateReviewTracking.reviewOnAnonymousClickTracker(getOrderId(), productId.toString(10), isEditMode, feedbackId.toString())
            }
        }
    }

    private fun trackRatingChanged(position: Int) {
        CreateReviewTracking.reviewOnRatingChangedTracker(
                getOrderId(),
                productId.toString(10),
                (position).toString(10),
                true,
                isEditMode,
                feedbackId.toString()
        )
    }

    private fun updateButtonState(isGoodRating: Boolean, isTextAreaNotEmpty: Boolean) {
        if (isGoodRating) {
            createReviewViewModel.updateButtonState(isGoodRating)
        } else {
            createReviewViewModel.updateButtonState(isTextAreaNotEmpty)
        }
    }

    private fun setOnTouchOutsideListener() {
        setShowListener {
            CreateReviewTracking.openScreenWithCustomDimens(CreateReviewTrackingConstants.SCREEN_NAME, productId.toString())
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            isCancelable = false
            dialog?.setCanceledOnTouchOutside(false)
            this.dialog?.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)?.setOnClickListener {
                handleDismiss()
            }
            observeLiveDatas()
            getData()
        }
        setCloseClickListener {
            handleDismiss()
        }
    }

    private fun handleDismiss() {
        if (isUserEligible()) {
            showIncentivesExitWarningDialog()
            return
        }
        if (isGoodRating() && textArea?.isEmpty() == true) {
            showSendRatingOnlyDialog()
            return
        }
        if(textArea?.isEmpty() == false || createReviewViewModel.isImageNotEmpty()) {
            showReviewUnsavedWarningDialog()
            return
        }
        dismiss()
    }

    private fun finishIfRoot(success: Boolean = false, errorMessage: String = "", feedbackId: String = "") {
        activity?.run {
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
                startActivity(intent)
            } else {
                val intent = Intent()
                if (success) {
                    intent.putExtra(ReputationCommonConstants.ARGS_FEEDBACK_ID, feedbackId)
                    intent.putExtra(ReputationCommonConstants.ARGS_RATING, rating)
                    intent.putExtra(ReputationCommonConstants.ARGS_PRODUCT_ID, productId)
                    intent.putExtra(ReputationCommonConstants.ARGS_REPUTATION_ID, reputationId)
                    intent.putExtra(ReputationCommonConstants.ARGS_REVIEW_STATE, ReputationCommonConstants.REVIEWED)
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    intent.putExtra(ReviewInboxConstants.CREATE_REVIEW_ERROR_MESSAGE, errorMessage)
                    intent.putExtra(ReputationCommonConstants.ARGS_REVIEW_STATE, ReputationCommonConstants.INVALID_TO_REVIEW)
                    setResult(Activity.RESULT_FIRST_USER, intent)
                }
            }
            dismiss()
            finish()
        }
    }

    private fun hideLoading() {
        loadingView?.hide()
    }

    private fun setHelperText(textLength: Int) {
        if (!isUserEligible()) {
            return
        }
        incentivesHelperText?.apply {
            incentiveHelper = when {
                textLength >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                    context?.getString(R.string.review_create_text_area_eligible) ?: ""
                }
                textLength < CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && textLength != 0 -> {
                    context?.getString(R.string.review_create_text_area_partial) ?: ""
                }
                else -> {
                    context?.getString(R.string.review_create_text_area_empty) ?: ""
                }
            }
            text = incentiveHelper
            show()
        }
    }

    private fun showButtonLoading() {
        submitButton?.apply {
            isLoading = true
            setOnClickListener(null)
        }
    }

    private fun stopButtonLoading() {
        submitButton?.apply {
            isLoading = false
            setSubmitButtonOnClickListener()
        }
    }

    private fun showToasterError(message: String) {
        reviewFormCoordinatorLayout?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR, getString(R.string.review_oke)).show()
        }
    }

    private fun getReputationId(): String {
        return (createReviewViewModel.getReputationDataForm.value as? Success)?.data?.productrevGetForm?.reputationID.toString()
    }

    private fun getRating(): Int {
        return ratingStars?.clickAt ?: 5
    }

    private fun isAnonymous(): Boolean {
        return anonymousOption?.isChecked() ?: false
    }

    private fun getReviewMessageLength(): Int {
        return textArea?.getText()?.length ?: 0
    }

    private fun getNumberOfPictures(): Int {
        return createReviewViewModel.getImageCount()
    }

    private fun isUserEligible(): Boolean {
        return createReviewViewModel.isUserEligible()
    }

    private fun isTemplateAvailable(): Boolean {
        return createReviewViewModel.isTemplateAvailable()
    }

    private fun getUserId(): String {
        return createReviewViewModel.getUserId()
    }

    private fun observeLiveDatas() {
        observeGetForm()
        observeIncentive()
        observeSubmitReview()
        observeTemplates()
        observeButtonState()
        observeProgressBarState()
    }

    private fun getData() {
        getForm()
        getIncentiveOvoData()
        getTemplates()
    }

    private fun setOnTouchListenerToHideKeyboard() {
        templatesRecyclerView?.setCustomTouchListener()
        anonymousOption?.setCustomTouchListener()
        photosRecyclerView?.setCustomTouchListener()
        incentivesTicker?.setCustomTouchListener()
        textAreaTitle?.setCustomTouchListener()
    }

    private fun View.setCustomTouchListener() {
        this.setOnTouchListener { v, event ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }
    }

    private fun getTemplatesForTextArea(): List<String> {
        if(isGoodRating()) {
            return (createReviewViewModel.reviewTemplates.value as? Success)?.data ?: listOf()
        }
        return listOf()
    }

    private fun showThankYouBottomSheet(data: ProductRevIncentiveOvoDomain?) {
        if (thankYouBottomSheet == null) {
            thankYouBottomSheet = context?.let { IncentiveOvoBottomSheetBuilder.getThankYouBottomSheet(it, data, this, getThankYouBottomSheetTrackerData()) }
        }
        thankYouBottomSheet?.let { bottomSheet ->
            activity?.supportFragmentManager?.let { bottomSheet.show(it, bottomSheet.tag) }
        }
        shouldShowThankYouBottomSheet = false
    }

    private fun goToReviewPending() {
        RouteManager.route(context, ApplinkConst.REPUTATION)
        activity?.finish()
    }

    private fun getThankYouBottomSheetTrackerData(): ThankYouBottomSheetTrackerData {
        return ThankYouBottomSheetTrackerData(getReputationId(), getOrderId(), productId.toString(), getUserId(), getFeedbackId())
    }

    private fun getFeedbackId(): String {
        return (createReviewViewModel.submitReviewResult.value as? com.tokopedia.review.common.data.Success)?.data ?: ""
    }

    private fun getTncBottomSheetTrackerData(): TncBottomSheetTrackerData {
        return TncBottomSheetTrackerData(getReputationId(), getOrderId(), productId.toString(), getUserId())
    }
}