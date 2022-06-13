package com.tokopedia.developer_options.presentation.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.StringBuilder

class SystemNonSystemAppsViewHolder(
    itemView: View
): AbstractViewHolder<SystemNonSystemAppsUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_system_and_non_system_apps
    }

    override fun bind(element: SystemNonSystemAppsUiModel?) {
        val systemBtn = itemView.findViewById<UnifyButton>(R.id.system_apps)
        val nonSystemBtn = itemView.findViewById<UnifyButton>(R.id.non_system_apps)
        systemBtn.setOnClickListener {
            showToastAndCopySystemOrNonSystemApps(true)
        }
        nonSystemBtn.setOnClickListener {
            showToastAndCopySystemOrNonSystemApps(false)
        }
    }

    private fun showToastAndCopySystemOrNonSystemApps(isSystemApps: Boolean) {
        itemView.context.apply {
            val clipboard = itemView.context.getSystemService(BaseActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val apps: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val systemApps = StringBuilder()
            for (applicationInfo in apps) {
                val check = if (isSystemApps) {
                    applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM
                } else {
                    applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != ApplicationInfo.FLAG_SYSTEM
                }
                if (check) {
                    if (systemApps.isNotEmpty()) {
                        systemApps.append(",")
                    }
                    systemApps.append(applicationInfo.packageName)
                }
            }
            val text = systemApps.toString()
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}