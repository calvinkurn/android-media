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
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodIcon
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TokoFoodHomeIconsViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodHomeIconsUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_icons

        private const val GRID_ITEM = 4
    }

    private val adapter = TokoFoodIconAdapter()
    private var iconRecyclerView: RecyclerView? = null

    override fun bind(element: TokoFoodHomeIconsUiModel) {
        setupTokoFoodIcon(element)
    }

    private fun setupTokoFoodIcon(element: TokoFoodHomeIconsUiModel){
        val icons = element.listIcons
        iconRecyclerView = itemView.findViewById(R.id.tokofood_icon_recycler_view)
        if (icons.isNotEmpty()){
            adapter.submitList(element)
        }
        iconRecyclerView?.adapter = adapter
        iconRecyclerView?.layoutManager = GridLayoutManager(itemView.context, GRID_ITEM, GridLayoutManager.VERTICAL, false)
    }

    internal inner class TokoFoodIconAdapter: RecyclerView.Adapter<TokoFoodIconViewHolder>() {
        private val iconList = mutableListOf<TokoFoodIcon>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoFoodIconViewHolder {
            return TokoFoodIconViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_tokofood_icon, parent, false))
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
            iconList.clear()
            iconList.addAll(list.listIcons)
        }
    }

    internal inner class TokoFoodIconViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgIcon : ImageUnify? = null
        var tgIcon : Typography? = null

        fun bind(item: TokoFoodIcon){
            imgIcon = itemView.findViewById(R.id.img_icon_tokofood_home)
            tgIcon = itemView.findViewById(R.id.tg_icon_tokofood_home)

            imgIcon?.loadImage(item.imageUrl)
            tgIcon?.text = item.textIcon

            itemView?.setOnClickListener {

            }
        }
    }
}