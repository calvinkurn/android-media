package com.tokopedia.tokopoints.view.merchantcoupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem
import com.tokopedia.tokopoints.view.model.merchantcoupon.Productlist
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.ImageUnify
import java.util.*

class MerchantCouponListAdapter(val viewmodel: MerchantCouponViewModel, callback: AdapterCallback) : BaseAdapter<CatalogMVCWithProductsListItem>(callback) {
    private var mRecyclerView: RecyclerView? = null

    inner class CouponListViewHolder(view: View) : BaseVH(view) {

        var ivShopIcon: AppCompatImageView = view.findViewById(R.id.iv_shop_icon)
        var ivCouponOne: ImageUnify = view.findViewById(R.id.iv_coupon1)
        var ivCouponTwo: ImageUnify = view.findViewById(R.id.iv_coupon2)
        var tvShopName = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_shop_name)
        var tvCashBackTitle = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_cashback_title)
        var tvCashBackValue = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_cashback_value)
        var tvCouponCount = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_coupon_count)
        var tvDealsCouponOne = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_deals_coupon1)
        var tvDealsCouponTwo = view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_deals_coupon2)


        override fun bindView(item: CatalogMVCWithProductsListItem?, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(parent: ViewGroup?, inflater: LayoutInflater?, viewType: Int): BaseVH {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.tp_layout_mvc_item, parent, false)

        return CouponListViewHolder(itemView)
    }

    private fun setData(vh: CouponListViewHolder, item: CatalogMVCWithProductsListItem?) {

        item?.shopInfo?.iconUrl?.let {
            if (it.isNotEmpty()) {
                vh.ivShopIcon.loadImage(it)
            }
        }

        item?.products?.get(0)?.imageURL?.let {
            if (it.isNotEmpty()) {
                vh.ivCouponOne.setImageUrl(it, 1f)
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
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_TITLE)
        }

    }

    override fun loadData(currentPageIndex: Int) {
        super.loadData(currentPageIndex)
        viewmodel.merchantCouponData(currentPageIndex)
    }

    fun onSuccess(data: Productlist) {
        data.catalogMVCWithProductsList?.let { loadCompleted(it, data) }
        isLastPage = !data.tokopointsPaging?.hasNext!!
    }

    fun onError() {
        loadCompletedWithError()
    }

    fun sendCouponClickEvent(shopName: String?, eventAction: String) {
        AnalyticsTrackerUtil.sendEvent(
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO,
                eventAction, "{$shopName}",
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is CouponListViewHolder) {
            val data = items[holder.getAdapterPosition()] ?: return

            val item: MutableMap<String, Any?> = HashMap()
            val (shopInfo, _, title, _, _) = data
            val eventLabel = "mvc - {${holder.adapterPosition}} - {${shopInfo?.name}}"
            item["name"] = shopInfo?.name
            item["position"] = holder.adapterPosition.toString()
            item["creative"] = title
            val promotions = HashMap<String, Any>()
            promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
            AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO,
                    AnalyticsTrackerUtil.ActionKeys.VIEW_MVC_COUPON,
                    eventLabel,
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE, promotions)
        }
    }
}