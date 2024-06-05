package com.tokopedia.developer_options.presentation.viewholder

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.bannerenvironment.BannerEnvironment
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.BannerEnvironmentUiModel
import com.tokopedia.developer_options.presentation.model.PercentViewUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class PercentVisibleViewHolder(
    itemView: View
) : AbstractViewHolder<PercentViewUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_percent_visible_view
        private const val DEV_OPT_ON_PERCENT_VIEW_ENABLED = "DEV_OPT_ON_PERCENT_VIEW_ENABLED"
        private const val IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED = "IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED"
    }

    private fun isPercentViewEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, false)
    }

    private fun setPercentViewEnabled(context: Context, isDevOptOnNotifEnabled: Boolean) {
        context.getSharedPreferences(DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, isDevOptOnNotifEnabled)
            .apply()
    }

    private val context = itemView.context

    override fun bind(element: PercentViewUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.percent_view_visible)
        cb.isChecked = isPercentViewEnabled(context)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            DevOpsTracker.trackEntryEvent(DevopsFeature.ENABLE_PERCENT_VIEW)
            setPercentViewEnabled(context, state)
            (context as Activity).recreate()
        }
    }
}
