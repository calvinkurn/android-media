package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerpersona.databinding.ItemPersonaSimpleListBinding
import timber.log.Timber

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSimpleListAdapter : Adapter<PersonaSimpleListAdapter.ResultViewHolder>() {

    private var itemClickListener: (() -> Unit)? = null
    private val items: MutableList<String> = mutableListOf()
    var isSelected: Boolean = false

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

    fun setItems(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun notifyAdapter() {
        try {
            notifyItemRangeChanged(Int.ZERO, itemCount)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun setOnItemClickListener(callback: () -> Unit) {
        this.itemClickListener = callback
    }

    inner class ResultViewHolder(
        private val binding: ItemPersonaSimpleListBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                tvSpResultInfoItem.text = item

                val textColor = if (isSelected) {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                } else {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                }
                tvSpResultInfoItem.setTextColor(textColor)
                root.setOnClickListener {
                    itemClickListener?.invoke()
                }
            }
        }
    }
}