package com.tokopedia.shopadmin.invitationaccepted.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shopadmin.databinding.ItemTncAdminListBinding
import java.lang.StringBuilder

class ItemTncAdminAdapter(private val tncList: List<String>):
    RecyclerView.Adapter<ItemTncAdminAdapter.ItemTncAdminViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTncAdminViewHolder {
        val binding = ItemTncAdminListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTncAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTncAdminViewHolder, position: Int) {
        if (tncList.isNotEmpty()) {
            holder.bind(tncList[position])
        }
    }

    override fun getItemCount(): Int = tncList.size

    inner class ItemTncAdminViewHolder(private val binding: ItemTncAdminListBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: String) {
                with(binding) {
                    tvTncNo.text = StringBuilder("${adapterPosition+1}").toString()
                    tvTncName.text = item
                }
            }
        }
}