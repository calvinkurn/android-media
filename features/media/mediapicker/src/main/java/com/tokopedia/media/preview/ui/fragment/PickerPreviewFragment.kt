package com.tokopedia.media.preview.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.R
import com.tokopedia.media.databinding.FragmentPreviewBinding
import com.tokopedia.media.preview.di.DaggerPreviewComponent
import com.tokopedia.media.preview.di.module.PickerPreviewModule
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.utils.view.binding.viewBinding

class PickerPreviewFragment : BaseDaggerFragment() {

    private val binding: FragmentPreviewBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_preview,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as? PickerPreviewActivity)?.let {
            it.supportActionBar?.title = screenName
        }
    }

    override fun initInjector() {
        DaggerPreviewComponent.builder()
            .pickerPreviewModule(PickerPreviewModule())
            .build()
            .inject(this)
    }

    override fun getScreenName() = PAGE_NAME

    companion object {
        private const val PAGE_NAME = "Picker Preview"
    }

}