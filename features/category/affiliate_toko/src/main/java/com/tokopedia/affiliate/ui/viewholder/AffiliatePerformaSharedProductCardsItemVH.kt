package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.AffiliatePerformanceListData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import kotlinx.android.synthetic.main.affiliate_performa_vertical_product_card_item_layout.view.*

class AffiliatePerformaSharedProductCardsItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliatePerformaSharedProductCardsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
    }

    override fun bind(element: AffiliatePerformaSharedProductCardsModel?) {
        setItemData(element)
        setPerformaData(element)
    }

    private fun setPerformaData(element: AffiliatePerformaSharedProductCardsModel?) {
        if (element?.product?.metrics?.isNotEmpty() == true) {
            element.product.metrics?.get(0)?.let {
                setCommisionData(element.product.metrics?.get(0)!!)
            }
            element.product.metrics?.get(1)?.let {
                setSoldData(element.product.metrics?.get(1)!!)
            }
            element.product.metrics?.get(2)?.let {
                setClickData(element.product.metrics?.get(2)!!)
            }
        }
    }

    private fun setCommisionData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.pendapatan_value.text = data.metricDifferenceValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.pendapatan_icon, metricIntValue)
    }

    private fun setSoldData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.terjual_value.text = data.metricDifferenceValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.terjual_icon, metricIntValue)
    }

    private fun setClickData(data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric) {
        itemView.klik_value.text = data.metricDifferenceValueFmt
        val metricIntValue: Double? = data.metricDifferenceValue?.toDouble()
        setTrend(itemView.klik_icon, metricIntValue)
    }

    private fun setTrend(view: IconUnify?, metricIntValue: Double?) {
        metricIntValue?.let {
            when {
                it > 0 -> {
                    view?.apply {
                        setImage(
                            newLightEnable = ContextCompat.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        )
                        rotation = 90f
                    }
                }
                it < 0 -> {
                    view?.apply {
                        setImage(
                            newLightEnable = ContextCompat.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_RN500
                            )
                        )
                        rotation = 270f
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun setItemData(element: AffiliatePerformaSharedProductCardsModel?) {
        element?.product?.let { product ->
            itemView.product_image.setImageUrl(product.image?.androidURL ?: "")
            itemView.product_name.text = product.itemTitle
            if (product.status == PRODUCT_ACTIVE) {
                itemView.status_bullet.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_active
                    )
                )
                itemView.product_status.setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
                itemView.product_status.text = getString(R.string.affiliate_active)
            } else {
                itemView.status_bullet.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_inactive
                    )
                )
                itemView.product_status.setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    )
                )
                itemView.product_status.text = getString(R.string.affiliate_inactive)
            }
            itemView.setOnClickListener {
                productClickInterface?.onProductClick(
                    product.itemID!!, product.itemTitle ?: "", product.image?.androidURL
                        ?: "", "",
                    product.itemID!!, product.status ?: 0
                )
            }
        }
    }
}
