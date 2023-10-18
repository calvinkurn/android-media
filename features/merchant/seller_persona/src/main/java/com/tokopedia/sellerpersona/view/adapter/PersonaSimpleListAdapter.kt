package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerpersona.databinding.ItemPersonaSimpleListBinding
import com.tokopedia.utils.resources.isDarkMode
import timber.log.Timber

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSimpleListAdapter : Adapter<PersonaSimpleListAdapter.ResultViewHolder>() {

    private var itemClickListener: (() -> Unit)? = null
    private val items: MutableList<String> = mutableListOf()
    var isSelectedMode: Boolean = false

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

    inner class ResultViewHolder(
        private val binding: ItemPersonaSimpleListBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                tvSpResultInfoItem.text = item

                val textColor = when {
                    root.context.isDarkMode() && isSelectedMode -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_96
                    }
                    isSelectedMode -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    }
                    else -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    }
                }

                tvSpResultInfoItem.setTextColor(root.context.getResColor(textColor))
                root.setOnClickListener {
                    itemClickListener?.invoke()
                }
            }
        }
    }
}