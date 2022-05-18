package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcWarningPeriodBinding
import com.tokopedia.vouchercreation.shop.detail.model.WarningPeriodUiModel

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class WarningPeriodViewHolder(
        itemView: View?,
        private val onSelectPeriod: (dataKey: String) -> Unit
) : AbstractViewHolder<WarningPeriodUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_warning_period
    }

    private var binding: ItemMvcWarningPeriodBinding? by viewBinding()

    override fun bind(element: WarningPeriodUiModel) {
        binding?.apply {
            tickerMvcWarning.setHtmlDescription(tickerMvcWarning.context.getString(R.string.mvc_set_your_voucher_active_period))
            tickerMvcWarning.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    onSelectPeriod(element.dataKey)
                }

                override fun onDismiss() {
                }
            })
        }
    }
}