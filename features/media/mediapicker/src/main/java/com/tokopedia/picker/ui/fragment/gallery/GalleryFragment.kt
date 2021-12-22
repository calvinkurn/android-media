package com.tokopedia.picker.ui.fragment.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentGalleryBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.common.PickerSelectionType
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.gallery.adapter.GalleryPickerAdapter
import com.tokopedia.picker.ui.fragment.gallery.adapter.utils.GridSpacingItemDecoration
import com.tokopedia.picker.utils.EventChannelState
import com.tokopedia.picker.utils.EventPublisher
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class GalleryFragment : BaseDaggerFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentGalleryBinding? by viewBinding()

    private val config by lazy {
        PickerUiConfig.createFileLoaderParam()
    }

    private val adapter by lazy {
        GalleryPickerAdapter(emptyList()) {
            selectImage(it)
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        ).get(GalleryViewModel::class.java)
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

    private fun initObservable() {
        viewModel.files.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        viewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initView() {
        setRecyclerView()
        viewModel.fetch(config)
    }

    private fun setRecyclerView() {
        val spanCount = 3

        binding?.lstMedia?.layoutManager = GridLayoutManager(
            requireContext(),
            spanCount
        )

        binding?.lstMedia?.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                resources.getDimensionPixelSize(
                    R.dimen.picker_item_padding
                )
            )
        )

        binding?.lstMedia?.adapter = adapter

        adapter.setListener {
            EventPublisher.send(
                EventChannelState.SelectedMedia(
                    it
                )
            )
        }
    }

    private fun selectImage(isSelected: Boolean): Boolean {
        if (PickerUiConfig.paramType == PickerSelectionType.MULTIPLE) {
            if (adapter.selectedMedias.size >= config.limit && !isSelected) {
                Toast.makeText(
                    requireContext(),
                    "media selection limit",
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
        context?.applicationContext?.let {
            DaggerPickerComponent.builder()
                .pickerModule(PickerModule(it))
                .build()
                .inject(this)
        }
    }

    override fun getScreenName() = "Camera"

}