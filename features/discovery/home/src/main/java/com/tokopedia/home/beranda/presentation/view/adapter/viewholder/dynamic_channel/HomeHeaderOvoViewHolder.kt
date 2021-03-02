package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.BalanceWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoWidgetView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import kotlinx.android.synthetic.main.home_header_ovo.view.*

class HomeHeaderOvoViewHolder(itemView: View,
                              private val listener: HomeCategoryListener,
                              private val chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener
)
: AbstractViewHolder<HomeHeaderOvoDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
    }

    override fun bind(element: HomeHeaderOvoDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace(element.headerDataModel?.isUserLogin?:false)
        renderOvoLayout(element.headerDataModel, element.needToShowUserWallet)
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: HomeHeaderOvoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        val chooseAddressView = itemView.widget_choose_address
        chooseAddressView.bindChooseAddress(chooseAddressWidgetListener)
        chooseAddressView.run {
            visibility = if (needToShowChooseAddress) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }

    private fun renderEmptySpace(isUserLogin: Boolean) {
        val emptySpace = itemView.findViewById<FrameLayout>(R.id.view_empty)
        emptySpace.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = emptySpace.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        val layoutParams = emptySpace.layoutParams
                        if (!isUserLogin) {
                            layoutParams.height = listener.homeMainToolbarHeight -
                                    itemView.resources.getDimensionPixelOffset(R.dimen.dp_12)
                        } else {
                            layoutParams.height = listener.homeMainToolbarHeight -
                                    itemView.resources.getDimensionPixelOffset(R.dimen.dp_8)
                        }
                        emptySpace.layoutParams = layoutParams
                        emptySpace.invalidate()
                    }
                }
        )
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

    private fun renderBalanceLayout(data: BalanceWidgetDataModel?, needToShowUserWallet: Boolean ) {
        val balanceWidgetView = itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget)
//        data?.let {
//            if (it.isUserLogin && needToShowUserWallet) {
//                ovoView.visible()
//                ovoView.bind(it, listener)
//            } else {
//                ovoView.gone()
//            }
//        }
    }
}