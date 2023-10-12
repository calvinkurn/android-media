package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.analytics.merchantvoucher.DiscoMerchantAnalytics
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.MerchantVoucherGridItemLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.user.session.UserSession

class MerchantVoucherGridItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding = MerchantVoucherGridItemLayoutBinding.bind(itemView)

    private var viewModel: MerchantVoucherGridItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? MerchantVoucherGridItemViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getComponentData()?.observe(lifeCycle) {
                renderVoucher(it)
                trackImpression()
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.getComponentData()?.removeObservers(it) }
    }

    private fun renderVoucher(item: DataItem) {
        binding.merchantVoucherGrid.run {
            setData(item)
            onClick {
                RouteManager.route(itemView.context, item.shopInfo?.appLink)
                trackClickEvent(item)
            }
        }
    }

    private fun trackClickEvent(item: DataItem) {
        val discoveryAnalytics = getAnalytics() ?: return

        viewModel?.run {
            val merchantAnalytics = DiscoMerchantAnalytics(
                discoveryAnalytics,
                component,
                component.parentComponentPosition,
                null,
                position
            )

            val shopName = item.shopInfo?.name.orEmpty()
            val eventAction = "click shop name"

            merchantAnalytics.mvcMultiShopCardClick(
                shopName,
                eventAction,
                MvcSource.DISCO,
                UserSession(fragment.context).userId,
                0
            )
        }
    }

    private fun trackImpression() {
        viewModel?.run {
            getAnalytics()?.trackMerchantVoucherMultipleImpression(
                component,
                UserSession(fragment.context).userId,
                position
            )
        }
    }

    private fun getAnalytics() = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
}
