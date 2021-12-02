package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.SHOW_AND_COPY_APPLINK_TOGGLE_DEFAULT_VALUE
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.SHOW_AND_COPY_APPLINK_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.SHOW_AND_COPY_APPLINK_TOGGLE_NAME
import com.tokopedia.developer_options.presentation.model.ShowApplinkOnToastUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ShowApplinkOnToastViewHolder(
    itemView: View
): AbstractViewHolder<ShowApplinkOnToastUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_show_applink_on_toast
    }

    override fun bind(element: ShowApplinkOnToastUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.show_applink_on_toast_cb)
        itemView.context.applicationContext.apply {
            cb.isChecked = getShowAndCopyApplinkToggleValue(this)
            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                val editor = getSharedPreferences(SHOW_AND_COPY_APPLINK_TOGGLE_NAME, Context.MODE_PRIVATE).edit()
                    .putBoolean(SHOW_AND_COPY_APPLINK_TOGGLE_KEY, state)
                editor.apply()
            }
        }
    }

    private fun getShowAndCopyApplinkToggleValue(context: Context): Boolean {
        return context.getSharedPreferences(SHOW_AND_COPY_APPLINK_TOGGLE_NAME, Context.MODE_PRIVATE).getBoolean(SHOW_AND_COPY_APPLINK_TOGGLE_KEY, SHOW_AND_COPY_APPLINK_TOGGLE_DEFAULT_VALUE)
    }
}