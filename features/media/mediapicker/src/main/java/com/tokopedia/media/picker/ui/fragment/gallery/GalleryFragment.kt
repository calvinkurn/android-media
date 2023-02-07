package com.tokopedia.media.picker.ui.fragment.gallery

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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.media.databinding.FragmentGalleryBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.data.repository.AlbumRepository.Companion.RECENT_ALBUM_ID
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivityContract
import com.tokopedia.media.picker.ui.activity.picker.PickerViewModel
import com.tokopedia.media.picker.ui.adapter.GalleryAdapter
import com.tokopedia.media.picker.ui.adapter.utils.GridItemDecoration
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnAddPublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.generateKey
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class GalleryFragment @Inject constructor(
    private var viewModelFactory: ViewModelProvider.Factory,
    private var param: PickerCacheManager,
    private var galleryAnalytics: GalleryAnalytics,
) : BaseDaggerFragment(), DrawerSelectionWidget.Listener {

    private val viewModel: PickerViewModel by activityViewModels { viewModelFactory }

    private val binding: FragmentGalleryBinding? by viewBinding()
    private var contract: PickerActivityContract? = null

    private val adapter by lazy {
        GalleryAdapter(emptyList(), ::selectMedia)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            val (id, name) = AlbumActivity.getAlbumBucketDetails(data)

            binding?.albumSelector?.txtName?.text = name
            viewModel.loadLocalGalleryBy(bucketId = id)

            // force and scroll to up if the bucketId is "recent medias / all media"
            if (id == ALL_MEDIA_BUCKET_ID) {
                binding?.lstMedia?.smoothScrollToPosition(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)

        if (!param.get().isMultipleSelectionType()) {
            adapter.removeAllSelectedSingleClick()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onItemClicked(media: MediaUiModel) {
        galleryAnalytics.clickGalleryThumbnail()
    }

    override fun onDataSetChanged(action: DrawerActionType) {
        if (!param.get().isMultipleSelectionType()) return

        when (action) {
            is DrawerActionType.Add -> stateOnAddPublished(action.media, param.get().generateKey())
            is DrawerActionType.Remove -> stateOnRemovePublished(action.mediaToRemove, param.get().generateKey())
            is DrawerActionType.Reorder -> stateOnChangePublished(action.data, param.get().generateKey())
        }
    }

    private fun initObservable() {
        viewModel.medias.observe(viewLifecycleOwner) {
            hasMediaList(it.isNotEmpty())

            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.observe(
                onChanged = {
                    binding?.drawerSelector?.addAllData(it)
                    adapter.removeSubtractionSelected(it)
                },
                onRemoved = {
                    binding?.drawerSelector?.removeData(it)
                    adapter.removeSelected(it)
                },
                onAdded = {
                    binding?.drawerSelector?.addData(it)
                }
            )
        }
    }

    private fun initView() {
        // for first time
        viewModel.loadLocalGalleryBy(RECENT_ALBUM_ID)

        setupRecyclerView()
    }

    private fun hasMediaList(isShown: Boolean) {
        setupWidgetAlbumSelector(isShown)
        setupSelectionDrawerWidget(isShown)
        setupEmptyState(!isShown)
    }

    private fun setupEmptyState(isShown: Boolean) {
        binding?.emptyState?.root?.showWithCondition(isShown)
        binding?.emptyState?.imgEmptyState?.loadImage(getString(R.string.picker_img_empty_state))
        binding?.emptyState?.emptyNavigation?.showWithCondition(param.get().isCommonPageType())
        binding?.emptyState?.emptyNavigation?.setOnClickListener {
            contract?.onEmptyStateActionClicked()
        }
    }

    private fun setupSelectionDrawerWidget(isShown: Boolean) {
        val isMultipleSelectionType = param.get().isMultipleSelectionType()

        if (isMultipleSelectionType) {
            binding?.drawerSelector?.setMaxAdapterSize(param.get().maxMediaTotal())
            binding?.drawerSelector?.showWithCondition(isShown)
        }
    }

    private fun setupWidgetAlbumSelector(isShown: Boolean) {
        binding?.albumSelector?.root?.showWithCondition(isShown)

        binding?.albumSelector?.container?.setOnClickListener {
            galleryAnalytics.clickDropDown()
            AlbumActivity.start(this, RC_ALBUM_SELECTOR)
        }
    }

    private fun setupRecyclerView() {
        binding?.lstMedia?.layoutManager = GridLayoutManager(
            requireContext(),
            LIST_SPAN_COUNT
        )

        binding?.lstMedia?.addItemDecoration(
            GridItemDecoration(
                LIST_SPAN_COUNT,
                requireContext().resources.getDimensionPixelSize(
                    R.dimen.picker_item_padding
                )
            )
        )

        binding?.lstMedia?.adapter = adapter
    }

    private fun hasVideoValid(media: MediaUiModel): Boolean {
        if (contract?.hasVideoLimitReached() == true) {
            contract?.onShowVideoLimitReachedGalleryToast()
            return false
        }

        if (contract?.isMinVideoDuration(media) == true) {
            contract?.onShowVideoMinDurationToast()
            return false
        }

        if(contract?.isMaxVideoDuration(media) == true){
            contract?.onShowVideoMaxDurationToast()
            return false
        }

        if (contract?.isMaxVideoSize(media) == true) {
            contract?.onShowVideoMaxFileSizeToast()
            return false
        }

        return true
    }

    private fun hasImageValid(media: MediaUiModel): Boolean {
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

    private fun selectMedia(media: MediaUiModel, isSelected: Boolean): Boolean {
        if (!isSelected && media.file?.isVideo() == true && !hasVideoValid(media)) {
            return false
        } else if (!isSelected && media.file?.isImage() == true && !hasImageValid(media)) {
            return false
        }

        if (param.get().isMultipleSelectionType()) {
            if (!isSelected && contract?.hasMediaLimitReached() == true) {
                contract?.onShowMediaLimitReachedGalleryToast()
                return false
            }
        }

        // publish the state and send tracking
        if (!isSelected) {
            stateOnAddPublished(media, param.get().generateKey())
            galleryAnalytics.selectGalleryItem()
        } else {
            stateOnRemovePublished(media, param.get().generateKey())
        }

        return true
    }

    override fun initInjector() {}

    override fun getScreenName() = "Camera"

    companion object {
        private const val RC_ALBUM_SELECTOR = 123
        private const val LIST_SPAN_COUNT = 3

        private const val ALL_MEDIA_BUCKET_ID = -1L
    }
}
