package com.tokopedia.mvcwidget.views.benefit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.mvcwidget.databinding.LayoutItemInfoBinding
import com.tokopedia.mvcwidget.utils.getUnifyColorFromHex

class AdditionalInfoAdapter : RecyclerView.Adapter<AdditionalInfoAdapter.InfoViewHolder>() {

    private var list: List<String> = listOf()
    private var color: String = ""

    fun submitList(list: List<String>, color: String) {
        this.list = list
        this.color = color
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val b = LayoutItemInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(b)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(list[position], color)
    }

    override fun getItemCount(): Int = list.size

    class InfoViewHolder(private val binding: LayoutItemInfoBinding) : ViewHolder(binding.root) {
        fun bind(s: String, color: String) {
            val context = binding.root.context
            val c = context.getUnifyColorFromHex(color)
            binding.text.text = s
            binding.text.setTextColor(c)
        }
    }
}
