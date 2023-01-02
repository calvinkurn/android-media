package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dilayanitokopedia.databinding.DtItemAnchorTabsBinding
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify

/**
 * Created by irpan on 8/11/22.
 */
class DtAnchorTabAdapter(private val listener: AnchorTabListener) :
    RecyclerView.Adapter<DtAnchorTabAdapter.DtAnchorTabViewHolder>() {

    var listMenu = mutableListOf<AnchorTabUiModel>()

    private var selectedMenu = 0

    private var maximizeIcons = true

    interface AnchorTabListener {
        fun onMenuSelected(anchorTabUiModel: AnchorTabUiModel, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DtAnchorTabViewHolder {
        val binding = DtItemAnchorTabsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DtAnchorTabViewHolder(binding, listener)
    }

    override fun getItemCount() = listMenu.size

    override fun onBindViewHolder(holder: DtAnchorTabViewHolder, position: Int) {
        holder.itemView.isClickable = true
        val isSelected = position == selectedMenu
        holder.bindData(listMenu[position], maximizeIcons, isSelected)
    }

    fun updateList(data: List<AnchorTabUiModel>) {
        listMenu.clear()
        listMenu.addAll(data)
        notifyDataSetChanged()
    }

    fun selectMenu(selectedMenu: AnchorTabUiModel) {
        this.selectedMenu = listMenu.indexOf(selectedMenu)
        notifyDataSetChanged()
    }

    fun setMaximizeIcons() {
        maximizeIcons = true
        notifyDataSetChanged()
    }

    fun setMinimizeIcons() {
        maximizeIcons = false
        notifyDataSetChanged()
    }

    inner class DtAnchorTabViewHolder(
        private val binding: DtItemAnchorTabsBinding,
        private val listener: AnchorTabListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: AnchorTabUiModel, isMaximize: Boolean = false, isSelected: Boolean = false) {
            binding.anchorIcon.loadImage(data.imageUrl)
            binding.anchorText.text = data.title

            setIconVisibility(isMaximize)

            cardSelection(isSelected)
            setListener(data)
        }

        private fun setIconVisibility(isMaximize: Boolean) {
            if (isMaximize) {
                binding.anchorIcon.visibility = View.VISIBLE
            } else {
                binding.anchorIcon.visibility = View.GONE
            }
        }

        private fun setListener(anchorTabUiModel: AnchorTabUiModel) {
            binding.root.setOnClickListener {
                listener.onMenuSelected(anchorTabUiModel, bindingAdapterPosition)
            }
        }

        private fun cardSelection(isSelected: Boolean) {
            if (isSelected) {
                binding.anchorCard.cardType = CardUnify.TYPE_BORDER_ACTIVE
            } else {
                binding.anchorCard.cardType = CardUnify.TYPE_SHADOW
            }
        }
    }
}
