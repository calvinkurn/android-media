package com.tokopedia.reviewcommon.feature.media.player.image.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.image_gallery.ImagePreview
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.FragmentReviewMediaImagePlayerBinding
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.viewmodel.ReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.image.di.component.DaggerReviewImagePlayerComponent
import com.tokopedia.reviewcommon.feature.media.player.image.di.qualifier.ReviewImagePlayerViewModelFactory
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uistate.ReviewImagePlayerUiState
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel.ReviewImagePlayerViewModel
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewImagePlayerFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val ARG_IMAGE_URI = "argImageUri"
        private const val ARG_SHOW_SEE_MORE = "argShowSeeMore"
        private const val ARG_TOTAL_MEDIA_COUNT = "argTotalMediaCount"
        private const val ZOOM_SCALE_FACTOR = 2f
        private const val UNZOOM_SCALE_FACTOR = 1f

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
    lateinit var dispatchers: CoroutineDispatchers

    private var binding by viewBinding(FragmentReviewMediaImagePlayerBinding::bind)
    private var listener: Listener? = null
    private var uiStateCollectorJob: Job? = null

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

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        uiStateCollectorJob?.cancel()
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
        binding?.imagePreviewReviewMediaImagePlayer?.imagePreviewUnifyListener = object: ImagePreview.ImagePreviewUnifyListener {
            override fun onZoom(scaleFactor: Float) {}

            override fun onZoomEnd(scaleFactor: Float) {
                reviewMediaGalleryViewModel.requestToggleViewPagerSwipe(scaleFactor == UNZOOM_SCALE_FACTOR)
            }

            override fun onZoomStart(scaleFactor: Float) {
                reviewMediaGalleryViewModel.requestToggleViewPagerSwipe(scaleFactor == UNZOOM_SCALE_FACTOR)
            }
        }
        binding?.imagePreviewReviewMediaImagePlayer?.onImageDoubleClickListener = {
            if (binding?.imagePreviewReviewMediaImagePlayer?.mScaleFactor == UNZOOM_SCALE_FACTOR) {
                binding?.imagePreviewReviewMediaImagePlayer?.setScaleFactor(ZOOM_SCALE_FACTOR)
            } else {
                binding?.imagePreviewReviewMediaImagePlayer?.setScaleFactor(UNZOOM_SCALE_FACTOR)
            }
        }
        binding?.btnReviewMediaImagePlayerSeeMore?.setOnClickListener {
            listener?.onSeeMoreClicked()
        }
    }

    private fun collectUiState() {
        uiStateCollectorJob = uiStateCollectorJob?.takeIf { !it.isCompleted } ?: launch {
            reviewImagePlayerViewModel.uiState.collectLatest { updateUi(it) }
        }
    }

    private fun updateUi(uiState: ReviewImagePlayerUiState) {
        binding?.imagePreviewReviewMediaImagePlayer?.mLoaderView?.hide()
        binding?.imagePreviewReviewMediaImagePlayer?.mImageView?.loadImage(uiState.imageUri)
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

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    interface Listener {
        fun onSeeMoreClicked()
    }
}