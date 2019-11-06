package com.tokopedia.tokopoints.view.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tokopoints.view.model.addpointsection.CategoriesItem
import kotlinx.android.synthetic.main.tp_item_section_category.view.*

class AddPointAdapterVH(val view: View, val listenerItemClick: ListenerItemClick, val context: Context) : RecyclerView.ViewHolder(view) {
    fun bind(data: CategoriesItem?) {
        view.view_category_new.visibility = View.GONE
        data?.let {
            view.text_title.text = it.text
            ImageHandler.loadImageFitCenter(context, view.iv_bg, it.icon)
        }
        view.setOnClickListener { listenerItemClick.onClickItem(data?.appLink) }

    }

    interface ListenerItemClick {
        fun onClickItem(appLink: String?)
    }
}
