package com.tokopedia.mlp.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.mlp.adapter.RecylerAdapter
import com.tokopedia.mlp.merchantViewModel.MerchantLendingViewModel

class MerchantLending : BaseDaggerFragment {



    lateinit var merchantLendingViewModel:MerchantLendingViewModel

    override fun getScreenName(): String? = null

    override fun initInjector() {
    }


    constructor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        merchantLendingViewModel=ViewModelProviders.of(this).get(MerchantLendingViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.container_mlp, container, false)



    }
}

