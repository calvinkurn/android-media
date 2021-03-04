package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder


import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageMyUmrahAdapter
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.partial_umrah_home_page_dream_fund.view.*
import kotlinx.android.synthetic.main.partial_umrah_home_page_my_umrah.view.*

/**
 * @author by firman on 23/10/19
 */

class UmrahHomepageMyUmrahViewHolder(view: View, private val onBindListener: onItemBindListener,
                                     val adapterMyUmrah: UmrahHomepageMyUmrahAdapter,
                                     val userSessionInterface: UserSessionInterface
) : AbstractViewHolder<UmrahHomepageMyUmrahEntity>(view) {

    override fun bind(element: UmrahHomepageMyUmrahEntity) {
        with(itemView) {
            if (element.isLoaded && element.listMyUmrahEntity.isEmpty()) {
                shimmering.hide()
                section_my_umrah.hide()
                section_layout.show()
                if (!UmrahHomepageFragment.isDreamFundViewed) {
                    onBindListener.onImpressionDanaImpian()
                    UmrahHomepageFragment.isDreamFundViewed = true
                }
                dream_fund_umrah_home_page.setOnClickListener {
                    if (userSessionInterface.isLoggedIn) {
                        onBindListener.onClickDanaImpian()
                        RouteManager.route(context, URI_WEB + "" + getString(R.string.umrah_dana_impian_link))
                    }else{
                        goToLoginPage(context)
                    }
                }
            } else if (element.isLoaded && element.listMyUmrahEntity.isNotEmpty()) {

                shimmering.hide()
                section_layout.hide()
                section_my_umrah.show()

                adapterMyUmrah.setList(element.listMyUmrahEntity)
                rv_umrah_home_page_my_umrah.apply {
                    adapter = adapterMyUmrah
                    layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.HORIZONTAL, false
                    )

                    while (itemDecorationCount > 0) removeItemDecorationAt(0)
                    addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
                            RecyclerView.HORIZONTAL))


                    if (element.listMyUmrahEntity.isNotEmpty() && element.listMyUmrahEntity.getOrNull(0)?.isViewed == false) {
                        onBindListener.onImpressionMyUmrah((element.listMyUmrahEntity.getOrNull(0)
                                ?: MyUmrahEntity()).nextActionText
                                , element.listMyUmrahEntity.getOrNull(0) ?: MyUmrahEntity(), 0)
                        element.listMyUmrahEntity.getOrNull(0)?.isViewed = true
                    }
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            var position = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                            if (element.listMyUmrahEntity.getOrNull(position)?.isViewed == true) {
                                position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            }

                            if (element.listMyUmrahEntity.getOrNull(position)?.isViewed == false) {
                                onBindListener.onImpressionMyUmrah((element.listMyUmrahEntity.getOrNull(position)
                                        ?: MyUmrahEntity()).nextActionText
                                        , element.listMyUmrahEntity.getOrNull(position)
                                        ?: MyUmrahEntity(), position)
                                element.listMyUmrahEntity.getOrNull(position)?.isViewed = true
                            }
                        }
                    })
                }
            }
            else {
                section_layout.hide()
                section_my_umrah.hide()
                shimmering.show()
                if (!UmrahHomepageFragment.isRequestedMyUmrah) {
                    onBindListener.onBindMyUmrahVH(element.isLoadFromCloud)
                    UmrahHomepageFragment.isRequestedMyUmrah = true
                }else{}
            }

        }

    }

    private fun goToLoginPage(context: Context) {
            RouteManager.route(context, ApplinkConst.LOGIN)
    }

    companion object {
        val URI_WEB = "tokopedia://webview?url="
        val LAYOUT = R.layout.partial_umrah_home_page_dream_fund

    }
}