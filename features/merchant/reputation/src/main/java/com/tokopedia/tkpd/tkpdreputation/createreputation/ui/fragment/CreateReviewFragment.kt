package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ReviewLottieModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.fragment_create_review.*
import java.util.*


class CreateReviewFragment : BaseDaggerFragment() {

    companion object {
        const val REQUEST_CODE_IMAGE = 111
        const val TOTAL_REVIEW_STARS = 4
        fun createInstance() = CreateReviewFragment()
    }

    private val imageAdapter: ImageReviewAdapter by lazy {
        ImageReviewAdapter(this::addImageClick)
    }

    private var imageData: MutableList<BaseImageReviewViewModel> = mutableListOf()
    private var selectedImage: ArrayList<String> = arrayListOf()
    private var listOfLottie: MutableList<ReviewLottieModel> = mutableListOf()
    private var isImageAdded: Boolean = false
    private var handle = Handler()
    private var count = 0
    private var lastReview = 0
    private var reviewClickAt = 0

    private val normalAnimation = object : Runnable {
        override fun run() {
            if (count <= TOTAL_REVIEW_STARS) {
                val reviewData = listOfLottie[count]
                if (isResversedAnim(reviewData)) {
                    reviewData.isAnimated = false
                    reviewData.reviewView.reverseAnimationSpeed()
                    reviewData.reviewView.playAnimation()
                } else if (isNormalAnim(reviewData)) {
                    if (isAnimReversed(reviewData)) {
                        reviewData.reviewView.reverseAnimationSpeed()
                        reviewData.reviewView.playAnimation()
                    } else {
                        reviewData.reviewView.playAnimation()
                    }
                    reviewData.isAnimated = true
                }
                count++
                handle.postDelayed(this, 50)
            } else {
                lastReview = reviewClickAt
                count = 0
                handle.removeCallbacks(this)
            }
        }

        private fun isResversedAnim(reviewData: ReviewLottieModel): Boolean {
            return count > reviewClickAt && reviewClickAt <= lastReview && reviewData.isAnimated
        }

        private fun isNormalAnim(reviewData: ReviewLottieModel): Boolean {
            return count <= reviewClickAt && !reviewData.isAnimated
        }

        private fun isAnimReversed(reviewData: ReviewLottieModel): Boolean {
            return reviewData.reviewView.speed < 0.0
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepper_review.max = 3F
        stepper_review.progress = 1F
        setupViewData()
        listOfLottie.forEachIndexed { index, it ->
            it.reviewView.setOnClickListener {
                reviewClickAt = index
                handle.post(normalAnimation)
            }
        }

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
        initImageData()
    }

    private fun setupViewData() {
        listOfLottie = mutableListOf(ReviewLottieModel(false, lottie_star_1), ReviewLottieModel(false, lottie_star_2), ReviewLottieModel(false, lottie_star_3),
                ReviewLottieModel(false, lottie_star_4), ReviewLottieModel(false, lottie_star_5))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedImage = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    initImageData()
                    when (selectedImage.size) {
                        5 -> {
                            imageData = (selectedImage.take(4).map {
                                ImageReviewViewModel(it, shouldDisplayOverlay = true)
                            }).asReversed().toMutableList()
                        }
                        4 -> {
                            imageData.addAll(selectedImage.take(3).map {
                                ImageReviewViewModel(it, shouldDisplayOverlay = true)
                            }.asReversed())
                        }
                        else -> {
                            imageData.addAll(selectedImage.map {
                                ImageReviewViewModel(it, shouldDisplayOverlay = false)
                            }.asReversed())
                        }
                    }


                    if (selectedImage.isNotEmpty()) {
                        if (!isImageAdded) {
                            isImageAdded = true
                            stepper_review.progress = stepper_review.progress + 1F
                        }
                        imageAdapter.setImageReviewData(imageData)
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initImageData() {
        imageData.clear()
        imageData.add(DefaultImageReviewModel())
        imageAdapter.setImageReviewData(imageData)
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