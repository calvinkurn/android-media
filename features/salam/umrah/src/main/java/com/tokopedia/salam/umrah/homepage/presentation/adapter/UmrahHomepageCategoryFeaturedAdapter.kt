package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.data.UmrahCategoriesFeatured
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.item_umrah_home_page_category_featured.view.*

/**
 * @author by firman on 22/10/19
 */

class UmrahHomepageCategoryFeaturedAdapter(val onItemBindListener: onItemBindListener) : RecyclerView.Adapter<UmrahHomepageCategoryFeaturedAdapter.UmrahHomepageCategoryFeaturedViewHolder>() {

    private var listCategories = emptyList<UmrahCategoriesFeatured>()
    lateinit var adapterFeaturedCategory : UmrahHomepageCategoryFeaturedItemAdapter
    val viewPool = RecyclerView.RecycledViewPool()


    inner class UmrahHomepageCategoryFeaturedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(categories: UmrahCategoriesFeatured, positionAdapter: Int) {
            with(itemView) {
                if (categories.products.isNotEmpty()) {
                    ll_umrah_home_page_category_featured.show()
                    tv_umrah_home_page_category_featured_item.text = categories.title
                    rv_umrah_home_page_category_featured_item.apply {
                        setHasFixedSize(true)
                        setItemViewCacheSize(20)
                        setDrawingCacheEnabled(true)
                        setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
                        setRecycledViewPool(viewPool)
                        adapter = adapterFeaturedCategory
                        layoutManager = LinearLayoutManager(
                                this@with.context,
                                RecyclerView.HORIZONTAL, false
                        )

                        while (itemDecorationCount > 0) removeItemDecorationAt(0)
                        addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
                                RecyclerView.HORIZONTAL))
                        if (categories.products.isNotEmpty() && categories.products.getOrNull(0)?.isViewed == false)

                            onItemBindListener.onImpressionFeaturedCategory(categories.title, categories.products.getOrNull(0)
                                    ?: Products(), 0, position)
                        categories.products.getOrNull(0)?.isViewed = true


                        addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)
                                var positionItem = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                                if (categories.products.getOrNull(positionItem)?.isViewed == true) {
                                    positionItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                                }

                                if (categories.products.getOrNull(positionItem)?.isViewed == false) {
                                    onItemBindListener.onImpressionFeaturedCategory(categories.title, categories.products.getOrNull(positionItem)
                                            ?: Products(), positionItem, position)
                                    categories.products.getOrNull(positionItem)?.isViewed = true
                                }
                            }
                        })
                    }
                    adapterFeaturedCategory.apply {
                        setList(categories.products, categories.title, positionAdapter)
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepageCategoryFeaturedViewHolder, position: Int) {
        holder.bind(listCategories[position], holder.adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageCategoryFeaturedViewHolder {
        adapterFeaturedCategory = UmrahHomepageCategoryFeaturedItemAdapter(onItemBindListener)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_home_page_category_featured, parent, false)
        return UmrahHomepageCategoryFeaturedViewHolder(itemView)
    }

    fun setList(list: List<UmrahCategoriesFeatured>) {
        listCategories = list
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return listCategories.get(position).id.toLong()
    }
}