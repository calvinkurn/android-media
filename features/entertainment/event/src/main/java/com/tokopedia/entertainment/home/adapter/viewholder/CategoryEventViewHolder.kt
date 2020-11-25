package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.CategoryModel
import kotlinx.android.synthetic.main.ent_layout_category_adapter_item.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_category.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class CategoryEventViewHolder(itemView: View,
                              val categoryEventListener: TrackingListener) : HomeEventViewHolder<CategoryModel>(itemView) {

    val listAdapter = InnerCategoryItemAdapter(categoryEventListener)

    init {
        itemView.ent_recycle_view_category.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(itemView.context, 5)
            adapter = listAdapter
        }

    }

    override fun bind(element: CategoryModel) {
        listAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_category
    }

    data class CategoryItemModel(var id: String, var imageUrl: String, var title: String, var applink: String)

    class InnerCategoryItemAdapter(val categoryEventListener: TrackingListener) : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<CategoryItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_category_adapter_item, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.icon)
            holder.view.title.text = items.get(position).title
            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context,
                        ApplinkConstInternalEntertainment.EVENT_CATEGORY, items.get(position).id, "", "")
                categoryEventListener.clickCategoryIcon(items.get(position), position + 1)
            }
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}