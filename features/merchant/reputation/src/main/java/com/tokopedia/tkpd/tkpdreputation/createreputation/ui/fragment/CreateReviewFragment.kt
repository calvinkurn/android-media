package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import android.animation.Animator
import android.app.Activity
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter.ImageReviewAdapter
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Fail
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.LoadingView
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.Success
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity.ARGS_RATING
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment
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

    private var reviewUserName: String = ""
    lateinit var imgAnimationView: LottieAnimationView

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
            reviewClickAt = it.getInt(REVIEW_CLICK_AT, 0)
            reviewId = it.getString(REVIEW_ID, "").toIntOrNull() ?: 0
        }
        createReviewViewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateReviewViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createReviewViewModel.getReputationDataForm.observe(this, Observer {
            when (it) {
                is CoroutineSuccess -> {
                    productRevGetForm = it.data
                    onSuccessGetReviewForm(it.data)
                }
                is CoroutineFail -> onErrorGetReviewForm()
            }
        })

        createReviewViewModel.getSubmitReviewResponse.observe(this, Observer {
            when (it) {
                is LoadingView -> showLoading()
                is Fail -> {
                    stopLoading()
                    showToasterError(it.fail.message ?: "")
                }
                is Success -> {
                    stopLoading()
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

        edit_text_review.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

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

        rv_img_review.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        imageAdapter.setImageReviewData(createReviewViewModel.initImageData())

        btn_submit_review.setOnClickListener {
            submitReview()
        }
    }

    private fun getReviewData() {
        createReviewViewModel.getProductReputation(productId, reviewId)
    }

    private fun submitReview() {
        createReviewViewModel.submitReview(DEFAULT_REVIEW_ID, reviewId.toString(), productId.toString(),
                shopId, edit_text_review.text.toString(), reviewClickAt.toFloat(), selectedImage, anonymous_cb.isChecked)
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        ImageHandler.loadImage(context, img_review, data.productrevGetForm.productData.productImageURL, R.drawable.ic_loading_image)
        shopId = data.productrevGetForm.shopData.shopID.toString(10)
        txt_create.text = data.productrevGetForm.productData.productName
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
            Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {}

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            transitionDrawable(BitmapDrawable(context?.resources, resource))
                        }
                    })
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
        Handler(Looper.getMainLooper()).postDelayed({
            activity?.run {
                if (isTaskRoot) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.HOME)

                    setResult(Activity.RESULT_OK, intent)
                    startActivity(intent)
                } else {
                    val intent = Intent()
                    intent.putExtra(ARGS_RATING, reviewClickAt.toFloat())
                    setResult(InboxReputationDetailFragment.REQUEST_CODE_ON_SUCCESS_REVIEW, intent)
                    onBackPressed()
                }

                finish()
            }
        }, 700)
    }

    private fun showLoading() {
        progressBarReview.show()
    }

    private fun stopLoading() {
        progressBarReview.hide()
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun showToasterSuccess() {
        view?.let {
            Toaster.make(it, getString(R.string.txt_success_submit_review, createReviewViewModel.userSessionInterface.name), Toaster.LENGTH_LONG)
        }
    }

    private fun onErrorGetReviewForm() {
        NetworkErrorHelper.showEmptyState(context, review_root) {
            getReviewData()
        }
    }
}