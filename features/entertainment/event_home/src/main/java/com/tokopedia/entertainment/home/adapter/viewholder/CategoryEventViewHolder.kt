package com.tokopedia.entertainment.home.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.ent_layout_category_adapter_item.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_category.view.*
import java.util.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class CategoryEventViewHolder(itemView: View): HomeEventViewHolder<CategoryViewModel>(itemView) {

    val listAdapter = SimpleCategoryItemAdapter()

    init {
        itemView.list.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(itemView.context, 5)
            adapter = listAdapter
        }

    }

    override fun bind(element: CategoryViewModel) {
        listAdapter.items = element.items
        listAdapter.notifyDataSetChanged()
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_category
    }

    data class CategoryItemModel(var imageUrl: String, var title : String, var applink: String)

    class SimpleCategoryItemAdapter : RecyclerView.Adapter<SimpleCategoryItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items : List<CategoryItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_category_adapter_item, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.icon)
            holder.view.title.text = items.get(position).title
            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context, items.get(position).applink)
            }
        }
        override fun getItemCount() = items.size
    }
}