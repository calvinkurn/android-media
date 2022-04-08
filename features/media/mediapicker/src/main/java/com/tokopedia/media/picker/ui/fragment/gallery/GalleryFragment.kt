package com.tokopedia.media.picker.ui.fragment.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.databinding.FragmentGalleryBinding
import com.tokopedia.media.picker.data.repository.AlbumRepositoryImpl.Companion.RECENT_ALBUM_ID
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.main.PickerActivity
import com.tokopedia.media.picker.ui.activity.main.PickerActivityListener
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.adapter.GalleryAdapter
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.utils.GridItemDecoration
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnAddPublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class GalleryFragment : BaseDaggerFragment(), DrawerSelectionWidget.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var param: ParamCacheManager

    private val binding: FragmentGalleryBinding? by viewBinding()
    private var listener: PickerActivityListener? = null

    private val adapter by lazy {
        GalleryAdapter(emptyList(), ::selectMedia)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[GalleryViewModel::class.java]
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
            listener = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as PickerActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ALBUM_SELECTOR && resultCode == Activity.RESULT_OK) {
            val bucketId = data?.getLongExtra(AlbumActivity.INTENT_BUCKET_ID, 0)?: -1
            val bucketName = data?.getStringExtra(AlbumActivity.INTENT_BUCKET_NAME)

            // set the title of album selector
            binding?.albumSelector?.txtName?.text = bucketName

            // fetch album by bucket id
            viewModel.fetch(bucketId, param.get())

            // force and scroll to up if the bucketId is "recent medias / all media"
            if (bucketId == -1L) {
                binding?.lstMedia?.smoothScrollToPosition(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onItemClicked(media: MediaUiModel) {} //no-op

    override fun onDataSetChanged(action: DrawerActionType) {
        if (!param.get().isMultipleSelectionType()) return

        when (action) {
            is DrawerActionType.Add -> stateOnAddPublished(action.media)
            is DrawerActionType.Remove -> stateOnRemovePublished(action.mediaToRemove)
            is DrawerActionType.Reorder -> stateOnChangePublished(action.data)
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
        setupRecyclerView()

        viewModel.fetch(RECENT_ALBUM_ID, param.get())
    }

    private fun hasMediaList(isShown: Boolean) {
        setupWidgetAlbumSelector(isShown)
        setupSelectionDrawerWidget(isShown)
        setupEmptyState(!isShown)
    }

    private fun setupEmptyState(isShown: Boolean) {
        binding?.emptyState?.root?.showWithCondition(isShown)
        binding?.emptyState?.emptyNavigation?.showWithCondition(param.get().isCommonPageType())
        binding?.emptyState?.emptyNavigation?.setOnClickListener {
            listener?.navigateToCameraPage()
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
            startActivityForResult(Intent(
                requireContext(),
                AlbumActivity::class.java
            ), RC_ALBUM_SELECTOR)
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
                resources.getDimensionPixelSize(
                    R.dimen.picker_item_padding
                )
            )
        )

        binding?.lstMedia?.adapter = adapter
    }

    private fun selectMedia(media: MediaUiModel, isSelected: Boolean): Boolean {
        if (param.get().isMultipleSelectionType()) {
            if (!isSelected && media.isVideo()) {
                // video validation
                if (listener?.hasVideoLimitReached() == true) {
                    listener?.onShowVideoLimitReachedGalleryToast()
                    return false
                }

                if (listener?.isMinVideoDuration(media) == true) {
                    listener?.onShowVideoMinDurationToast()
                    return false
                }

                if(listener?.isMaxVideoDuration(media) == true){
                    listener?.onShowVideoMaxDurationToast()
                    return false
                }

                if (listener?.isMaxVideoSize(media) == true) {
                    listener?.onShowVideoMaxFileSizeToast()
                    return false
                }
            } else if (!isSelected && !media.isVideo()) {
                // image validation
                if (listener?.isMaxImageResolution(media) == true) {
                    listener?.onShowImageMaxResToast()
                    return false
                }

                if (listener?.isMinImageResolution(media) == true) {
                    listener?.onShowImageMinResToast()
                    return false
                }

                if (listener?.isMaxImageSize(media) == true) {
                    listener?.onShowImageMaxFileSizeToast()
                    return false
                }
            }

            if (!isSelected && listener?.hasMediaLimitReached() == true) {
                listener?.onShowMediaLimitReachedGalleryToast()
                return false
            }
        } else if (!param.get().isMultipleSelectionType() && (listener?.mediaSelected()?.isNotEmpty() == true || adapter.selectedMedias.isNotEmpty())) {
            adapter.removeAllSelectedSingleClick()
        }

        if (!isSelected) {
            stateOnAddPublished(media)
        } else {
            stateOnRemovePublished(media)
        }

        return true
    }

    override fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName() = "Camera"

    companion object {
        private const val RC_ALBUM_SELECTOR = 123
        private const val LIST_SPAN_COUNT = 3
    }
}