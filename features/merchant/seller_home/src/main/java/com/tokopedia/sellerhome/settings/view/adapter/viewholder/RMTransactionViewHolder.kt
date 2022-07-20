package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.RmTransactionData
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.RMTransactionWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

class RMTransactionViewHolder(itemView: View?,
                              private val onRmTransactionClicked: () -> Unit,
                              private val onErrorClicked: () -> Unit) :
    AbstractViewHolder<RMTransactionWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_transaction
    }

    private val successGroup: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_shop_transaction_success)
    private val titleTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_transaction_title)
    private val descTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_transaction_desc)
    private val loadingLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.shimmer_sah_new_other_shop_transaction)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_transaction)

    override fun bind(element: RMTransactionWidgetUiModel) {
        when (val state = element.state) {
            is SettingResponseState.SettingSuccess -> setSuccessTransactionInfo(state.data)
            is SettingResponseState.SettingError -> setErrorTransactionInfo()
            else -> setLoadingTransactionInfo()
        }
    }

    private fun setSuccessTransactionInfo(rmTransaction: RmTransactionData) {
        val title = getTransactionTitle(rmTransaction)
        val desc = getTransactionDesc(rmTransaction)

        successGroup?.show()
        titleTextView?.text = title
        descTextView?.text = desc
        loadingLayout?.gone()
        errorLayout?.gone()

        itemView.setOnClickListener {
            onRmTransactionClicked()
        }
    }

    private fun getTransactionTitle(rmTransaction: RmTransactionData): String {
        return when {
            rmTransaction.totalTransaction > Constant.ShopStatus.MAX_TRANSACTION ->
                getString(R.string.sah_new_other_rm_since_has_passed)
            rmTransaction.dateCreated.isBlank() ->
                getString(R.string.sah_new_other_rm_since_june_fourteenth)
            rmTransaction.isBeforeOnDate ->
                getString(R.string.sah_new_other_rm_since_june_fourteenth)
            else -> getString(R.string.sah_new_other_rm_joined_since)
        }.orEmpty()
    }

    private fun getTransactionDesc(rmTransaction: RmTransactionData): String {
        val transactionCountString =
            if (rmTransaction.totalTransaction > Constant.ShopStatus.MAX_TRANSACTION) {
                Constant.ShopStatus.MAX_TRANSACTION.toString()
            } else {
                getString(
                    R.string.sah_new_other_rm_transaction_remaining,
                    rmTransaction.totalTransaction.toString()
                )
            }.orEmpty()
        return getString(
            R.string.sah_new_other_rm_total_transaction,
            transactionCountString
        ).orEmpty()
    }

    private fun setLoadingTransactionInfo() {
        successGroup?.gone()
        loadingLayout?.show()
        errorLayout?.gone()

        itemView.setOnClickListener(null)
    }

    private fun setErrorTransactionInfo() {
        successGroup?.gone()
        loadingLayout?.gone()
        errorLayout?.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }

        itemView.setOnClickListener(null)
    }

}