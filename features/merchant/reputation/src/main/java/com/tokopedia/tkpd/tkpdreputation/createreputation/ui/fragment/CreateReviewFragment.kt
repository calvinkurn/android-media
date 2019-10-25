package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import android.animation.Animator
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.reputation.common.view.AnimatedReviewPicker
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.fragment_create_review.*
import java.util.*
import javax.inject.Inject

class CreateReviewFragment : BaseDaggerFragment() {

    companion object {
        const val REQUEST_CODE_IMAGE = 111
        fun createInstance() = CreateReviewFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var animatedReviewPicker: AnimatedReviewPicker
    private lateinit var createReviewViewModel: CreateReviewViewModel
    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this::addImageClick)
    }
    private var selectedImage: ArrayList<String> = arrayListOf()
    private var isImageAdded: Boolean = false
    private var shouldPlayAnimation: Boolean = true

    override fun getScreenName(): String = ""

    override fun initInjector() {
        context?.let {
            val appComponent = (it.applicationContext as BaseMainApplication).baseAppComponent
            DaggerCreateReviewComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_review, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createReviewViewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateReviewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animatedReviewPicker = view.findViewById(R.id.animatedReview)
        stepper_review.max = 3F
        stepper_review.progress = 1F
        animatedReviewPicker.setListener(object : AnimatedReviewPicker.AnimatedReviewPickerListener {
            override fun onStarsClick(position: Int) {
                shouldPlayAnimation = true
                playAnimation()
            }
        })
        animatedReviewPicker.renderInitialReviewWithData(0)

        img_animation_review.addAnimatorListener(object : Animator.AnimatorListener {
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
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    stepper_review.progress = stepper_review.progress - 1F
                } else {
                    stepper_review.progress = stepper_review.progress + 1F
                }
            }

        })

        rv_img_review.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        imageAdapter.setImageReviewData(createReviewViewModel.initImageData())
    }

    private fun playAnimation() {
        if (!img_animation_review.isAnimating && shouldPlayAnimation) {
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
                            stepper_review.progress = stepper_review.progress + 1F
                        }
                        imageAdapter.setImageReviewData(imageListData)
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun generateAnimationByIndex(index: Int) {
        when (index) {
            0 -> img_animation_review.playAnimation()
            1 -> img_animation_review.playAnimation()
            2 -> img_animation_review.playAnimation()
            3 -> img_animation_review.playAnimation()
            4 -> img_animation_review.playAnimation()
        }
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
}