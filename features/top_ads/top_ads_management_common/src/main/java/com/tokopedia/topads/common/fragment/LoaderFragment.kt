package com.tokopedia.topads.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.common.R

class LoaderFragment : BaseDaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.topads_common_loader_fragment, container, false)
        return view
    }

    companion object {
        fun newInstance(): LoaderFragment {
            return LoaderFragment()
        }
    }

    override fun getScreenName(): String {
        return LoaderFragment::class.java.name
    }

    override fun initInjector() {}
}
