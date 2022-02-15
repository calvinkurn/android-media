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
import com.tokopedia.media.common.types.PickerSelectionType
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.databinding.FragmentGalleryBinding
import com.tokopedia.media.picker.data.repository.AlbumRepositoryImpl.Companion.RECENT_ALBUM_ID
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.*
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.adapter.GalleryAdapter
import com.tokopedia.media.picker.ui.fragment.gallery.recyclers.utils.GridItemDecoration
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnAddPublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.utils.view.binding.viewBinding
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

    override fun onItemClicked(media: MediaUiModel) {} //no-op

    override fun onDataSetChanged(action: DrawerActionType) {
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
            stateOnAddPublished(media)
        } else {
            stateOnRemovePublished(media)
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