package com.tokopedia.tokopoints.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.view.model.addpointsection.SectionsItem
import kotlinx.android.synthetic.main.tp_item_category_title.view.*

class AddPointTitleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindTile(sectionsItem: SectionsItem){
        view.tvCategoryTitle.text=sectionsItem.title
    }
}
