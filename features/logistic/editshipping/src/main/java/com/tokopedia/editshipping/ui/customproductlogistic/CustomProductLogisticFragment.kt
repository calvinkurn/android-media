package com.tokopedia.editshipping.ui.customproductlogistic

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.databinding.FragmentCustomProductLogisticBinding
import com.tokopedia.utils.lifecycle.autoCleared

class CustomProductLogisticFragment: BaseDaggerFragment() {


    private var binding by autoCleared<FragmentCustomProductLogisticBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        TODO("Not yet implemented")
    }

}