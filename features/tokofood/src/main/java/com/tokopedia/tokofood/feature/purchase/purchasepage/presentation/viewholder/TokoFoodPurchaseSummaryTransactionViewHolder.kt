package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CartListAddOnsCustomResponseInfo
import com.tokopedia.tokofood.databinding.ItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.feature.purchase.goneAllChildren
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.visibleAllChildren
import com.tokopedia.unifycomponents.ImageUnify

class TokoFoodPurchaseSummaryTransactionViewHolder(private val viewBinding: ItemPurchaseSummaryTransactionBinding,
                                                   private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_summary_transaction
    }

    override fun bind(element: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.isLoading) {
                constraintTransactionLoading.children.forEach {
                    (it as? ViewGroup)?.visibleAllChildren()
                }
                containerTransactionItem.gone()
            } else {
                constraintTransactionLoading.children.forEach {
                    (it as? ViewGroup)?.goneAllChildren()
                }
                containerTransactionItem.removeAllViews()
                element.transactionList.forEach {
                    val summaryTransactionItem = SubItemPurchaseSummaryTransactionBinding.inflate(
                        LayoutInflater.from(itemView.context)
                    ).apply {
                        textTransactionTitle.text = it.title
                        textTransactionValue.text = it.value
                        iconTransactionSurgePrice.setDetailInfo(it.detailInfo)
                    }
                    containerTransactionItem.addView(summaryTransactionItem.root)
                }
                containerTransactionItem.show()
            }
        }
    }

    private fun ImageUnify.setDetailInfo(detailInfo: CartListAddOnsCustomResponseInfo?) {
        if (detailInfo == null || detailInfo.imageUrl.isBlank()) {
            hide()
            setOnClickListener(null)
        } else {
            show()
            setImageUrl(detailInfo.imageUrl)
            setOnClickListener {
                listener.onSurgePriceIconClicked(
                    detailInfo.bottomSheet.title,
                    detailInfo.bottomSheet.description
                )
            }
        }
    }

}
