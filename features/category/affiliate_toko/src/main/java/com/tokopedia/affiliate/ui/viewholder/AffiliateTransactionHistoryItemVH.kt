package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.NEW_DATE_FORMAT
import com.tokopedia.affiliate.PRODUCT_TYPE
import com.tokopedia.affiliate.ui.activity.AffiliateSaldoWithdrawalDetailActivity
import com.tokopedia.affiliate.ui.activity.AffiliateTransactionDetailActivity
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateTransactionHistoryItemVH(itemView: View) :
    AbstractViewHolder<AffiliateTransactionHistoryItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_transaction_history_item_layout

        const val TRANSACTION_TYPE_DEPOSIT = "TRANSACTION_TYPE_DEPOSIT"
        const val TRANSACTION_TYPE_WITHDRAWAL = "TRANSACTION_TYPE_WITHDRAWAL"
        const val TRANSACTION_TYPE_ADMIN_DEPOSIT = "TRANSACTION_TYPE_ADMIN_DEPOSIT"
        const val TRANSACTION_TYPE_ADMIN_WITHDRAWAL = "TRANSACTION_TYPE_ADMIN_WITHDRAWAL"
        const val DANGER = "DANGER"
        const val SUCCESS = "SUCCESS"
        const val WARNING = "WARNING"
        const val GRAY = "GRAY"
    }

    override fun bind(element: AffiliateTransactionHistoryItemModel?) {
        setDetail(element)
        setTextColorTitle(element)
        setTransactionHistoryLabel(element)
    }

    private fun setDetail(element: AffiliateTransactionHistoryItemModel?) {
        if (element?.transaction?.hasDetail == true) {
            setHasDetailUI(element)
        } else {
            senNoDetailUI()
        }
    }

    private fun senNoDetailUI() {
        itemView.findViewById<IconUnify>(R.id.transaction_history_chevron).hide()
        itemView.findViewById<ConstraintLayout>(R.id.transaction_history_parent_view)
            .setOnClickListener(null)
    }

    private fun setHasDetailUI(element: AffiliateTransactionHistoryItemModel) {
        itemView.findViewById<IconUnify>(R.id.transaction_history_chevron).show()
        itemView.findViewById<ConstraintLayout>(R.id.transaction_history_parent_view)
            .setOnClickListener {
                when (element.transaction.transactionType) {
                    TRANSACTION_TYPE_DEPOSIT -> {
                        val label =
                            if (element.transaction.commissionType == PRODUCT_TYPE) AffiliateAnalytics.LabelKeys.DEPOSIT_ORDER
                            else AffiliateAnalytics.LabelKeys.DEPOSIT_TRAFFIC
                        sendTransactionClickEvent(element.transaction.transactionID, label)
                        itemView.context.startActivity(
                            AffiliateTransactionDetailActivity.createIntent(
                                itemView.context,
                                element.transaction.transactionID
                            )
                        )
                    }
                    TRANSACTION_TYPE_WITHDRAWAL -> {
                        sendTransactionClickEvent(
                            element.transaction.transactionID,
                            AffiliateAnalytics.LabelKeys.WITHDRAWAL
                        )
                        itemView.context.startActivity(
                            AffiliateSaldoWithdrawalDetailActivity.newInstance(
                                itemView.context,
                                element.transaction.transactionID
                            )
                        )
                    }
                }
            }
    }

    private fun setTextColorTitle(element: AffiliateTransactionHistoryItemModel?) {
        itemView.findViewById<Typography>(R.id.transaction_history_heading).text =
            element?.transaction?.title
        itemView.findViewById<Typography>(R.id.transaction_history_amount).apply {
            text = element?.transaction?.amountFormatted
            if (element?.transaction?.transactionType == TRANSACTION_TYPE_DEPOSIT || element?.transaction?.transactionType == TRANSACTION_TYPE_ADMIN_DEPOSIT)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            else if (element?.transaction?.transactionType == TRANSACTION_TYPE_WITHDRAWAL || element?.transaction?.transactionType == TRANSACTION_TYPE_ADMIN_WITHDRAWAL)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
        }
    }

    private fun setTransactionHistoryLabel(element: AffiliateTransactionHistoryItemModel?) {
        itemView.findViewById<Typography>(R.id.transaction_history_notes).text =
            element?.transaction?.notes
        itemView.findViewById<Typography>(R.id.transaction_history_date).text =
            element?.transaction?.createdAt?.let {
                DateUtils().formatDate(
                    newFormat = NEW_DATE_FORMAT,
                    dateString = it
                )
            }
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

    private fun sendTransactionClickEvent(transactionID: String, label: String) {
        AffiliateAnalytics.sendIcomeTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_TRANSACTION_CARD,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
            label,
            bindingAdapterPosition,
            transactionID,
            UserSession(itemView.context).userId
        )
    }
}
