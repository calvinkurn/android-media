package com.tokopedia.review.feature.createreputation.ui.fragment

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.ui.activity.CreateReviewActivity
import com.tokopedia.review.feature.createreputation.ui.adapter.ImageReviewAdapter
import com.tokopedia.review.feature.createreputation.ui.listener.ImageClickListener
import com.tokopedia.review.feature.createreputation.ui.listener.TextAreaListener
import com.tokopedia.review.feature.createreputation.ui.widget.CreateReviewTextAreaBottomSheet
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_create_review.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewFragment : BaseDaggerFragment(), ImageClickListener, TextAreaListener, OnBackPressedListener {

    companion object {
        private const val REQUEST_CODE_IMAGE = 111
        private const val DEFAULT_REVIEW_ID = "0"
        private const val PRODUCT_ID_REVIEW = "PRODUCT_ID"
        private const val REVIEW_ID = "REVIEW_ID"
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"
        const val REVIEW_ORDER_ID = "REVIEW_ORDER_ID"
        const val UTM_SOURCE = "UTM_SOURCE"

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

        fun createInstance(productId: String, reviewId: String, reviewClickAt: Int = 0, utmSource: String, isEditMode: Boolean, feedbackId: Int) = CreateReviewFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID_REVIEW, productId)
                putString(REVIEW_ID, reviewId)
                putInt(REVIEW_CLICK_AT, reviewClickAt)
                putString(UTM_SOURCE, utmSource)
                putBoolean(ReviewConstants.PARAM_IS_EDIT_MODE, isEditMode)
                putInt(ReviewConstants.PARAM_FEEDBACK_ID, feedbackId)
            }
        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel

    private lateinit var animatedReviewPicker: AnimatedReputationView
    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this)
    }
    private var isLowDevice = false
    private var selectedImage: ArrayList<String> = arrayListOf()

    private var isImageAdded: Boolean = false
    private var shouldPlayAnimation: Boolean = true
    private var reviewClickAt: Int = 0
    private var reviewId: Int = 0
    private var productId: Int = 0
    private var productRevGetForm: ProductRevGetForm = ProductRevGetForm()
    private var productRevIncentiveOvo: ProductRevIncentiveOvoDomain = ProductRevIncentiveOvoDomain()
    private var shopId: String = ""
    private var orderId: String = ""
    private var utmSource: String = ""
    private var isEditMode: Boolean = false
    private var feedbackId: Int = 0

    val getOrderId
    get() = orderId

    val getIsEditMode
    get() = isEditMode

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

        arguments?.let {
            productId = it.getString(PRODUCT_ID_REVIEW, "").toIntOrNull() ?: 0
            orderId = it.getString(REVIEW_ORDER_ID) ?: ""
            reviewClickAt = it.getInt(REVIEW_CLICK_AT, 0)
            reviewId = it.getString(REVIEW_ID, "").toIntOrNull() ?: 0
            utmSource = it.getString(UTM_SOURCE, "")
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLowDevice = DevicePerformanceInfo.isLow(context)
        initCreateReviewTextArea()
        initEmptyPhoto()
        initAnonymousText()
        if(isEditMode) {
            getReviewDetailData()
        } else {
            getReviewData()
            getIncentiveOvoData()
        }
        animatedReviewPicker = view.findViewById(R.id.animatedReview)
        imgAnimationView = view.findViewById(R.id.img_animation_review)
        animatedReviewPicker.resetStars()
        animatedReviewPicker.setListener(object : AnimatedReputationView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                ReviewTracking.reviewOnRatingChangedTracker(
                        orderId,
                        productId.toString(10),
                        (position).toString(10),
                        true,
                        false
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
        animatedReviewPicker.renderInitialReviewWithData(reviewClickAt)
        updateViewBasedOnSelectedRating(if (reviewClickAt != 0) reviewClickAt else 5)

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
                ReviewTracking.reviewOnAnonymousClickTracker(orderId, productId.toString(10), false)
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
        imageAdapter.setImageReviewData(createReviewViewModel.initImageData())

        createReviewSubmitButton.setOnClickListener {
            submitReview()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        createReviewViewModel.getReputationDataForm.removeObservers(this)
    }

    override fun onAddImageClick() {
        clearFocusAndHideSoftInput(view)
        context?.let {
            val builder = ImagePickerBuilder(getString(R.string.image_picker_title),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA),
                    GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST,
                                    ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                            false, null),
                    ImagePickerMultipleSelectionBuilder(
                            selectedImage, null, -1, 5
                    ))

            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onRemoveImageClick(item: BaseImageReviewViewModel) {
        imageAdapter.removeItem(item)
    }

    override fun onExpandButtonClicked(text: String) {
        textAreaBottomSheet = CreateReviewTextAreaBottomSheet.createNewInstance(this, text)
        fragmentManager?.let { textAreaBottomSheet?.show(it,"") }
    }

    override fun onCollapseButtonClicked(text: String) {
        textAreaBottomSheet?.dismiss()
        createReviewExpandableTextArea.setText(text)
    }

    override fun onBackPressed() {

    }

    private fun getReviewData() {
        showShimmering()
        createReviewViewModel.getProductReputation(productId, reviewId)
    }

    private fun getReviewDetailData() {
        showShimmering()
        createReviewViewModel.getReviewDetails(feedbackId)
    }

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo()
    }

    private fun submitReview() {
        val reviewMessage = ""

        ReviewTracking.reviewOnSubmitTracker(
                orderId,
                productId.toString(10),
                reviewClickAt.toString(10),
                reviewMessage.isEmpty(),
                selectedImage.size.toString(10),
                createReviewAnonymousCheckbox.isChecked,
                false
        )

        createReviewViewModel.submitReview(DEFAULT_REVIEW_ID, reviewId.toString(), productId.toString(),
                shopId, reviewMessage, reviewClickAt.toFloat(), selectedImage, createReviewAnonymousCheckbox.isChecked, utmSource)
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        productRevGetForm = data
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

            ReviewTracking.reviewOnViewTracker(orderID, productId.toString())

            shopId = shopData.shopID.toString()
            orderId = orderID
            setProductDetail(productData.productName, productData.productVariant.variantName, productData.productImageURL)
        }
    }

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain) {
        productRevIncentiveOvo = data
        data.productrevIncentiveOvo?.let {
            it.ticker?.let {
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
        }
    }

    private fun setReview(review: ProductrevGetReviewDetailReview) {
        with(review) {
            createReviewExpandableTextArea.setText(reviewText)
            updateViewBasedOnSelectedRating(rating)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createReviewAnonymousCheckbox.isChecked = sentAsAnonymous
            }
        }
    }

    private fun setProduct(product: ProductrevGetReviewDetailProduct) {
        with(product) {
            setProductDetail(productName, productVariantName, productImageUrl)
        }
    }

    private fun setReputation(reputation: ProductrevGetReviewDetailReputation) {

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
            position < 3 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_negative, createReviewViewModel.userSessionInterface.name))
                createReviewContainer.setContainerColor(ContainerUnify.RED)
                createReviewTextAreaTitle.text = resources.getString(R.string.review_create_negative_title)
            }
            position == 3 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_neutral, createReviewViewModel.userSessionInterface.name))
                createReviewContainer.setContainerColor(ContainerUnify.YELLOW)
                createReviewTextAreaTitle.text = resources.getString(R.string.review_create_neutral_title)
            }
            else -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_positive, createReviewViewModel.userSessionInterface.name))
                createReviewContainer.setContainerColor(ContainerUnify.GREEN)
                createReviewTextAreaTitle.text = resources.getString(R.string.review_create_positive_title)
            }
        }
    }

    private fun playAnimation() {
        if (shouldPlayAnimation && ::imgAnimationView.isInitialized) {
            generateAnimationByIndex(animatedReviewPicker.getReviewClickAt())
            shouldPlayAnimation = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedImage = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    createReviewViewModel.initImageData()

                    ReviewTracking.reviewOnImageUploadTracker(
                            orderId,
                            productId.toString(10),
                            true,
                            selectedImage.size.toString(10),
                            false
                    )

                    val imageListData = createReviewViewModel.getImageList(selectedImage)
                    if (selectedImage.isNotEmpty()) {
                        if (!isImageAdded) {
                            isImageAdded = true
                        }
                        imageAdapter.setImageReviewData(imageListData)
                        rv_img_review.show()
                        createReviewAddPhotoEmpty.hide()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun generatePeddieImageByIndex() {
        val url = when (animatedReviewPicker.getReviewClickAt()) {
            1 -> IMAGE_PEDIE_1
            2 -> IMAGE_PEDIE_2
            3 -> IMAGE_PEDIE_3
            4 -> IMAGE_PEDIE_4
            5 -> IMAGE_PEDIE_5
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
                1 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_1)
                }
                2 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_2)
                }
                3 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_3)
                }
                4 -> {
                    setLottieAnimationFromUrl(LOTTIE_ANIM_4)
                }
                5 -> {
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
        Handler(Looper.getMainLooper()).postDelayed({
            finishIfRoot(true)
        }, 800)
    }

    private fun showShimmering() {
        shimmering_create_review.show()
    }

    private fun hideShimmering() {
        shimmering_create_review.hide()
    }

    private fun showLoading() {
        hideLayout()
        progressBarReview.show()
    }

    private fun stopLoading() {
        progressBarReview.hide()
    }

    private fun hideLayout() {
        create_review_container.hide()
    }

    private fun showLayout() {
        create_review_container.show()
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.make(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
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
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initCreateReviewTextArea() {
        createReviewExpandableTextArea.setListener(this)
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
                ReviewTracking.reviewOnAnonymousClickTracker(orderId, productId.toString(10), false)
            }
        }
    }

    private fun setProductDetail(productName: String, productVariant: String, productImageUrl: String) {
        createReviewProductImage.loadImage(productImageUrl)
        createReviewProductName.apply {
            text = productName
            if(productVariant.isNotEmpty()) {
                maxLines = 1
                createReviewProductVariant.apply{
                    text = resources.getString(R.string.review_pending_variant, productVariant)
                    hide()
                }
            }
        }
    }

    fun showCancelDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.review_edit_dialog_title))
                setDescription(getString(R.string.review_edit_dialog_subtitle))
                setPrimaryCTAText(getString(R.string.review_edit_dialog_continue_writing))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAText(getString(R.string.review_edit_dialog_exit))
                setSecondaryCTAClickListener {
                    dismiss()
                    activity?.finish()
                }
                show()
            }
        }
    }
}