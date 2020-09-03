package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography


class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var mProductRevampComponentViewModel: ProductCardRevampViewModel
    private var mCardTitle: Typography = itemView.findViewById(R.id.title)
    private var mCardSubHeader: Typography = itemView.findViewById(R.id.sub_header)
    private var mLihatSemuaButton: Typography = itemView.findViewById(R.id.lihat_semua_button)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mProductRevampComponentViewModel.getSyncPageLiveData().observe(it, Observer { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }

            })
            mProductRevampComponentViewModel.getProductCarouselComponentData().observe(it, Observer { component ->
                addCardHeader(component)
            })
        }
    }

    private fun addCardHeader(componentsItem: ComponentsItem) {
        componentsItem.lihatSemua?.run {
            mCardTitle.setTextAndCheckShow(header)
            mCardSubHeader.setTextAndCheckShow(subheader)
            mLihatSemuaButton.visibility = if (applink.isNotEmpty()) {
                mLihatSemuaButton.setOnClickListener {
                    sendOnClickSeeAllGtm(componentsItem)
                    RouteManager.route(fragment.context, applink)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun sendOnClickSeeAllGtm(componentsItem: ComponentsItem) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackHeaderSeeAllClick(mProductRevampComponentViewModel.isUserLoggedIn(), componentsItem)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductRevampComponentViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }

}