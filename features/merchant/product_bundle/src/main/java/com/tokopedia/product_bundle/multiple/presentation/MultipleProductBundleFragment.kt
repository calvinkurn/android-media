package com.tokopedia.product_bundle.multiple.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product_bundle.R

class MultipleProductBundleFragment : BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_product_bundle, container, false)
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MultipleProductBundleFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}