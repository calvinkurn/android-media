package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
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
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.*
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoBottomSheetBuilder
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CreateReviewBottomSheet : BottomSheetUnify(), IncentiveOvoListener, TextAreaListener,
        ImageClickListener, ReviewTemplateListener {

    companion object {
        fun createInstance(rating: Int, productId: Long, reputationId: Long, feedbackId: Long, utmSource: String, isEditMode: Boolean): CreateReviewBottomSheet {
            return CreateReviewBottomSheet().apply {
                this.rating = rating
                this.productId = productId
                this.reputationId = reputationId
                this.feedbackId = feedbackId
                this.utmSource = utmSource
                this.isEditMode = isEditMode
            }
        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel

    // View Elements
    private var productCard: CreateReviewProductCard? = null
    private var ratingStars: AnimatedRatingPickerCreateReviewView? = null
    private var incentivesTicker: Ticker? = null
    private var textAreaTitle: Typography? = null
    private var textArea: CreateReviewTextArea? = null
    private var templatesRecyclerView: RecyclerView? = null
    private var addPhoto: CreateReviewAddPhoto? = null
    private var photosRecyclerView: RecyclerView? = null
    private var textAreaBottomSheet: CreateReviewTextAreaBottomSheet? = null
    private var anonymousOption: CreateReviewAnonymousOption? = null
    private var progressBar: CreateReviewProgressBar? = null
    private var submitButton: UnifyButton? = null
    private var ovoIncentiveBottomSheet: BottomSheetUnify? = null

    private var rating: Int = 0
    private var productId: Long = 0L
    private var reputationId: Long = 0L
    private var feedbackId: Long = 0L
    private var isEditMode: Boolean = false
    private var utmSource: String = ""
    private var isReviewIncomplete = false

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
        if(isEditMode) {
            observeReviewDetail()
            getReviewDetailData()
        } else {
            observeGetForm()
            observeIncentive()
            getForm()
            getIncentiveOvoData()
        }
        observeTemplates()
        observeButtonState()
        observeProgressBarState()
        setRatingClickListener()
        setAddPhotoOnClickListener()
        setSubmitButtonOnClickListener()
        setTextAreaListener()
        setDismissBehavior()
        setPaddings()
        setRatingInitialState()
        getTemplates()
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
                        photosRecyclerView?.show()
                        addPhoto?.hide()
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
        dismiss()
    }

    override fun onClickReviewAnother() {
        dismiss()
    }

    override fun onExpandButtonClicked(text: String) {
        CreateReviewTracking.onExpandTextBoxClicked(getOrderId(), productId.toString())
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(this, text)
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
    }

    override fun onTextChanged(textLength: Int) {
        val isNotEmpty = textLength != 0
        createReviewViewModel.updateButtonState(isNotEmpty)
        createReviewViewModel.updateProgressBarFromTextArea(isNotEmpty)
    }

    override fun hideText() {
        // No Op
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
    }

    override fun onTemplateSelected(template: String) {
        textArea?.append(context?.getString(R.string.review_form_templates_formatting, template) ?: template)
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
        productCard = view?.findViewById(R.id.review_form_product_card)
        ratingStars = view?.findViewById(R.id.review_form_rating)
        incentivesTicker = view?.findViewById(R.id.review_form_incentives_ticker)
        addPhoto = view?.findViewById(R.id.review_form_add_photo)
        photosRecyclerView = view?.findViewById(R.id.review_form_photos_rv)
        textAreaTitle = view?.findViewById(R.id.review_form_text_area_title)
        textArea = view?.findViewById(R.id.review_form_text_area)
        templatesRecyclerView = view?.findViewById(R.id.review_form_templates_rv)
        anonymousOption = view?.findViewById(R.id.review_form_anonymous_option)
        progressBar = view?.findViewById(R.id.review_form_progress_bar_widget)
        submitButton = view?.findViewById(R.id.review_form_submit_button)
    }

    private fun setRatingClickListener() {
        ratingStars?.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                super.onClick(position)
                updateTitleBasedOnSelectedRating(position)
                val isGoodRating = isGoodRating()
                createReviewViewModel.updateButtonState(isGoodRating)
                createReviewViewModel.updateProgressBarFromRating(isGoodRating)
                if(isGoodRating) {
                    showTemplates()
                } else {
                    hideTemplates()
                }
            }
        })
    }

    private fun setRatingInitialState() {
        ratingStars?.apply {
            resetStars()
            renderInitialReviewWithData(rating)
        }
        updateTitleBasedOnSelectedRating(rating)
    }

    private fun getForm() {
        createReviewViewModel.getProductReputation(productId, reputationId)
    }

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo(productId, reputationId)
    }

    private fun getReviewDetailData() {
        createReviewViewModel.getReviewDetails(feedbackId)
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
            when(it) {
                is Success -> onSuccessGetTemplate(it.data)
                is Fail -> onFailGetTemplate(it.throwable)
            }
        })
    }

    private fun observeReviewDetail() {
        createReviewViewModel.reviewDetails.observe(viewLifecycleOwner, Observer {
            when(it) {
                is com.tokopedia.review.common.data.Success -> {

                }
                is com.tokopedia.review.common.data.Fail -> {

                }
                is com.tokopedia.review.common.data.LoadingView -> {

                }
            }
        })
    }

    private fun observeSubmitReview() {
        createReviewViewModel.submitReviewResult.observe(viewLifecycleOwner, Observer {

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
                    dismiss()
                    return
                }
                productData.productStatus == 0 -> {
                    dismiss()
                    return
                }
            }
            setProductDetail(productData)
            updateProductId(productData.productID)
        }
    }

    private fun onSuccessGetOvoIncentive(ovoDomain: ProductRevIncentiveOvoDomain?) {
        ovoDomain?.productrevIncentiveOvo?.let {
            if (ovoIncentiveBottomSheet == null) {
                ovoIncentiveBottomSheet = context?.let { context -> IncentiveOvoBottomSheetBuilder.getTermsAndConditionsBottomSheet(context, ovoDomain, this, "") }
            }
            incentivesTicker?.apply {
                setHtmlDescription(it.subtitle)
                setOnClickListener { _ ->
                    ovoIncentiveBottomSheet?.let { bottomSheet ->
                        activity?.supportFragmentManager?.let { supportFragmentManager -> bottomSheet.show(supportFragmentManager, bottomSheet.tag) }
                        ReviewTracking.onClickReadSkIncentiveOvoTracker(it.subtitle, "")
                    }
                }
                show()
            }
            return
        }
        incentivesTicker?.hide()
    }

    private fun onFailGetOvoIncentive(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessSubmitReview() {
        dismiss()
    }

    private fun onFailSubmitReview() {

    }

    private fun onSuccessGetTemplate(templates: List<String>) {
        templatesRecyclerView?.apply {
            adapter = templatesAdapter
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL)
            showTemplates()
        }
        templatesAdapter.setData(templates)
    }

    private fun onFailGetTemplate(throwable: Throwable) {
        logToCrashlytics(throwable)
        hideTemplates()
    }

    private fun setProductDetail(data: ProductData) {
        productCard?.setProduct(data)
    }

    private fun setProductOnClickListener() {
        productCard?.setOnClickListener {
            goToPdp()
        }
    }

    private fun setAddPhotoOnClickListener() {
        addPhoto?.setOnClickListener {
            goToImagePicker()
        }
    }

    private fun setSubmitButtonOnClickListener() {
        submitButton?.setOnClickListener {
            submitNewReview()
        }
    }

    private fun setTextAreaListener() {
        textArea?.setListener(this)
    }

    private fun goToPdp() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId.toString())
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
            dismiss()
            startActivityForResult(intent, CreateReviewFragment.REQUEST_CODE_IMAGE)
        }
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {

    }

    private fun updateTitleBasedOnSelectedRating(position: Int) {
        when {
            position < CreateReviewFragment.RATING_3 -> {
                if (position == CreateReviewFragment.RATING_1) {
                    textAreaTitle?.text = resources.getString(R.string.review_create_worst_title)
                } else {
                    textAreaTitle?.text = resources.getString(R.string.review_create_negative_title)
                }
            }
            position == CreateReviewFragment.RATING_3 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_neutral_title)
            }
            position == CreateReviewFragment.RATING_4 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_positive_title)
            }
            else -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_best_title)
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
        createReviewViewModel.submitReview(ratingStars?.getReviewClickAt()
                ?: 0, reviewMessage, anonymousOption?.isChecked() ?: false, utmSource)
    }

    private fun isGoodRating(): Boolean {
        return ratingStars?.clickAt ?: 0 > 3
    }

    private fun setDismissBehavior() {
        setOnDismissListener {
            activity?.finish()
        }
    }

    private fun setPaddings() {
        bottomSheetWrapper.setPadding(0, 16.toPx(), 0, 0)
    }

    private fun isReviewComplete(): Boolean {
        val reviewMessage = textArea?.getText() ?: ""
        return (reviewMessage.length >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && ratingStars?.getReviewClickAt() != 0 && createReviewViewModel.isImageNotEmpty())
    }

    private fun showReviewIncompleteDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title = getString(R.string.review_create_incomplete_title)
                setTitle(title)
                setDescription(getString(R.string.review_create_incomplete_subtitle))
                setPrimaryCTAText(getString(R.string.review_create_incomplete_cancel))
                setPrimaryCTAClickListener {
                    dismiss()
                    CreateReviewTracking.eventClickCompleteReviewFirst(title)
                }
                setSecondaryCTAText(getString(R.string.review_create_incomplete_send_anyways))
                setSecondaryCTAClickListener {
                    isReviewIncomplete = true
                    dismiss()
                    submitNewReview()
                    CreateReviewTracking.eventClickSendNow(title)
                }
                show()
                CreateReviewTracking.eventViewDialog(title)
            }
        }
    }

    private fun showSendRatingOnlyDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.review_form_send_rating_only_dialog_title))
                setDescription(getString(R.string.review_form_send_rating_only_body))
                setPrimaryCTAText(getString(R.string.review_form_send_rating_only_exit))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAText(getString(R.string.review_form_send_rating_only))
                setSecondaryCTAClickListener {
                    dismiss()
                    submitNewReview()
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
}