package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderAtf2DataModel
import com.tokopedia.home.databinding.HomeHeaderAtf2Binding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.usercomponents.loginwidget.view.LoginWidgetListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeHeaderAtf2ViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener
) :
    AbstractViewHolder<HomeHeaderAtf2DataModel>(itemView) {

    private var binding: HomeHeaderAtf2Binding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_atf_2
    }

    override fun bind(element: HomeHeaderAtf2DataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        renderHeader()
        renderLoginWidget()
        element.headerDataModel?.let {
            renderBalanceLayout(
                it.homeBalanceModel,
                element.headerDataModel?.isUserLogin ?: false
            )
        }
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderLoginWidget() {
        binding?.loginWidgetAtf2?.setListener(
            listener = object : LoginWidgetListener {
                override fun goToLogin() {
                    listener.goToLogin()
                }
            }
        )
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        binding?.viewChooseAddress?.let {
            listener.initializeChooseAddressWidget(it, needToShowChooseAddress)
        }
    }

    private fun renderHeader() {
        binding?.viewPullRefresh?.let {
            listener.pullRefreshIconCaptured(it)
        }
    }

    private fun renderEmptySpace() {
        val layoutParams = binding?.viewEmpty?.layoutParams
        layoutParams?.height = NavToolbarExt.getFullToolbarHeight(itemView.context)
        binding?.viewEmpty?.layoutParams = layoutParams
        binding?.viewEmpty?.invalidate()
    }

    private fun renderBalanceLayout(data: HomeBalanceModel?, isUserLogin: Boolean) {
        data?.let {
            if (isUserLogin) {
                binding?.viewBalanceWidget?.visible()
                binding?.viewBalanceWidget?.bind(it, listener)
            } else {
                binding?.viewBalanceWidget?.gone()
            }
        }
    }

    override fun bind(element: HomeHeaderAtf2DataModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
