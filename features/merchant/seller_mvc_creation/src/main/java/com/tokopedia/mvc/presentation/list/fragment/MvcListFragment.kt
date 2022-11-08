package com.tokopedia.mvc.presentation.list.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListBinding
import com.tokopedia.mvc.presentation.list.adapter.DummyAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MvcListFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()

    override fun getScreenName() = ""

    override fun initInjector() {
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setupView()
    }

    private fun SmvcFragmentMvcListBinding.setupView() {
        rvVoucher.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvVoucher.adapter = DummyAdapter().apply {
            setDataList(List(100) {
                it.toString() + it.toString()
            })
        }
    }
}
