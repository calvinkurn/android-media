package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.DeviceIdUiModel
import com.tokopedia.unifyprinciples.Typography

class DeviceIdViewHolder(
    itemView: View
): AbstractViewHolder<DeviceIdUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_device_id
    }

    override fun bind(element: DeviceIdUiModel) {
        val tp = itemView.findViewById<Typography>(R.id.device_id_tp)
        tp.text = String.format("DEVICE ID: ${GlobalConfig.DEVICE_ID}")
    }
}