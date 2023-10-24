@file:SuppressLint("NotifyDataSetChanged")

package com.tokopedia.editor.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.tokopedia.editor.base.BaseViewHolder
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.databinding.PartialNavigationToolItemBinding
import com.tokopedia.editor.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.picker.common.types.ToolType

class NavigationToolAdapter constructor(
    private val listener: NavigationToolViewHolder.Listener? = null
) : Adapter<NavigationToolAdapter.NavigationToolViewHolder>() {

    private val mTools = mutableListOf<NavigationTool>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationToolViewHolder {
        return NavigationToolViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: NavigationToolViewHolder, position: Int) {
        holder.onBind(mTools[position])
    }

    override fun getItemCount() = mTools.size

    fun setData(tools: List<NavigationTool>) {
        mTools.clear()
        mTools.addAll(tools)
        notifyDataSetChanged()
    }

    fun updateRemoveAudioState(isRemoved: Boolean) {
        val removeAudioTool = mTools.firstOrNull { it.type == ToolType.AUDIO_MUTE }
        val index = mTools.indexOf(removeAudioTool)

        if (index != -1 && removeAudioTool != null) {
            mTools[index].isSelected = isRemoved
            notifyItemChanged(index)
        }
    }

    class NavigationToolViewHolder constructor(
        private val binding: PartialNavigationToolItemBinding,
        private val listener: Listener?
    ) : BaseViewHolder<NavigationTool>(binding.root) {

        private val context by lazy {
            itemView.context
        }

        override fun onBind(data: NavigationTool) {
            binding.imgIcon.setImage(data.iconId)
            binding.txtName.text = data.name

            if (data.type == ToolType.AUDIO_MUTE) {
                updateRemoveAudioState(data.isSelected)
            }

            binding.root.setOnClickListener {
                listener?.onToolClicked(data)
            }
        }

        private fun updateRemoveAudioState(isRemoved: Boolean) {
            if (isRemoved) {
                binding.imgIcon.setImage(IconUnify.VOLUME_UP)
                binding.txtName.text = context.getString(R.string.universal_editor_nav_tool_audio_unmute)
            } else {
                binding.imgIcon.setImage(IconUnify.VOLUME_MUTE)
                binding.txtName.text = context.getString(R.string.universal_editor_nav_tool_audio_mute)
            }
        }

        fun interface Listener {
            fun onToolClicked(tool: NavigationTool)
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener?): NavigationToolViewHolder {
                val view = PartialNavigationToolItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return NavigationToolViewHolder(view, listener)
            }
        }
    }
}
