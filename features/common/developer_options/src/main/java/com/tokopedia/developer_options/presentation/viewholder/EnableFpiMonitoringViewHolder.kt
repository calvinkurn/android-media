package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.developer_options.presentation.model.FpiMonitoringUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class EnableFpiMonitoringViewHolder(
    itemView: View
) : AbstractViewHolder<FpiMonitoringUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_fpi_monitoring
    }

    override fun bind(element: FpiMonitoringUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.enable_fpi_monitoring)
        val context = cb.context
        cb.isChecked = context.isFpiMonitoringEnable()
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            context.setFpiMonitoringState(state)
        }
    }

    private fun Context.setFpiMonitoringState(state: Boolean) {
        val sharedPref = getSharedPreferences(
            DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP,
            MODE_PRIVATE
        )
        val editor = sharedPref.edit().putBoolean(
            DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP,
            state
        )
        editor.apply()
    }

    private fun Context.isFpiMonitoringEnable(): Boolean = getSharedPreferences(
        DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP,
        MODE_PRIVATE
    ).getBoolean(DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP, false)

}
