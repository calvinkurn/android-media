package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageChoosePacketAdapter
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_category.view.*

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageCategoryViewHolder(view: View, val onBindListener: onItemBindListener, val adapterChoosePacket: UmrahHomepageChoosePacketAdapter
) : AbstractViewHolder<UmrahHomepageCategoryEntity>(view) {
    override fun bind(element: UmrahHomepageCategoryEntity) {
        if (element.isLoaded && element.umrahCategories.isNotEmpty()) {
            with(itemView) {
                shimmering.hide()
                section_layout.show()

                adapterChoosePacket.setList(element.umrahCategories)
                rv_umrah_home_page_choose_packet.apply {
                    adapter = adapterChoosePacket
                    layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.HORIZONTAL, false
                    )
                    while (itemDecorationCount > 0) removeItemDecorationAt(0)
                    addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
                            RecyclerView.HORIZONTAL))

                    if (element.umrahCategories.isNotEmpty() && element.umrahCategories.getOrNull(0)?.isViewed == false) {
                        onBindListener.onImpressionCategory(element.umrahCategories.getOrNull(0)
                                ?: UmrahCategories(), 0)
                        element.umrahCategories.getOrNull(0)?.isViewed = true
                    }

                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            var position = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                            if (element.umrahCategories.getOrNull(position)?.isViewed == true) {
                                position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            }


                            if (element.umrahCategories.getOrNull(position)?.isViewed == false) {
                                onBindListener.onImpressionCategory(element.umrahCategories.getOrNull(position)
                                        ?: UmrahCategories(), position)
                                element.umrahCategories.getOrNull(position)?.isViewed = true
                            }
                        }
                    })
                }
            }
        } else if(element.isLoaded && element.umrahCategories.isEmpty()){
            itemView.shimmering.gone()
            itemView.section_layout.gone()
        } else {
            itemView.shimmering.show()
            itemView.section_layout.hide()
            if (!UmrahHomepageFragment.isRequestedCategory) {
                onBindListener.onBindCategoryVH(element.isLoadFromCloud)
                UmrahHomepageFragment.isRequestedCategory = true
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_umrah_home_page_category
    }

}