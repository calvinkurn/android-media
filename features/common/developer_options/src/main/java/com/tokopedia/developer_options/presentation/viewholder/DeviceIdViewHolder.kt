package com.tokopedia.developer_options.presentation.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.bytedance.applog.AppLog
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.DeviceIdUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.Toaster
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

        val didView = itemView.findViewById<Typography>(R.id.applog_id)
        val appLogDId = AppLog.getDid()
        didView.text = String.format("APPLOG DEVICE ID: $appLogDId")

        val didCopy = itemView.findViewById<IconUnify>(R.id.ic_copy)
        didCopy.setOnClickListener {
            val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Applog Device ID", appLogDId)
            clipboard.setPrimaryClip(clip)
            Toaster.build(itemView, "Device ID Copied: $appLogDId", Toaster.LENGTH_LONG).show()
        }
    }
}
