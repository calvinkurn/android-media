package com.tokopedia.tokopoints.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tokopoints.view.model.addpointsection.CategoriesItem
import kotlinx.android.synthetic.main.tp_points_item_section_category.view.*

class AddPointGridViewHolder(val view: View,val listenerItemClick: ListenerItemClick) : RecyclerView.ViewHolder(view) {

    fun bindGridItem(data: CategoriesItem){
        view.view_category_new.visibility = View.GONE
        data?.let {
            view.text_title.text = it.text
            ImageHandler.loadImageFitCenter(view.context, view.iv_bg, it.icon)
        }
        view.setOnClickListener { listenerItemClick.onClickItem(data?.appLink) }

    }

    interface ListenerItemClick {
        fun onClickItem(appLink: String?)
    }
}

