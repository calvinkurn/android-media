package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageCategoryFeaturedAdapter
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_category_featured.view.*

/**
 * @author by firman on 23/10/19
 */

class UmrahHomepageCategoryFeaturedViewHolder(view: View, private val onBindListener: onItemBindListener, val adapterFeaturedCategory:UmrahHomepageCategoryFeaturedAdapter)
    : AbstractViewHolder<UmrahHomepageCategoryFeaturedEntity>(view) {

    val viewPool = RecyclerView.RecycledViewPool()
    override fun bind(element: UmrahHomepageCategoryFeaturedEntity) {
        if (element.isLoaded && element.umrahCategoriesFeatured.isNotEmpty()) {
            with(itemView) {
                shimmering.hide()
                section_layout.show()

                adapterFeaturedCategory.setList(element.umrahCategoriesFeatured)
                rv_umrah_home_page_category_featured.apply {
                    setHasFixedSize(true)
                    setItemViewCacheSize(20)
                    setDrawingCacheEnabled(true)
                    setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
                    setRecycledViewPool(viewPool)
                    adapter = adapterFeaturedCategory
                    layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.VERTICAL, false
                    )
                }
            }
        } else {
            itemView.shimmering.show()
            itemView.section_layout.hide()
            if (!isRequested) {
                onBindListener.onBindCategoryFeaturedVH(element.isLoadFromCloud)
            }
        }

    }

    companion object{
        var isRequested = false
        val LAYOUT = R.layout.partial_umrah_home_page_category_featured

    }
}