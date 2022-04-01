package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.TranslatorUiModel

class TranslatorSettingViewHolder(
    itemView: View
): AbstractViewHolder<TranslatorUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_translator_setting
    }

    override fun bind(element: TranslatorUiModel) { /* no need to implement */ }
}