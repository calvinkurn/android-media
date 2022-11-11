package com.tokopedia.mvc.presentation.list.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.mvc.databinding.SmcFragmentMvcListBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MvcListFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<SmcFragmentMvcListBinding>()

    override fun getScreenName() = ""

    override fun initInjector() {
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }
}
