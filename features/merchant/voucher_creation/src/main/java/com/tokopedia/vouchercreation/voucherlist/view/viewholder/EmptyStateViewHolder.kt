package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.EmptyStateUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list_empty_state.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class EmptyStateViewHolder(itemView: View?,
                           private val onImpressionListener: (dataKey: String) -> Unit) : AbstractViewHolder<EmptyStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_empty_state
    }

    override fun bind(element: EmptyStateUiModel) {
        with(itemView) {
            addOnImpressionListener(element.impressHolder) {
                onImpressionListener(EmptyStateUiModel.DATA_KEY)
            }
            val title = if (element.isActiveVoucher) {
                context.getString(R.string.mvc_no_active_voucher)
            } else {
                context.getString(R.string.mvc_no_voucher_history_yet)
            }
            tvMvcEmptyStateTitle.text = title
            tvMvcEmptyStateViewHistory.isVisible = element.isActiveVoucher

            btnMvcEmptyStateAction?.setOnClickListener {
                element.onCreateVoucherClicked()
            }
            tvMvcEmptyStateViewHistory?.setOnClickListener {
                element.onSeeHistoryClicked()
            }
        }
    }
}