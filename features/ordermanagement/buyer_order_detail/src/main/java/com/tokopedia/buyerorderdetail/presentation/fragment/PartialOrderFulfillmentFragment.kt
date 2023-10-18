package com.tokopedia.buyerorderdetail.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.databinding.FragmentPartialOrderFulfillmentBinding
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.PartialOrderFulfillmentActivity
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.PartialOrderFulfillmentBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PartialOrderFulfillmentFragment : BaseDaggerFragment() {

    private val orderId by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(ApplinkConstInternalOrder.PARAM_ORDER_ID)
    }

    private var binding by autoClearedNullable<FragmentPartialOrderFulfillmentBinding>()

    override fun getScreenName(): String = PartialOrderFulfillmentFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPartialOrderFulfillmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPartialOrderFulfillmentBottomSheet()
    }

    private fun showPartialOrderFulfillmentBottomSheet() {
        orderId?.let {
            val bottomSheet = PartialOrderFulfillmentBottomSheet.newInstance(it)
            bottomSheet.setOnDismissListener {
                (activity as? PartialOrderFulfillmentActivity)?.setResultFinish(Activity.RESULT_CANCELED)
            }
            bottomSheet.show(childFragmentManager)
        }
    }

    companion object {
        fun newInstance(bundle: Bundle): PartialOrderFulfillmentFragment {
            return PartialOrderFulfillmentFragment().apply {
                arguments = bundle
            }
        }
    }
}
