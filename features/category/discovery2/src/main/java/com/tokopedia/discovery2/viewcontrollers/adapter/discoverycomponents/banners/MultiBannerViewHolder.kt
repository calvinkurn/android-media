package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.utils.Utils
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.multi_banner_layout.view.*

class MultiBannerViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    private var constraintLayout: ConstraintLayout
    private var context: Context

    private lateinit var multiBannerViewModel: MultiBannerViewModel
    lateinit var bannersItemList: ArrayList<BannerItem>

    init {
        constraintLayout = itemView.findViewById(R.id.banner_container_layout)
        context = constraintLayout.context
    }

    override fun bindView(fragment: Fragment, viewModel: DiscoveryBaseViewModel) {
        multiBannerViewModel = viewModel as MultiBannerViewModel
        multiBannerViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { item ->

            if (item.data != null && item.data.isNotEmpty()) {
                constraintLayout.removeAllViews()
                bannersItemList = ArrayList()
                addBanners(item.data)
            }
        })

        multiBannerViewModel.getPushBannerStatusData().observe(fragment, Observer {
            if (bannersItemList.isNotEmpty() && it != Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS) {
                ImageHandler.LoadImage(bannersItemList[it].bannerImageView, bannersItemList[it].bannerItemData.registeredImageApp)
            }
        })

        multiBannerViewModel.getPushBannerSubscriptionData().observe(fragment, Observer {
            if (bannersItemList.isNotEmpty() && it != Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS) {
                ImageHandler.LoadImage(bannersItemList[it].bannerImageView, bannersItemList[it].bannerItemData.registeredImageApp)
            }
        })

        multiBannerViewModel.getshowLoginData().observe(fragment, Observer {
            context.startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        })
    }

    private fun addBanners(data: List<DataItem>) {
        val constraintSet = ConstraintSet()
        val height = multiBannerViewModel.getBannerUrlHeight()
        val width = multiBannerViewModel.getBannerUrlWidth()

        for ((index, bannerItem) in data.withIndex()) {
            var bannerView: BannerItem
            val isLastItem = index == data.size - 1

            bannerView = if (index == 0) {
                BannerItem(bannerItem, constraintLayout, constraintSet, width, height, index,
                        null, context, isLastItem)
            } else {
                BannerItem(bannerItem, constraintLayout, constraintSet, width, height, index,
                        bannersItemList.get(index - 1), context, isLastItem)
            }
            bannersItemList.add(bannerView)

            checkSubscriptionStatus(index)
            setClickOnBanners(index)
        }
    }

    private fun checkSubscriptionStatus(position: Int) {
        multiBannerViewModel.campaignSubscribedStatus(position)
    }

    private fun setClickOnBanners(index: Int) {
        bannersItemList[index].bannerImageView.setOnClickListener {
            multiBannerViewModel.onBannerClicked(index)

        }
    }
}