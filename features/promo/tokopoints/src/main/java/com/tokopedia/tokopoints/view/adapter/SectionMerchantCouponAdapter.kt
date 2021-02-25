package com.tokopedia.tokopoints.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.ImageUnify
import java.util.*

class SectionMerchantCouponAdapter(val arrayList: MutableList<CatalogMVCWithProductsListItem>) : RecyclerView.Adapter<SectionMerchantCouponAdapter.CouponListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_mvc_item_section, parent, false)
        return CouponListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CouponListViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class CouponListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivShopIcon: AppCompatImageView = view.findViewById(R.id.iv_shop_icon)
        var ivCouponOne: ImageUnify = view.findViewById(R.id.iv_coupon1)
        var ivCouponTwo: ImageUnify = view.findViewById(R.id.iv_coupon2)
        var tvShopName = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_shop_name)
        var tvCashBackTitle = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_cashback_title)
        var tvCashBackValue = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_cashback_value)
        var tvCouponCount = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_coupon_count)
        var tvDealsCouponOne = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_deals_coupon1)
        var tvDealsCouponTwo = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_deals_coupon2)

        fun bind(position: Int) {
            setData(this, arrayList[position])
        }
    }

    private fun setData(vh: CouponListViewHolder, item: CatalogMVCWithProductsListItem?) {

        item?.shopInfo?.iconUrl?.let {
            if (it.isNotEmpty()) {
                vh.ivShopIcon.loadImage(it)
            }
        }

        item?.products?.get(0)?.imageURL?.let {
            if (it.isNotEmpty()) {
                vh.ivCouponOne.loadImage(it)
            }
        }

        item?.products?.get(1)?.imageURL?.let {
            if (it.isNotEmpty()) {
                vh.ivCouponTwo.loadImage(it)
            }
        }

        vh.tvShopName.text = item?.shopInfo?.name
        vh.tvCashBackTitle.text = item?.title
        vh.tvCashBackValue.text = item?.maximumBenefitAmountStr
        vh.tvCouponCount.text = item?.subtitle
        vh.tvDealsCouponOne.text = item?.products?.get(0)?.benefitLabel
        vh.tvDealsCouponTwo.text = item?.products?.get(1)?.benefitLabel

        vh.tvShopName.setOnClickListener {
            RouteManager.route(vh.itemView.context, item?.shopInfo?.appLink)
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_SHOP_NAME)
        }

        vh.ivCouponOne.setOnClickListener {
            RouteManager.route(vh.itemView.context, item?.products?.get(0)?.redirectAppLink)
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_PRODUCT_CARD)
        }

        vh.ivCouponTwo.setOnClickListener {
            RouteManager.route(vh.itemView.context, item?.products?.get(1)?.redirectAppLink)
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_PRODUCT_CARD)
        }

        vh.itemView.setOnClickListener {
            item?.shopInfo?.id?.let { it1 -> it.context.startActivity(TransParentActivity.getIntent(it.context, it1, 0)) }
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_TITLE)

        }

    }

    private fun sendCouponClickEvent(shopName: String?, eventAction: String) {
        AnalyticsTrackerUtil.sendEvent(
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                eventAction, "mvc section - {$shopName}",
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
    }

    override fun onViewAttachedToWindow(holder: CouponListViewHolder) {
        super.onViewAttachedToWindow(holder)
        val item: MutableMap<String, Any?> = HashMap()
        val (shopInfo, _, title, _, _) = arrayList[holder.adapterPosition] ?: return
        val eventLabel = "mvc - {${holder.adapterPosition}} - {${shopInfo?.name}}"
        item["item_name"] = shopInfo?.name
        item["position"] = holder.adapterPosition.toString()
        item["creative_name"] = title
        item["item_id"] = shopInfo?.id

        val promotions = HashMap<String, Any>()
        promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_MVC_COUPON_ON_REWARDS,
                eventLabel,
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE, promotions)
    }
}