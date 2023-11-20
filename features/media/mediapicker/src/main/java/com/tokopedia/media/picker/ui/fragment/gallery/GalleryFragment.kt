@file:SuppressLint("NotifyDataSetChanged")

package com.tokopedia.media.picker.ui.fragment.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.FragmentGalleryBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl.Companion.BUCKET_ALL_MEDIA_ID
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivityContract
import com.tokopedia.media.picker.ui.activity.picker.PickerViewModel
import com.tokopedia.media.picker.ui.adapter.MediaGalleryAdapter
import com.tokopedia.media.picker.ui.adapter.decoration.GridItemDecoration
import com.tokopedia.media.picker.ui.adapter.viewholder.MediaGalleryViewHolder
import com.tokopedia.media.picker.ui.component.AlbumSelectorUiComponent
import com.tokopedia.media.picker.ui.component.MediaDrawerUiComponent
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.ui.publisher.observe
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.parcelable
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class GalleryFragment @Inject constructor(
    private var viewModelFactory: ViewModelProvider.Factory,
    private var param: PickerCacheManager,
    private var galleryAnalytics: GalleryAnalytics,
    private val eventBus: PickerEventBus
) : BaseDaggerFragment(),
    MediaGalleryViewHolder.Listener,
    AlbumSelectorUiComponent.Listener {

    private val viewModel: PickerViewModel by activityViewModels { viewModelFactory }
    private val binding: FragmentGalleryBinding? by viewBinding()
    private var contract: PickerActivityContract? = null

    private val albumSelector by uiComponent {
        AlbumSelectorUiComponent(
            parent = it,
            listener = this
        )
    }

    private val mediaDrawer by uiComponent {
        MediaDrawerUiComponent(
            parent = it,
            param = param,
            eventBus = eventBus,
            analytics = galleryAnalytics,
        )
    }

    private val mLayoutManager by lazy {
        GridLayoutManager(requireContext(), SPAN_COUNT_SIZE)
    }

    private val featureAdapter by lazy {
        MediaGalleryAdapter(this)
    }

    private var uiModel = GalleryUiModel()

    private val endlessScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                // indicates the pagination isn't album changed
                uiModel.hasChangeAlbum = false

                val hasNextPage = if (uiModel.bucketCount.isMoreThanZero()) {
                    featureAdapter.itemCount < uiModel.bucketCount
                } else {
                    true // force as true for recent media
                }

                if (hasNextPage) {
                    viewModel.loadMedia(uiModel.bucketId, totalItemsCount)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_gallery,
            container,
            false
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(GalleryUiModel.KEY, uiModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState
            ?.parcelable<GalleryUiModel>(GalleryUiModel.KEY)
            ?.let {
                uiModel = it
            }

        initObservable()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exceptionHandler {
            contract = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contract = (context as PickerActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_ALBUM_SELECTOR && resultCode == Activity.RESULT_OK) {
            val (id, name, count) = AlbumActivity.getIntentResult(data)
            albumSelector.setAlbumName(name)

            uiModel.hasChangeAlbum = true
            uiModel.bucketCount = count
            uiModel.bucketId = id

            endlessScrollListener.resetState()
            viewModel.loadMedia(uiModel.bucketId)
        }
    }

    override fun onResume() {
        super.onResume()
        mediaDrawer.setListener()
    }

    override fun onPause() {
        super.onPause()
        mediaDrawer.removeListener()
    }

    override fun onMediaItemClicked(data: MediaUiModel, position: Int) {
        val isSelected = isMediaSelected(data)

        if (isSelected) {
            featureAdapter.removeSelected(data, position)
        }

        if (shouldAbleToSelected(data, isSelected)) {
            featureAdapter.addSelected(data, position)

            if (!isSelected) {
                eventBus.addMediaEvent(data)
                galleryAnalytics.selectGalleryItem()
            } else {
                eventBus.removeMediaEvent(data)
            }
        }
    }

    override fun isMediaSelected(data: MediaUiModel): Boolean {
        return featureAdapter.isMediaSelected(data)
    }

    override fun onAlbumSelectorClicked() {
        AlbumActivity.start(this, RC_ALBUM_SELECTOR)
        galleryAnalytics.clickDropDown()
    }

    private fun initView() {
        setupRecyclerView()
        setupSelectionDrawerWidget()
        albumSelector.setupView()

        viewModel.loadMedia(uiModel.bucketId)
    }

    private fun initObservable() {
        viewModel.medias.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                if (uiModel.hasChangeAlbum) {
                    val isNotComputingLayout = binding?.lstMedia?.isComputingLayout != true
                    val isRecyclerViewIdle = binding?.lstMedia?.scrollState == SCROLL_STATE_IDLE

                    if (isNotComputingLayout && isRecyclerViewIdle) {
                        mLayoutManager.scrollToPosition(Int.ZERO)
                        featureAdapter.setItems(it)
                        featureAdapter.notifyDataSetChanged()
                    }
                } else {
                    featureAdapter.addItems(it)
                    featureAdapter.notifyItemRangeInserted(featureAdapter.getItems().size, it.size)
                    endlessScrollListener.updateStateAfterGetData()
                }
            }
        }

        viewModel.isFetchMediaLoading.observe(viewLifecycleOwner) {
            // the simmering only works if the user change the album
            if (uiModel.hasChangeAlbum) {
                binding?.shimmering?.container?.showWithCondition(it)
                binding?.lstMedia?.showWithCondition(!it)
            }
        }

        viewModel.isMediaEmpty.observe(viewLifecycleOwner) { isEmpty ->
            onShowEmptyState(isEmpty && uiModel.bucketId == BUCKET_ALL_MEDIA_ID)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.observe(
                onChanged = {
                    binding?.drawerSelector?.addAllData(it)
                    featureAdapter.removeSubtractionSelected(it)
                },
                onRemoved = {
                    binding?.drawerSelector?.removeData(it)
                    featureAdapter.removeSelected(it)
                },
                onAdded = {
                    binding?.drawerSelector?.addData(it)
                }
            )
        }
    }

    private fun onShowEmptyState(isShown: Boolean) {
        binding?.emptyState?.root?.showWithCondition(isShown)
        binding?.emptyState?.imgEmptyState?.loadImage(getString(R.string.picker_img_empty_state))
        binding?.emptyState?.emptyNavigation?.showWithCondition(param.get().isCommonPageType())
        binding?.emptyState?.emptyNavigation?.setOnClickListener {
            contract?.onEmptyStateActionClicked()
        }
    }

    private fun setupSelectionDrawerWidget() {
        val isMultipleSelectionType = param.get().isMultipleSelectionType()

        if (isMultipleSelectionType) {
            binding?.drawerSelector?.setMaxAdapterSize(param.get().maxMediaTotal())
            binding?.drawerSelector?.show()
        }
    }

    private fun setupRecyclerView() {
        binding?.lstMedia?.let {
            // item decoration
            it.addItemDecoration(GridItemDecoration(requireContext(), SPAN_COUNT_SIZE))

            // common config
            it.setHasFixedSize(true)
            it.isNestedScrollingEnabled = false
            it.layoutManager = mLayoutManager
            it.adapter = featureAdapter

            // scroll listener
            it.addOnScrollListener(endlessScrollListener)
        }
    }

    private fun isValidVideo(media: MediaUiModel): Boolean {
        if (contract?.hasVideoLimitReached() == true) {
            contract?.onShowVideoLimitReachedGalleryToast()
            return false
        }

        if (contract?.isMinVideoDuration(media) == true) {
            contract?.onShowVideoMinDurationToast()
            return false
        }

        if (contract?.isMaxVideoDuration(media) == true) {
            contract?.onShowVideoMaxDurationToast()
            return false
        }

        if (contract?.isMaxVideoSize(media) == true) {
            contract?.onShowVideoMaxFileSizeToast()
            return false
        }

        return true
    }

    private fun isValidImage(media: MediaUiModel): Boolean {
        if (contract?.isMaxImageResolution(media) == true) {
            contract?.onShowImageMaxResToast()
            return false
        }

        if (contract?.isMinImageResolution(media) == true) {
            contract?.onShowImageMinResToast()
            return false
        }

        if (contract?.isMaxImageSize(media) == true) {
            contract?.onShowImageMaxFileSizeToast()
            return false
        }

        return true
    }

    private fun shouldAbleToSelected(media: MediaUiModel, isSelected: Boolean): Boolean {
        if (!isSelected && media.file?.isVideo() == true && !isValidVideo(media)) {
            return false
        } else if (!isSelected && media.file?.isImage() == true && !isValidImage(media)) {
            return false
        }

        if (param.get().isMultipleSelectionType()) {
            if (!isSelected && contract?.hasMediaLimitReached() == true) {
                contract?.onShowMediaLimitReachedGalleryToast()
                return false
            }
        }

        return true
    }

    override fun initInjector() = Unit
    override fun getScreenName() = "Camera"

    companion object {
        private const val RC_ALBUM_SELECTOR = 123
        private const val SPAN_COUNT_SIZE = 3
    }
}
