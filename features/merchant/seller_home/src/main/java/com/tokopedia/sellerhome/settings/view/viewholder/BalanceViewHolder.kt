package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.BalanceUiModel

class BalanceViewHolder(itemView: View): AbstractViewHolder<BalanceUiModel>(itemView){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_balance
    }

    override fun bind(element: BalanceUiModel?) {

    }
}