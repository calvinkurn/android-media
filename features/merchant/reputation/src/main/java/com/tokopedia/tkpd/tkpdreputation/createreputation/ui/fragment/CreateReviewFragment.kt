package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.device.info.DevicePerformanceInfo
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevIncentiveOvo
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevIncentiveOvoResponse
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter.ImageReviewAdapter
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Fail
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.LoadingView
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Success
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.IncentiveOvoMapper
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity.ARGS_RATING
import com.tokopedia.tkpd.tkpdreputation.inbox.view.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_create_review_old.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewFragment : BaseDaggerFragment(), OnAddImageClickListener {

    companion object {
        private const val REQUEST_CODE_IMAGE = 111
        private const val DEFAULT_REVIEW_ID = "0"
        private const val PRODUCT_ID_REVIEW = "PRODUCT_ID"
        private const val REVIEW_ID = "REVIEW_ID"
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"
        const val REVIEW_ORDER_ID = "REVIEW_ORDER_ID"
        const val UTM_SOURCE = "UTM_SOURCE"

        private const val IMAGE_REVIEW_GREY_BG = "https://ecs7.tokopedia.net/android/others/1_2reviewbg.png"
        private const val IMAGE_REVIEW_GREEN_BG = "https://ecs7.tokopedia.net/android/others/3reviewbg.png"
        private const val IMAGE_REVIEW_YELLOW_BG = "https://ecs7.tokopedia.net/android/others/4_5reviewbg.png"
        private const val IMAGE_BG_TRANSITION = 250
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

        fun createInstance(productId: String, reviewId: String, reviewClickAt: Int = 0, utmSource: String) = CreateReviewFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID_REVIEW, productId)
                putString(REVIEW_ID, reviewId)
                putInt(REVIEW_CLICK_AT, reviewClickAt)
                putString(UTM_SOURCE, utmSource)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var reviewTracker: ReputationTracking

    private lateinit var animatedReviewPicker: AnimatedReputationView
    private lateinit var createReviewViewModel: CreateReviewViewModel
    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this)
    }
    private var isLowDevice = false
    private var selectedImage: ArrayList<String> = arrayListOf()

    private var isImageAdded: Boolean = false
    private var shouldPlayAnimation: Boolean = true
    private var shouldIncreaseProgressBar = true
    private var reviewClickAt: Int = 0
    private var reviewId: Int = 0
    private var productId: Int = 0
    private var currentBackground: Drawable? = null
    private var productRevGetForm: ProductRevGetForm = ProductRevGetForm()
    private var productRevIncentiveOvo: ProductRevIncentiveOvo = ProductRevIncentiveOvo()
    private var shopId: String = ""
    private var orderId: String = ""
    private var utmSource: String = ""

    private var reviewUserName: String = ""
    lateinit var imgAnimationView: LottieAnimationView

    val getOrderId: String
        get() = orderId

    override fun getScreenName(): String = ""

    override fun initInjector() {
        context?.let {
            DaggerReputationComponent
                    .builder()
                    .reputationModule(ReputationModule())
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_review_old, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getString(PRODUCT_ID_REVIEW, "").toIntOrNull() ?: 0
            orderId = it.getString(REVIEW_ORDER_ID) ?: ""
            reviewClickAt = it.getInt(REVIEW_CLICK_AT, 0)
            reviewId = it.getString(REVIEW_ID, "").toIntOrNull() ?: 0
            utmSource = it.getString(UTM_SOURCE, "")
        }

        if (reviewClickAt > CreateReviewActivityOld.DEFAULT_PRODUCT_RATING || reviewClickAt < 0) {
            reviewClickAt = CreateReviewActivityOld.DEFAULT_PRODUCT_RATING
        }

        createReviewViewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateReviewViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createReviewViewModel.getReputationDataForm.observe(this, Observer {
            when (it) {
                is CoroutineSuccess -> onSuccessGetReviewForm(it.data)
                is CoroutineFail -> onErrorGetReviewForm(it.throwable)
            }
        })

        createReviewViewModel.getSubmitReviewResponse.observe(this, Observer {
            when (it) {
                is LoadingView -> showLoading()
                is Fail -> {
                    stopLoading()
                    showLayout()
                    showToasterError(it.fail.message ?: "")
                }
                is Success -> {
                    showToasterSuccess()
                    onSuccessSubmitReview()
                }
            }
        })

        createReviewViewModel.incentiveOvo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CoroutineSuccess -> {
                    it.data.productrevIncentiveOvo.let { data ->
                        if(data != null) {
                            onSuccessGetIncentiveOvo(data)
                        } else {
                            onErrorGetIncentiveOvo()
                        }
                    }
                }
                is CoroutineFail -> onErrorGetIncentiveOvo()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewUserName = createReviewViewModel.userSessionInterface.name
        isLowDevice = DevicePerformanceInfo.isLow(context)

        getReviewData()
        getIncentiveOvoData()
        anonymous_text.text = generateAnonymousText()
        animatedReviewPicker = view.findViewById(R.id.animatedReview)
        imgAnimationView = view.findViewById(R.id.img_animation_review)
        animatedReviewPicker.resetStars()
        animatedReviewPicker.setListener(object : AnimatedReputationView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                reviewTracker.reviewOnRatingChangedTracker(
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
                generateReviewBackground(position)
                clearFocusAndHideSoftInput(view)
            }
        })
        animatedReviewPicker.renderInitialReviewWithData(reviewClickAt)
        generateReviewBackground(if (reviewClickAt != 0) reviewClickAt else 5)
        stepper_review.progress = 1

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

        anonymous_cb.setOnClickListener {
            if (anonymous_cb.isChecked) {
                reviewTracker.reviewOnAnonymousClickTracker(orderId, productId.toString(10), false)
            }
            clearFocusAndHideSoftInput(view)
        }

        edit_text_review.setOnFocusChangeListener { _, _ ->
            if(!edit_text_review.isFocused) {
                reviewTracker.reviewOnMessageChangedTracker(
                        orderId,
                        productId.toString(10),
                        edit_text_review.text.toString().isEmpty(),
                        false
                )

                val editTextReview = edit_text_review.text.toString()
                if (editTextReview.isEmpty() && !shouldIncreaseProgressBar) {
                    shouldIncreaseProgressBar = true
                    stepper_review.progress = stepper_review.progress - 1
                } else if (editTextReview.isNotEmpty() && shouldIncreaseProgressBar) {
                    stepper_review.progress = stepper_review.progress + 1
                    shouldIncreaseProgressBar = false
                }
            }
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

        btn_submit_review.setOnClickListener {
            submitReview()
        }

        playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        createReviewViewModel.getReputationDataForm.removeObservers(this)
        createReviewViewModel.getSubmitReviewResponse.removeObservers(this)
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

    private fun getReviewData() {
        showShimmering()
        createReviewViewModel.getProductReputation(productId, reviewId)
    }

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo()
    }

    private fun submitReview() {
        val reviewMessage = edit_text_review.text.toString()

        reviewTracker.reviewOnSubmitTracker(
                orderId,
                productId.toString(10),
                reviewClickAt.toString(10),
                reviewMessage.isEmpty(),
                selectedImage.size.toString(10),
                anonymous_cb.isChecked,
                false
        )

        createReviewViewModel.submitReview(DEFAULT_REVIEW_ID, reviewId.toString(), productId.toString(),
                shopId, reviewMessage, reviewClickAt.toFloat(), selectedImage, anonymous_cb.isChecked, utmSource)
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

            reviewTracker.reviewOnViewTracker(orderID, productId.toString())
            img_review.loadImage(productData.productImageURL)

            shopId = shopData.shopID.toString()
            orderId = orderID
            txt_create.text = productData.productName
        }
    }

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoResponse) {
        with(data) {
            ovoPointsTicker?.apply {
                visibility = View.VISIBLE
                tickerTitle = ticker.title
                setHtmlDescription(ticker.subtitle)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        val bottomSheet: BottomSheetUnify = IncentiveOvoBottomSheet(IncentiveOvoMapper.mapIncentiveOvoReviewtoIncentiveOvoInbox(data), "")
                        fragmentManager?.let { bottomSheet.show(it, bottomSheet.tag)}
                        reviewTracker.onClickReadSkIncentiveOvoTracker(tickerTitle, "")
                    }

                    override fun onDismiss() {
                        reviewTracker.onClickDismissIncentiveOvoTracker(tickerTitle, "")
                    }
                })
                reviewTracker.onSuccessGetIncentiveOvoTracker(tickerTitle, "")
            }
        }
    }

    private fun onErrorGetIncentiveOvo() {
        ovoPointsTicker.visibility = View.GONE
    }

    private fun generateReviewBackground(position: Int) {
        when (position) {
            1 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_1, reviewUserName))
                renderBackgroundTransition(IMAGE_REVIEW_GREY_BG)
            }
            2 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_2, reviewUserName))
                renderBackgroundTransition(IMAGE_REVIEW_GREY_BG)
            }
            3 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_3, reviewUserName))
                renderBackgroundTransition(IMAGE_REVIEW_GREEN_BG)
            }
            4 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_4, reviewUserName))
                renderBackgroundTransition(IMAGE_REVIEW_YELLOW_BG)
            }
            5 -> {
                txt_review_desc.text = MethodChecker.fromHtml(getString(R.string.review_text_5, reviewUserName))
                renderBackgroundTransition(IMAGE_REVIEW_YELLOW_BG)
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

                    reviewTracker.reviewOnImageUploadTracker(
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
                            stepper_review.progress = stepper_review.progress + 1
                        }
                        imageAdapter.setImageReviewData(imageListData)
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // For transition background drawable *grey,green,yellow
    private fun transitionDrawable(drawable: Drawable) {
        if (currentBackground == null) {
            currentBackground = drawable
            review_bg.setImageDrawable(drawable)
        } else {
            val transitionDrawable = TransitionDrawable(arrayOf(currentBackground, drawable))
            transitionDrawable.isCrossFadeEnabled = true
            review_bg.setImageDrawable(transitionDrawable)
            transitionDrawable.startTransition(IMAGE_BG_TRANSITION)
            currentBackground = drawable
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

    private fun renderBackgroundTransition(url: String) {
        context?.run {
            val customTarget = BackgroundCustomTarget(this)
            customTarget.callback = ::transitionDrawable
            Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(customTarget)
        }
    }

    private fun generateAnonymousText(): String {
        if (reviewUserName.isNotEmpty()) {
            val firstChar = reviewUserName.firstOrNull() ?: ""
            val lastChar = reviewUserName.lastOrNull() ?: ""
            return getString(
                    R.string.anonymous_review_prefix,
                    "$firstChar***$lastChar")
        }

        return ""
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
        btn_submit_container.hide()
        create_review_container.hide()
    }

    private fun showLayout() {
        btn_submit_container.show()
        create_review_container.show()
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.make(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
        }
    }

    private fun showToasterSuccess() {
        view?.let {
            Toaster.make(it, getString(R.string.txt_success_submit_review, createReviewViewModel.userSessionInterface.name), Toaster.toasterLength)
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
                intent.putExtra(ARGS_RATING, reviewClickAt.toFloat())
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }
    }

    private fun clearFocusAndHideSoftInput(view: View?) {
        edit_text_review.clearFocus()
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    class BackgroundCustomTarget(private val context: Context) : CustomTarget<Bitmap>() {
        var callback: ((Drawable) -> Unit)? = null
        override fun onLoadCleared(placeholder: Drawable?) {}

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            callback?.invoke(BitmapDrawable(context.resources, resource))
        }
    }
}
