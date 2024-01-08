package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.TranslatorUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.translator.ui.TranslatorSettingView

class TranslatorSettingViewHolder(
    itemView: View
): AbstractViewHolder<TranslatorUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_translator_setting
    }

    override fun bind(element: TranslatorUiModel) {
        val translatorView = itemView.findViewById<TranslatorSettingView>(R.id.translator_view)
        translatorView.setOnSwithcListener {
            DevOpsTracker.trackEntryEvent(DevopsFeature.TRANSLATOR_TOGGLE)
        }

    }
}
