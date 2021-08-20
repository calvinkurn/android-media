package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.mvcwidget.views.MvcView

class DiscoMerchantVoucherViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var discoMerchantVoucherViewModel: DiscoMerchantVoucherViewModel
    private val mvcView: MvcView = itemView.findViewById(R.id.disco_single_mv)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        discoMerchantVoucherViewModel = discoveryBaseViewModel as DiscoMerchantVoucherViewModel
        getSubComponent().inject(discoMerchantVoucherViewModel)
        discoveryBaseViewModel.fetchDataForCoupons()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            discoMerchantVoucherViewModel.mvcData.observe(owner, {
//                TODO:: Add start activity for result HOF
                mvcView.setData(it, discoMerchantVoucherViewModel.getShopID(), MvcSource.DISCO)
            })
            discoMerchantVoucherViewModel.errorState.observe(owner, {
                if (it) {
                    mvcView.hide()
                }
            })
        }

    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoMerchantVoucherViewModel.mvcData.removeObservers(it)
            discoMerchantVoucherViewModel.errorState.removeObservers(it)
        }
    }
}