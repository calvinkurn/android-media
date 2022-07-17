package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.databinding.HomeHeaderOvoBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt.getFullToolbarHeight
import com.tokopedia.utils.view.binding.viewBinding

class HomeHeaderOvoViewHolder(itemView: View,
                              private val listener: HomeCategoryListener
)
: AbstractViewHolder<HomeHeaderDataModel>(itemView) {

    private var binding: HomeHeaderOvoBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
    }

    override fun bind(element: HomeHeaderDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        element.headerDataModel?.let {
            renderBalanceLayout(
                it.homeBalanceModel,
                element.headerDataModel?.isUserLogin ?: false
            )
        }

        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: HomeHeaderDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        val chooseAddressView = binding?.widgetChooseAddress
        chooseAddressView?.let {
            if (needToShowChooseAddress) {
                listener.initializeChooseAddressWidget(it, needToShowChooseAddress)
            } else {
                it.gone()
            }
        }
    }

    private fun renderEmptySpace() {
        val emptySpace = itemView.findViewById<FrameLayout>(R.id.view_empty)
        val layoutParams = emptySpace.layoutParams
        layoutParams.height = getFullToolbarHeight(itemView.context)
        emptySpace.layoutParams = layoutParams
        emptySpace.invalidate()
    }

    private fun renderBalanceLayout(data: HomeBalanceModel?, isUserLogin: Boolean) {
        val balanceWidgetView = itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget)
        data?.let {
            if (isUserLogin) {
                balanceWidgetView.visible()
                balanceWidgetView.bind(it, listener)
            } else {
                balanceWidgetView.gone()
            }
        }
    }
}