package com.tokopedia.review.feature.media.player.image.presentation.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.FragmentReviewMediaImagePlayerBinding
import com.tokopedia.review.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.base.presentation.viewmodel.ReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uimodel.ToasterUiModel
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.image.di.component.DaggerReviewImagePlayerComponent
import com.tokopedia.review.feature.media.player.image.di.qualifier.ReviewImagePlayerViewModelFactory
import com.tokopedia.review.feature.media.player.image.presentation.uistate.ReviewImagePlayerUiState
import com.tokopedia.review.feature.media.player.image.presentation.viewmodel.ReviewImagePlayerViewModel
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class ReviewImagePlayerFragment : BaseDaggerFragment() {

    companion object {
        private const val ARG_IMAGE_URI = "argImageUri"
        private const val ARG_SHOW_SEE_MORE = "argShowSeeMore"
        private const val ARG_TOTAL_MEDIA_COUNT = "argTotalMediaCount"
        private const val ZOOM_SCALE_FACTOR = 2f
        private const val UNZOOM_SCALE_FACTOR = 1f
        private const val TOASTER_KEY_ERROR_LOAD_IMAGE = "toasterKeyErrorLoadImage - %s"

        fun createInstance(
            imageUri: String,
            showSeeMore: Boolean,
            totalMediaCount: Int
        ): ReviewImagePlayerFragment {
            return ReviewImagePlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                    putBoolean(ARG_SHOW_SEE_MORE, showSeeMore)
                    putInt(ARG_TOTAL_MEDIA_COUNT, totalMediaCount)
                }
            }
        }
    }

    @Inject
    @ReviewImagePlayerViewModelFactory
    lateinit var reviewImagePlayerViewModelFactory: ViewModelProvider.Factory

    @Inject
    @ReviewMediaGalleryViewModelFactory
    lateinit var reviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(FragmentReviewMediaImagePlayerBinding::bind)
    private var listener: Listener? = null

    private val reviewImagePlayerViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), reviewImagePlayerViewModelFactory).get(
            getImageUri(), ReviewImagePlayerViewModel::class.java
        )
    }

    private val reviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), reviewMediaGalleryViewModelFactory).get(
            ReviewMediaGalleryViewModel::class.java
        )
    }

    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory).get(
            SharedReviewMediaGalleryViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        SplitCompat.installActivity(requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUiState(savedInstanceState)
        return FragmentReviewMediaImagePlayerBinding.inflate(
            inflater,
            container,
            false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        collectUiState()
        collectToasterEvent()
    }

    override fun onDestroyView() {
        binding?.imagePreviewReviewMediaImagePlayer?.mImageView?.clearImage()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        reviewImagePlayerViewModel.saveState(outState)
    }

    override fun getScreenName(): String {
        return ReviewImagePlayerFragment::class.simpleName.orEmpty()
    }

    override fun initInjector() {
        DaggerReviewImagePlayerComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .reviewMediaGalleryComponent(ReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .build()
            .inject(this)
    }

    private fun getImageUri(): String {
        return arguments?.getString(ARG_IMAGE_URI).orEmpty()
    }

    private fun getShowSeeMore(): Boolean {
        return arguments?.getBoolean(ARG_SHOW_SEE_MORE).orFalse()
    }

    private fun getTotalMediaCount(): Int {
        return arguments?.getInt(ARG_TOTAL_MEDIA_COUNT).orZero()
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            reviewImagePlayerViewModel.setImageUri(getImageUri())
            reviewImagePlayerViewModel.setShowSeeMore(getShowSeeMore())
            reviewImagePlayerViewModel.setTotalMediaCount(getTotalMediaCount())
        } else {
            reviewImagePlayerViewModel.restoreSavedState(savedInstanceState)
        }
    }

    private fun setupLayout() {
        binding?.imagePreviewReviewMediaImagePlayer?.onImageDoubleClickListener = {
            if (isAdded) {
                if (binding?.imagePreviewReviewMediaImagePlayer?.mScaleFactor == UNZOOM_SCALE_FACTOR) {
                    reviewMediaGalleryViewModel.requestToggleViewPagerSwipe(false)
                    binding?.imagePreviewReviewMediaImagePlayer?.setScaleFactor(ZOOM_SCALE_FACTOR)
                } else {
                    reviewMediaGalleryViewModel.requestToggleViewPagerSwipe(true)
                    binding?.imagePreviewReviewMediaImagePlayer?.setScaleFactor(UNZOOM_SCALE_FACTOR)
                }
            }
        }
        binding?.btnReviewMediaImagePlayerSeeMore?.setOnClickListener {
            listener?.onSeeMoreClicked()
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewImagePlayerViewModel.uiState, ::updateUi)
    }

    private fun collectToasterEvent() {
        viewLifecycleOwner.collectWhenResumed(sharedReviewMediaGalleryViewModel.toasterEventActionClickQueue) {
            if (it == String.format(TOASTER_KEY_ERROR_LOAD_IMAGE, getImageUri())) {
                binding?.imagePreviewReviewMediaImagePlayer?.mImageView?.loadImage(getImageUri()) {
                    listener(onError = ::onErrorLoadImage, onSuccess = ::onSuccessLoadImage)
                }
            }
        }
    }

    private fun updateUi(uiState: ReviewImagePlayerUiState) {
        if (uiState.imageUri.isNotEmpty()) {
            binding?.imagePreviewReviewMediaImagePlayer?.mLoaderView?.hide()
            binding?.imagePreviewReviewMediaImagePlayer?.mImageView?.loadImage(getImageUri()) {
                listener(onError = ::onErrorLoadImage, onSuccess = ::onSuccessLoadImage)
            }
            binding?.imagePreviewReviewMediaImagePlayer?.show()
            when (uiState) {
                is ReviewImagePlayerUiState.Showing -> {
                    binding?.btnReviewMediaImagePlayerSeeMore?.gone()
                    binding?.tvReviewMediaImagePlayerSeeMore?.gone()
                    binding?.overlayReviewMediaImagePlayerSeeMore?.gone()
                }
                is ReviewImagePlayerUiState.ShowingSeeMore -> {
                    binding?.btnReviewMediaImagePlayerSeeMore?.show()
                    binding?.overlayReviewMediaImagePlayerSeeMore?.show()
                    binding?.tvReviewMediaImagePlayerSeeMore?.apply {
                        text = context.getString(R.string.review_image_player_see_more, uiState.totalImageCount)
                        show()
                    }
                }
            }
        }
    }

    private fun onErrorLoadImage(exception: MediaException?) {
        binding?.imagePreviewReviewMediaImagePlayerShimmer?.gone()
        val messageStringRes = if (exception == null) {
            StringRes(R.string.review_image_player_toaster_error_message)
        } else {
            StringRes(
                R.string.review_image_player_toaster_error_message,
                listOf(
                    ErrorHandler.getErrorMessagePair(
                        context = null,
                        e = exception,
                        builder = ErrorHandler.Builder().build()
                    ).second
                )
            )
        }
        sharedReviewMediaGalleryViewModel.enqueueToaster(
            ToasterUiModel(
                String.format(TOASTER_KEY_ERROR_LOAD_IMAGE, getImageUri()),
                messageStringRes,
                Toaster.TYPE_ERROR,
                Toaster.LENGTH_INDEFINITE,
                StringRes(R.string.review_image_player_toaster_error_action)
            )
        )
    }

    private fun onSuccessLoadImage(bitmap: Bitmap?, mediaDataSource: MediaDataSource?) {
        binding?.imagePreviewReviewMediaImagePlayerShimmer?.gone()
        if (!reviewImagePlayerViewModel.getImpressHolder().isInvoke) {
            reviewImagePlayerViewModel.getImpressHolder().invoke()
            listener?.onImageImpressed(getImageUri())
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    interface Listener {
        fun onSeeMoreClicked()
        fun onImageImpressed(imageUri: String)
    }
}