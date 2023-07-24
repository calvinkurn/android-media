package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.LoginWidgetView
import com.tokopedia.home.databinding.HomeHeaderAtf2Binding
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
class HomeHeaderAtf2ViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener
) : AbstractViewHolder<HomeHeaderDataModel>(itemView) {

    private var binding: HomeHeaderAtf2Binding? by viewBinding()
    private var balanceWidgetView: BalanceWidgetView? = null
    private var loginWidgetView: LoginWidgetView? = null
    private var chooseAddressView: ChooseAddressWidget? = null
    private var viewPullRefresh: LayoutIconPullRefreshView? = null
    private val paddingBottomChooseAddress = 3f.toDpInt()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_atf_2
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
        balanceWidgetView?.bind(data, listener)
    }

    private fun renderLoginWidget() {
        if(loginWidgetView == null) {
            loginWidgetView = getParentLayout(binding?.viewLoginWidget)
        }
        balanceWidgetView?.gone()
        loginWidgetView?.visible()
        loginWidgetView?.bind(listener)
    }

    override fun bind(element: HomeHeaderDataModel, payloads: MutableList<Any>) {
        bind(element)
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
}
