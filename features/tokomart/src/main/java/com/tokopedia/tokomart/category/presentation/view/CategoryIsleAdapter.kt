package com.tokopedia.tokomart.category.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import kotlinx.android.synthetic.main.item_tokomart_category_isle_card.view.*

class CategoryIsleAdapter(
        private val categoryItem: List<CategoryIsleDataView>
): RecyclerView.Adapter<CategoryIsleAdapter.CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tokomart_category_isle_card, parent, false)
        return CategoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(categoryItem[position])
    }

    override fun getItemCount(): Int {
        return categoryItem.size
    }

    inner class CategoryItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: CategoryIsleDataView) {
            itemView.txt_category_name.text = item.name
//            ImageHandler.loadImage(itemView.context, itemView.img_category,
//                    ImageAssets.BRAND_NOT_FOUND, null)
        }
    }
}