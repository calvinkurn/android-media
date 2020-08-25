package com.tokopedia.review.feature.createreputation.presentation.fragment

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.device.info.DevicePerformanceInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.createreputation.presentation.adapter.ImageReviewAdapter
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextAreaBottomSheet
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.model.Reputation
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_create_review.*
import kotlinx.android.synthetic.main.widget_create_review_text_area.*
import kotlinx.android.synthetic.main.widget_create_review_text_area.view.*
import kotlinx.android.synthetic.main.widget_create_review_text_area.view.createReviewEditText
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewFragment : BaseDaggerFragment(),
        ImageClickListener, TextAreaListener, ReviewScoreClickListener {

    companion object {
        private const val REQUEST_CODE_IMAGE = 111
        private const val PRODUCT_ID_REVIEW = "PRODUCT_ID"
        private const val REPUTATION_ID = "REPUTATION_ID"
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"

        private const val LOTTIE_ANIM_1 = "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_1.json"
        private const val LOTTIE_ANIM_2 = "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_2.json"
        private const val LOTTIE_ANIM_3 = "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_3.json"
        private const val LOTTIE_ANIM_4 = "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_4.json"
        private const val LOTTIE_ANIM_5 = "https://ecs7.tokopedia.net/android/reputation/lottie_anim_pedi_5.json"

        private const val IMAGE_PEDIE_1 = "https://ecs7.tokopedia.net/android/pedie/1star.png"
        private const val IMAGE_PEDIE_2 = "https://ecs7.tokopedia.net/android/pedie/2star.png"
        private const val IMAGE_PEDIE_3 = "https://ecs7.tokopedia.net/android/pedie/3star.png"
        private const val IMAGE_PEDIE_4 = "https://ecs7.tokopedia.net/android/pedie/4star.png"
        private const val IMAGE_PEDIE_5 = "https://ecs7.tokopedia.net/android/pedie/5star.png"

        private const val RATING_1 = 1
        private const val RATING_2 = 2
        private const val RATING_3 = 3
        private const val RATING_4 = 4
        private const val RATING_5 = 5

        fun createInstance(productId: String, reviewId: String, reviewClickAt: Int = 0, isEditMode: Boolean, feedbackId: Int) = CreateReviewFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID_REVIEW, productId)
                putString(REPUTATION_ID, reviewId)
                putInt(REVIEW_CLICK_AT, reviewClickAt)
                putBoolean(ReviewConstants.PARAM_IS_EDIT_MODE, isEditMode)
                putInt(ReviewConstants.PARAM_FEEDBACK_ID, feedbackId)
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

    private var shouldPlayAnimation: Boolean = true
    private var reviewClickAt: Int = 0
    private var reputationId: Int = 0
    private var productId: Int = 0
    private var shopId: String = ""
    private var isEditMode: Boolean = false
    private var feedbackId: Int = 0

    lateinit var imgAnimationView: LottieAnimationView
    private var textAreaBottomSheet: CreateReviewTextAreaBottomSheet? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerCreateReviewComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_review, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)

        arguments?.let {
            productId = it.getString(PRODUCT_ID_REVIEW, "").toIntOrNull() ?: 0
            reviewClickAt = it.getInt(REVIEW_CLICK_AT, 0)
            reputationId = it.getString(REPUTATION_ID, "").toIntOrNull() ?: 0
            isEditMode = it.getBoolean(ReviewConstants.PARAM_IS_EDIT_MODE, false)
            feedbackId = it.getInt(ReviewConstants.PARAM_FEEDBACK_ID, 0)
        }

        if (reviewClickAt > CreateReviewActivity.DEFAULT_PRODUCT_RATING || reviewClickAt < 0) {
            reviewClickAt = CreateReviewActivity.DEFAULT_PRODUCT_RATING
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createReviewViewModel.getReputationDataForm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CoroutineSuccess -> onSuccessGetReviewForm(it.data)
                is CoroutineFail -> onErrorGetReviewForm(it.throwable)
            }
        })

        createReviewViewModel.incentiveOvo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CoroutineSuccess -> onSuccessGetIncentiveOvo(it.data)
                is CoroutineFail -> onErrorGetIncentiveOvo()
            }
        })

        createReviewViewModel.reviewDetails.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> onSuccessGetReviewDetail(it.data)
                is Fail -> onFailGetReviewDetail(it.fail)
            }
        })

        createReviewViewModel.submitReviewResult.observe(viewLifecycleOwner, Observer {
            when(it) {
                is LoadingView -> {
                    showLoading()
                }
                is Success -> {
                    onSuccessSubmitReview()
                }
                is Fail -> {
                    onFailSubmitReview()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLowDevice = DevicePerformanceInfo.isLow(context)
        initCreateReviewTextArea()
        initEmptyPhoto()
        initAnonymousText()
        if(isEditMode) {
            hideScoreWidgetAndDivider()
            getReviewDetailData()
        } else {
            getReviewData()
            getIncentiveOvoData()
        }
        animatedReviewPicker = view.findViewById(R.id.animatedReview)
        imgAnimationView = view.findViewById(R.id.img_animation_review)
        animatedReviewPicker.resetStars()
        animatedReviewPicker.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                CreateReviewTracking.reviewOnRatingChangedTracker(
                        getOrderId(),
                        productId.toString(10),
                        (position).toString(10),
                        true,
                        isEditMode,
                        feedbackId.toString()
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
        if(!isEditMode) {
            animatedReviewPicker.renderInitialReviewWithData(reviewClickAt)
            updateViewBasedOnSelectedRating(if (reviewClickAt != 0) reviewClickAt else 5)
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

        createReviewAnonymousCheckbox.setOnClickListener {
            if (createReviewAnonymousCheckbox.isChecked) {
                CreateReviewTracking.reviewOnAnonymousClickTracker(getOrderId(), productId.toString(10), isEditMode, feedbackId.toString())
            }
            clearFocusAndHideSoftInput(view)
        }

        review_container.setOnTouchListener { _, _ ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }

        rv_img_review.setOnTouchListener { _, _ ->
            clearFocusAndHideSoftInput(view)
            return@setOnTouchListener false
        }

        rv_img_review?.adapter = imageAdapter

        createReviewSubmitButton.setOnClickListener {
            submitReview()
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
            val builder = ImagePickerBuilder(getString(R.string.image_picker_title),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_CAMERA, ImagePickerTabTypeDef.TYPE_GALLERY),
                    GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST,
                                    ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                            false, null),
                    ImagePickerMultipleSelectionBuilder(
                            createReviewViewModel.getSelectedImagesUrl(), null, -1, 5
                    ))

            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onRemoveImageClick(item: BaseImageReviewUiModel) {
        imageAdapter.setImageReviewData(createReviewViewModel.removeImage(item))
        if(imageAdapter.isEmpty()) {
            rv_img_review.hide()
            createReviewAddPhotoEmpty.show()
        }
    }

    override fun onExpandButtonClicked(text: String) {
        CreateReviewTracking.onExpandTextBoxClicked(getOrderId(), productId.toString())
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(this, text)
        (textAreaBottomSheet as BottomSheetUnify).setTitle(createReviewTextAreaTitle.text.toString())
        fragmentManager?.let { textAreaBottomSheet?.show(it,"") }
    }

    override fun onCollapseButtonClicked(text: String) {
        CreateReviewTracking.onCollapseTextBoxClicked(getOrderId(), productId.toString())
        textAreaBottomSheet?.dismiss()
    }

    override fun onDismissBottomSheet(text: String) {
        createReviewExpandableTextArea.setText(text)
        clearFocusAndHideSoftInput(view)
    }

    override fun scrollToShowTextArea() {
        createReviewScrollView.smoothScrollTo(0, createReviewDivider.bottom)
    }

    override fun trackWhenHasFocus(isEmpty: Boolean) {
        CreateReviewTracking.reviewOnMessageChangedTracker(getOrderId(), productId.toString(), isEmpty, isEditMode, feedbackId.toString())
    }

    override fun onReviewScoreClicked(score: Int): Boolean {
        CreateReviewTracking.eventClickSmiley(getOrderId(), productId.toString())
        createReviewScore.onScoreSelected(score)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    createReviewViewModel.clearImageData()

                    CreateReviewTracking.reviewOnImageUploadTracker(
                            getOrderId(),
                            productId.toString(10),
                            true,
                            selectedImage.size.toString(10),
                            isEditMode,
                            feedbackId.toString()
                    )

                    val imageListData = createReviewViewModel.getImageList(selectedImage)
                    if (selectedImage.isNotEmpty()) {
                        imageAdapter.setImageReviewData(imageListData)
                        rv_img_review.show()
                        createReviewAddPhotoEmpty.hide()
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

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo()
    }

    private fun submitReview() {
        val reviewMessage = createReviewExpandableTextArea.getText()
        CreateReviewTracking.reviewOnSubmitTracker(
                getOrderId(),
                productId.toString(10),
                reviewClickAt.toString(10),
                reviewMessage.isEmpty(),
                createReviewViewModel.getSelectedImagesUrl().size.toString(10),
                createReviewAnonymousCheckbox.isChecked,
                isEditMode,
                feedbackId.toString()
        )
        if(isEditMode) {
            createReviewViewModel.editReview(feedbackId, reputationId, productId, shopId.toIntOrZero(),
                    createReviewScore.getScore(), reviewClickAt, reviewMessage, createReviewAnonymousCheckbox.isChecked)
        } else {
            createReviewViewModel.submitReview(reputationId, productId, shopId.toIntOrZero(),
                    createReviewScore.getScore(), reviewClickAt, reviewMessage, createReviewAnonymousCheckbox.isChecked)
        }
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        with(data.productrevGetForm) {
            if (!validToReview) {
                activity?.let {
                    Toast.makeText(it, R.string.review_already_submit, Toast.LENGTH_LONG).show()
                    finishIfRoot()
                }
                return
            }

            hideShimmering()
            showLayout()

            CreateReviewTracking.reviewOnViewTracker(orderID, productId.toString())

            shopId = shopData.shopID.toString()
            setProductDetail(productData.productName, productData.productVariant.variantName, productData.productImageURL)
            setReputation(reputation, shopData.shopName)
        }
    }

    private fun setReputation(reputation: Reputation, shopName: String) {
        with(reputation) {
            if(locked || filled || score != 0) {
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

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain) {
        data.productrevIncentiveOvo?.let {
            it.ticker.let {
                ovoPointsTicker.apply {
                    visibility = View.VISIBLE
                    tickerTitle = it.title
                    setHtmlDescription(it.subtitle)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            val bottomSheet: BottomSheetUnify = IncentiveOvoBottomSheet(data, "")
                            bottomSheet.isFullpage = true
                            fragmentManager?.let { bottomSheet.show(it, bottomSheet.tag)}
                            bottomSheet.setCloseClickListener {
                                ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker("")
                                bottomSheet.dismiss()
                            }
                            ReviewTracking.onClickReadSkIncentiveOvoTracker(tickerTitle, "")
                        }

                        override fun onDismiss() {
                            ReviewTracking.onClickDismissIncentiveOvoTracker(tickerTitle, "")
                        }
                    })
                    ReviewTracking.onSuccessGetIncentiveOvoTracker(tickerTitle, "")
                }
            }
        }
    }

    private fun onErrorGetIncentiveOvo() {
        ovoPointsTicker.visibility = View.GONE
    }

    private fun onSuccessGetReviewDetail(productrevGetReviewDetail: ProductrevGetReviewDetail) {
        hideShimmering()
        with(productrevGetReviewDetail) {
            setReview(review)
            setProduct(product)
            shopId = response.shopId
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview) {
        with(review) {
            createReviewExpandableTextArea.setText(reviewText)
            animatedReviewPicker.renderInitialReviewWithData(rating)
            playAnimation()
            updateViewBasedOnSelectedRating(rating)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createReviewAnonymousCheckbox.isChecked = sentAsAnonymous
            }
            if (attachments.isNotEmpty()) {
                createReviewViewModel.clearImageData()
                val imageListData = createReviewViewModel.getImageList(attachments)
                imageAdapter.setImageReviewData(imageListData)
                rv_img_review.show()
                createReviewAddPhotoEmpty.hide()
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
            activity?.let {
                Toast.makeText(it, R.string.review_error_not_found, Toast.LENGTH_LONG).show()
                finishIfRoot()
            }
        } else {
            NetworkErrorHelper.showEmptyState(context, review_root) {
                getReviewDetailData()
            }
        }
    }

    private fun updateViewBasedOnSelectedRating(position: Int) {
        when {
            position < RATING_3 -> {
                if(position == RATING_1) {
                    createReviewTextAreaTitle.text = resources.getString(R.string.review_create_worst_title)
                } else {
                    createReviewTextAreaTitle.text = resources.getString(R.string.review_create_negative_title)
                }
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_negative, createReviewViewModel.getUserName()))
                createReviewContainer.setContainerColor(ContainerUnify.RED)
            }
            position == RATING_3 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_neutral, createReviewViewModel.getUserName()))
                createReviewContainer.setContainerColor(ContainerUnify.YELLOW)
                createReviewTextAreaTitle.text = resources.getString(R.string.review_create_neutral_title)
            }
            else -> {
                if(position == RATING_4) {
                    createReviewTextAreaTitle.text = resources.getString(R.string.review_create_positive_title)
                } else {
                    createReviewTextAreaTitle.text = resources.getString(R.string.review_create_best_title)
                }
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_positive, createReviewViewModel.getUserName()))
                createReviewContainer.setContainerColor(ContainerUnify.GREEN)
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
        ImageHandler.loadImageWithoutPlaceholder(img_animation_review, url)
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

            lottieCompositionLottieTask.addFailureListener { throwable -> }
        }
    }

    private fun onSuccessSubmitReview() {
        stopLoading()
        showLayout()
        finishIfRoot(true)
    }

    private fun onFailSubmitReview() {
        stopLoading()
        showLayout()
        showToasterError(getString(R.string.review_create_fail_toaster))
    }

    private fun showShimmering() {
        shimmering_create_review.show()
    }

    private fun hideShimmering() {
        shimmering_create_review.hide()
    }

    private fun showLoading() {
        createReviewSubmitButton.isLoading = true
    }

    private fun stopLoading() {
        createReviewSubmitButton.isLoading = false
    }

    private fun showLayout() {
        createReviewScrollView.show()
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {
        hideShimmering()
        if (throwable is MessageErrorException) {
            activity?.let {
                Toast.makeText(it, R.string.review_error_not_found, Toast.LENGTH_LONG).show()
                finishIfRoot()
            }
        } else {
            NetworkErrorHelper.showEmptyState(context, review_root) {
                getReviewData()
            }
        }

    }

    private fun finishIfRoot(success: Boolean = false) {
        activity?.run {
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.putExtra(ReviewConstants.ARGS_RATING, reviewClickAt.toFloat())
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }
    }

    private fun clearFocusAndHideSoftInput(view: View?) {
        createReviewEditText.clearFocus()
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initCreateReviewTextArea() {
        createReviewExpandableTextArea.apply {
            setListener(this@CreateReviewFragment)
            addOnImpressionListener(ImpressHolder()) {
                    CreateReviewTracking.reviewOnScoreVisible(getOrderId(), productId.toString())
            }
        }
    }

    private fun initEmptyPhoto() {
        createReviewAddPhotoEmpty.setOnClickListener {
            onAddImageClick()
        }
    }

    private fun initAnonymousText() {
        createReviewAnonymousText.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createReviewAnonymousCheckbox.isChecked = !createReviewAnonymousCheckbox.isChecked
            }
            if (createReviewAnonymousCheckbox.isChecked) {
                CreateReviewTracking.reviewOnAnonymousClickTracker(getOrderId(), productId.toString(10), isEditMode, feedbackId.toString())
            }
        }
    }

    private fun setProductDetail(productName: String, productVariant: String, productImageUrl: String) {
        createReviewProductImage.loadImage(productImageUrl)
        createReviewProductName.apply {
            text = productName
            if(productVariant.isNotEmpty()) {
                createReviewProductVariant.apply{
                    text = resources.getString(R.string.review_pending_variant, productVariant)
                    show()
                }
            }
        }
    }

    private fun hideScoreWidgetAndDivider() {
        createReviewScoreDivider.hide()
        createReviewScore.hide()
    }

    fun getOrderId(): String {
        return (createReviewViewModel.getReputationDataForm.value as? CoroutineSuccess<ProductRevGetForm>)?.data?.productrevGetForm?.orderID ?: ""
    }

    fun showCancelDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                if(isEditMode) {
                    setTitle(getString(R.string.review_edit_dialog_title))
                    setDescription(getString(R.string.review_edit_dialog_subtitle))
                } else {
                    setTitle(getString(R.string.review_create_dialog_title))
                    setDescription(getString(R.string.review_create_dialog_body))
                }
                setPrimaryCTAText(getString(R.string.review_edit_dialog_continue_writing))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAText(getString(R.string.review_edit_dialog_exit))
                setSecondaryCTAClickListener {
                    if (activity?.isTaskRoot == true) {
                        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                        startActivity(intent)
                    } else {
                        dismiss()
                    }
                    activity?.finish()
                }
                show()
            }
        }
    }
}