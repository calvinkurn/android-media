package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoPlusWidgetUiModel

class TokoPlusViewHolder(
    itemView: View?,
    private val onClicked: () -> Unit,
    private val onImpressed: () -> Unit
) : AbstractViewHolder<TokoPlusWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_toko_plus
    }

    override fun bind(element: TokoPlusWidgetUiModel) {

    }
}