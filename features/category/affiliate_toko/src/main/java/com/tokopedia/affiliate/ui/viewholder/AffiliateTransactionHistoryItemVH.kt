package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ui.activity.AffiliateSaldoWithdrawalDetailActivity
import com.tokopedia.affiliate.ui.activity.AffiliateTransactionDetailActivity
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateTransactionHistoryItemVH(itemView: View)
    : AbstractViewHolder<AffiliateTransactionHistoryItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_transaction_history_item_layout

        const val TRANSACTION_TYPE_DEPOSIT = "TRANSACTION_TYPE_DEPOSIT"
        const val TRANSACTION_TYPE_WITHDRAWAL = "TRANSACTION_TYPE_WITHDRAWAL"
        const val DANGER = "DANGER"
        const val SUCCESS = "SUCCESS"
        const val WARNING = "WARNING"
        const val GRAY = "GRAY"
    }

    override fun bind(element: AffiliateTransactionHistoryItemModel?) {

        if(element?.transaction?.hasDetail == true){
            itemView.findViewById<IconUnify>(R.id.transaction_history_chevron).show()
            itemView.findViewById<ConstraintLayout>(R.id.transaction_history_parent_view).setOnClickListener {
                when(element?.transaction?.transactionType){
                    TRANSACTION_TYPE_DEPOSIT -> {
                        AffiliateAnalytics.sendEvent(
                                AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                                AffiliateAnalytics.CategoryKeys.PENDAPATAN_PAGE,
                                AffiliateAnalytics.ActionKeys.CLICK_TRANSACTION_CARD,
                                "${element.transaction.transactionID} ${AffiliateAnalytics.LabelKeys.INCOMING}",
                                UserSession(itemView.context).userId)
                        itemView.context.startActivity(AffiliateTransactionDetailActivity.createIntent(itemView.context, element?.transaction?.transactionID))
                    }
                    TRANSACTION_TYPE_WITHDRAWAL -> {
                        AffiliateAnalytics.sendEvent(
                                AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                                AffiliateAnalytics.CategoryKeys.PENDAPATAN_PAGE,
                                AffiliateAnalytics.ActionKeys.CLICK_TRANSACTION_CARD,
                                "${element.transaction.transactionID} ${AffiliateAnalytics.LabelKeys.OUTGOING}",
                                UserSession(itemView.context).userId)
                        itemView.context.startActivity(AffiliateSaldoWithdrawalDetailActivity.newInstance(itemView.context, element.transaction.transactionID))
                    }
                }
            }
        } else{
            itemView.findViewById<IconUnify>(R.id.transaction_history_chevron).hide()
            itemView.findViewById<ConstraintLayout>(R.id.transaction_history_parent_view).setOnClickListener(null)
        }
        itemView.findViewById<Typography>(R.id.transaction_history_heading).text = element?.transaction?.title
        itemView.findViewById<Typography>(R.id.transaction_history_amount).apply {
            text = element?.transaction?.amountFormatted
            if(element?.transaction?.transactionType == TRANSACTION_TYPE_DEPOSIT)
                setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            else
                setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_RN500))
        }
        itemView.findViewById<Typography>(R.id.transaction_history_notes).text = element?.transaction?.notes
        itemView.findViewById<Typography>(R.id.transaction_history_date).text = element?.transaction?.createdAtFormatted
        itemView.findViewById<Label>(R.id.transaction_history_label).apply {
            element?.transaction?.label?.let {
                if (it.labelText.isEmpty() || it.labelType.isEmpty()) {
                    hide()
                } else {
                    show()
                    text = it.labelText
                    when (it.labelType) {
                        DANGER -> setLabelType(Label.HIGHLIGHT_LIGHT_RED)
                        SUCCESS -> setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                        WARNING -> setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
                        GRAY -> setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
                    }
                }
            }
        }
    }
}