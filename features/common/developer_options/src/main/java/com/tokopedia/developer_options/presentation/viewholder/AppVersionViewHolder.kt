package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.AppVersionUiModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.Exception

class AppVersionViewHolder(
    itemView: View
): AbstractViewHolder<AppVersionUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_app_version
    }

    override fun bind(element: AppVersionUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.change_version_btn)
        val appVersionTf = itemView.findViewById<TextFieldUnify>(R.id.app_version_tf)
        val specificVersionTf = itemView.findViewById<TextFieldUnify>(R.id.specific_version_tf)

        appVersionTf.textFieldInput.setText(GlobalConfig.VERSION_NAME)
        specificVersionTf.textFieldInput.setText(GlobalConfig.VERSION_CODE.toString())

        btn.setOnClickListener {
            val versionCode: String = specificVersionTf.textFieldInput.text.toString()
            val versionName: String = appVersionTf.textFieldInput.text.toString()
            itemView.context.apply {
                when {
                    versionCode.isBlank() -> {
                        Toast.makeText(this, "Version Code should not be empty", Toast.LENGTH_SHORT).show()
                    }
                    versionName.isBlank() -> {
                        Toast.makeText(this, "Version Name should not be empty", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            GlobalConfig.VERSION_NAME = versionName
                            GlobalConfig.VERSION_CODE = versionCode.toInt()
                            Toast.makeText(this, "Version has been changed: $versionName - $versionCode", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}