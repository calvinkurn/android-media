package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageCategoryFeaturedAdapter
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_category_featured.view.*

/**
 * @author by firman on 23/10/19
 */

class UmrahHomepageCategoryFeaturedViewHolder(view: View, private val onBindListener: onItemBindListener,
                                              private val trackingUmrahUtil: TrackingUmrahUtil
) : AbstractViewHolder<UmrahHomepageCategoryFeaturedEntity>(view){
    val adapterFeaturedCategory = UmrahHomepageCategoryFeaturedAdapter(view.context, trackingUmrahUtil)

    override fun bind(element: UmrahHomepageCategoryFeaturedEntity) {
        if (element.isLoaded && element.umrahCategoriesFeatured.isNotEmpty()) {
            with(itemView) {
                shimmering.hide()
                section_layout.show()

                adapterFeaturedCategory.setList(element.umrahCategoriesFeatured)
                rv_umrah_home_page_category_featured.apply {
                    adapter = adapterFeaturedCategory
                    layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL, false
                    )
                }
            }
        }else{
            itemView.shimmering.show()
            itemView.section_layout.hide()
            onBindListener.onBindCategoryFeaturedVH(element.isLoadFromCloud)
        }
    }
}