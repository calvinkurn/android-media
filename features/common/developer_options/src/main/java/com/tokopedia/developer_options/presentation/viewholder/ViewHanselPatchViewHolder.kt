package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.hansel.HanselActivity
import com.tokopedia.developer_options.presentation.model.ViewHanselPatchUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import android.content.ClipData
import android.content.ClipboardManager
import com.tokopedia.unifycomponents.Toaster

class ViewHanselPatchViewHolder(itemView: View) : AbstractViewHolder<ViewHanselPatchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_hansel_patch

        private const val KEY_IS_TEST_DEVICE = "is_in_testgroup"
        private const val PREFERENCE_NAME_HANSEL = "PREFERENCE_NAME_HANSEL"
        private const val KEY_STATIC_CRITERIA_VERSION = "key_static_criteria_version"
        private const val KEY_DEVICE_ID = "device_id"
        private const val LABEL_COPY = "Copy Text Device ID"
    }

    override fun bind(element: ViewHanselPatchUiModel?) {
        val tvTestDevice = itemView.findViewById<Typography>(R.id.tv_is_test_device)
        val button = itemView.findViewById<UnifyButton>(R.id.btn_hansel_patch)
        val tvDeviceId = itemView.findViewById<Typography>(R.id.tv_device_id)
        val iconCopy = itemView.findViewById<IconUnify>(R.id.ic_copy)

        val prefs = itemView.context.getSharedPreferences(PREFERENCE_NAME_HANSEL, Context.MODE_PRIVATE)
        val isTestDevice = prefs.getBoolean(KEY_IS_TEST_DEVICE, false)
        val deviceId = getDeviceId()

        tvDeviceId.text = itemView.context.getString(R.string.label_is_device_test, deviceId)
        tvTestDevice.text = itemView.context.getString(R.string.label_is_device_test, isTestDevice.toString())
        button.text = itemView.context.resources.getString(com.tokopedia.developer_options.R.string.title_button_hansel_patch_list)
        button.setOnClickListener {
            val intent = Intent(itemView.context, HanselActivity::class.java)
            itemView.context.startActivity(intent)
        }

        iconCopy.setOnClickListener {
            val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(LABEL_COPY, deviceId)
            clipboard.setPrimaryClip(clip)
            Toaster.build(itemView, "Device ID Copied: $deviceId", Toaster.LENGTH_LONG).show()
        }
    }

    private fun getDeviceId(): String {
        val prefs = itemView.context.getSharedPreferences(PREFERENCE_NAME_HANSEL, Context.MODE_PRIVATE)
        val value = prefs.getString(KEY_STATIC_CRITERIA_VERSION, "") ?: ""
        if (value.isNotEmpty()) {
            val jsonValue = Gson().fromJson<Map<String, Any>>(value, object: TypeToken<Map<String, Any>>() {}.type)
            val deviceId = jsonValue[KEY_DEVICE_ID] ?: ""
            return deviceId.toString()
        }
        return ""
    }
}
