package com.tokopedia.pms.howtopay_native.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.pms.R

class HowToPayFragment : BaseDaggerFragment(){

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pms_hwp_info, container, false)
    }

    private fun addVirtualAccountPayment(){

    }

    private fun addBankTransferPayment(){

    }

    private fun addStoreTransferPayment(){

    }

    private fun addKlickBCAPayment(){

    }


    companion object{
        fun getInstance(intent: Intent) : HowToPayFragment{
            return HowToPayFragment()
        }
    }

}