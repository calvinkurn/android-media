package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderAtf1DataModel
import com.tokopedia.home.databinding.HomeHeaderAtf1Binding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeHeaderAtf1ViewHolder (itemView: View,
                                private val listener: HomeCategoryListener
)
: AbstractViewHolder<HomeHeaderAtf1DataModel>(itemView) {

    private var binding: HomeHeaderAtf1Binding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_atf_1
    }

    override fun bind(element: HomeHeaderAtf1DataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderHeader()
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderChooseAddress(needToShowChooseAddress: Boolean) {
        binding?.viewChooseAddress?.let {
            if (needToShowChooseAddress) {
                listener.initializeChooseAddressWidget(it, needToShowChooseAddress)
            } else {
                it.gone()
            }
        }
    }

    private fun renderHeader() {
        binding?.viewPullRefresh?.let {
            listener.pullRefreshIconCaptured(it)
        }
    }

    override fun bind(element: HomeHeaderAtf1DataModel, payloads: MutableList<Any>) {
        bind(element)
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
