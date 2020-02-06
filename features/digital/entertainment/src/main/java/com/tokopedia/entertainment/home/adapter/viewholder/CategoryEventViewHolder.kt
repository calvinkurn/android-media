package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        listAdapter.items = Arrays.asList(
                CategoryItemModel("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/5/47672011/47672011_a49223fb-656a-4665-b8c2-7af12cb869c5.png",
                        "Taman bermain"),
                CategoryItemModel("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/5/47672011/47672011_a49223fb-656a-4665-b8c2-7af12cb869c5.png",
                        "Taman bermain"),
                CategoryItemModel("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/5/47672011/47672011_a49223fb-656a-4665-b8c2-7af12cb869c5.png",
                        "Taman bermain"),
                CategoryItemModel("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/5/47672011/47672011_a49223fb-656a-4665-b8c2-7af12cb869c5.png",
                        "Taman bermain"),
                CategoryItemModel("https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/5/47672011/47672011_a49223fb-656a-4665-b8c2-7af12cb869c5.png",
                        "Taman bermain")
        )
        itemView.list.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(itemView.context, 5)
            adapter = listAdapter
        }

    }

    override fun bind(element: CategoryViewModel) {
        listAdapter.notifyDataSetChanged()
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_category
    }

    data class CategoryItemModel(var imageUrl: String, var title : String)

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
        }
        override fun getItemCount() = items.size
    }
}