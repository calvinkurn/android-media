package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.TokoNowProductCardAdapter
import com.tokopedia.tokopedianow.common.adapter.TokoNowProductCardAdapter.*
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.*
import com.tokopedia.unifyprinciples.Typography

class TokoNowRecentPurchaseViewHolder(
    itemView: View,
    private val productCardListener: TokoNowProductCardListener?,
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<TokoNowRecentPurchaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_list_carousel
    }

    private var vsTitle: ViewStub? = null
    private var tvTitle: Typography? = null
    private var layoutShimmering: View? = null
    private var rvProduct: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    private val adapter by lazy {
        TokoNowProductCardAdapter(TokoNowProductCardAdapterTypeFactory(productCardListener))
    }

    init {
        vsTitle = itemView.findViewById(R.id.vsTitle)
        tvTitle = vsTitle?.inflate()?.findViewById(R.id.channel_title)
        layoutShimmering = itemView.findViewById(R.id.layoutShimmering)
        rvProduct = itemView.findViewById(R.id.rvProduct)
    }

    override fun bind(data: TokoNowRecentPurchaseUiModel) {
        when(data.state) {
            TokoNowLayoutState.LOADING -> {
                hideAllView()
                showShimmering()
            }
            TokoNowLayoutState.SHOW -> {
                initView(data)
                restoreScrollState()
                hideShimmering()
                showAllView()
            }
        }
    }

    private fun initView(data: TokoNowRecentPurchaseUiModel) {
        tvTitle?.text = data.title
        tvTitle?.setType(Typography.HEADING_4)
        rvProduct?.apply {
            adapter = this@TokoNowRecentPurchaseViewHolder.adapter
            linearLayoutManager = createLinearLayoutManager()
            layoutManager = linearLayoutManager
            addOnScrollListener(createScrollListener())
        }
        adapter.submitList(data.productList)
    }

    private fun showAllView() {
        vsTitle?.show()
        rvProduct?.show()
    }

    private fun hideAllView() {
        vsTitle?.hide()
        rvProduct?.hide()
    }

    private fun showShimmering() {
        layoutShimmering?.show()
    }

    private fun hideShimmering() {
        layoutShimmering?.hide()
    }

    private fun RecyclerView.createLinearLayoutManager(): LinearLayoutManager {
        return object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                    parent,
                    child,
                    rect,
                    immediate,
                    focusedChildVisible
                )
            }
        }
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return  object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollState = linearLayoutManager?.onSaveInstanceState()
                tokoNowView?.saveScrollState(adapterPosition, scrollState)
            }
        }
    }

    private fun restoreScrollState() {
        val scrollState = tokoNowView?.getScrollState(adapterPosition)
        linearLayoutManager?.onRestoreInstanceState(scrollState)
    }
}