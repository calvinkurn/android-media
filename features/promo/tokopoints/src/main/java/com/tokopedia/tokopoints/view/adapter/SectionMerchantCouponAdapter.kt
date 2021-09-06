package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.multishopmvc.MvcMultiShopView
import com.tokopedia.mvcwidget.multishopmvc.data.AdInfo
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem
import com.tokopedia.mvcwidget.multishopmvc.data.DataMapperMultiShopView
import com.tokopedia.mvcwidget.multishopmvc.data.MultiShopModel
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.tokopointhome.merchantvoucher.MerchantVoucherViewholder
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.set

class SectionMerchantCouponAdapter(val arrayList: ArrayList<CatalogMVCWithProductsListItem>) :
    RecyclerView.Adapter<SectionMerchantCouponAdapter.CouponListViewHolder>(),
    MerchantVoucherViewholder.GetAdInfoData {

    val eventSet = HashSet<String?>()
    val REWARDS_MVC_SOURCE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_mvclist_item, parent, false)
        return CouponListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CouponListViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class CouponListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mvcShopView: MvcMultiShopView = view.findViewById(R.id.mvc_multishop)

        fun bind(position: Int) {
            setData(this, DataMapperMultiShopView.map(arrayList[position]))
        }
    }

    private fun setData(vh: CouponListViewHolder, item: MultiShopModel) {
        vh.mvcShopView.setMultiShopModel(item,REWARDS_MVC_SOURCE)
    }

    private fun sendTopadsClick(context: Context, adInfo: AdInfo?) {
        TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                adInfo?.AdClickUrl,
                adInfo?.AdID,
                "",
                "",
                ""
        )
    }

    private fun sendTopadsImpression(context: Context, adInfo: AdInfo?) {
        if (eventSet.contains(adInfo?.AdID)) {
            eventSet.add(adInfo?.AdID)
            TopAdsUrlHitter(packageName).hitImpressionUrl(
                context,
                adInfo?.AdViewUrl,
                "",
                "",
                ""
            )
        }
    }

    private fun sendCouponClickEvent(shopName: String?, eventAction: String, vh: CouponListViewHolder, adInfo: AdInfo?) {
        sendTopadsClick(vh.itemView.context, adInfo)
        AnalyticsTrackerUtil.sendEvent(
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                eventAction, "mvc section - $shopName",
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
    }

    override fun onViewAttachedToWindow(holder: CouponListViewHolder) {
        super.onViewAttachedToWindow(holder)
        val item: MutableMap<String, Any?> = HashMap()
        val (shopInfo, _, title, _, _) = arrayList[holder.adapterPosition] ?: return
        val eventLabel = "mvc - {${holder.adapterPosition + 1}} - ${shopInfo?.name}"
        item["item_name"] = shopInfo?.name
        item["position"] = (holder.adapterPosition + 1).toString()
        item["creative_name"] = title
        item["item_id"] = shopInfo?.id

        val promotions = HashMap<String, Any>()
        promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
        sendTopadsImpression(holder.itemView.context, arrayList[holder.adapterPosition].AdInfo)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_MVC_COUPON_ON_REWARDS,
                eventLabel,
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE, promotions)
    }

    companion object {
        val packageName = SectionMerchantCouponAdapter::class.java.`package`.toString()
    }

    override fun getAdInfo(): HashSet<String?> {
        return eventSet
    }
}