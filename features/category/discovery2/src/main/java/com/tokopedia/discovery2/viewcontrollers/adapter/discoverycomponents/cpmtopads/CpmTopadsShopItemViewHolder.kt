package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.topads.sdk.domain.model.ImageShop

class CpmTopadsShopItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var cpmTopadsItemViewModel: CpmTopadsShopItemViewModel
    private var dataItem: DataItem? = null

    private val brandImage: ImageView = itemView.findViewById(R.id.shop_image)
    private val brandName: TextView = itemView.findViewById(R.id.shop_name)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopadsItemViewModel = discoveryBaseViewModel as CpmTopadsShopItemViewModel
        cpmTopadsItemViewModel.getComponent().observe(fragment.viewLifecycleOwner, Observer { item ->
            dataItem = item.data?.getOrElse(0) { DataItem() }
            ImageHandler.loadImageCircle2(itemView.context,
                    brandImage,
                    dataItem?.imageUrlMobile)
            dataItem?.let { setName(it) }
            setTrackEvent(dataItem, brandImage)
            setClick(dataItem)
        })

    }

    private fun setTrackEvent(data: DataItem?, view: View) {
        val shopData = ImageShop()
        shopData.setsUrl(data?.imageUrlMobile ?: "")
        view.addOnImpressionListener(shopData, object : ViewHintListener {
            override fun onViewHint() {
                sendTopAdsShopImpression(data)
            }
        })
    }

    private fun sendTopAdsShopImpression(data: DataItem?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventImpressionTopAdsShop(data)
    }

    private fun setClick(data: DataItem?) {
        data?.let {
            if (!it.applinks.isNullOrEmpty()) {
                itemView.setOnClickListener { itemView ->
                    RouteManager.route(itemView.context, it.applinks)
                    sendTopAdsShopClick(it)
                }
            }
        }
    }

    private fun sendTopAdsShopClick(data: DataItem) {
        cpmTopadsItemViewModel.sendTopAdsClickTracking(data.shopAdsClickURL)
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickTopAdsShop(data)
    }

    private fun setName(item: DataItem) {
        val name = "${item.name} \n${item.buttonText}"
        val button = item.buttonText ?: ""
        val startIndexOfLink = name.indexOf(button)
        val spannableString = SpannableString(name)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(itemView.context, R.color.green_250)
            }
        }, startIndexOfLink, startIndexOfLink + button.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        brandName.highlightColor = Color.TRANSPARENT
        brandName.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        dataItem?.let { cpmTopadsItemViewModel.sendTopAdsViewTracking(it.shopAdsViewURL) }
    }

}