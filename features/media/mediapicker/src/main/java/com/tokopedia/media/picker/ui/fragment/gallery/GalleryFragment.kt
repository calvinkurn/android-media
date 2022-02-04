package com.tokopedia.media.picker.ui.fragment.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.PickerSelectionType
import com.tokopedia.media.picker.data.repository.AlbumRepositoryImpl.Companion.RECENT_ALBUM_ID
import com.tokopedia.media.databinding.FragmentGalleryBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.adapter.GalleryAdapter
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.utils.GridItemDecoration
import com.tokopedia.media.picker.ui.uimodel.MediaUiModel
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.utils.ActionType
import com.tokopedia.media.picker.utils.EventState
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

open class GalleryFragment : BaseDaggerFragment(), DrawerSelectionWidget.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentGalleryBinding? by viewBinding()
    private val param by lazy { PickerUiConfig.pickerParam() }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ALBUM_SELECTOR && resultCode == Activity.RESULT_OK) {
            val bucketId = data?.getLongExtra(AlbumActivity.INTENT_BUCKET_ID, 0)?: -1
            val bucketName = data?.getStringExtra(AlbumActivity.INTENT_BUCKET_NAME)

            // set the title of album selector
            binding?.albumSelector?.txtName?.text = bucketName

            // fetch album by bucket id
            viewModel.fetch(bucketId, param)

            // force and scrool to up
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

    override fun onDataSetChanged(action: ActionType) {
        when (action) {
            is ActionType.Add -> {
                viewModel.send(EventState.SelectionAdded(action.media))
            }
            is ActionType.Remove -> {
                viewModel.send(EventState.SelectionRemoved(action.mediaToRemove))
            }
            is ActionType.Reorder -> {
                viewModel.send(EventState.SelectionChanged(action.data))
            }
        }
    }

    private fun initObservable() {
        viewModel.mediaFiles.observe(viewLifecycleOwner) {
            hasMediaList(it.isNotEmpty())

            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.collect {
                when (it) {
                    is EventState.SelectionChanged -> {
                        binding?.drawerSelector?.addAllData(it.data)
                    }
                    is EventState.CameraCaptured -> {
                        binding?.drawerSelector?.addData(it.data)
                    }
                    is EventState.SelectionAdded -> {
                        binding?.drawerSelector?.addData(it.data)
                    }
                    is EventState.SelectionRemoved -> {
                        binding?.drawerSelector?.removeData(it.media)
                        adapter.removeSelected(it.media)
                    }
                }
            }
        }
    }

    private fun initView() {
        setupRecyclerView()

        viewModel.fetch(RECENT_ALBUM_ID, param)
    }

    private fun hasMediaList(isShown: Boolean) {
        setupWidgetAlbumSelector(isShown)
        setupSelectionDrawerWidget(isShown)
        binding?.emptyState?.root?.showWithCondition(!isShown)
    }

    private fun setupSelectionDrawerWidget(isShown: Boolean) {
        val isMultipleSelectionType = PickerUiConfig.paramType == PickerSelectionType.MULTIPLE

        if (isMultipleSelectionType) {
            binding?.drawerSelector?.setMaxAdapterSize(param.limitOfMedia())
            binding?.drawerSelector?.isAbleToReorder(false)
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
        val mediaSelectionDrawer = binding?.drawerSelector?.getData()?: emptyList()

        if (PickerUiConfig.paramType == PickerSelectionType.MULTIPLE) {
            val containsVideo = binding
                ?.drawerSelector
                ?.containsVideoMaxOf(param.maxVideoCount())
                ?: false

            val mediaSelectionDrawerSize = mediaSelectionDrawer.size

            if (media.isVideo() && containsVideo && !isSelected) {
                Toast.makeText(
                    requireContext(),
                    getString(
                        R.string.picker_selection_limit_video,
                        param.maxVideoCount()
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            if (mediaSelectionDrawerSize >= param.limitOfMedia() && !isSelected) {
                Toast.makeText(
                    requireContext(),
                    getString(
                        R.string.picker_selection_limit_message,
                        param.limitOfMedia()
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            if (media.isVideo() && !media.isVideoDurationValid(requireContext()) && !isSelected) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.picker_video_duration_min_limit),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else if (PickerUiConfig.paramType == PickerSelectionType.SINGLE) {
            if (mediaSelectionDrawer.isNotEmpty() || adapter.selectedMedias.isNotEmpty()) {
                adapter.removeAllSelectedSingleClick()
            }
        }

        if (!isSelected) {
            viewModel.send(EventState.SelectionAdded(media))
        } else {
            viewModel.send(EventState.SelectionRemoved(media))
        }

        return true
    }

    override fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .pickerModule(PickerModule())
            .build()
            .inject(this)
    }

    override fun getScreenName() = "Camera"

    companion object {
        private const val RC_ALBUM_SELECTOR = 123
        private const val LIST_SPAN_COUNT = 3
    }

}