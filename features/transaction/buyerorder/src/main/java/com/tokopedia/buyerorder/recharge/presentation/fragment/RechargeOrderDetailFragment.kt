package com.tokopedia.buyerorder.recharge.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorder.databinding.FragmentRechargeOrderDetailBinding
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.di.RechargeOrderDetailComponent
import com.tokopedia.buyerorder.recharge.presentation.viewmodel.RechargeOrderDetailViewModel
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentRechargeOrderDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var rechargeViewModel: RechargeOrderDetailViewModel

    private var orderId: String = ""
    private var orderCategory: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(RechargeOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            rechargeViewModel = ViewModelProvider(this, viewModelFactory).get(RechargeOrderDetailViewModel::class.java)
        }

        savedInstanceState?.let {
            orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
        }

        arguments?.let {
            if (it.containsKey(EXTRA_ORDER_ID))
                orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            if (it.containsKey(EXTRA_ORDER_CATEGORY))
                orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rechargeViewModel.fetchData(
                RechargeOrderDetailRequest(
                        orderCategory = orderCategory,
                        orderId = orderId
                )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_ORDER_ID, orderId)
        outState.putString(EXTRA_ORDER_CATEGORY, orderCategory)
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