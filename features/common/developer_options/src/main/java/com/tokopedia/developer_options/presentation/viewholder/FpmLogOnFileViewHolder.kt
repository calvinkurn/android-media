package com.tokopedia.developer_options.presentation.viewholder

import android.Manifest
import android.app.Activity
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.FpmLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.FpmLogOnFileUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper.PermissionCheckListener

class FpmLogOnFileViewHolder(
    itemView: View
): AbstractViewHolder<FpmLogOnFileUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_fpm_log_on_file
    }

    private var cb: CheckboxUnify? = null

    override fun bind(element: FpmLogOnFileUiModel) {
        cb = itemView.findViewById(R.id.fpm_log_on_file_cb)
        cb?.isChecked = FpmLogger.getInstance()?.isAutoLogFileEnabled ?: false
        cb?.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            if (state) {
                requestPermissionWriteFile()
            } else {
                FpmLogger.getInstance()?.enableAutoLogFile(false)
            }
        }
    }

    private fun requestPermissionWriteFile() {
        val permissionCheckerHelper = PermissionCheckerHelper()
        permissionCheckerHelper.checkPermission(
            itemView.context as Activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            object : PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    cb?.isChecked = false
                }
                override fun onNeverAskAgain(permissionText: String) {}
                override fun onPermissionGranted() {
                    FpmLogger.getInstance()?.enableAutoLogFile(true)
                }
            },
            "Please give storage access permission to write log file"
        )
    }
}