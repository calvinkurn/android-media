package com.tokopedia.tokopoints.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.addpointsection.CategoriesItem
import com.tokopedia.tokopoints.view.model.addpointsection.SectionsItem
import kotlinx.android.synthetic.main.tp_item_category_title.view.*

class AddPointsAdapter(val item: ArrayList<SectionsItem>, val listenerItemClick: AddPointGridViewHolder.ListenerItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var multiView: View
    lateinit var initialViewHolder: RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        multiView = LayoutInflater.from(parent.context).inflate(R.layout.tp_item_category_title, parent, false)
        initialViewHolder = AddPointTitleViewHolder(multiView)

        return initialViewHolder
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val parentHolder = holder as AddPointTitleViewHolder
        parentHolder.bindTile(item[position])

        parentHolder.view.gridRecyclerview.let {
            it.layoutManager = GridLayoutManager(parentHolder.view.context, 4, GridLayoutManager.VERTICAL, false)
            it.addItemDecoration(AddPointsItemDecoration(parentHolder.view.context.resources.getDimensionPixelSize(R.dimen.tp_item_left_margin),
                   parentHolder.view.context.resources.getDimensionPixelSize(R.dimen.tp_item_right_margin)))
            it.adapter = AddPointGridAdapter(item[position].categories as ArrayList<CategoriesItem>, listenerItemClick)
            it.setHasFixedSize(true)
        }
    }
}
