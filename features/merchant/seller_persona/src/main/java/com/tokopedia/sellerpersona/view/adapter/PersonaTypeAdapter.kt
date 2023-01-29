package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.sellerpersona.databinding.ItemPersonaTypeBinding
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaTypeAdapter @Inject constructor() : Adapter<PersonaTypeAdapter.TypeViewHolder>() {

    private val items: MutableList<PersonaUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonaTypeBinding.inflate(inflater, parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<PersonaUiModel>) {
        this.items.clear()
        this.items.addAll(items)
    }

    class TypeViewHolder(private val binding: ItemPersonaTypeBinding) : ViewHolder(binding.root) {

        fun bind(item: PersonaUiModel) {
            with(binding) {
                tvSpPersonaType.text = item.headerTitle
                tvSpSellerTypeStatus.text = item.headerSubTitle

                showList(item.itemList)
            }
        }

        private fun showList(itemList: List<String>) {
            with(binding.rvSpSelectTypeInfo) {
                layoutManager = LinearLayoutManager(context)
                adapter = PersonaSimpleListAdapter(itemList)
            }
        }
    }
}