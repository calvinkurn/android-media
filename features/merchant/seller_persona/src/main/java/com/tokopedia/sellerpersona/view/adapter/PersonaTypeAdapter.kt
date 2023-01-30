package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerpersona.R
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

        notifyItemRangeChanged(Int.ZERO, items.size.minus(Int.ONE))
    }

    inner class TypeViewHolder(private val binding: ItemPersonaTypeBinding) :
        ViewHolder(binding.root) {

        fun bind(item: PersonaUiModel) {
            with(binding) {
                tvSpPersonaType.text = item.headerTitle
                tvSpSellerTypeStatus.text = item.headerSubTitle
                radioSpPersonaType.isChecked = item.isSelected

                showList(item.itemList)
                showBackground(item)
            }
        }

        private fun showBackground(item: PersonaUiModel) {
            val drawableRes = if (item.isSelected) {
                R.drawable.sp_bg_seller_type_active
            } else {
                R.drawable.sp_bg_seller_type_inactive
            }
            with(binding.containerSpItemPersonaType) {
                setBackgroundResource(drawableRes)
                val dp16 = context.resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl2
                )
                if (absoluteAdapterPosition == items.size.minus(Int.ONE)) {
                    setMargin(dp16, top, dp16, bottom)
                } else {
                    setMargin(dp16, top, Int.ZERO, bottom)
                }
            }
        }

        private fun showList(itemList: List<String>) {
            with(binding.rvSpSelectTypeInfo) {
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean = false

                    override fun canScrollVertically(): Boolean = false
                }
                adapter = PersonaSimpleListAdapter(itemList)
            }
        }
    }
}