package com.tokopedia.developer_options.presentation.viewholder

import android.content.res.Configuration
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ForceDarkModeUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ForceDarkModeViewHolder(
    itemView: View
): AbstractViewHolder<ForceDarkModeUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_force_dark_mode
    }

    override fun bind(element: ForceDarkModeUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.force_dark_mode_cb)
        cb.isChecked = itemView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (state) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}