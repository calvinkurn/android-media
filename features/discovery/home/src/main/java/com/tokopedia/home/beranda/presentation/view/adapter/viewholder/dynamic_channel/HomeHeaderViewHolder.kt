package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R as homeR
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.LoginWidgetView
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home.databinding.HomeHeaderBinding
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeHeaderViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener,
    private val homeThematicUtil: HomeThematicUtil,
) : AbstractViewHolder<HomeHeaderDataModel>(itemView) {

    private var binding: HomeHeaderBinding? by viewBinding()
    private var balanceWidgetView: BalanceWidgetView? = null
    private var loginWidgetView: LoginWidgetView? = null
    private var chooseAddressView: ChooseAddressWidget? = null
    private var viewPullRefresh: LayoutIconPullRefreshView? = null
    private val paddingBottomChooseAddress = 3f.toDpInt()

    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.home_header
    }

    override fun bind(element: HomeHeaderDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        renderHeader()
        element.headerDataModel?.let {
            if(it.isUserLogin) {
                renderBalanceLayout(it.homeBalanceModel)
            } else {
                renderLoginWidget()
            }
        }
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        if (chooseAddressView == null) {
            chooseAddressView = getParentLayout(binding?.viewChooseAddress)
        }
        chooseAddressView?.let {
            if (needToShowChooseAddress) {
                it.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, paddingBottomChooseAddress)
                listener.initializeChooseAddressWidget(it, needToShowChooseAddress)
            } else {
                it.gone()
            }
        }
    }

    private fun renderHeader() {
        if (viewPullRefresh == null) {
            viewPullRefresh = getParentLayout(binding?.viewPullRefresh)
        }
        viewPullRefresh?.let {
            it.setColorPullRefresh(getPullRefreshLoaderColor())
            listener.pullRefreshIconCaptured(it)
        }
    }

    private fun renderEmptySpace() {
        val layoutParams = binding?.viewEmpty?.layoutParams
        layoutParams?.height = NavToolbarExt.getFullToolbarHeight(itemView.context)
        binding?.viewEmpty?.layoutParams = layoutParams
        binding?.viewEmpty?.invalidate()
    }

    private fun renderBalanceLayout(data: HomeBalanceModel) {
        if (balanceWidgetView == null) {
            balanceWidgetView = getParentLayout(binding?.viewBalanceWidget)
        }
        loginWidgetView?.gone()
        balanceWidgetView?.visible()
        balanceWidgetView?.bind(data, listener, homeThematicUtil)
    }

    private fun renderLoginWidget() {
        if(loginWidgetView == null) {
            loginWidgetView = getParentLayout(binding?.viewLoginWidget)
        }
        balanceWidgetView?.gone()
        loginWidgetView?.visible()
        loginWidgetView?.bind(listener, homeThematicUtil)
    }

    override fun bind(element: HomeHeaderDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && (payloads[0] as? Bundle)?.getBoolean(HomeThematicUtil.PAYLOAD_APPLY_THEMATIC_COLOR) == true) {
            chooseAddressView?.updateWidget()
            balanceWidgetView?.applyThematicColor()
            viewPullRefresh?.setColorPullRefresh(getPullRefreshLoaderColor())
            loginWidgetView?.renderTextColor(homeThematicUtil)
        } else {
            bind(element)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getParentLayout(viewStub: ViewStub?): T? {
        return if (viewStub is ViewStub &&
            !isViewStubHasBeenInflated(viewStub)
        ) {
            try {
                val stubChannelView = viewStub.inflate()
                stubChannelView as T
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    private fun getPullRefreshLoaderColor(): Int {
        return if(homeThematicUtil.isBackgroundLoaded && !homeThematicUtil.isDefault())
            LayoutIconPullRefreshView.TYPE_WHITE
        else LayoutIconPullRefreshView.TYPE_GREEN
    }
}
