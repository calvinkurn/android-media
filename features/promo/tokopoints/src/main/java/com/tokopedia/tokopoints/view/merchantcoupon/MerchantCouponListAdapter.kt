package com.tokopedia.tokopoints.view.merchantcoupon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.SectionMerchantCouponAdapter
import com.tokopedia.tokopoints.view.model.merchantcoupon.AdInfo
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem
import com.tokopedia.tokopoints.view.model.merchantcoupon.Productlist
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.isEventTriggered
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.ImageUnify
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MerchantCouponListAdapter(val viewmodel: MerchantCouponViewModel, callback: AdapterCallback, context: Context?) : BaseAdapter<CatalogMVCWithProductsListItem>(callback) {
    private var mRecyclerView: RecyclerView? = null
    private var adIdImpression = HashSet<String>()
    private val mvcDetailView = context?.let { MvcDetailView(it) }

    inner class CouponListViewHolder(view: View) : BaseVH(view) {

        var ivShopIcon: AppCompatImageView = view.findViewById(R.id.iv_shop_icon)
        var ivShopChevron: AppCompatImageView = view.findViewById(R.id.iv_shop_arrow)
        var ivCouponOne: ImageUnify = view.findViewById(R.id.iv_coupon1)
        var ivCouponTwo: ImageUnify = view.findViewById(R.id.iv_coupon2)
        var productParentOne: ConstraintLayout = view.findViewById(R.id.container_coupon1)
        var productParentTwo: ConstraintLayout = view.findViewById(R.id.container_coupon2)
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

        item?.shopInfo?.shopStatusIconURL?.let {
            if (it.isNotEmpty()) {
                vh.ivShopIcon.loadImage(it)
            }
        }

        if (item?.products?.size != null && item?.products?.size > 0) {
            if (item?.products?.size == 1) {
                vh.productParentOne.show()
                item?.products?.get(0)?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        vh.ivCouponOne.setImageUrl(it, 1f)
                    }
                }
                vh.tvDealsCouponOne.text = item?.products?.get(0)?.benefitLabel
            }

            if (item?.products?.size > 1) {
                vh.productParentTwo.show()
                item?.products?.get(1)?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        vh.ivCouponTwo.setImageUrl(it, 1f)
                    }
                }
                vh.tvDealsCouponTwo.text = item?.products?.get(1)?.benefitLabel
                vh.productParentOne.show()
                item?.products?.get(0)?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        vh.ivCouponOne.loadImage(it)
                    }
                }
                vh.tvDealsCouponOne.text = item?.products?.get(0)?.benefitLabel
            }
        }


        vh.tvShopName.text = item?.shopInfo?.name
        vh.tvCashBackTitle.text = item?.title
        vh.tvCashBackValue.text = item?.maximumBenefitAmountStr
        vh.tvCouponCount.text = item?.subtitle

        vh.tvShopName.setOnClickListener {
            shopClickListener(vh, item)
        }

        vh.ivShopChevron.setOnClickListener {
            shopClickListener(vh, item)
        }

        vh.ivCouponOne.setOnClickListener {
            RouteManager.route(vh.itemView.context, item?.products?.get(0)?.redirectAppLink)
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_PRODUCT_CARD, vh, item?.AdInfo)
        }

        vh.ivCouponTwo.setOnClickListener {
            RouteManager.route(vh.itemView.context, item?.products?.get(1)?.redirectAppLink)
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_PRODUCT_CARD, vh, item?.AdInfo)
        }

        vh.itemView.setOnClickListener {
            item?.shopInfo?.id?.let { it1 -> it.context.startActivity(item?.shopInfo?.appLink?.let { it2 -> TransParentActivity.getIntent(it.context, it1, 0 , it2) }) }
            sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON_TITLE, vh, item?.AdInfo)
        }

    }

    override fun loadData(currentPageIndex: Int) {
        super.loadData(currentPageIndex)
        viewmodel.merchantCouponData(currentPageIndex)
    }

    fun onSuccess(data: Productlist) {
        data.catalogMVCWithProductsList?.let { loadCompleted(it, data) }
        isLastPage = true
    }

    fun onError() {
        loadCompletedWithError()
    }

    fun shopClickListener(vh: CouponListViewHolder, item: CatalogMVCWithProductsListItem?) {
        RouteManager.route(vh.itemView.context, item?.shopInfo?.appLink)
        sendCouponClickEvent(item?.shopInfo?.name, AnalyticsTrackerUtil.ActionKeys.CLICK_SHOP_NAME, vh, item?.AdInfo)
    }

    fun sendCouponClickEvent(shopName: String?, eventAction: String, vh: CouponListViewHolder, adInfo: AdInfo?) {
        sendTopadsClick(vh.itemView.context, adInfo)
        if (shopName != null) {
            AnalyticsTrackerUtil.sendEvent(
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO,
                    eventAction, shopName,
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
            )
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
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
        if (!adIdImpression.contains(adInfo?.AdID) && adInfo?.AdViewUrl!!.isNotBlank() && !isEventTriggered(context,adInfo)){
            adInfo?.AdID?.let { adIdImpression?.add(it) }
            TopAdsUrlHitter(SectionMerchantCouponAdapter.packageName).hitImpressionUrl(
                    context,
                    adInfo?.AdViewUrl,
                    "",
                    "",
                    ""
            )
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is CouponListViewHolder) {
            val data = items[holder.getAdapterPosition()] ?: return

            val item: MutableMap<String, Any?> = HashMap()
            val (shopInfo, _, title, _, _) = data
            val eventLabel = "mvc - {${holder.adapterPosition + 1}} - ${shopInfo?.name}"
            item["item_name"] = shopInfo?.name
            item["position"] = holder.adapterPosition.toString()
            item["creative_name"] = title
            item["item_id"] = shopInfo?.id
            val promotions = HashMap<String, Any>()
            promotions["promotions"] = Arrays.asList<Map<String, Any?>>(item)
            sendTopadsImpression(holder.itemView.context, items[holder.adapterPosition].AdInfo)
            AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO,
                    AnalyticsTrackerUtil.ActionKeys.VIEW_MVC_COUPON,
                    eventLabel,
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE, promotions)
        }
    }
}