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
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt.getFullToolbarHeight
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class HomeHeaderOvoViewHolder(itemView: View,
                              private val listener: HomeCategoryListener
)
: AbstractViewHolder<HomeHeaderDataModel>(itemView) {

    private var binding: HomeHeaderOvoBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
        private const val IMAGE_HEADER_MOBILE = "https://dhabawidhikari.com/wp-content/uploads/2022/06/header_phone.png"
        private const val IMAGE_HEADER_TABLET_700 = "https://dhabawidhikari.com/wp-content/uploads/2022/06/header_tablet_700_px.png"
        private const val IMAGE_HEADER_ABOVE_700 = "https://dhabawidhikari.com/wp-content/uploads/2022/06/header_tablet_800_px.png"
    }

    override fun bind(element: HomeHeaderDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        renderHeaderImage()
        element.headerDataModel?.let {
            resetView()
            when(it.homeBalanceModel.balanceType) {
                HomeBalanceModel.TYPE_STATE_2 -> {
                    renderBalanceLayout(
                        it.homeBalanceModel,
                        element.headerDataModel?.isUserLogin?: false,
                        element.needToShowUserWallet)
                }
                HomeBalanceModel.TYPE_STATE_3 -> {
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

    private fun renderHeaderImage() {
        val headerBackgroundHomeImage = itemView.findViewById<ImageUnify>(R.id.header_background_home_image)
        val headerBackgroundHome = itemView.findViewById<View>(R.id.header_background_home)
        if (listener.isSuperGraphicHeaderActive()) {
            headerBackgroundHomeImage.visible()
            headerBackgroundHomeImage.setImageUrl(IMAGE_HEADER_MOBILE)
            headerBackgroundHome.invisible()
        } else {
            headerBackgroundHomeImage.gone()
            headerBackgroundHome.visible()
        }
    }

    override fun bind(element: HomeHeaderDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun resetView() {
        itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget).gone()
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