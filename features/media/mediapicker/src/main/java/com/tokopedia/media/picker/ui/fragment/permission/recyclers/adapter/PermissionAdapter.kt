package com.tokopedia.media.picker.ui.fragment.permission.recyclers.adapter

import android.Manifest.permission.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ViewItemRuntimePermissionBinding
import com.tokopedia.media.picker.ui.uimodel.PermissionUiModel
import com.tokopedia.utils.view.binding.viewBinding

class PermissionAdapter constructor(
    private val items: List<PermissionUiModel> = mutableListOf()
) : RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateState(permissionName: String, status: Boolean) {
        val permission = items.first { it.name == permissionName }
        val index = items.indexOf(permission)

        if (index != -1) {
            items[index].isGranted = status
            notifyItemChanged(index)
        }
    }

    class PermissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemRuntimePermissionBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(element: PermissionUiModel) {
            binding?.txtTitle?.text = context.getString(element.title)

            binding?.btnGrant?.setOnClickListener {
                openAppSettings()
            }

            setPermissionState(element.isGranted)
            setIcon(element.name)
        }

        private fun openAppSettings() {
            context.startActivity(
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse("package:" + context.packageName)
                    addCategory(Intent.CATEGORY_DEFAULT)
                }
            )
        }

        private fun setPermissionState(isGranted: Boolean) {
            binding?.imgAllowedIcon?.showWithCondition(isGranted)
            binding?.btnGrant?.showWithCondition(!isGranted)
        }

        private fun setIcon(permission: String) {
            when (permission) {
                READ_EXTERNAL_STORAGE -> binding?.imgAction?.setImage(IconUnify.IMAGE)
                RECORD_AUDIO -> binding?.imgAction?.setImage(IconUnify.MICROPHONE)
                CAMERA -> binding?.imgAction?.setImage(IconUnify.CAMERA)
            }
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_item_runtime_permission

            fun create(parent: ViewGroup): PermissionViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return PermissionViewHolder(view)
            }
        }
    }

}