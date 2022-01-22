package com.tokopedia.picker.ui.fragment.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.common.PickerSelectionType
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.AlbumRepositoryImpl.Companion.RECENT_ALBUM_ID
import com.tokopedia.picker.databinding.FragmentGalleryBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.album.AlbumActivity
import com.tokopedia.picker.ui.fragment.gallery.recyclers.adapter.GalleryAdapter
import com.tokopedia.picker.ui.fragment.gallery.recyclers.utils.GridItemDecoration
import com.tokopedia.picker.ui.widget.selectornav.MediaSelectionNavigationWidget
import com.tokopedia.picker.utils.ActionType
import com.tokopedia.picker.utils.isVideoFormat
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class GalleryFragment : BaseDaggerFragment(), MediaSelectionNavigationWidget.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentGalleryBinding? by viewBinding()
    private val param = PickerUiConfig.pickerParam()

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
            binding?.selector?.txtName?.text = bucketName

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
        binding?.bottomNavDrawer?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.bottomNavDrawer?.removeListener()
    }

    override fun onDataSetChanged(action: ActionType) {
        when (action) {
            is ActionType.Add -> {}
            is ActionType.Remove -> {
//                viewModel.publishSelectionRemovedChanged(
//                    action.mediaToRemove,
//                    action.data
//                )
//                viewModel.publishSelectionDataChanged(action.data)
            }
            is ActionType.Reorder -> {
//                viewModel.publishSelectionDataChanged(action.data)
            }
        }
    }

    private fun initObservable() {
        lifecycle.addObserver(viewModel)

        viewModel.mediaFiles.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.isMediaNotEmpty.observe(viewLifecycleOwner) {

        }

//        viewModel.mediaRemoved.observe(viewLifecycleOwner) {
//            it?.let { media ->
//                adapter.removeSelected(media)
//            }
//        }
    }

    private fun initView() {
        setupSelectionDrawerWidget()
        setupWidgetAlbumSelector()
        setupRecyclerView()

        viewModel.fetch(RECENT_ALBUM_ID, param)
    }

    private fun setupSelectionDrawerWidget() {
        val isMultipleSelectionType = PickerUiConfig.paramType == PickerSelectionType.MULTIPLE

        if (isMultipleSelectionType) {
            binding?.bottomNavDrawer?.setMaxAdapterSize(param.limit)
            binding?.bottomNavDrawer?.show()
        }
    }

    //TODO, create separated view component
    private fun setupWidgetAlbumSelector() {
        binding?.selector?.container?.setOnClickListener {
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

        adapter.setListener {
//            viewModel.publishSelectionDataChanged(it)
        }
    }

    private fun selectMedia(media: Media, isSelected: Boolean): Boolean {
        if (PickerUiConfig.paramType == PickerSelectionType.MULTIPLE) {
            if (isVideoFormat(media.path) && PickerUiConfig.hasAtLeastOneVideoOnGlobalSelection()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.picker_selection_limit_video),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            if (PickerUiConfig.mediaSelectionList().size >= param.limit && !isSelected) {
                Toast.makeText(
                    requireContext(),
                    getString(
                        R.string.picker_selection_limit_message,
                        param.limit
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else if (PickerUiConfig.paramType == PickerSelectionType.SINGLE) {
            if (PickerUiConfig.mediaSelectionList().isNotEmpty()) {
                adapter.removeAllSelectedSingleClick()
            }
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
        private const val KEY_SELECTED_MEDIA = "selected_media.key"
        private const val RC_ALBUM_SELECTOR = 123
        private const val LIST_SPAN_COUNT = 3
    }

}