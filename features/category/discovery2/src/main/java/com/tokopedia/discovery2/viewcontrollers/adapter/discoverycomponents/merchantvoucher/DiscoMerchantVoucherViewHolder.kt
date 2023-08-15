package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.merchantvoucher.DiscoMerchantAnalytics
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSession

class DiscoMerchantVoucherViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var discoMerchantVoucherViewModel: DiscoMerchantVoucherViewModel? = null
    private val mvcView: MvcView = itemView.findViewById(R.id.disco_single_mv)
    private val shimmer: LoaderUnify = itemView.findViewById(R.id.shimmer_view)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        discoMerchantVoucherViewModel = discoveryBaseViewModel as DiscoMerchantVoucherViewModel
        discoMerchantVoucherViewModel?.let {
            getSubComponent().inject(it)
        }
        if (UserSession(fragment.context).isLoggedIn) {
            shimmer.show()
            discoveryBaseViewModel.fetchDataForCoupons()
        } else {
            shimmer.hide()
            mvcView.hide()
        }
    }

    private fun sendImpressionTracking(data: MvcData) {
        discoMerchantVoucherViewModel?.let { discoMerchantVoucherViewModel ->
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackSingleMerchantVoucherImpression(
                    discoMerchantVoucherViewModel.components,
                    discoMerchantVoucherViewModel.getShopID(),
                    UserSession(fragment.context).userId,
                    discoMerchantVoucherViewModel.position,
                    Utils.extractFromHtml(data.animatedInfoList?.firstOrNull()?.title)
                )
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            discoMerchantVoucherViewModel?.let { discoMerchantVoucherViewModel ->
                discoMerchantVoucherViewModel.mvcData.observe(owner) {
                    shimmer.hide()
                    mvcView.show()
                    getAnalytics(it.animatedInfoList?.firstOrNull()?.title)?.let { analytics ->
                        mvcView.setData(
                            it,
                            discoMerchantVoucherViewModel.getShopID(),
                            MvcSource.DISCO,
                            {
                                (fragment as DiscoveryFragment).startMVCTransparentActivity(
                                    discoMerchantVoucherViewModel.position,
                                    discoMerchantVoucherViewModel.getShopID(),
                                    discoMerchantVoucherViewModel.getProductId(),
                                    it.hashCode()
                                )
                            },
                            mvcTrackerImpl = analytics
                        )
                    }
                    sendImpressionTracking(it)
                }
                discoMerchantVoucherViewModel.errorState.observe(owner) {
                    if (it) {
                        shimmer.hide()
                        mvcView.hide()
                    }
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoMerchantVoucherViewModel?.mvcData?.removeObservers(it)
            discoMerchantVoucherViewModel?.errorState?.removeObservers(it)
        }
    }

    private fun getAnalytics(couponName: String?): DiscoMerchantAnalytics? {
        return discoMerchantVoucherViewModel?.let {
            DiscoMerchantAnalytics(
                (fragment as DiscoveryFragment).getDiscoveryAnalytics(),
                it.components,
                it.position,
                Utils.extractFromHtml(couponName)
            )
        }
    }
}
