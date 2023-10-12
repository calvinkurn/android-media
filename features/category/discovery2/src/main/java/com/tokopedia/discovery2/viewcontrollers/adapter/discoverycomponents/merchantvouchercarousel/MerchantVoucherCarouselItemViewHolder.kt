package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.CAROUSEL_ITEM_DESIGN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.merchantvoucher.DiscoMerchantAnalytics
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.multishopmvc.MvcMultiShopView
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.user.session.UserSession

const val RATIO_FOR_CAROUSEL = 0.85

class MerchantVoucherCarouselItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val mvcMultiShopView: MvcMultiShopView = itemView.findViewById(R.id.mvc_multi_view)
    private val parentView: ConstraintLayout = itemView.findViewById(R.id.multishop_parent_view)
    private var merchantVoucherCarouselItemViewModel: MerchantVoucherCarouselItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        merchantVoucherCarouselItemViewModel = discoveryBaseViewModel as MerchantVoucherCarouselItemViewModel
        merchantVoucherCarouselItemViewModel?.let {
            setupView(it.components.design)
            setupMargins(it.components.name)
        }
    }

    private fun setupView(design: String) {
        val params = parentView.layoutParams
        if (design == CAROUSEL_ITEM_DESIGN) {
            val width = Resources.getSystem().displayMetrics.widthPixels
            params.width = (width * RATIO_FOR_CAROUSEL).toInt()
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        parentView.layoutParams = params
    }

    private fun setupMargins(componentName: String?) {
        if (ComponentNames.MerchantVoucherListItem.componentName == componentName) {
            fragment.context?.let {
                parentView.setMargin(
                    it.resources.getDimensionPixelOffset(R.dimen.dp_12),
                    0,
                    it.resources.getDimensionPixelOffset(R.dimen.dp_12),
                    0
                )
            }
        } else {
            parentView.setMargin(0, 0, 0, 0)
        }
    }
    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            merchantVoucherCarouselItemViewModel?.multiShopModel?.observe(lifecycle) {
                mvcMultiShopView.show()
                merchantVoucherCarouselItemViewModel?.syncParentPosition()
                getMerchantAnalytics()?.let { it1 -> mvcMultiShopView.setTracker(it1) }
                mvcMultiShopView.setMultiShopModel(it, MvcSource.DISCO)
                merchantVoucherCarouselItemViewModel?.let { viewModel ->
                    (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                        .trackMerchantVoucherMultipleImpression(
                            viewModel.components,
                            UserSession(fragment.context).userId,
                            viewModel.position
                        )
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { merchantVoucherCarouselItemViewModel?.multiShopModel?.removeObservers(it) }
    }

    private fun getMerchantAnalytics(): DiscoMerchantAnalytics? {
        return merchantVoucherCarouselItemViewModel?.let { viewModel ->
            DiscoMerchantAnalytics(
                (fragment as DiscoveryFragment).getDiscoveryAnalytics(),
                viewModel.components,
                viewModel.components.parentComponentPosition,
                "",
                viewModel.position
            )
        }
    }
}
