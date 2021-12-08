package com.tokopedia.picker.ui.fragment.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentPickerBinding
import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.common.PickerPageType
import com.tokopedia.picker.ui.fragment.PickerFragmentFactory
import com.tokopedia.picker.ui.fragment.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.fragment.PickerNavigator
import com.tokopedia.picker.ui.fragment.PickerUiConfig
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
        setupPickerByPage()
        setupTabView()
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

    private fun setupTabView() {
        binding?.tabContainer?.addNewTab("Camera")
        binding?.tabContainer?.addNewTab("Gallery")

        binding?.tabContainer?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    navigator?.onPageSelected(PickerFragmentType.CAMERA)
                } else if (tab?.position == 1) {
                    navigator?.onPageSelected(PickerFragmentType.GALLERY)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupPickerByPage() {
        when (PickerUiConfig.paramPage) {
            PickerPageType.CAMERA -> navigator?.start(PickerFragmentType.CAMERA)
            PickerPageType.GALLERY -> navigator?.start(PickerFragmentType.GALLERY)
            else -> {
                // show camera as first fragment page
                navigator?.start(PickerFragmentType.CAMERA)

                // show tab navigation
                binding?.tabContainer?.visibility = View.VISIBLE
            }
        }
    }

    override fun initInjector() {}

    override fun getScreenName() = "Picker"

}