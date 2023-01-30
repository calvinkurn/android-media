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
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardMetricsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AffiliatePerformaSharedProductCardsItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliatePerformaSharedProductCardsModel>(itemView) {
    private val adapterMetrics = AffiliateAdapter(AffiliateAdapterFactory())
    private val metricRv = itemView.findViewById<RecyclerView>(R.id.metric_rv)
    private val productImage = itemView.findViewById<ImageUnify>(R.id.product_image)
    private val productName = itemView.findViewById<Typography>(R.id.product_name)
    private val productStatus = itemView.findViewById<Typography>(R.id.product_status)
    private val ssaLabel = itemView.findViewById<Label>(R.id.ssa_label)
    private val rvLayoutManager = GridLayoutManager(itemView.context, SPAN_COUNT)

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
        const val PRODUCT_INACTIVE = 0
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
            element.product.metrics?.sortedBy { metrics -> metrics?.order }?.forEach { metric ->
                tempList.add(
                    AffiliateProductCardMetricsModel(
                        metric,
                        element.product,
                        element.affiliateSSEAdpTotalClickItem
                    )
                )
            }
            if (tempList.size > SPAN_COUNT) rvLayoutManager.spanCount = tempList.size
            adapterMetrics.addMoreData(tempList)
        }
    }

    private fun setItemData(element: AffiliatePerformaSharedProductCardsModel?) {
        element?.product?.let { product ->
            productImage.setImageUrl(product.image?.androidURL ?: "")
            productName.text = product.itemTitle
            productStatus.text =
                getString(R.string.affiliate_date, formatDate(element.product.linkGeneratedAt))
            val disabledColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN400
            )
            val activeColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
            productStatus.setTextColor(disabledColor)
            productName.setTextColor(
                if (product.status == PRODUCT_ACTIVE) {
                    activeColor
                } else {
                    disabledColor
                }
            )
            ssaLabel.apply {
                isVisible = product.ssaStatus.orFalse()
                text = product.ssaLabel?.labelText.orEmpty()
            }

            itemView.setOnClickListener {
                if (product.itemType == PRODUCT_ITEM) {
                    sendSelectContentEvent(product)
                } else {
                    sendShopClickEvent(
                        product
                    )
                }
                productClickInterface?.onProductClick(
                    product.itemID!!,
                    product.itemTitle ?: "",
                    product.image?.androidURL
                        ?: "",
                    product.defaultLinkURL ?: "",
                    product.itemID!!,
                    product.status ?: PRODUCT_INACTIVE,
                    if (product.itemType == PRODUCT_ITEM) PAGE_TYPE_PDP else PAGE_TYPE_SHOP,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        ssaStatus = product.ssaStatus.orFalse(),
                        ssaMessage = product.ssaMessage.orEmpty(),
                        message = product.message.orEmpty(),
                        label = AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelType = product.ssaLabel?.labelType.orEmpty(),
                            labelText = product.ssaLabel?.labelText.orEmpty()
                        )
                    )
                )
            }
        }
    }

    private fun formatDate(rfc3339Date: String?): String {
        val rfc3339DatePattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val dayMonYearPattern = "dd MMM yyyy"
        return rfc3339Date?.let {
            try {
                val inputFormat = SimpleDateFormat(rfc3339DatePattern, Locale.getDefault())
                val outputFormat = SimpleDateFormat(dayMonYearPattern, Locale.getDefault())
                inputFormat.parse(rfc3339Date)?.let {
                    outputFormat.format(it)
                } ?: rfc3339Date
            } catch (e: Exception) {
                Timber.e(e)
                rfc3339Date
            }
        } ?: ""
    }

    private fun sendSelectContentEvent(
        product: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item
    ) {
        val label =
            if (product.status == PRODUCT_ACTIVE) {
                AffiliateAnalytics.LabelKeys.ACTIVE
            } else {
                AffiliateAnalytics.LabelKeys.INACTIVE
            }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_PRODUCT_PRODUL_YANG_DIPROMOSIKAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            UserSession(itemView.context).userId,
            product.itemID,
            bindingAdapterPosition - 1,
            "${product.itemID}" +
                " - ${product.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue}" +
                " - ${product.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue}" +
                " - ${product.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue}" +
                " - $label - ${
                if (product.ssaStatus == true) "komisi extra" else ""
                }",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SELECT_CONTENT
        )
    }

    private fun sendShopClickEvent(shop: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item) {
        val label =
            if (shop.status == PRODUCT_ACTIVE) {
                AffiliateAnalytics.LabelKeys.ACTIVE
            } else {
                AffiliateAnalytics.LabelKeys.INACTIVE
            }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SHOP_LINK_DENGAN_PERFORMA,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            UserSession(itemView.context).userId,
            shop.itemID,
            bindingAdapterPosition - 1,
            shop.itemTitle,
            "${shop.itemID}" +
                " - ${shop.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue}" +
                " - ${shop.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue}" +
                " - ${shop.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue}" +
                " - $label - ${
                if (shop.ssaStatus == true) "komisi extra" else ""
                }",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SHOP_SELECT_CONTENT
        )
    }
}
