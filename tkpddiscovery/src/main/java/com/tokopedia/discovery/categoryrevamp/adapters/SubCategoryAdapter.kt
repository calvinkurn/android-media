package com.tokopedia.discovery.categoryrevamp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SubCategoryListener
import kotlinx.android.synthetic.main.item_sub_category.view.*

class SubCategoryAdapter(private val subCategoryList: MutableList<SubCategoryItem>,
                         private val subCategoryListener: SubCategoryListener) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_category, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = subCategoryList[position]

        if (item.is_default) {
            holder.img_sub_category.setImageResource(R.drawable.ic_default_category_icon)
        } else {
            ImageHandler.loadImage(holder.itemView.context,
                    holder.img_sub_category,
                    item.thumbnailImage,
                    R.drawable.loading_page)
        }
        holder.txt_sub_category.text = item.name

        holder.parent_view.setOnClickListener {
            if (item.is_default) {
                subCategoryListener.OnDefaultItemClicked()
            } else {
                subCategoryListener.OnSubCategoryClicked(item.id.toString(), item.name ?: "")
            }
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parent_view = view.cardView_sub_category
        val img_sub_category = view.img_sub_category
        val txt_sub_category = view.txt_sub_category
    }


}