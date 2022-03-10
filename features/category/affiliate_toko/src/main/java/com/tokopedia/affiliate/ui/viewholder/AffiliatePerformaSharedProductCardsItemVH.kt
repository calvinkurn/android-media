package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliatePerformaSharedProductCardsItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliatePerformaSharedProductCardsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
        const val PENDAPATAN = "Pendapatan"
        const val KLIK = "Klik"
        const val TERJUAL = "Terjual"
    }

    override fun bind(element: AffiliatePerformaSharedProductCardsModel?) {
        setItemData(element)
        setPerformaData(element)
    }

    private fun setPerformaData(element: AffiliatePerformaSharedProductCardsModel?) {
        if (element?.product?.metrics?.isNotEmpty() == true) {
            element.product.metrics?.forEach { metric ->
                when(metric?.metricTitle){
                    PENDAPATAN -> {
                        setCommisionData(metric)
                    }
                    KLIK -> {
                        setClickData(metric)
                    }
                    TERJUAL -> {
                        setSoldData(metric)
                    }
                }
            }
        }
    }

    private fun setCommisionData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.findViewById<Typography>(R.id.pendapatan_value)?.text = data.metricValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.findViewById(R.id.pendapatan_icon), metricIntValue)
    }

    private fun setSoldData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.findViewById<Typography>(R.id.terjual_value)?.text = data.metricValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.findViewById(R.id.terjual_icon), metricIntValue)
    }

    private fun setClickData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.findViewById<Typography>(R.id.klik_value)?.text = data.metricValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.findViewById(R.id.klik_icon), metricIntValue)
    }

    private fun setTrend(view: IconUnify?, metricIntValue: Double?) {
        metricIntValue?.let {
            when {
                it > 0 -> {
                    view?.apply {
                        show()
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        )
                        rotation = 90f
                    }
                }
                it < 0 -> {
                    view?.apply {
                        show()
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_RN500
                            )
                        )
                        rotation = 270f
                    }
                }
                else -> {
                    view?.hide()
                }
            }
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
                sendSelectContentEvent(product)
                productClickInterface?.onProductClick(
                    product.itemID!!, product.itemTitle ?: "", product.image?.androidURL
                        ?: "", product.defaultLinkURL ?: "",
                    product.itemID!!, product.status ?: 0
                )
            }
        }
    }

    private fun sendSelectContentEvent(product: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item) {
        val label = if(product.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(AffiliateAnalytics.EventKeys.SELECT_CONTENT,AffiliateAnalytics.ActionKeys.CLICK_PRODUCT_PRODUL_YANG_DIPROMOSIKAN,
        AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,UserSession(itemView.context).userId,product.itemID,adapterPosition-1,product.itemTitle,"${product.itemID} - $label",AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SELECT_CONTENT)
    }
}
