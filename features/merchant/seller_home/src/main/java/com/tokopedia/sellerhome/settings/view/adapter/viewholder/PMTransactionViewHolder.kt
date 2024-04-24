package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.PMTransactionDataUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.PMTransactionWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PMTransactionViewHolder(
    itemView: View?,
    private val onTransactionClicked: (Long) -> Unit,
    private val onErrorClicked: () -> Unit
) : AbstractViewHolder<PMTransactionWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_transaction
        private const val TRANSACTION_FORMAT = "%s/" + Constant.ShopStatus.MAX_TRANSACTION
        private const val REACH_MAX_FREE_TRANSACTION = ">" + Constant.ShopStatus.MAX_TRANSACTION
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

    override fun bind(element: PMTransactionWidgetUiModel) {
        when (val state = element.state) {
            is SettingResponseState.SettingSuccess -> setSuccessTransactionInfo(state.data)
            is SettingResponseState.SettingError -> setErrorTransactionInfo()
            else -> setLoadingTransactionInfo()
        }
    }

    private fun setSuccessTransactionInfo(rmTransaction: PMTransactionDataUiModel) {
        val desc = getTransactionDesc(rmTransaction)

        successGroup?.show()
        descTextView?.text = desc
        loadingLayout?.gone()
        errorLayout?.gone()

        showTooltipIcon()
        itemView.setOnClickListener {
            onTransactionClicked(rmTransaction.totalTransaction)
        }
    }

    private fun showTooltipIcon() {
        val iconColor = itemView.context.getResColor(
            unifyprinciplesR.color.Unify_NN900
        )
        val iconSize = itemView.context.dpToPx(14)
        titleTextView?.setUnifyDrawableEnd(
            iconId = IconUnify.INFORMATION,
            colorIcon = iconColor,
            width = iconSize,
            height = iconSize
        )
    }

    private fun getTransactionDesc(transaction: PMTransactionDataUiModel): String {
        val totalTransaction = transaction.totalTransaction
        return if (totalTransaction <= Constant.ShopStatus.MAX_TRANSACTION) {
            String.format(TRANSACTION_FORMAT, totalTransaction)
        } else {
            REACH_MAX_FREE_TRANSACTION
        }
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
