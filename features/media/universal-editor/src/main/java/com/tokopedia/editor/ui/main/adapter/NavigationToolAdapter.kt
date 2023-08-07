@file:SuppressLint("NotifyDataSetChanged")

package com.tokopedia.editor.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.tokopedia.editor.base.BaseViewHolder
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.databinding.PartialNavigationToolItemBinding

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

    class NavigationToolViewHolder constructor(
        private val binding: PartialNavigationToolItemBinding,
        private val listener: Listener?
    ) : BaseViewHolder<NavigationTool>(binding.root) {

        override fun onBind(data: NavigationTool) {
            binding.imgIcon.setImage(data.iconId)
            binding.txtName.text = data.name

            binding.root.setOnClickListener {
                listener?.onToolClicked(data)
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
