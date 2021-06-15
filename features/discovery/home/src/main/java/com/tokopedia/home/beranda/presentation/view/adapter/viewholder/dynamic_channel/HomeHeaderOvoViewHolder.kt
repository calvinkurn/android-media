package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoWidgetView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt.getFullToolbarHeight
import kotlinx.android.synthetic.main.home_header_ovo.view.*

class HomeHeaderOvoViewHolder(itemView: View,
                              private val listener: HomeCategoryListener
)
: AbstractViewHolder<HomeHeaderOvoDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
    }

    override fun bind(element: HomeHeaderOvoDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        element.headerDataModel?.let {
            resetView()
            when(it.homeBalanceModel.balanceType) {
                HomeBalanceModel.TYPE_STATE_1 -> {
                    renderOvoLayout(element.headerDataModel, element.needToShowUserWallet)
                }
                HomeBalanceModel.TYPE_STATE_2, HomeBalanceModel.TYPE_STATE_3 -> {
                    renderBalanceLayout(
                            it.homeBalanceModel,
                            element.headerDataModel?.isUserLogin?: false,
                            element.needToShowUserWallet)
                }
                else -> resetView()
            }
        }

        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: HomeHeaderOvoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun resetView() {
        itemView.findViewById<OvoWidgetView>(R.id.view_ovo).gone()
        itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget).gone()
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        val chooseAddressView = itemView.widget_choose_address
        if (needToShowChooseAddress) {
            listener.initializeChooseAddressWidget(chooseAddressView, needToShowChooseAddress)
        } else {
            chooseAddressView.gone()
        }
    }

    private fun renderEmptySpace() {
        val emptySpace = itemView.findViewById<FrameLayout>(R.id.view_empty)
        val layoutParams = emptySpace.layoutParams
        layoutParams.height = getFullToolbarHeight(itemView.context)
        emptySpace.layoutParams = layoutParams
        emptySpace.invalidate()
    }

    private fun renderOvoLayout(data: HeaderDataModel?, needToShowUserWallet: Boolean ) {
        val ovoView = itemView.findViewById<OvoWidgetView>(R.id.view_ovo)
        data?.let {
            if (it.isUserLogin && needToShowUserWallet) {
                ovoView.visible()
                ovoView.bind(it, listener)
            } else {
                ovoView.gone()
            }
        }
    }

    private fun renderBalanceLayout(data: HomeBalanceModel?, isUserLogin: Boolean, needToShowUserWallet: Boolean) {
        val balanceWidgetView = itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget)
        data?.let {
            if (isUserLogin && needToShowUserWallet) {
                balanceWidgetView.visible()
                balanceWidgetView.bind(it, listener)
            } else {
                balanceWidgetView.gone()
            }
        }
    }
}