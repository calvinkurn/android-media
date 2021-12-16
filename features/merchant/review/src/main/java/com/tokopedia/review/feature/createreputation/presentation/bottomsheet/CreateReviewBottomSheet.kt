package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTrackingConstants
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductData
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.createreputation.presentation.adapter.ImageReviewAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.ReviewBadRatingCategoriesAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.ReviewTemplatesAdapter
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewBadRatingCategoryListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewTemplateListener
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewAddPhoto
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewAnonymousOption
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewProductCard
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewProgressBar
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextArea
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextAreaBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.widget.ReviewBadRatingItemDecoration
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoBottomSheetBuilder
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CreateReviewBottomSheet : BottomSheetUnify(), IncentiveOvoListener, TextAreaListener,
    ImageClickListener, ReviewTemplateListener, ReviewBadRatingCategoryListener {

    companion object {
        const val GOOD_RATING_THRESHOLD = 2
        const val CREATE_REVIEW_TEXT_AREA_BOTTOM_SHEET_TAG = "CreateReviewTextAreaBottomSheet"
        const val TEMPLATES_ROW_COUNT = 2
        const val BAD_RATING_OTHER_ID = "6"
        const val BAD_RATING_FLOW_EXPERIMENT_KEY = "Bad_ReviewForm_AB"
        const val OLD_FORM_VARIANT = "old_form"
        const val BAD_RATING_FORM_VARIANT = "bad_rating_form"

        fun createInstance(
            rating: Int,
            productId: String,
            reputationId: String,
            utmSource: String
        ): CreateReviewBottomSheet {
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

    @Inject
    lateinit var trackingQueue: TrackingQueue

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
    private var badRatingCategoryRecyclerView: RecyclerView? = null

    private var rating: Int = 0
    private var productId: String = ""
    private var reputationId: String = ""
    private var feedbackId: Long = 0L
    private var isEditMode: Boolean = false
    private var utmSource: String = ""
    private var isReviewIncomplete = false
    private var incentiveHelper = ""
    private var templatesSelectedCount = 0

    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this)
    }

    private val templatesAdapter: ReviewTemplatesAdapter by lazy {
        ReviewTemplatesAdapter(this)
    }

    private val badRatingCategoriesAdapter: ReviewBadRatingCategoriesAdapter by lazy {
        ReviewBadRatingCategoriesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, com.tokopedia.review.R.layout.bottomsheet_create_review, null)
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
        if (shouldShowBadRatingReasons()) setUpBadRatingCategoriesRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CreateReviewFragment.REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleOnActivityResult(data)
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
        finishIfRoot(
            success = true,
            message = getString(
                R.string.review_create_success_toaster,
                createReviewViewModel.getUserName()
            ),
            feedbackId = getFeedbackId()
        )
    }

    override fun onExpandButtonClicked(text: String) {
        CreateReviewTracking.onExpandTextBoxClicked(getOrderId(), productId)
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(
            this,
            text,
            incentiveHelper,
            hasIncentive(),
            hasOngoingChallenge(),
            getTemplatesForTextArea(),
            textArea?.getPlaceHolder() ?: ""
        )
        (textAreaBottomSheet as BottomSheetUnify).setTitle(textAreaTitle?.text.toString())
        fragmentManager?.let {
            textAreaBottomSheet?.show(
                it,
                CREATE_REVIEW_TEXT_AREA_BOTTOM_SHEET_TAG
            )
        }
    }

    override fun onCollapseButtonClicked(text: String) {
        CreateReviewTracking.onCollapseTextBoxClicked(getOrderId(), productId)
        textAreaBottomSheet?.dismiss()
    }

    override fun onDismissBottomSheet(text: String, templates: List<String>) {
        textArea?.setText(text)
        clearFocusAndHideSoftInput(view)
        if (isGoodRating()) {
            templatesAdapter.setData(templates)
        }
        if (templates.isEmpty()) {
            hideTemplates()
            return
        }
    }

    override fun scrollToShowTextArea() {
        // No Op
    }

    override fun trackWhenHasFocus(textLength: Int) {
        CreateReviewTracking.reviewOnMessageChangedTracker(
            getOrderId(),
            productId,
            textLength == 0,
            isEditMode,
            feedbackId.toString()
        )
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
        textArea?.append(
            context?.getString(R.string.review_form_templates_formatting, template)
                ?: template
        )
        CreateReviewTracking.eventClickReviewTemplate(
            template,
            getReputationId(),
            getOrderId(),
            productId,
            createReviewViewModel.getUserId()
        )
    }

    override fun onResume() {
        super.onResume()
        handleOnBackPressed()
    }

    override fun onImpressBadRatingCategory(title: String) {
        CreateReviewTracking.eventViewBadRatingReason(
            trackingQueue,
            getOrderId(),
            productId,
            title,
            getUserId()
        )
    }

    override fun onBadRatingCategoryClicked(
        title: String,
        isSelected: Boolean,
        badRatingCategoryId: String,
        shouldRequestFocus: Boolean
    ) {
        CreateReviewTracking.eventClickBadRatingReason(
            getOrderId(),
            productId,
            createReviewViewModel.getUserId(),
            title,
            isSelected
        )
        if (isSelected) {
            createReviewViewModel.addBadRatingCategory(badRatingCategoryId)
            if (badRatingCategoryId == BAD_RATING_OTHER_ID) {
                textArea?.setPlaceHolder(getString(R.string.review_form_bad_helper_must_fill))
            }
            if (shouldRequestFocus) {
                textArea?.requestFocusForEditText()
            } else {
                textArea?.clearFocus()
            }
        } else {
            if (badRatingCategoryId == BAD_RATING_OTHER_ID) {
                textArea?.apply {
                    setPlaceHolder(resources.getString(R.string.review_form_bad_helper))
                    clearFocus()
                }
            }
            createReviewViewModel.removeBadRatingCategory(badRatingCategoryId.toString())
        }
        updateButtonState(isGoodRating(), textArea?.isEmpty()?.not() ?: false)
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
        badRatingCategoryRecyclerView =
            view?.findViewById(R.id.review_form_bad_rating_categories_rv)
    }

    private fun setBottomSheetCallback() {
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        onBottomSheetExpanded()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // noop
            }
        })
    }

    private fun setRatingClickListener() {
        ratingStars?.setListener(object :
            AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                super.onClick(position)
                trackRatingChanged(position)
                updateTitleBasedOnSelectedRating(position)
                val isGoodRating = isGoodRating()
                updateButtonState(isGoodRating, textArea?.isEmpty()?.not() ?: false)
                createReviewViewModel.updateProgressBarFromRating(isGoodRating)
                if (isGoodRating) {
                    if (shouldShowBadRatingReasons()) badRatingCategoryRecyclerView?.hide()
                } else {
                    if (shouldShowBadRatingReasons()) badRatingCategoryRecyclerView?.show()
                }
                setTemplateVisibility()
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
        setTemplateVisibility()
    }

    private fun getForm() {
        createReviewViewModel.getProductReputation(productId, reputationId)
    }

    private fun getIncentiveOvoData(productId: String = "", reputationId: String = "") {
        createReviewViewModel.getProductIncentiveOvo(productId, reputationId)
    }

    private fun getTemplates() {
        createReviewViewModel.getReviewTemplates(productId)
    }

    private fun getBadRatingCategories() {
        createReviewViewModel.getBadRatingCategories()
    }

    private fun observeGetForm() =
        createReviewViewModel.getReputationDataForm.observe(this, {
            when (it) {
                is Success -> onSuccessGetReviewForm(it.data)
                is Fail -> onErrorGetReviewForm(it.throwable)
            }
        })

    private fun observeIncentive() {
        createReviewViewModel.incentiveOvo.observe(this, {
            when (it) {
                is Success -> onSuccessGetOvoIncentive(it.data)
                is Fail -> onFailGetOvoIncentive(it.throwable)
                else -> onSuccessGetOvoIncentive(null)
            }
        })
    }

    private fun observeTemplates() {
        createReviewViewModel.reviewTemplates.observe(this, {
            when (it) {
                is Success -> onSuccessGetTemplate(it.data)
                is Fail -> onFailGetTemplate(it.throwable)
            }
        })
    }

    private fun observeSubmitReview() {
        createReviewViewModel.submitReviewResult.observe(this, {
            when (it) {
                is com.tokopedia.review.common.data.Success -> {
                    onSuccessSubmitReview(it.data)
                }
                is com.tokopedia.review.common.data.Fail -> {
                    onFailSubmitReview(it.fail)
                }
                is LoadingView -> {
                    showButtonLoading()
                }
            }
        })
    }

    private fun observeButtonState() {
        createReviewViewModel.submitButtonState.observe(this, {
            submitButton?.isEnabled = it
        })
    }

    private fun observeProgressBarState() {
        createReviewViewModel.progressBarState.observe(this, {
            if (shouldShowBadRatingReasons()) {
                progressBar?.setProgressBarValue(it)
                return@observe
            }
            progressBar?.setProgressBarValueForOldFlow(it)
        })
    }

    private fun observeBadRatingCategories() {
        createReviewViewModel.badRatingCategories.observe(this, {
            when (it) {
                is Success -> onSuccessGetBadRatingCategories(it.data)
                is Fail -> onFailGetBadRatingCategories(it.throwable)
            }
        })
    }

    private fun observePostSubmitBottomSheetData() {
        createReviewViewModel.postSubmitUiState.observe(this) { result ->
            stopButtonLoading()
            when (result) {
                is PostSubmitUiState.ShowThankYouBottomSheet -> showThankYouBottomSheet(
                    result.data
                )
                is PostSubmitUiState.ShowThankYouToaster -> showThankYouToaster(
                    result.data
                )
            }
        }
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        with(data.productrevGetForm) {
            when {
                !validToReview -> {
                    finishIfRoot(
                        success = false,
                        message = getString(R.string.review_pending_invalid_to_review),
                        feedbackId = ""
                    )
                    return
                }
                productData.productStatus == 0 -> {
                    finishIfRoot(
                        success = false,
                        message = getString(R.string.review_pending_deleted_product_error_toaster),
                        feedbackId = ""
                    )
                    return
                }
            }
            hideLoading()
            updateProductId(productData.productIDStr)
            setProductDetail(productData)
            CreateReviewTracking.reviewOnViewTracker(orderID, productId)
        }
    }

    private fun onSuccessGetOvoIncentive(ovoDomain: ProductRevIncentiveOvoDomain?) {
        ovoDomain?.productrevIncentiveOvo?.let {
            incentivesTicker?.apply {
                setHtmlDescription(it.ticker.subtitle)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        if (ovoIncentiveBottomSheet == null) {
                            ovoIncentiveBottomSheet = context?.let { context ->
                                IncentiveOvoBottomSheetBuilder.getTermsAndConditionsBottomSheet(
                                    context = context,
                                    productRevIncentiveOvoDomain = ovoDomain,
                                    hasIncentive = hasIncentive(),
                                    hasOngoingChallenge = hasOngoingChallenge(),
                                    incentiveOvoListener = this@CreateReviewBottomSheet,
                                    category = "",
                                    trackerData = getTncBottomSheetTrackerData()
                                )
                            }
                        }
                        ovoIncentiveBottomSheet?.let { bottomSheet ->
                            activity?.supportFragmentManager?.let { supportFragmentManager ->
                                bottomSheet.show(
                                    supportFragmentManager,
                                    bottomSheet.tag
                                )
                            }
                            if (hasIncentive()) {
                                CreateReviewTracking.eventClickIncentivesTicker(
                                    it.subtitle,
                                    getReputationId(),
                                    getOrderId(),
                                    productId,
                                    getUserId()
                                )
                            } else if (hasOngoingChallenge()) {
                                CreateReviewTracking.eventClickOngoingChallengeTicker(
                                    getReputationId(),
                                    getOrderId(),
                                    productId,
                                    getUserId()
                                )
                            }
                        }
                    }

                    override fun onDismiss() {
                        // No Op
                    }

                })
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        CreateReviewTracking.eventViewIncentivesTicker(
                            it.subtitle,
                            getReputationId(),
                            getOrderId(),
                            productId,
                            getUserId()
                        )
                        incentivesTicker?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
                show()
            }
            return
        }
        setTemplateVisibility()
        incentivesTicker?.hide()
    }

    private fun onFailGetOvoIncentive(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessSubmitReview(feedbackId: String) {
        val reviewMessage = textArea?.getText() ?: ""
        createReviewViewModel.getPostSubmitBottomSheetData(reviewMessage, feedbackId)
    }

    private fun onFailSubmitReview(throwable: Throwable) {
        stopButtonLoading()
        showToasterError(context?.let { ErrorHandler.getErrorMessage(it, throwable) }
            ?: getString(R.string.review_create_fail_toaster))
        logToCrashlytics(throwable)
    }

    private fun onSuccessGetTemplate(templates: List<String>) {
        setTemplateVisibility()
        templatesRecyclerView?.apply {
            adapter = templatesAdapter
            layoutManager = StaggeredGridLayoutManager(TEMPLATES_ROW_COUNT, RecyclerView.HORIZONTAL)
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (templates.isNotEmpty()) {
                        CreateReviewTracking.eventViewReviewTemplate(
                            templates.size,
                            productId,
                            getUserId()
                        )
                    }
                    templatesRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            })
        }
        templatesAdapter.setData(templates)
    }

    private fun onFailGetTemplate(throwable: Throwable) {
        logToCrashlytics(throwable)
        hideTemplates()
    }

    private fun onSuccessGetBadRatingCategories(categories: List<BadRatingCategory>) {
        badRatingCategoriesAdapter.setData(categories)
        if (!isGoodRating()) {
            badRatingCategoryRecyclerView?.show()
        }
    }

    private fun onFailGetBadRatingCategories(throwable: Throwable) {
        logToCrashlytics(throwable)
        badRatingCategoryRecyclerView?.hide()
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
            CreateReviewTracking.eventClickSubmitForm(
                getRating(),
                getReviewMessageLength(),
                getNumberOfPictures(),
                isAnonymous(),
                hasIncentive(),
                isTemplateAvailable(),
                templatesSelectedCount,
                getOrderId(),
                productId,
                getUserId()
            )
            if (!isReviewComplete() && hasIncentive()) {
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
            intent.putParamPageSource(ImagePickerPageSource.REVIEW_PAGE)
            startActivityForResult(intent, CreateReviewFragment.REQUEST_CODE_IMAGE)
        }
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {
        logToCrashlytics(throwable)
        finishIfRoot(
            success = false,
            message = context?.let { ErrorHandler.getErrorMessage(it, throwable) }
                ?: getString(R.string.review_toaster_page_error),
            feedbackId = ""
        )
    }

    private fun updateTitleBasedOnSelectedRating(position: Int) {
        when (position) {
            CreateReviewFragment.RATING_1 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_worst_title)
                textArea?.apply {
                    if (createReviewViewModel.isOtherCategoryOnly()) {
                        setPlaceHolder(getString(R.string.review_form_bad_helper_must_fill))
                    } else {
                        setPlaceHolder(resources.getString(R.string.review_form_bad_helper))
                    }
                }
            }
            CreateReviewFragment.RATING_2 -> {
                textAreaTitle?.text = resources.getString(R.string.review_form_bad_title)
                textArea?.apply {
                    if (createReviewViewModel.isOtherCategoryOnly()) {
                        setPlaceHolder(getString(R.string.review_form_bad_helper_must_fill))
                    } else {
                        setPlaceHolder(resources.getString(R.string.review_form_bad_helper))
                    }
                }
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
        val imm =
            view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            CreateReviewTracking.eventDismissForm(
                getRating(),
                getReviewMessageLength(),
                getNumberOfPictures(),
                isAnonymous(),
                hasIncentive(),
                isTemplateAvailable(),
                templatesSelectedCount,
                getOrderId(),
                productId,
                getUserId()
            )
            if (activity?.isTaskRoot == true) {
                activity?.finish()
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                startActivity(intent)
                return@setOnDismissListener
            }
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
        showDialog(
            title,
            getString(R.string.review_form_incentives_incomplete_dialog_body),
            getString(R.string.review_create_incomplete_cancel),
            {
                CreateReviewTracking.eventClickCompleteReviewFirst(title)
            },
            getString(R.string.review_create_incomplete_send_anyways),
            {
                isReviewIncomplete = true
                submitNewReview()
                CreateReviewTracking.eventClickSendNow(title)
            })
        CreateReviewTracking.eventViewDialog(title)
    }

    private fun showSendRatingOnlyDialog() {
        val title = getString(R.string.review_form_send_rating_only_dialog_title)
        showDialog(
            title,
            getString(R.string.review_form_send_rating_only_body),
            getString(R.string.review_form_send_rating_only_exit),
            { dismiss() },
            getString(R.string.review_form_send_rating_only),
            {
                submitNewReview()
                CreateReviewTracking.eventClickDialogOption(
                    CreateReviewDialogType.CreateReviewSendRatingOnlyDialog,
                    title,
                    getReputationId(),
                    getOrderId(),
                    productId,
                    getUserId()
                )
            },
            DialogUnify.HORIZONTAL_ACTION
        )
        CreateReviewTracking.eventViewDialog(
            CreateReviewDialogType.CreateReviewSendRatingOnlyDialog,
            title,
            getReputationId(),
            getOrderId(),
            productId,
            getUserId()
        )
    }

    private fun showReviewUnsavedWarningDialog() {
        val title = getString(R.string.review_form_dismiss_form_dialog_title)
        showDialog(
            title,
            getString(R.string.review_form_dismiss_form_dialog_body),
            getString(R.string.review_edit_dialog_exit),
            { dismiss() },
            getString(R.string.review_form_dismiss_form_dialog_stay),
            {
                CreateReviewTracking.eventClickDialogOption(
                    CreateReviewDialogType.CreateReviewUnsavedDialog,
                    title,
                    getReputationId(),
                    getOrderId(),
                    productId,
                    getUserId()
                )
            },
            DialogUnify.HORIZONTAL_ACTION
        )
        CreateReviewTracking.eventViewDialog(
            CreateReviewDialogType.CreateReviewUnsavedDialog,
            title,
            getReputationId(),
            getOrderId(),
            productId,
            getUserId()
        )
    }

    private fun showIncentivesExitWarningDialog() {
        showDialog(
            getString(R.string.review_form_incentives_exit_dialog_title),
            getString(R.string.review_form_incentives_exit_dialog_body),
            getString(R.string.review_form_dismiss_form_dialog_stay),
            {},
            getString(R.string.review_edit_dialog_exit),
            { dismiss() },
            DialogUnify.HORIZONTAL_ACTION
        )
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCtaText: String,
        primaryCtaAction: () -> Unit,
        secondaryCtaText: String,
        secondaryCtaAction: () -> Unit,
        orientation: Int = DialogUnify.VERTICAL_ACTION
    ) {
        context?.let {
            DialogUnify(it, orientation, DialogUnify.NO_IMAGE).apply {
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

    private fun updateProductId(productId: String) {
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
                CreateReviewTracking.reviewOnAnonymousClickTracker(
                    getOrderId(),
                    productId,
                    isEditMode,
                    feedbackId.toString()
                )
            }
        }
    }

    private fun trackRatingChanged(position: Int) {
        CreateReviewTracking.reviewOnRatingChangedTracker(
            getOrderId(),
            productId,
            (position).toString(),
            true,
            isEditMode,
            feedbackId.toString()
        )
    }

    private fun updateButtonState(isGoodRating: Boolean, isTextAreaNotEmpty: Boolean) {
        if (isGoodRating) {
            createReviewViewModel.updateButtonState(isGoodRating)
        } else {
            createReviewViewModel.updateButtonState(
                if (shouldShowBadRatingReasons()) {
                    createReviewViewModel.isBadRatingReasonSelected(isTextAreaNotEmpty)
                } else {
                    isTextAreaNotEmpty
                }
            )
        }
    }

    private fun setOnTouchOutsideListener() {
        setShowListener {
            setBottomSheetCallback()
            CreateReviewTracking.openScreenWithCustomDimens(
                CreateReviewTrackingConstants.SCREEN_NAME_BOTTOM_SHEET,
                productId,
                reputationId,
                utmSource
            )
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            isCancelable = false
            dialog?.setCanceledOnTouchOutside(false)
            this.dialog?.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)
                ?.setOnClickListener {
                    handleDismiss()
                }
            getData()
        }
        setCloseClickListener {
            handleDismiss()
        }
    }

    private fun handleDismiss() {
        if (hasIncentive()) {
            showIncentivesExitWarningDialog()
            return
        }
        if (isGoodRating() && textArea?.isEmpty() == true && !createReviewViewModel.isImageNotEmpty()) {
            showSendRatingOnlyDialog()
            return
        }
        if (textArea?.isEmpty() == false || createReviewViewModel.isImageNotEmpty()) {
            showReviewUnsavedWarningDialog()
            return
        }
        dismiss()
    }

    private fun finishIfRoot(
        success: Boolean,
        message: String,
        feedbackId: String
    ) {
        activity?.run {
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.putExtra(ReviewInboxConstants.CREATE_REVIEW_MESSAGE, message)
                if (success) {
                    intent.putExtra(ReputationCommonConstants.ARGS_FEEDBACK_ID, feedbackId)
                    intent.putExtra(ReputationCommonConstants.ARGS_RATING, rating)
                    intent.putExtra(ReputationCommonConstants.ARGS_PRODUCT_ID, productId)
                    intent.putExtra(ReputationCommonConstants.ARGS_REPUTATION_ID, reputationId)
                    intent.putExtra(
                        ReputationCommonConstants.ARGS_REVIEW_STATE,
                        ReputationCommonConstants.REVIEWED
                    )
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    intent.putExtra(
                        ReputationCommonConstants.ARGS_REVIEW_STATE,
                        ReputationCommonConstants.INVALID_TO_REVIEW
                    )
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
        incentivesHelperText?.apply {
            incentiveHelper = when {
                textLength >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_eligible_for_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_eligible_for_challenge) ?: ""
                    } else {
                        ""
                    }
                }
                textLength < CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && textLength != 0 -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_partial_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_partial_challenge) ?: ""
                    } else {
                        ""
                    }
                }
                else -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_empty_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_bottom_sheet_text_area_empty_challenge) ?: ""
                    } else {
                        ""
                    }
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
            Toaster.build(
                it,
                message,
                Toaster.toasterLength,
                Toaster.TYPE_ERROR,
                getString(R.string.review_oke)
            ).show()
        }
    }

    private fun getReputationId(): String {
        return (createReviewViewModel.getReputationDataForm.value as? Success)?.data?.productrevGetForm?.reputationIDStr
            ?: ""
    }

    private fun getRating(): Int {
        return ratingStars?.clickAt ?: CreateReviewActivity.DEFAULT_PRODUCT_RATING
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

    private fun hasIncentive(): Boolean {
        return createReviewViewModel.hasIncentive()
    }

    private fun hasOngoingChallenge(): Boolean {
        return createReviewViewModel.hasOngoingChallenge()
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
        observePostSubmitBottomSheetData()
        if (shouldShowBadRatingReasons()) observeBadRatingCategories()
    }

    private fun getData() {
        getForm()
        getIncentiveOvoData(productId, reputationId)
        getTemplates()
        if (shouldShowBadRatingReasons()) getBadRatingCategories()
    }

    private fun setOnTouchListenerToHideKeyboard() {
        templatesRecyclerView?.setCustomTouchListener()
        anonymousOption?.setCustomTouchListener()
        photosRecyclerView?.setCustomTouchListener()
        incentivesTicker?.setCustomTouchListener()
        textAreaTitle?.setCustomTouchListener()
        submitButton?.setCustomTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun View.setCustomTouchListener() {
        this.setOnTouchListener { _, _ ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }
    }

    private fun getTemplatesForTextArea(): List<String> {
        if (isGoodRating()) {
            return templatesAdapter.getTemplates()
        }
        return listOf()
    }

    private fun showThankYouBottomSheet(
        data: ProductrevGetPostSubmitBottomSheetResponse
    ) {
        if (thankYouBottomSheet == null) {
            thankYouBottomSheet = context?.let {
                IncentiveOvoThankYouBottomSheetBuilder.getThankYouBottomSheet(
                    context = it,
                    postSubmitBottomSheetData = data,
                    hasIncentive = hasIncentive(),
                    hasOngoingChallenge = hasOngoingChallenge(),
                    incentiveOvoListener = this,
                    trackerData = getThankYouBottomSheetTrackerData(),
                )
            }
        }
        thankYouBottomSheet?.let { bottomSheet ->
            activity?.supportFragmentManager?.let { bottomSheet.show(it, bottomSheet.tag) }
        }
    }

    private fun showThankYouToaster(data: ProductrevGetPostSubmitBottomSheetResponse?) {
        finishIfRoot(
            success = true,
            message = data?.getToasterText(createReviewViewModel.getUserName()) ?: getString(
                R.string.review_create_success_toaster,
                createReviewViewModel.getUserName()
            ),
            feedbackId = getFeedbackId()
        )
    }

    private fun getThankYouBottomSheetTrackerData(): ThankYouBottomSheetTrackerData {
        return ThankYouBottomSheetTrackerData(
            getReputationId(),
            getOrderId(),
            productId,
            getUserId(),
            getFeedbackId()
        )
    }

    private fun getFeedbackId(): String {
        return (createReviewViewModel.submitReviewResult.value as? com.tokopedia.review.common.data.Success)?.data
            ?: ""
    }

    private fun getTncBottomSheetTrackerData(): TncBottomSheetTrackerData {
        return TncBottomSheetTrackerData(
            getReputationId(),
            getOrderId(),
            productId,
            getUserId()
        )
    }

    private fun handleOnActivityResult(data: Intent) {
        val result = ImagePickerResultExtractor.extract(data)
        val selectedImage = result.imageUrlOrPathList
        val imagesFedIntoPicker = result.imagesFedIntoPicker
        createReviewViewModel.clearImageData()

        CreateReviewTracking.reviewOnImageUploadTracker(
            getOrderId(),
            productId,
            true,
            selectedImage.size.toString(),
            isEditMode,
            feedbackId.toString()
        )

        if (!selectedImage.isNullOrEmpty()) {
            val imageListData =
                createReviewViewModel.getAfterEditImageList(selectedImage, imagesFedIntoPicker)
            imageAdapter.setImageReviewData(imageListData)
            photosRecyclerView?.apply {
                adapter = imageAdapter
                show()
            }
            addPhoto?.hide()
            createReviewViewModel.updateProgressBarFromPhotos()
        }
    }

    private fun handleOnBackPressed() {
        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                handleDismiss()
                true
            } else false
        }
    }

    private fun setTemplateVisibility() {
        if (isGoodRating()) {
            showTemplates()
        } else {
            hideTemplates()
        }
    }

    private fun setUpBadRatingCategoriesRecyclerView() {
        badRatingCategoryRecyclerView?.apply {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            adapter = badRatingCategoriesAdapter
            addItemDecoration(ReviewBadRatingItemDecoration())
        }
    }

    private fun shouldShowBadRatingReasons(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                BAD_RATING_FLOW_EXPERIMENT_KEY,
                OLD_FORM_VARIANT
            ) == BAD_RATING_FORM_VARIANT
        } catch (t: Throwable) {
            false
        }
    }

    private fun onBottomSheetExpanded() {
        observeLiveDatas()
    }
}