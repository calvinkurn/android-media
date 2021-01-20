package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoWidgetView
import com.tokopedia.home_component.util.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class HomeHeaderOvoViewHolder(itemView: View, private val listener: HomeCategoryListener)
: AbstractViewHolder<HomeHeaderOvoDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
    }

    override fun bind(element: HomeHeaderOvoDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        renderOvoLayout(element.headerDataModel)
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: HomeHeaderOvoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun renderEmptySpace() {
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
                        layoutParams.height = listener.homeMainToolbarHeight
                        emptySpace.layoutParams = layoutParams
                        emptySpace.invalidate()
                    }
                }
        )
    }

    private fun renderOvoLayout(data: HeaderDataModel?) {
        val ovoView = itemView.findViewById<OvoWidgetView>(R.id.view_ovo)
        data?.let {
            if (it.isUserLogin) {
                ovoView.visible()
                ovoView.bind(it, listener)
            } else {
                ovoView.gone()
            }
        }
    }
}