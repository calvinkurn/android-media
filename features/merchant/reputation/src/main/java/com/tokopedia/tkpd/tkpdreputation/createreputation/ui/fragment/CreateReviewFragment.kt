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
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.base.di.component.AppComponent
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
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter.ImageReviewAdapter
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Fail
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.LoadingView
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Success
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity.ARGS_RATING
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_create_review.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewFragment : BaseDaggerFragment() {

    companion object {
        private const val REQUEST_CODE_IMAGE = 111
        private const val DEFAULT_REVIEW_ID = "0"
        private const val PRODUCT_ID_REVIEW = "PRODUCT_ID"
        private const val REVIEW_ID = "REVIEW_ID"
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"
        const val REVIEW_ORDER_ID = "REVIEW_ORDER_ID"

        private const val IMAGE_REVIEW_GREY_BG = "https://ecs7.tokopedia.net/android/others/1_2reviewbg.png"
        private const val IMAGE_REVIEW_GREEN_BG = "https://ecs7.tokopedia.net/android/others/3reviewbg.png"
        private const val IMAGE_REVIEW_YELLOW_BG = "https://ecs7.tokopedia.net/android/others/4_5reviewbg.png"
        private const val IMAGE_BG_TRANSITION = 250

        fun createInstance(productId: String, reviewId: String, reviewClickAt: Int = 0) = CreateReviewFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID_REVIEW, productId)
                putString(REVIEW_ID, reviewId)
                putInt(REVIEW_CLICK_AT, reviewClickAt)
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
        ImageReviewAdapter(this::addImageClick)
    }
    private var selectedImage: ArrayList<String> = arrayListOf()

    private var isImageAdded: Boolean = false
    private var shouldPlayAnimation: Boolean = true
    private var shouldIncreaseProgressBar = true
    private var reviewClickAt: Int = 0
    private var reviewId: Int = 0
    private var productId: Int = 0
    private var currentBackground: Drawable? = null
    private var productRevGetForm: ProductRevGetForm = ProductRevGetForm()
    private var shopId: String = ""
    private var orderId: String = ""

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
                    .appComponent(getComponent(AppComponent::class.java))
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewUserName = createReviewViewModel.userSessionInterface.name
        getReviewData()
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
                        true
                )
                reviewClickAt = position
                shouldPlayAnimation = true
                playAnimation()
                generateReviewBackground(position)
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
                reviewTracker.reviewOnAnonymousClickTracker(orderId, productId.toString(10))
            }
        }

        edit_text_review.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                reviewTracker.reviewOnMessageChangedTracker(orderId, productId.toString(10), s.toString().isEmpty())

                if (s.toString().isEmpty() && !shouldIncreaseProgressBar) {
                    shouldIncreaseProgressBar = true
                    stepper_review.progress = stepper_review.progress - 1
                } else if (shouldIncreaseProgressBar) {
                    stepper_review.progress = stepper_review.progress + 1
                    shouldIncreaseProgressBar = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        rv_img_review?.adapter = imageAdapter
        imageAdapter.setImageReviewData(createReviewViewModel.initImageData())

        btn_submit_review.setOnClickListener {
            submitReview()
        }
    }

    private fun getReviewData() {
        showShimmering()
        createReviewViewModel.getProductReputation(productId, reviewId)
    }

    private fun submitReview() {
        val reviewMessage = edit_text_review.text.toString()

        reviewTracker.reviewOnSubmitTracker(
                orderId,
                productId.toString(10),
                reviewClickAt.toString(10),
                reviewMessage.isEmpty(),
                selectedImage.size.toString(10),
                anonymous_cb.isChecked
        )

        createReviewViewModel.submitReview(DEFAULT_REVIEW_ID, reviewId.toString(), productId.toString(),
                shopId, reviewMessage, reviewClickAt.toFloat(), selectedImage, anonymous_cb.isChecked)
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
                            selectedImage.size.toString(10)
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

    private fun generateAnimationByIndex(index: Int) {
        imgAnimationView.repeatCount = 0
        imgAnimationView.repeatCount = LottieDrawable.INFINITE
        when (index) {
            1 -> {
                imgAnimationView.setAnimation(R.raw.lottie_anim_pedi_1)
                imgAnimationView.playAnimation()
            }
            2 -> {
                imgAnimationView.setAnimation(R.raw.lottie_anim_pedi_2)
                imgAnimationView.playAnimation()
            }
            3 -> {
                imgAnimationView.setAnimation(R.raw.lottie_anim_pedi_3)
                imgAnimationView.playAnimation()
            }
            4 -> {
                imgAnimationView.setAnimation(R.raw.lottie_anim_pedi_4)
                imgAnimationView.playAnimation()
            }
            5 -> {
                imgAnimationView.setAnimation(R.raw.lottie_anim_pedi_5)
                imgAnimationView.playAnimation()
            }
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
            val firstName = reviewUserName.substringBefore(" ")

            return getString(
                    R.string.anonymous_review_prefix,
                    firstName.replaceRange(1, firstName.length - 1, "***")
            )
        }

        return ""
    }

    private fun addImageClick() {
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

    private fun onSuccessSubmitReview() {
        stopLoading()
        showLayout()
        Handler(Looper.getMainLooper()).postDelayed({
            finishIfRoot()
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
            showToasterError(throwable.message ?: "")
        } else {
            NetworkErrorHelper.showEmptyState(context, review_root) {
                getReviewData()
            }
        }

    }

    private fun finishIfRoot() {
        activity?.run {
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                setResult(Activity.RESULT_OK, intent)
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.putExtra(ARGS_RATING, reviewClickAt.toFloat())
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    class BackgroundCustomTarget(private val context: Context) : CustomTarget<Bitmap>() {
        var callback: ((Drawable) -> Unit)? = null
        override fun onLoadCleared(placeholder: Drawable?) {}

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            callback?.invoke(BitmapDrawable(context.resources, resource))
        }
    }
}