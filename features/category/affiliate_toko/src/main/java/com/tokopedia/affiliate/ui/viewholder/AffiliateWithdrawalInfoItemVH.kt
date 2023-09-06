package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.bodyTypoMap
import com.tokopedia.affiliate.headerTypoMap
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithrawalInfoAttributionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateWithdrawalInfoItemVH(itemView: View) :
    AbstractViewHolder<AffiliateWithrawalInfoAttributionModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_withdrawal_info_item
        private const val TYPE_BOLD = "bold"
        private const val TYPE_REGULER = "regular"
    }

    override fun bind(element: AffiliateWithrawalInfoAttributionModel?) {
        itemView.findViewById<Typography>(R.id.text_title)?.apply {
            text = element?.advanceTooltip?.tooltipText

            when (element?.advanceTooltip?.textType) {
                TYPE_BOLD -> {
                    element.advanceTooltip.textSize?.let {
                        headerTypoMap[it]?.let { type ->
                            setType(type)
                        }
                    }
                    setWeight(Typography.BOLD)
                }
                TYPE_REGULER -> {
                    element.advanceTooltip.textSize?.let {
                        bodyTypoMap[it]?.let { type ->
                            setType(type)
                        }
                    }
                    setWeight(Typography.REGULAR)
                    setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                        )
                    )
                }
            }
        }
    }
}
