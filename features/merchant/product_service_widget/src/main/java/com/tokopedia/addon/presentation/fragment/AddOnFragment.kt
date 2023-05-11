package com.tokopedia.addon.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product_service_widget.databinding.FragmentBottomsheetAddonBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AddOnFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentBottomsheetAddonBinding>()

    override fun getScreenName(): String = AddOnFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomsheetAddonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding?.addonWidget?.getBundleData(param)
    }
}
