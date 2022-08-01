package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_TYPE_PDP
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardMetricsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliatePerformaSharedProductCardsItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliatePerformaSharedProductCardsModel>(itemView) {
    private val adapterMetrics = AffiliateAdapter(AffiliateAdapterFactory())
    private val metricRv = itemView.findViewById<RecyclerView>(R.id.metric_rv)
    private val rvLayoutManager = GridLayoutManager(itemView.context,SPAN_COUNT)
    init {
        metricRv?.apply {
            layoutManager = rvLayoutManager
            adapter = adapterMetrics
        }
    }
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
        const val SPAN_COUNT = 3
        private const val PRODUCT_ITEM = 0

    }

    override fun bind(element: AffiliatePerformaSharedProductCardsModel?) {
        setItemData(element)
        setPerformaData(element)
    }

    private fun setPerformaData(element: AffiliatePerformaSharedProductCardsModel?) {
        if (element?.product?.metrics?.isNotEmpty() == true) {
            adapterMetrics.clearAllElements()
            val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
            element.product.metrics?.sortedBy { metrics -> metrics?.order }?.forEach {metric ->
                tempList.add(AffiliateProductCardMetricsModel(metric))
            }
            if(tempList.size > SPAN_COUNT)rvLayoutManager.spanCount = tempList.size
            adapterMetrics.addMoreData(tempList)
        }
    }

    private fun setItemData(element: AffiliatePerformaSharedProductCardsModel?) {
        element?.product?.let { product ->
            itemView.findViewById<ImageUnify>(R.id.product_image)?.setImageUrl(product.image?.androidURL ?: "")
            itemView.findViewById<Typography>(R.id.product_name)?.text = product.itemTitle
            if (product.status == PRODUCT_ACTIVE) {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_active
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.text = getString(R.string.affiliate_active)
            } else {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_inactive
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.text = getString(R.string.affiliate_inactive)
            }
            itemView.setOnClickListener {
                if (product.itemType == PRODUCT_ITEM) sendSelectContentEvent(product) else sendShopClickEvent(product)
                productClickInterface?.onProductClick(
                    product.itemID!!, product.itemTitle ?: "", product.image?.androidURL
                        ?: "", product.defaultLinkURL ?: "",
                    product.itemID!!, product.status ?: 0,
                    if (product.itemType == PRODUCT_ITEM) PAGE_TYPE_PDP else PAGE_TYPE_SHOP
                )
            }
        }
    }

    private fun sendSelectContentEvent(product: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item) {
        val label = if (product.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_PRODUCT_PRODUL_YANG_DIPROMOSIKAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            UserSession(itemView.context).userId,
            product.itemID,
            adapterPosition-1,
            "${product.itemID} - ${product.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue} - ${product.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue} - ${product.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue} - $label",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SELECT_CONTENT
        )
    }
    private fun sendShopClickEvent(shop: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item){
        val label = if (shop.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SHOP_LINK_DENGAN_PERFORMA,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            UserSession(itemView.context).userId,
            shop.itemID,
            adapterPosition-1,
            shop.itemTitle,
            "${shop.itemID} - ${shop.metrics?.findLast { it?.metricType == "shopOrderCommissionPerItem" }?.metricValue} - ${shop.metrics?.findLast { it?.metricType == "shopTotalClickPerItem" }?.metricValue} - ${shop.metrics?.findLast { it?.metricType == "shopOrderPerItem" }?.metricValue} - $label",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SHOP_SELECT_CONTENT)
    }
}
