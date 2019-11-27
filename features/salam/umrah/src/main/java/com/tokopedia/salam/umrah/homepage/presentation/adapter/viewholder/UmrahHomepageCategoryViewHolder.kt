package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageChoosePacketAdapter
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_category.view.*

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageCategoryViewHolder(view: View, private val onBindListener: onItemBindListener,
                                      private val trackingUmrahUtil: TrackingUmrahUtil
                                      ) : AbstractViewHolder<UmrahHomepageCategoryEntity>(view){
    val adapterChoosePacket = UmrahHomepageChoosePacketAdapter(trackingUmrahUtil)

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
                            LinearLayoutManager.HORIZONTAL, false
                    )
                    addOnScrollListener(object : RecyclerView.OnScrollListener(){
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_IDLE){
                                val positions = (rv_umrah_home_page_choose_packet.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                                trackingUmrahUtil.umrahImpressionCategoryTracker(element.umrahCategories.getOrNull(positions)
                                        ?: UmrahCategories() ,positions)
                            }
                        }
                    })
                }
            }
        }else{
            itemView.shimmering.show()
            itemView.section_layout.hide()
            onBindListener.onBindCategoryVH(element.isLoadFromCloud)
        }
    }
}