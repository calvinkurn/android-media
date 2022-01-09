package com.tokopedia.picker.ui.fragment.media

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.common.PickerSelectionType
import com.tokopedia.picker.data.repository.AlbumRepositoryImpl.Companion.RECENT_ALBUM_ID
import com.tokopedia.picker.databinding.FragmentGalleryBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.album.AlbumActivity
import com.tokopedia.picker.ui.activity.main.PickerActivity
import com.tokopedia.picker.ui.fragment.media.recyclers.adapter.MediaAdapter
import com.tokopedia.picker.ui.fragment.media.recyclers.utils.GridItemDecoration
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class MediaFragment : BaseDaggerFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentGalleryBinding? by viewBinding()
    private val param = PickerUiConfig.getFileLoaderParam()

    private val adapter by lazy {
        MediaAdapter(emptyList()) {
            selectMedia(it)
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        ).get(MediaViewModel::class.java)
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
        }
    }

    private fun initObservable() {
        viewModel.files.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        setupWidgetAlbumSelector()
        setupRecyclerView()

        viewModel.fetch(RECENT_ALBUM_ID, param)
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

        // force scroll to top if user trying to change the album
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                binding?.lstMedia?.scrollToPosition(positionStart)
            }
        })

        adapter.setListener {
            (activity as PickerActivity).onUpdateSelectedMedia(it)
        }
    }

    private fun selectMedia(isSelected: Boolean): Boolean {
        if (PickerUiConfig.paramType == PickerSelectionType.MULTIPLE) {
            if (adapter.selectedMedias.size >= param.limit && !isSelected) {
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
            if (adapter.selectedMedias.size > 0) {
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
        const val RC_ALBUM_SELECTOR = 123

        const val LIST_SPAN_COUNT = 3
    }

}