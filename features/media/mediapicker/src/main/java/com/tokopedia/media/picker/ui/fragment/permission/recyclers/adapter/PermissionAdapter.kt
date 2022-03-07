package com.tokopedia.media.picker.ui.fragment.permission.recyclers.adapter

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
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

    class PermissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemRuntimePermissionBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(element: PermissionUiModel) {
            binding?.txtTitle?.text = context.getString(element.title)
            setIcon(element.permission)
        }

        private fun setIcon(permission: String) {
            when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    binding?.imgAction?.setImage(IconUnify.IMAGE)
                }
                Manifest.permission.CAMERA -> {
                    binding?.imgAction?.setImage(IconUnify.CAMERA)
                }
                Manifest.permission.RECORD_AUDIO -> {
                    binding?.imgAction?.setImage(IconUnify.MICROPHONE)
                }
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