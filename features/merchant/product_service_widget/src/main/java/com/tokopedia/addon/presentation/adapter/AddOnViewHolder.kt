package com.tokopedia.addon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemAddonBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class AddOnViewHolder(
    itemView: View,
    private val onClickListener: (index: Int, indexChild: Int, addOnUIModels: List<AddOnUIModel>) -> Unit,
    private val onHelpClickListener: (index: Int, indexChild: Int, AddOnUIModel) -> Unit,
    private val onItemImpressionListener: (index: Int, indexChild: Int, AddOnUIModel) -> Unit
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_addon, parent, false)
    }

    private val binding: ItemAddonBinding? by viewBinding()

    fun bind(item: AddOnGroupUIModel, isShowDescription: Boolean) {
        binding?.apply {
            tfTitle.text = item.title
            iuServiceType.loadImage(
                if (root.context.isDarkMode()) {
                    item.iconDarkmodeUrl
                } else {
                    item.iconUrl
                }
            )
            rvAddonChild.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvAddonChild.adapter = AddOnChildAdapter(
                ::onChildClickListener,
                ::onChildHelpClickListener,
                ::onChildItemImpressionListener
            ).apply {
                showDescription(isShowDescription)
                setItems(item.addon)
            }
            tfHint.isVisible = item.addonCount > Int.ONE
            topSpacer.isVisible = bindingAdapterPosition.isMoreThanZero()
        }
    }

    private fun onChildHelpClickListener(indexChild: Int, addOnUIModel: AddOnUIModel) {
        onHelpClickListener(bindingAdapterPosition, indexChild, addOnUIModel)
    }

    private fun onChildItemImpressionListener(indexChild: Int, addOnUIModel: AddOnUIModel) {
        onItemImpressionListener(bindingAdapterPosition, indexChild, addOnUIModel)
    }

    private fun onChildClickListener(indexChild: Int, addOnUIModels: List<AddOnUIModel>) {
        onClickListener(bindingAdapterPosition, indexChild, addOnUIModels)
    }

}
