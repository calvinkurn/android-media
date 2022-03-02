package com.tokopedia.review.feature.createreputation.presentation.fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.device.info.DevicePerformanceInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevGetReviewDetailProduct
import com.tokopedia.review.common.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.databinding.FragmentCreateReviewBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTrackingConstants
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.createreputation.model.Reputation
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.createreputation.presentation.adapter.ImageReviewAdapter
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.IncentiveOvoThankYouBottomSheetBuilder
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextAreaBottomSheet
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoBottomSheetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewFragment : BaseDaggerFragment(),
    ImageClickListener, TextAreaListener, ReviewScoreClickListener,
    ReviewPerformanceMonitoringContract,
    IncentiveOvoListener {

    companion object {
        const val REQUEST_CODE_IMAGE = 111
        private const val PRODUCT_ID_REVIEW = "PRODUCT_ID"
        private const val REPUTATION_ID = "REPUTATION_ID"
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"

        private const val LOTTIE_ANIM_1 =
            "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_1.json"
        private const val LOTTIE_ANIM_2 =
            "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_2.json"
        private const val LOTTIE_ANIM_3 =
            "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_3.json"
        private const val LOTTIE_ANIM_4 =
            "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_4.json"
        private const val LOTTIE_ANIM_5 =
            "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_5.json"

        private const val IMAGE_PEDIE_1 = "https://ecs7.tokopedia.net/android/pedie/1star.png"
        private const val IMAGE_PEDIE_2 = "https://ecs7.tokopedia.net/android/pedie/2star.png"
        private const val IMAGE_PEDIE_3 = "https://ecs7.tokopedia.net/android/pedie/3star.png"
        private const val IMAGE_PEDIE_4 = "https://ecs7.tokopedia.net/android/pedie/4star.png"
        private const val IMAGE_PEDIE_5 = "https://ecs7.tokopedia.net/android/pedie/5star.png"

        const val RATING_1 = 1
        const val RATING_2 = 2
        const val RATING_3 = 3
        const val RATING_4 = 4
        const val RATING_5 = 5
        private const val SAME_ARGS_ERROR = 9

        const val REVIEW_INCENTIVE_MINIMUM_THRESHOLD = 40

        fun createInstance(
            productId: String,
            reviewId: String,
            reviewClickAt: Int = 0,
            isEditMode: Boolean,
            feedbackId: String,
            utmSource: String
        ) = CreateReviewFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID_REVIEW, productId)
                putString(REPUTATION_ID, reviewId)
                putInt(REVIEW_CLICK_AT, reviewClickAt)
                putBoolean(ReviewConstants.PARAM_IS_EDIT_MODE, isEditMode)
                putString(ReviewConstants.PARAM_FEEDBACK_ID, feedbackId)
                putString(ReviewConstants.PARAM_SOURCE, utmSource)
            }
        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel

    private lateinit var animatedReviewPicker: AnimatedRatingPickerCreateReviewView
    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this)
    }
    private var isLowDevice = false

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null
    private var shouldPlayAnimation: Boolean = true
    private var reviewClickAt: Int = 0
    private var reputationId: String = ""
    private var productId: String = ""
    private var shopId: String = ""
    private var isEditMode: Boolean = false
    private var feedbackId: String = ""
    private var utmSource: String = ""

    lateinit var imgAnimationView: LottieAnimationView
    private var textAreaBottomSheet: CreateReviewTextAreaBottomSheet? = null
    private var ovoIncentiveBottomSheet: IncentiveOvoBottomSheet? = null
    private var thankYouBottomSheet: BottomSheetUnify? = null
    private var incentiveHelper = ""
    private var isReviewIncomplete = false

    private var binding by autoClearedNullable<FragmentCreateReviewBinding>()

    override fun stopPreparePerfomancePageMonitoring() {
        reviewPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        binding?.createReviewScrollView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding?.createReviewScrollView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewPerformanceMonitoringListener? {
        return if (context is ReviewPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun getScreenName(): String = CreateReviewTrackingConstants.SCREEN_NAME

    override fun initInjector() {
        activity?.let {
            DaggerCreateReviewComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(it.application))
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateReviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )

        arguments?.let {
            productId = it.getString(PRODUCT_ID_REVIEW, "")
            reviewClickAt = it.getInt(REVIEW_CLICK_AT, 0)
            reputationId = it.getString(REPUTATION_ID, "")
            isEditMode = it.getBoolean(ReviewConstants.PARAM_IS_EDIT_MODE, false)
            feedbackId = it.getString(ReviewConstants.PARAM_FEEDBACK_ID, "")
            utmSource = it.getString(ReviewConstants.PARAM_SOURCE, "")
        }

        CreateReviewTracking.openScreen(screenName, productId, reputationId, utmSource)

        if (reviewClickAt > CreateReviewActivity.DEFAULT_PRODUCT_RATING || reviewClickAt < 0) {
            reviewClickAt = CreateReviewActivity.DEFAULT_PRODUCT_RATING
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createReviewViewModel.getReputationDataForm.observe(viewLifecycleOwner, {
            when (it) {
                is CoroutineSuccess -> onSuccessGetReviewForm(it.data)
                is CoroutineFail -> onErrorGetReviewForm(it.throwable)
            }
        })

        createReviewViewModel.incentiveOvo.observe(viewLifecycleOwner, {
            when (it) {
                is CoroutineSuccess -> onSuccessGetIncentiveOvo(it.data)
                is CoroutineFail -> onErrorGetIncentiveOvo()
                else -> onSuccessGetIncentiveOvo(null)
            }
        })

        createReviewViewModel.reviewDetails.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetReviewDetail(it.data)
                is Fail -> onFailGetReviewDetail(it.fail)
            }
        })

        createReviewViewModel.submitReviewResult.observe(viewLifecycleOwner, {
            when (it) {
                is LoadingView -> {
                    showLoading()
                }
                is Success -> {
                    onSuccessSubmitReview(it.data)
                }
                is Fail -> {
                    onFailSubmitReview(it.fail)
                }
            }
        })

        createReviewViewModel.editReviewResult.observe(viewLifecycleOwner, {
            when (it) {
                is LoadingView -> {
                    showLoading()
                }
                is Success -> {
                    onSuccessSubmitReview(feedbackId)
                }
                is Fail -> {
                    onFailEditReview(it.fail)
                }
            }
        })

        createReviewViewModel.postSubmitUiState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is PostSubmitUiState.ShowThankYouBottomSheet -> showThankYouBottomSheet(
                    result.data,
                    result.hasPendingIncentive
                )
                is PostSubmitUiState.ShowThankYouToaster -> showThankYouToaster(
                    result.data
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        isLowDevice = DevicePerformanceInfo.isLow(context)
        initCreateReviewTextArea()
        initEmptyPhoto()
        initAnonymousText()
        if (isEditMode) {
            hideScoreWidgetAndDivider()
            getReviewDetailData()
        } else {
            getReviewData()
            getIncentiveOvoData(productId, reputationId)
        }
        animatedReviewPicker = view.findViewById(R.id.animatedReview)
        imgAnimationView = view.findViewById(R.id.img_animation_review)
        animatedReviewPicker.resetStars()
        animatedReviewPicker.setListener(object :
            AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                CreateReviewTracking.reviewOnRatingChangedTracker(
                    getOrderId(),
                    productId,
                    (position).toString(),
                    true,
                    isEditMode,
                    feedbackId
                )
                reviewClickAt = position
                shouldPlayAnimation = true
                context?.let {
                    if (isLowDevice) {
                        generatePeddieImageByIndex()
                    } else {
                        playAnimation()
                    }
                }
                updateViewBasedOnSelectedRating(position)
                clearFocusAndHideSoftInput(view)
            }
        })
        if (!isEditMode) {
            animatedReviewPicker.renderInitialReviewWithData(reviewClickAt)
            updateViewBasedOnSelectedRating(if (reviewClickAt != 0) reviewClickAt else RATING_5)
            playAnimation()
        }

        imgAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                playAnimation()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        binding?.createReviewAnonymousCheckbox?.setOnClickListener {
            if (binding?.createReviewAnonymousCheckbox?.isChecked == true) {
                CreateReviewTracking.reviewOnAnonymousClickTracker(
                    getOrderId(),
                    productId,
                    isEditMode,
                    feedbackId
                )
            }
            clearFocusAndHideSoftInput(view)
        }

        binding?.reviewContainer?.setOnTouchListener { _, _ ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }

        binding?.rvImgReview?.setOnTouchListener { _, _ ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }

        binding?.rvImgReview?.adapter = imageAdapter

        binding?.createReviewSubmitButton?.apply {
            if (isEditMode) {
                text = getString(R.string.review_create_submit_edit)
            }
            setOnClickListener {
                submitReview()
            }
        }
    }

    override fun onDestroy() {
        createReviewViewModel.getReputationDataForm.removeObservers(this)
        createReviewViewModel.incentiveOvo.removeObservers(this)
        createReviewViewModel.reviewDetails.removeObservers(this)
        createReviewViewModel.submitReviewResult.removeObservers(this)
        super.onDestroy()
    }

    override fun onAddImageClick() {
        clearFocusAndHideSoftInput(view)
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
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onRemoveImageClick(item: BaseImageReviewUiModel) {
        imageAdapter.setImageReviewData(createReviewViewModel.removeImage(item, isEditMode))
        if (imageAdapter.isEmpty()) {
            binding?.rvImgReview?.hide()
            binding?.createReviewAddPhotoEmpty?.show()
        }
    }

    override fun onExpandButtonClicked(text: String) {
        CreateReviewTracking.onExpandTextBoxClicked(getOrderId(), productId)
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(
            this,
            text,
            incentiveHelper,
            hasIncentive(),
            hasOngoingChallenge()
        )
        (textAreaBottomSheet as? BottomSheetUnify)?.setTitle(binding?.createReviewTextAreaTitle?.text.toString())
        fragmentManager?.let { textAreaBottomSheet?.show(it, "") }
    }

    override fun onCollapseButtonClicked(text: String) {
        CreateReviewTracking.onCollapseTextBoxClicked(getOrderId(), productId)
        textAreaBottomSheet?.dismiss()
    }

    override fun onDismissBottomSheet(text: String, templates: List<String>) {
        binding?.createReviewExpandableTextArea?.setText(text)
        clearFocusAndHideSoftInput(view)
    }

    override fun scrollToShowTextArea() {
        binding?.let {
            it.createReviewScrollView.smoothScrollTo(0, it.createReviewDivider.bottom)
        }
    }

    override fun trackWhenHasFocus(textLength: Int) {
        CreateReviewTracking.reviewOnMessageChangedTracker(
            getOrderId(),
            productId,
            textLength == 0,
            isEditMode,
            feedbackId
        )
        setHelperText(textLength)
    }

    override fun onTextChanged(textLength: Int) {
        setHelperText(textLength)
    }

    override fun hideText() {
        hideIncentiveText()
    }

    override fun onReviewScoreClicked(score: Int): Boolean {
        CreateReviewTracking.eventClickSmiley(getOrderId(), productId)
        binding?.createReviewScore?.onScoreSelected(score)
        return true
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
            feedbackId = if (isEditMode) feedbackId else getFeedbackId()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = ImagePickerResultExtractor.extract(data)
                    val selectedImage = result.imageUrlOrPathList
                    createReviewViewModel.clearImageData()

                    CreateReviewTracking.reviewOnImageUploadTracker(
                        getOrderId(),
                        productId,
                        true,
                        selectedImage.size.toString(),
                        isEditMode,
                        feedbackId
                    )

                    if (!selectedImage.isNullOrEmpty()) {
                        val imageListData = createReviewViewModel.getAfterEditImageList(
                            selectedImage,
                            result.imagesFedIntoPicker
                        )
                        imageAdapter.setImageReviewData(imageListData)
                        binding?.rvImgReview?.show()
                        binding?.createReviewAddPhotoEmpty?.hide()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getReviewData() {
        showShimmering()
        createReviewViewModel.getProductReputation(productId, reputationId)
    }

    private fun getReviewDetailData() {
        showShimmering()
        createReviewViewModel.getReviewDetails(feedbackId)
    }

    private fun getIncentiveOvoData(productId: String = "", reputationId: String = "") {
        createReviewViewModel.getProductIncentiveOvo(productId, reputationId)
    }

    private fun submitReview() {
        val reviewMessage = binding?.createReviewExpandableTextArea?.getText()
        CreateReviewTracking.reviewOnSubmitTracker(
            getOrderId(),
            productId,
            reviewClickAt.toString(),
            reviewMessage?.isEmpty() ?: false,
            createReviewViewModel.getSelectedImagesUrl().size.toString(),
            binding?.createReviewAnonymousCheckbox?.isChecked ?: false,
            isEditMode,
            feedbackId,
            hasIncentive() && isReviewComplete()
        )
        if (isEditMode) {
            if (reviewMessage?.isBlank() == true) {
                showToasterError(getString(R.string.review_edit_blank_error))
                return
            }
            createReviewViewModel.editReview(
                feedbackId,
                reputationId,
                productId,
                shopId,
                binding?.createReviewScore?.getScore() ?: 0,
                animatedReviewPicker.getReviewClickAt(),
                reviewMessage ?: "",
                binding?.createReviewAnonymousCheckbox?.isChecked ?: false
            )
        } else {
            if (!isReviewComplete() && hasIncentive()) {
                showReviewIncompleteDialog()
                return
            }
            isReviewIncomplete = false
            submitNewReview()
        }
    }

    private fun hasIncentive(): Boolean {
        return createReviewViewModel.hasIncentive()
    }

    private fun hasOngoingChallenge(): Boolean {
        return createReviewViewModel.hasOngoingChallenge()
    }

    private fun submitNewReview() {
        binding?.apply {
            createReviewViewModel.submitReview(
                animatedReviewPicker.getReviewClickAt(),
                createReviewExpandableTextArea.getText(),
                createReviewScore.getScore().orZero(),
                createReviewAnonymousCheckbox.isChecked,
                utmSource
            )
        }
    }

    private fun isReviewComplete(): Boolean {
        val reviewMessage = binding?.createReviewExpandableTextArea?.getText() ?: ""
        return (reviewMessage.length >= REVIEW_INCENTIVE_MINIMUM_THRESHOLD && animatedReviewPicker.getReviewClickAt() != 0 && createReviewViewModel.isImageNotEmpty())
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

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        stopNetworkRequestPerformanceMonitoring()
        startRenderPerformanceMonitoring()
        with(data.productrevGetForm) {
            updateProductId(productData.productIDStr)
            when {
                !validToReview -> {
                    finishIfRoot(
                        success = false,
                        message = getString(R.string.review_pending_invalid_to_review),
                        feedbackId = if (isEditMode) feedbackId else getFeedbackId()
                    )
                    return
                }
                productData.productStatus == 0 -> {
                    finishIfRoot(
                        success = false,
                        message = getString(R.string.review_pending_deleted_product_error_toaster),
                        feedbackId = if (isEditMode) feedbackId else getFeedbackId()
                    )
                    return
                }
            }
            hideShimmering()
            showLayout()

            CreateReviewTracking.reviewOnViewTracker(orderID, productId)

            shopId = shopData.shopIDStr
            setProductDetail(
                productData.productName,
                productData.productVariant.variantName,
                productData.productImageURL
            )
            setReputation(reputation, shopData.shopName)
        }
    }

    private fun setReputation(reputation: Reputation, shopName: String) {
        with(reputation) {
            binding?.apply {
                if (locked || filled || score != 0) {
                    createReviewScore.hide()
                    createReviewScoreDivider.hide()
                    return
                } else {
                    createReviewScore.apply {
                        setEditableScore(score)
                        setShopName(shopName)
                        setReviewScoreClickListener(this@CreateReviewFragment)
                        show()
                    }
                    createReviewScoreDivider.show()
                }
            }
        }
    }

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain?) {
        data?.productrevIncentiveOvo?.let {
            it.ticker.let {
                binding?.ovoPointsTicker?.apply {
                    visibility = View.VISIBLE
                    ReviewTracking.onSuccessGetIncentiveOvoTracker(it.subtitle, "")
                    setHtmlDescription(it.subtitle)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            context?.let { context ->
                                val bottomSheet = ovoIncentiveBottomSheet ?: IncentiveOvoBottomSheet(context).also { ovoIncentiveBottomSheet = it }
                                val bottomSheetData = IncentiveOvoBottomSheetUiModel(data)
                                bottomSheet.init(bottomSheetData, this@CreateReviewFragment)
                                activity?.supportFragmentManager?.let { supportFragmentManager ->
                                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                                    ReviewTracking.onClickReadSkIncentiveOvoTracker(it.subtitle, "")
                                }
                            }
                        }

                        override fun onDismiss() {
                            ReviewTracking.onClickDismissIncentiveOvoTracker(it.subtitle, "")
                        }
                    })
                }
            }
            return
        }
        binding?.ovoPointsTicker?.hide()
    }

    private fun onErrorGetIncentiveOvo() {
        binding?.ovoPointsTicker?.hide()
    }

    private fun showThankYouBottomSheet(
        data: ProductrevGetPostSubmitBottomSheetResponse,
        hasPendingIncentive: Boolean
    ) {
        if (thankYouBottomSheet == null) {
            thankYouBottomSheet = context?.let {
                IncentiveOvoThankYouBottomSheetBuilder.getThankYouBottomSheet(
                    it,
                    data,
                    hasIncentive(),
                    hasOngoingChallenge(),
                    this@CreateReviewFragment,
                    getThankYouFormTrackerData()
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
            feedbackId = if (isEditMode) feedbackId else getFeedbackId()
        )
    }

    private fun onSuccessGetReviewDetail(productrevGetReviewDetail: ProductrevGetReviewDetail) {
        stopNetworkRequestPerformanceMonitoring()
        startRenderPerformanceMonitoring()
        hideShimmering()
        with(productrevGetReviewDetail) {
            setReview(review)
            setProduct(product)
            shopId = response.shopId
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview) {
        with(review) {
            binding?.apply {
                createReviewExpandableTextArea.setText(reviewText)
                animatedReviewPicker.renderInitialReviewWithData(rating)
                playAnimation()
                updateViewBasedOnSelectedRating(rating)
                createReviewAnonymousCheckbox.isChecked = sentAsAnonymous
                if (attachments.isNotEmpty()) {
                    createReviewViewModel.clearImageData()
                    val imageListData = createReviewViewModel.getImageList(attachments)
                    imageAdapter.setImageReviewData(imageListData)
                    rvImgReview.show()
                    createReviewAddPhotoEmpty.hide()
                }
            }
        }
    }

    private fun setProduct(product: ProductrevGetReviewDetailProduct) {
        with(product) {
            setProductDetail(productName, productVariantName, productImageUrl)
        }
    }

    private fun onFailGetReviewDetail(throwable: Throwable) {
        hideShimmering()
        if (throwable is MessageErrorException) {
            finishIfRoot(
                success = false,
                message = getString(R.string.review_error_not_found),
                feedbackId = if (isEditMode) feedbackId else getFeedbackId()
            )
        } else {
            binding?.reviewRoot?.let {
                NetworkErrorHelper.showEmptyState(context, it) {
                    getReviewDetailData()
                }
            }
        }
    }

    private fun updateViewBasedOnSelectedRating(position: Int) {
        binding?.apply {
            when {
                position < RATING_3 -> {
                    if (position == RATING_1) {
                        createReviewTextAreaTitle.text =
                            resources.getString(R.string.review_create_worst_title)
                    } else {
                        createReviewTextAreaTitle.text =
                            resources.getString(R.string.review_create_negative_title)
                    }
                    txtReviewDesc.text = MethodChecker.fromHtml(
                        getString(
                            R.string.review_text_negative,
                            createReviewViewModel.getUserName()
                        )
                    )
                    createReviewContainer.setContainerColor(ContainerUnify.RED)
                }
                position == RATING_3 -> {
                    txtReviewDesc.text = MethodChecker.fromHtml(
                        getString(
                            R.string.review_text_neutral,
                            createReviewViewModel.getUserName()
                        )
                    )
                    createReviewContainer.setContainerColor(ContainerUnify.YELLOW)
                    createReviewTextAreaTitle.text =
                        resources.getString(R.string.review_create_neutral_title)
                }
                else -> {
                    if (position == RATING_4) {
                        createReviewTextAreaTitle.text =
                            resources.getString(R.string.review_create_positive_title)
                    } else {
                        createReviewTextAreaTitle.text =
                            resources.getString(R.string.review_create_best_title)
                    }
                    txtReviewDesc.text = MethodChecker.fromHtml(
                        getString(
                            R.string.review_text_positive,
                            createReviewViewModel.getUserName()
                        )
                    )
                    createReviewContainer.setContainerColor(ContainerUnify.GREEN)
                }
            }
        }
    }

    private fun playAnimation() {
        if (shouldPlayAnimation && ::imgAnimationView.isInitialized) {
            generateAnimationByIndex(animatedReviewPicker.getReviewClickAt())
            shouldPlayAnimation = false
        }
    }

    private fun generatePeddieImageByIndex() {
        val url = when (animatedReviewPicker.getReviewClickAt()) {
            RATING_1 -> IMAGE_PEDIE_1
            RATING_2 -> IMAGE_PEDIE_2
            RATING_3 -> IMAGE_PEDIE_3
            RATING_4 -> IMAGE_PEDIE_4
            RATING_5 -> IMAGE_PEDIE_5
            else -> IMAGE_PEDIE_5
        }
        showImage(url)
    }

    private fun showImage(url: String) {
        ImageHandler.loadImageWithoutPlaceholder(binding?.imgAnimationReview, url)
    }

    private fun generateAnimationByIndex(index: Int) {
        context?.let {
            imgAnimationView.repeatCount = 0
            imgAnimationView.repeatCount = LottieDrawable.INFINITE
            when (index) {
                RATING_1 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_1)
                }
                RATING_2 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_2)
                }
                RATING_3 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_3)
                }
                RATING_4 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_4)
                }
                RATING_5 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_5)
                }
            }
        }
    }

    /**
     * Fetch the animation from http URL and play the animation
     */
    private fun setLottieAnimationFromUrl(animationUrl: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)

            lottieCompositionLottieTask.addListener { result ->
                imgAnimationView.setComposition(result)
                imgAnimationView.playAnimation()
            }

            lottieCompositionLottieTask.addFailureListener { throwable ->
                logToCrashlytics(throwable)
            }
        }
    }

    private fun onSuccessSubmitReview(feedbackId: String = "") {
        if (isEditMode) {
            stopLoading()
            showLayout()
            finishIfRoot(
                success = true,
                message = getString(R.string.review_create_success_toaster),
                feedbackId = feedbackId
            )
        } else {
            val reviewMessage = binding?.createReviewExpandableTextArea?.getText().orEmpty()
            createReviewViewModel.getPostSubmitBottomSheetData(reviewMessage, feedbackId)
        }
    }

    private fun onFailSubmitReview(throwable: Throwable) {
        stopLoading()
        showLayout()
        showToasterError(throwable.message ?: getString(R.string.review_create_fail_toaster))
        logToCrashlytics(throwable)
    }

    private fun onFailEditReview(throwable: Throwable) {
        stopLoading()
        showLayout()
        logToCrashlytics(throwable)
        (throwable as? MessageErrorException)?.let {
            if (throwable.errorCode.toIntOrZero() == SAME_ARGS_ERROR) {
                view?.let {
                    showToasterError(throwable.message ?: getString(R.string.review_edit_fail))
                }
            } else {
                showToasterError(getString(R.string.review_edit_fail))
            }
            return
        }
        showToasterError(getString(R.string.review_edit_fail))
    }

    private fun showShimmering() {
        binding?.shimmeringCreateReview?.root?.show()
    }

    private fun hideShimmering() {
        binding?.shimmeringCreateReview?.root?.hide()
    }

    private fun showLoading() {
        binding?.createReviewSubmitButton?.apply {
            isLoading = true
            setOnClickListener(null)
        }
    }

    private fun stopLoading() {
        binding?.createReviewSubmitButton?.apply {
            isLoading = false
            setOnClickListener {
                submitReview()
            }
        }
    }

    private fun showLayout() {
        binding?.createReviewScrollView?.show()
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.toasterLength,
                Toaster.TYPE_ERROR,
                getString(R.string.review_oke)
            ).show()
        }
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {
        hideShimmering()
        if (throwable is MessageErrorException) {
            finishIfRoot(
                success = false,
                message = getString(R.string.review_error_not_found),
                feedbackId = if (isEditMode) feedbackId else getFeedbackId()
            )
        } else {
            binding?.reviewRoot?.let {
                NetworkErrorHelper.showEmptyState(context, it) {
                    getReviewData()
                }
            }
        }

    }

    private fun finishIfRoot(
        success: Boolean = false,
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
                    intent.putExtra(ReputationCommonConstants.ARGS_RATING, reviewClickAt)
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
            finish()
        }
    }

    private fun goToReviewPending() {
        RouteManager.route(context, ApplinkConst.REPUTATION)
        activity?.finish()
    }

    private fun clearFocusAndHideSoftInput(view: View?) {
        binding?.createReviewExpandableTextArea?.clearFocus()
        val imm =
            view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initCreateReviewTextArea() {
        binding?.createReviewExpandableTextArea?.apply {
            setListener(this@CreateReviewFragment)
            addOnImpressionListener(ImpressHolder()) {
                CreateReviewTracking.reviewOnScoreVisible(getOrderId(), productId)
            }
        }
    }

    private fun initEmptyPhoto() {
        binding?.createReviewAddPhotoEmpty?.setOnClickListener {
            onAddImageClick()
        }
    }

    private fun initAnonymousText() {
        binding?.createReviewAnonymousText?.setOnClickListener {
            if (binding?.createReviewAnonymousCheckbox?.isChecked == true) {
                CreateReviewTracking.reviewOnAnonymousClickTracker(
                    getOrderId(),
                    productId,
                    isEditMode,
                    feedbackId
                )
            }
        }
    }

    private fun setProductDetail(
        productName: String,
        productVariant: String,
        productImageUrl: String
    ) {
        binding?.createReviewProductImage?.loadImage(productImageUrl)
        binding?.createReviewProductName?.apply {
            text = productName
            if (productVariant.isNotEmpty()) {
                binding?.createReviewProductVariant?.apply {
                    text = resources.getString(R.string.review_pending_variant, productVariant)
                    show()
                }
            }
        }
    }

    private fun hideScoreWidgetAndDivider() {
        binding?.createReviewScoreDivider?.hide()
        binding?.createReviewScore?.hide()
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

    private fun hideIncentiveText() {
        binding?.incentiveHelperText?.hide()
    }

    private fun setHelperText(textLength: Int) {
        binding?.incentiveHelperText?.apply {
            incentiveHelper = when {
                textLength >= REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_text_area_eligible_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_text_area_eligible_challenge) ?: ""
                    } else {
                        ""
                    }
                }
                textLength < REVIEW_INCENTIVE_MINIMUM_THRESHOLD && textLength != 0 -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_text_area_partial_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_text_area_partial_challenge) ?: ""
                    } else {
                        ""
                    }
                }
                else -> {
                    if (hasIncentive()) {
                        context?.getString(R.string.review_create_text_area_empty_incentive) ?: ""
                    } else if (hasOngoingChallenge()) {
                        context?.getString(R.string.review_create_text_area_empty_challenge) ?: ""
                    } else {
                        ""
                    }
                }
            }
            text = incentiveHelper
            show()
        }
    }

    private fun updateProductId(productId: String) {
        this.productId = productId
    }

    fun getOrderId(): String {
        return (createReviewViewModel.getReputationDataForm.value as? CoroutineSuccess<ProductRevGetForm>)?.data?.productrevGetForm?.orderID
            ?: ""
    }

    fun showCancelDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val defaultTitle = getString(R.string.review_create_dialog_title)
                if (isEditMode) {
                    setTitle(getString(R.string.review_edit_dialog_title))
                    setDescription(getString(R.string.review_edit_dialog_subtitle))
                } else {
                    setTitle(defaultTitle)
                    setDescription(getString(R.string.review_create_dialog_body))
                }
                setPrimaryCTAText(getString(R.string.review_edit_dialog_continue_writing))
                setPrimaryCTAClickListener {
                    dismiss()
                    CreateReviewTracking.eventClickContinueWrite(defaultTitle)
                }
                setSecondaryCTAText(getString(R.string.review_edit_dialog_exit))
                setSecondaryCTAClickListener {
                    CreateReviewTracking.eventClickLeavePage(defaultTitle)
                    if (activity?.isTaskRoot == true) {
                        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                        startActivity(intent)
                    } else {
                        dismiss()
                    }
                    activity?.finish()
                }
                show()
                CreateReviewTracking.eventViewDialog(defaultTitle)
            }
        }
    }

    private fun getThankYouFormTrackerData(): ThankYouBottomSheetTrackerData {
        return ThankYouBottomSheetTrackerData(
            reputationId,
            getOrderId(),
            productId,
            createReviewViewModel.getUserId(),
            getFeedbackId()
        )
    }

    private fun getFeedbackId(): String {
        return (createReviewViewModel.submitReviewResult.value as? Success)?.data ?: ""
    }
}