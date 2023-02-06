package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.ItemPersonaTypeBinding
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaTypeAdapter(
    private val listener: Listener
) : Adapter<PersonaTypeAdapter.TypeViewHolder>() {

    companion object {
        private const val SUB_TITLE_FORMAT = "(%s)"
    }

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

    fun getItems(): List<PersonaUiModel> {
        return items
    }

    fun setItems(items: List<PersonaUiModel>) {
        this.items.clear()
        this.items.addAll(items)

        notifyItemRangeChanged(Int.ZERO, items.size.minus(Int.ONE))
    }

    inner class TypeViewHolder(
        private val binding: ItemPersonaTypeBinding
    ) : ViewHolder(binding.root) {

        private val optionAdapter by lazy { PersonaSimpleListAdapter() }

        fun bind(item: PersonaUiModel) {
            with(binding) {
                tvSpPersonaType.text = item.headerTitle
                tvSpSellerTypeStatus.text = String.format(SUB_TITLE_FORMAT, item.headerSubTitle)
                radioSpPersonaType.isChecked = item.isSelected
                imgSpSellerTypeAvatar.loadImage(item.avatarImage)

                showList(item)
                showBackground(item)
                handleClickState(item)
            }
        }

        private fun handleClickState(item: PersonaUiModel) {
            with(binding) {
                radioSpPersonaType.setOnCheckedChangeListener { _, isChecked ->
                    item.isSelected = isChecked
                    showBackground(item)
                    listener.onItemClickListener(item)
                }
                containerSpItemPersonaType.setOnClickListener {
                    onItemClicked()
                }
            }
        }

        private fun onItemClicked() {
            with(binding) {
                if (!radioSpPersonaType.isChecked) {
                    radioSpPersonaType.isChecked = !radioSpPersonaType.isChecked
                }
            }
        }

        private fun showBackground(item: PersonaUiModel) {
            val drawableRes = if (item.isSelected) {
                R.drawable.sp_bg_seller_type_active
            } else {
                R.drawable.sp_bg_seller_type_inactive
            }

            with(binding) {
                val sectionTextColor = if (item.isSelected) {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                } else {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                }
                tvSpSellerTypeLblInfo.setTextColor(sectionTextColor)

                val subTitleTextColor = if (item.isSelected) {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                } else {
                    root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                }
                tvSpSellerTypeStatus.setTextColor(subTitleTextColor)

                containerSpItemPersonaType.setBackgroundResource(drawableRes)
                optionAdapter.isSelected = item.isSelected
                optionAdapter.notifyAdapter()
            }
        }

        private fun showList(item: PersonaUiModel) {
            with(binding.rvSpSelectTypeInfo) {
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean = false

                    override fun canScrollVertically(): Boolean = false
                }
                adapter = optionAdapter

                optionAdapter.setOnItemClickListener {
                    onItemClicked()
                }
                optionAdapter.setItems(item.itemList)
                optionAdapter.isSelected = item.isSelected
                optionAdapter.notifyAdapter()
            }
        }

    }

    interface Listener {
        fun onItemClickListener(item: PersonaUiModel)
    }
}