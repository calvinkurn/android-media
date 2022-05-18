package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodIconBinding
import com.tokopedia.tokofood.databinding.ItemTokofoodIconsBinding
import com.tokopedia.tokofood.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
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
            adapter.submitList(element)
        }
        iconRecyclerView?.adapter = adapter
        iconRecyclerView?.layoutManager = GridLayoutManager(itemView.context, GRID_ITEM, GridLayoutManager.VERTICAL, false)
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

        fun submitList(list: TokoFoodHomeIconsUiModel) {
            list.listIcons?.let {
                iconList.clear()
                iconList.addAll(list.listIcons)
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
                homeIconsListener?.onClickHomeIcon(item.applinks)
            }
        }
    }

    interface TokoFoodHomeIconsListener {
        fun onClickHomeIcon(applink: String)
    }
}