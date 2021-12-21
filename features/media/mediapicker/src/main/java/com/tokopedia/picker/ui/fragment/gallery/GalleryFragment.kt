package com.tokopedia.picker.ui.fragment.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import javax.inject.Inject

class GalleryFragment : BaseDaggerFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

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
        initView()
        initObservable()
    }

    private fun initView() {}

    private fun initObservable() {
        viewModel.files.observe(viewLifecycleOwner, {
            it.forEach { media ->
                println("MEDIAPICKER (file) -> ${media.name}")
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        })
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