package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntLayoutCategoryAdapterItemBinding
import com.tokopedia.entertainment.databinding.EntLayoutViewholderCategoryBinding
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.CategoryModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Author errysuprayogi on 27,January,2020
 */
class CategoryEventViewHolder(itemView: View,
                              categoryEventListener: TrackingListener)
    : AbstractViewHolder<CategoryModel>(itemView) {

    private val listAdapter = InnerCategoryItemAdapter(categoryEventListener)
    private val binding: EntLayoutViewholderCategoryBinding? by viewBinding()
    init {
        binding?.entRecycleViewCategory?.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(itemView.context, SPAN)
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

        private const val SPAN = 5
    }

    data class CategoryItemModel(var id: String, var imageUrl: String, var title: String, var applink: String)

    class InnerCategoryItemAdapter(private val categoryEventListener: TrackingListener) : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<CategoryItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = EntLayoutCategoryAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return InnerViewHolder(binding, categoryEventListener)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            return holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(private val binding: EntLayoutCategoryAdapterItemBinding,
                          private val categoryEventListener: TrackingListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItemModel) {
            with(binding) {
                icon.loadImage(item.imageUrl)
                title.text = item.title
                root.setOnClickListener {
                    RouteManager.route(root.context,
                        ApplinkConstInternalEntertainment.EVENT_CATEGORY, item.id, "", "")
                    categoryEventListener.clickCategoryIcon(item, position + Int.ONE)
                }
            }
        }
    }
}
