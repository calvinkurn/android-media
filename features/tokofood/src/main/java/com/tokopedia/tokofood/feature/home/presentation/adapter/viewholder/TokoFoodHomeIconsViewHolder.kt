package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodIconBinding
import com.tokopedia.tokofood.databinding.ItemTokofoodIconsBinding
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeIconsViewHolder(
    itemView: View,
    private val homeIconsListener: TokoFoodHomeIconsListener? = null,
): AbstractViewHolder<TokoFoodHomeIconsUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_icons

        private const val GRID_ITEM = 4
        private const val MAX_ICON_ITEM = 8
    }

    private val binding: ItemTokofoodIconsBinding? by viewBinding()
    private val adapter = TokoFoodIconAdapter()
    private var iconRecyclerView: RecyclerView? = null

    override fun bind(element: TokoFoodHomeIconsUiModel) {
        setupTokoFoodIcon(element)
    }

    private fun setupTokoFoodIcon(element: TokoFoodHomeIconsUiModel){
        val icons = element.listIcons
        iconRecyclerView = binding?.tokofoodIconRecyclerView
        if (!icons.isNullOrEmpty()){
            adapter.submitList(element.listIcons)
        }
        iconRecyclerView?.adapter = adapter
        iconRecyclerView?.layoutManager = GridLayoutManager(itemView.context, GRID_ITEM, GridLayoutManager.VERTICAL, false)
        setItemViewImpression(element)
    }

    private fun setItemViewImpression(element: TokoFoodHomeIconsUiModel) {
        itemView.addOnImpressionListener(element) {
            element.listIcons?.let {
                homeIconsListener?.onImpressHomeIcon(it.take(MAX_ICON_ITEM), verticalPosition = 0)
            }
        }
    }

    internal inner class TokoFoodIconAdapter: RecyclerView.Adapter<TokoFoodIconViewHolder>() {
        private val iconList = mutableListOf<DynamicIcon>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoFoodIconViewHolder {
            val bindingIcon = ItemTokofoodIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return TokoFoodIconViewHolder(bindingIcon)
        }

        override fun onBindViewHolder(holder: TokoFoodIconViewHolder, position: Int) {
            iconList.getOrNull(position)?.let {
                holder.bind(it)
            }
        }

        override fun getItemCount(): Int {
            return iconList.size
        }

        fun submitList(list: List<DynamicIcon>) {
            list?.let {
                iconList.clear()
                iconList.addAll(it.take(MAX_ICON_ITEM))
            }
        }
    }

    internal inner class TokoFoodIconViewHolder(private val bindingIcon: ItemTokofoodIconBinding):
        RecyclerView.ViewHolder(bindingIcon.root){
        var imgIcon : ImageUnify? = null
        var tgIcon : Typography? = null

        fun bind(item: DynamicIcon){
            imgIcon = bindingIcon.imgIconTokofoodHome
            tgIcon = bindingIcon.tgIconTokofoodHome

            imgIcon?.loadImage(item.imageUrl)
            tgIcon?.text = item.name

            bindingIcon.root.setOnClickListener {
                homeIconsListener?.onClickHomeIcon(item.applinks, listOf(item), adapterPosition, verticalPosition = 0)
            }
        }
    }

    interface TokoFoodHomeIconsListener {
        fun onClickHomeIcon(applink: String, data: List<DynamicIcon>, horizontalPosition: Int, verticalPosition: Int)
        fun onImpressHomeIcon(data: List<DynamicIcon>, verticalPosition: Int)
    }
}