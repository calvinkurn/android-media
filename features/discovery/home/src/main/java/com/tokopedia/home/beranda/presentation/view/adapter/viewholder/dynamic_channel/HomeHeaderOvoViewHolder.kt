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

class HomeHeaderOvoViewHolder(itemView: View, private val listener: HomeCategoryListener)
: AbstractViewHolder<HomeHeaderOvoDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
        val BASE_URL = "https://ecs7.tokopedia.net/img/android/"
        val BACKGROUND_LIGHT_1 = BASE_URL + "home/header/xxhdpi/home_header_light_1.png"
        val BACKGROUND_LIGHT_2 = BASE_URL + "home/header/xxhdpi/home_header_light_2.png"
        val BACKGROUND_DARK_1 = BASE_URL + "home/header/xxhdpi/home_header_dark_1.png"
        val BACKGROUND_DARK_2 = BASE_URL + "home/header/xxhdpi/home_header_dark_2.png"
    }

    override fun bind(element: HomeHeaderOvoDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        renderEmptySpace()
        renderOvoLayout(element.headerDataModel)
        renderBackgroundHeight()
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
        renderBackgroundHeight()
    }

    private fun renderBackgroundHeight() {
        val backgroundViewImage = itemView.findViewById<ImageView>(R.id.view_background_image)
        ImageHandler.LoadImage(backgroundViewImage, BACKGROUND_LIGHT_1)
//        val ovoView = itemView.findViewById<OvoWidgetView>(R.id.view_ovo)
//        if (ovoView.visibility == View.VISIBLE) {
//            val backgroundView = itemView.findViewById<FrameLayout>(R.id.view_background)
//            ovoView.viewTreeObserver.addOnGlobalLayoutListener(
//                    object : ViewTreeObserver.OnGlobalLayoutListener {
//                        override fun onGlobalLayout() {
//                            val viewTreeObserver = ovoView.viewTreeObserver
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                viewTreeObserver.removeOnGlobalLayoutListener(this)
//                            } else {
//                                @Suppress("DEPRECATION")
//                                viewTreeObserver.removeGlobalOnLayoutListener(this)
//                            }
//                            val ovoHeight = ovoView.measuredHeight
//                            val backgroundParam = backgroundView.layoutParams as ViewGroup.MarginLayoutParams
//                            backgroundParam.setMargins(0, 0, 0, ovoHeight / 2)
//                            backgroundView.layoutParams = backgroundParam
//                            backgroundView.invalidate()
//                        }
//                    })
//        }
    }
}