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
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBrandWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandWidgetViewHolder(
    itemView: View
) : AbstractViewHolder<BrandWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_brand_widget
    }

    private val binding: ItemTokopedianowBrandWidgetBinding? by viewBinding()

    private val adapter by lazy { BrandWidgetItemAdapter()  }

    init {
        setupRecyclerView()
    }

    override fun bind(uiModel: BrandWidgetUiModel) {
        when(uiModel.state) {
            TokoNowLayoutState.SHOW -> showWidget(uiModel)
            else -> hideWidget()
        }
    }

    private fun showWidget(uiModel: BrandWidgetUiModel) {
        binding?.apply {
            header.setModel(uiModel.header)
            adapter.setVisitables(uiModel.brandList)
            root.show()
        }
    }

    private fun hideWidget() {
        binding?.root?.hide()
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
