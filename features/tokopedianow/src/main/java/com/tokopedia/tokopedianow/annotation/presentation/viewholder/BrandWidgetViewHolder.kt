package com.tokopedia.tokopedianow.annotation.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory.BrandWidgetItemAdapter
import com.tokopedia.tokopedianow.annotation.presentation.itemdecoration.BrandWidgetItemDecoration
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel.BrandWidgetState
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBrandWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandWidgetViewHolder(
    itemView: View,
    private val headerListener: TokoNowDynamicHeaderListener?
) : AbstractViewHolder<BrandWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_brand_widget
    }

    private val binding: ItemTokopedianowBrandWidgetBinding? by viewBinding()

    private val adapter by lazy { BrandWidgetItemAdapter() }

    init {
        setupRecyclerView()
    }

    override fun bind(uiModel: BrandWidgetUiModel) {
        when (uiModel.state) {
            BrandWidgetState.LOADING -> {
                showLoading()
                hideWidget()
            }
            BrandWidgetState.LOADED -> {
                hideLoading()
                showWidget(uiModel)
            }
            BrandWidgetState.ERROR -> {
                hideLoading()
                hideWidget()
                showError()
            }
        }
    }

    private fun showWidget(uiModel: BrandWidgetUiModel) {
        binding?.apply {
            header.setModel(uiModel.header)
            header.setListener(headerListener)
            adapter.setVisitables(uiModel.items)
            widgetGroup.show()
        }
    }

    private fun showLoading() {
        binding?.loadingShimmer?.root?.show()
    }

    private fun hideLoading() {
        binding?.loadingShimmer?.root?.hide()
    }

    private fun showError() {

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
}
