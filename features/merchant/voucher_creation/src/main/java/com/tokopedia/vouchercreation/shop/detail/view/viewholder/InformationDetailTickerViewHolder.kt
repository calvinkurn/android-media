package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.InformationDetailTickerUiModel
import kotlinx.android.synthetic.main.item_mvc_detail_info_ticker.view.*

class InformationDetailTickerViewHolder(itemView: View?): AbstractViewHolder<InformationDetailTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_detail_info_ticker
    }

    override fun bind(element: InformationDetailTickerUiModel) {
        itemView.detailInfoTicker?.run {
            setTextDescription(
                    if (element.initiallyCanEdit) {
                        context?.getString(R.string.mvc_free_delivery_alert).toBlankOrString()
                    } else {
                        context?.getString(R.string.mvc_free_delivery_alert_duplicate).toBlankOrString()
                    }
            )
        }
    }
}