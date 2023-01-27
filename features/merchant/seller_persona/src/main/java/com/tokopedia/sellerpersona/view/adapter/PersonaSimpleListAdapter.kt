package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.sellerpersona.databinding.ItemPersonaSimpleListBinding

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSimpleListAdapter(
    private val items: List<String>
) : Adapter<PersonaSimpleListAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonaSimpleListBinding.inflate(layoutInflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ResultViewHolder(
        private val binding: ItemPersonaSimpleListBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                tvSpResultInfoItem.text = item
            }
        }
    }
}