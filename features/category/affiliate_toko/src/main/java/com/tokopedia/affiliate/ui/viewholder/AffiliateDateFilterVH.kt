package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AffiliateDateFilterVH(
    itemView: View,
    private val onDateRangeClickInterface: AffiliateDatePickerRangeChangeInterface?
) : AbstractViewHolder<AffiliateDateFilterModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_adp_range_picker_item
    }

    private val tvDate = itemView.findViewById<Typography>(R.id.text)
    private val filterMessage = itemView.findViewById<Typography>(R.id.filter_message)
    private val rootView = itemView.findViewById<ConstraintLayout>(R.id.date_range)
    private val infoIcon = itemView.findViewById<IconUnify>(R.id.icon_info)

    override fun bind(element: AffiliateDateFilterModel?) {
        tvDate.text = buildString {
            append(element?.data?.title)
            append(" (")
            append(element?.data?.message)
            append(")")
        }
        rootView.setOnClickListener {
            onDateRangeClickInterface?.onRangeSelectionButtonClicked()
        }
        filterMessage.text = element?.data?.dateUpdateDescription
        element?.isSSEConnected?.onEach { isConnected ->
            filterMessage?.post {
                filterMessage.isVisible = !isConnected
                infoIcon.isVisible = !isConnected
            }
        }?.launchIn(CoroutineScope(Dispatchers.IO))
    }
}
