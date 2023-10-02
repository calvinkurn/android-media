package com.tokopedia.developer_options.presentation.viewholder

import android.app.Activity
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.bannerenvironment.BannerEnvironment
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.BannerEnvironmentUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class BannerEnvironmentViewHolder(
    itemView: View
) : AbstractViewHolder<BannerEnvironmentUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_banner_environment
    }

    private val context = itemView.context

    override fun bind(element: BannerEnvironmentUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.banner_environment)
        cb.isChecked = BannerEnvironment.isBannerEnvironmentEnabled(context)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            BannerEnvironment.setBannerEnvironmentEnabled(context, state)
            (context as Activity).recreate()
        }
    }
}
