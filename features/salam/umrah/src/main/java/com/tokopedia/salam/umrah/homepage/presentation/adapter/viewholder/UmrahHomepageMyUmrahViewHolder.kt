package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageMyUmrahAdapter
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_dream_fund.view.*
import kotlinx.android.synthetic.main.partial_umrah_home_page_my_umrah.view.*

/**
 * @author by firman on 23/10/19
 */

class UmrahHomepageMyUmrahViewHolder(view: View, private val onBindListener: onItemBindListener
) : AbstractViewHolder<UmrahHomepageMyUmrahEntity>(view) {

    val adapterMyUmrah = UmrahHomepageMyUmrahAdapter(onBindListener)

    override fun bind(element: UmrahHomepageMyUmrahEntity) {
        if (element.isLoaded) {
            with(itemView) {
                if (element.listMyUmrahEntity.isEmpty()) {
                    shimmering.hide()
                    section_my_umrah.hide()
                    section_layout.show()
                    onBindListener.onImpressionDanaImpian()
                    dream_fund_umrah_home_page.setOnClickListener {
                        onBindListener.onClickDanaImpian()
                        RouteManager.route(context, getString(R.string.umrah_dana_impian_link))
                    }
                } else {
                    shimmering.hide()
                    section_layout.hide()
                    section_my_umrah.show()

                    adapterMyUmrah.setList(element.listMyUmrahEntity)
                    rv_umrah_home_page_my_umrah.apply {
                        adapter = adapterMyUmrah
                        layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.HORIZONTAL, false
                        )
                        addOnScrollListener(object : RecyclerView.OnScrollListener(){
                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                                    val positions = (rv_umrah_home_page_my_umrah.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                                    onBindListener.onImpressionMyUmrah((element.listMyUmrahEntity.getOrNull(positions)?: MyUmrahEntity()).header
                                            ,element.listMyUmrahEntity.getOrNull(positions) ?: MyUmrahEntity() ,positions)
                                }
                            }
                        })
                    }
                }
            }

        } else {
            itemView.section_layout.hide()
            itemView.section_my_umrah.hide()
            itemView.shimmering.show()
            onBindListener.onBindMyUmrahVH(element.isLoadFromCloud)
        }
    }
}