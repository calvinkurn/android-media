package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahCategoriesFeatured
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.item_umrah_home_page_category_featured.view.*

/**
 * @author by firman on 22/10/19
 */

class UmrahHomepageCategoryFeaturedAdapter(val onItemBindListener: onItemBindListener) : RecyclerView.Adapter<UmrahHomepageCategoryFeaturedAdapter.UmrahHomepageCategoryFeaturedViewHolder>() {

    private var listCategories = emptyList<UmrahCategoriesFeatured>()
    private var adapterFeaturedCategory = UmrahHomepageCategoryFeaturedItemAdapter(onItemBindListener)

    inner class UmrahHomepageCategoryFeaturedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(categories: UmrahCategoriesFeatured) {
            with(itemView) {

                onItemBindListener.onImpressionFeaturedCategory(categories.title, categories)

                tv_umrah_home_page_category_featured_item.text = categories.title
                rv_umrah_home_page_category_featured_item.apply {
                    adapter = adapterFeaturedCategory
                    layoutManager = LinearLayoutManager(
                            this@with.context,
                            LinearLayoutManager.HORIZONTAL, false
                    )
                }
                adapterFeaturedCategory.apply {
                    setList(categories.products)
                    headerTitle = categories.title
                    positionDC = position
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepageCategoryFeaturedViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageCategoryFeaturedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_home_page_category_featured, parent, false)
        return UmrahHomepageCategoryFeaturedViewHolder(itemView)
    }

    fun setList(list: List<UmrahCategoriesFeatured>) {
        listCategories = list
        notifyDataSetChanged()
    }
}