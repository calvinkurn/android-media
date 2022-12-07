package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.databinding.HomeHeaderOvoBinding
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt.getFullToolbarHeight
import com.tokopedia.utils.view.binding.viewBinding

class HomeHeaderOvoViewHolder(itemView: View,
                              private val listener: HomeCategoryListener
)
: AbstractViewHolder<HomeHeaderDataModel>(itemView) {

    private var binding: HomeHeaderOvoBinding? by viewBinding()
    private var balanceWidgetView: BalanceWidgetView? = null
    private var chooseAddressView: ChooseAddressWidget? = null
    private var containerHeaderImage: ConstraintLayout? = null
    private var headerTopRounded: ConstraintLayout? = null
    private var viewPullRefresh: LayoutIconPullRefreshView? = null

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
        renderHeader()
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderHeader() {
        if (containerHeaderImage == null) {
            containerHeaderImage = getParentLayout(binding?.containerSuperGraphicHeader)
        }
        if (headerTopRounded == null) {
            headerTopRounded = getParentLayout(binding?.headerTopRounded)
        }
        if (viewPullRefresh == null) {
            viewPullRefresh = getParentLayout(binding?.viewPullRefresh)
        }
        viewPullRefresh?.let {
            listener.pullRefreshIconCaptured(it)
        }
    }

    override fun bind(element: HomeHeaderDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        if (chooseAddressView == null) {
            chooseAddressView = getParentLayout(binding?.viewChooseAddress)
        }
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
        if (balanceWidgetView == null) {
            balanceWidgetView = getParentLayout(binding?.viewBalanceWidget)
        }
        data?.let {
            if (isUserLogin) {
                balanceWidgetView?.visible()
                balanceWidgetView?.bind(it, listener)
            } else {
                balanceWidgetView?.gone()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getParentLayout(viewStub: ViewStub?) : T? {
        return if (viewStub is ViewStub &&
            !isViewStubHasBeenInflated(viewStub)
        ) {
            try {
                val stubChannelView = viewStub.inflate()
                stubChannelView as T
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }
}
