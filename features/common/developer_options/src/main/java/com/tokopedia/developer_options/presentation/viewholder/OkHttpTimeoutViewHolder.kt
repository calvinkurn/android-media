package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.presentation.model.OkHttpTimeoutUiModel
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.developer_options.R as developer_optionsR

class OkHttpTimeoutViewHolder(
    itemView: View
): AbstractViewHolder<OkHttpTimeoutUiModel>(itemView) {

    override fun bind(element: OkHttpTimeoutUiModel?) {
        val tfTimeout = itemView.findViewById<TextFieldUnify>(R.id.text_field_ok_http_timeout)
        val btnChangeTimeout = itemView.findViewById<UnifyButton>(R.id.button_update_ok_http_timeout)
        val context = itemView.context
        tfTimeout.textFieldInput.setText(context.getOkHttpTimeout().toString())
        btnChangeTimeout.setOnClickListener {
            val timeout = tfTimeout.textFieldInput.text.toString()
            if (timeout.isEmpty()) {
                Toaster.build(
                    itemView,
                    getString(developer_optionsR.string.change_ok_http_timeout_empty_toaster),
                    Toaster.LENGTH_SHORT
                ).show()
            } else {
                context.setOkHttpTimeout(timeout.toLong())
                Toaster.build(
                    itemView,
                    getString(developer_optionsR.string.change_ok_http_timeout_success_toaster),
                    Toaster.LENGTH_SHORT
                ).show()
            }
            DevOpsTracker.trackEntryEvent(DevopsFeature.OK_HTTP_TIMEOUT_HANDLER)
        }
    }

    private fun Context.getOkHttpTimeout(): Long =
        getSharedPreferences(
            DeveloperOptionActivity.PREF_KEY_OK_HTTP_TIMEOUT,
            MODE_PRIVATE
        ).getLong(DeveloperOptionActivity.PREF_KEY_OK_HTTP_TIMEOUT_VALUE, OK_HTTP_DEFAULT_TIMEOUT)

    private fun Context.setOkHttpTimeout(newTimeout: Long) {
        val sharedPref = getSharedPreferences(
            DeveloperOptionActivity.PREF_KEY_OK_HTTP_TIMEOUT,
            MODE_PRIVATE
        )
        val editor = sharedPref.edit().putLong(
            DeveloperOptionActivity.PREF_KEY_OK_HTTP_TIMEOUT_VALUE,
            newTimeout
        )
        editor.apply()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_ok_http_timeout

        const val OK_HTTP_DEFAULT_TIMEOUT = 10L
    }
}
