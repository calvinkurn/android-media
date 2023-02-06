package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Matrix
import android.graphics.RectF
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_HEADER_OVO
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderAtf1DataModel
import com.tokopedia.home.databinding.HomeHeaderAtf1Binding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class HomeHeaderAtf1ViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener
) :
    AbstractViewHolder<HomeHeaderAtf1DataModel>(itemView) {

    private var binding: HomeHeaderAtf1Binding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_atf_1
    }

    override fun bind(element: HomeHeaderAtf1DataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_HEADER_OVO)
        measureHeightBanner()
        renderEmptySpace()
        renderHeader()
        element.headerDataModel?.let {
            renderBalanceLayout(
                it.homeBalanceModel,
                element.headerDataModel?.isUserLogin ?: false
            )
        }
        renderChooseAddress(element.needToShowChooseAddress)
        BenchmarkHelper.endSystraceSection()
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
        bottomCropBanner()
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

    override fun bind(element: HomeHeaderAtf1DataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun bottomCropBanner() {
        val imageBanner = binding?.ivBanner
        imageBanner?.run {
            val matrix: Matrix? = this.imageMatrix

            val scale: Float
            val viewWidth: Int = width - paddingLeft - paddingRight
            val viewHeight: Int = height - paddingTop - paddingBottom
            val drawableWidth: Int = drawable.intrinsicWidth
            val drawableHeight: Int = drawable.intrinsicHeight

            //Get the scale

            //Get the scale
            scale = if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
                viewHeight.toFloat() / drawableHeight.toFloat()
            } else {
                viewWidth.toFloat() / drawableWidth.toFloat()
            }

            //Define the rect to take image portion from

            //Define the rect to take image portion from
            val drawableRect = RectF(
                0f,
                drawableHeight - viewHeight / scale,
                drawableWidth.toFloat(),
                drawableHeight.toFloat()
            )
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            matrix?.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.START)

            imageMatrix = matrix
        }
    }

    private fun measureHeightBanner() {
        val width = itemView.context.resources.displayMetrics.widthPixels
        val density = itemView.context.resources.displayMetrics.density
        val widthPx = width / density
        val height = (widthPx * 240) / 411.42586
        val layoutParams = binding?.viewEmptyBanner?.layoutParams
        layoutParams?.height = height.toInt().toPx().toInt()
        binding?.viewEmptyBanner?.layoutParams = layoutParams
    }
}
