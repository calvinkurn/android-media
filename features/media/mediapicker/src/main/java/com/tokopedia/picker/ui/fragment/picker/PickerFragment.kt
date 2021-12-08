package com.tokopedia.picker.ui.fragment.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentPickerBinding
import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.fragment.PickerFragmentFactory
import com.tokopedia.picker.ui.fragment.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.fragment.PickerNavigator
import com.tokopedia.utils.view.binding.viewBinding

class PickerFragment : BaseDaggerFragment() {

    private val binding by viewBinding<FragmentPickerBinding>()
    private var navigator: PickerNavigator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_picker,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigator()

        navigator?.start(PickerFragmentType.CAMERA)

        binding?.btnCamera?.setOnClickListener {
            navigator?.onPageSelected(PickerFragmentType.CAMERA)
        }

        binding?.btnGallery?.setOnClickListener {
            navigator?.onPageSelected(PickerFragmentType.GALLERY)
        }
    }

    private fun setupNavigator() {
        navigator = PickerNavigator(
            requireContext(),
            R.id.picker_container,
            childFragmentManager,
            createFragmentFactory()
        )
    }

    private fun createFragmentFactory(): PickerFragmentFactory {
        return PickerFragmentFactoryImpl()
    }

    override fun initInjector() {}

    override fun getScreenName() = "Picker"

}