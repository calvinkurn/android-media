package com.tokopedia.tokopedianow.annotation.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.annotation.analytic.AnnotationWidgetAnalytic
import com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory.BrandWidgetItemAdapter
import com.tokopedia.tokopedianow.annotation.presentation.itemdecoration.BrandWidgetItemDecoration
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel.BrandWidgetState
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBrandWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandWidgetViewHolder(
    itemView: View,
    private val analytic: AnnotationWidgetAnalytic,
    private val listener: BrandWidgetListener?
) : AbstractViewHolder<BrandWidgetUiModel>(itemView), TokoNowDynamicHeaderListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_brand_widget
    }

    private val binding: ItemTokopedianowBrandWidgetBinding? by viewBinding()

    private val adapter by lazy { BrandWidgetItemAdapter(analytic) }

    init {
        setupRecyclerView()
    }

    override fun bind(uiModel: BrandWidgetUiModel) {
        when (uiModel.state) {
            BrandWidgetState.LOADING -> {
                showLoading()
                hideWidget()
                hideError()
            }
            BrandWidgetState.LOADED -> {
                showWidget(uiModel)
                hideLoading()
                hideError()
            }
            BrandWidgetState.ERROR -> {
                showError(uiModel)
                hideLoading()
                hideWidget()
            }
        }
    }

    private fun showWidget(uiModel: BrandWidgetUiModel) {
        binding?.apply {
            header.setModel(uiModel.header)
            header.setListener(this@BrandWidgetViewHolder)
            adapter.setVisitables(uiModel.items)
            addImpressionListener(uiModel)
            widgetGroup.show()
        }
    }

    private fun addImpressionListener(uiModel: BrandWidgetUiModel) {
        itemView.addOnImpressionListener(uiModel) {
            analytic.sendImpressionAnnotationWidgetEvent()
        }
    }

    private fun showLoading() {
        binding?.loadingShimmer?.root?.show()
    }

    private fun hideLoading() {
        binding?.loadingShimmer?.root?.hide()
    }

    private fun showError(uiModel: BrandWidgetUiModel) {
        binding?.apply {
            localLoad.refreshBtn?.setOnClickListener {
                listener?.clickRetryButton(uiModel.id)
            }
            localLoad.show()
        }
    }

    private fun hideError() {
        binding?.localLoad?.hide()
    }

    private fun hideWidget() {
        binding?.widgetGroup?.hide()
    }

    private fun setupRecyclerView() {
        binding?.rvBrand?.apply {
            adapter = this@BrandWidgetViewHolder.adapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(BrandWidgetItemDecoration())
        }
    }

    override fun onSeeAllClicked(
        context: Context,
        channelId: String,
        headerName: String,
        appLink: String,
        widgetId: String
    ) {
        RouteManager.route(context, appLink)
        analytic.sendClickArrowButtonEvent()
    }

    override fun onChannelExpired() {
        listener?.onChannelExpired()
    }

    interface BrandWidgetListener {
        fun clickRetryButton(id: String)

        fun onSeeAllClicked(
            context: Context,
            channelId: String,
            headerName: String,
            appLink: String,
            widgetId: String
        )

        fun onChannelExpired()
    }
}
