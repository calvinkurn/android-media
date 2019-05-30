package com.tokopedia.mlp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

class MerchantLending : BaseDaggerFragment {
    override fun getScreenName(): String? = null

    override fun initInjector() {
    }


    constructor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_merchant_lending, container, false)
    }
}
