package com.tokopedia.buyerorder.recharge.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorder.databinding.FragmentRechargeOrderDetailBinding
import com.tokopedia.buyerorder.recharge.di.RechargeOrderDetailComponent

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentRechargeOrderDetailBinding

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(RechargeOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        private const val EXTRA_ORDER_CATEGORY = "EXTRA_ORDER_CATEGORY"

        fun getInstance(orderId: String,
                        orderCategory: String): RechargeOrderDetailFragment =
                RechargeOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_ORDER_CATEGORY, orderCategory)
                        putString(EXTRA_ORDER_ID, orderId)
                    }
                }
    }

}