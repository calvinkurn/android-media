package com.tokopedia.tokopedianow.common.viewholder.oldrepurchase

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.oldrepurchase.TokoNowProductCardAdapter
import com.tokopedia.tokopedianow.common.adapter.oldrepurchase.TokoNowProductCardAdapter.*
import com.tokopedia.tokopedianow.common.model.oldrepurchase.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowProductCardViewHolder.*
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductListCarouselBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowRepurchaseViewHolder(
    itemView: View,
    private val productCardListener: TokoNowProductCardListener?,
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<TokoNowRepurchaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_list_carousel
    }

    private val adapter by lazy {
        TokoNowProductCardAdapter(TokoNowProductCardAdapterTypeFactory(productCardListener))
    }

    private var binding: ItemTokopedianowProductListCarouselBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private var vsTitle: ViewStub? = null
    private var tvTitle: Typography? = null
    private var tvSeeAll: Typography? = null
    private var layoutShimmering: View? = null
    private var rvProduct: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    init {
        vsTitle = binding?.vsTitle
        vsTitle?.setOnInflateListener { _, inflated ->
            stubBinding = PartialTokopedianowViewStubDcTitleBinding.bind(inflated)
        }
        vsTitle?.inflate()
        tvTitle = stubBinding?.channelTitle
        tvSeeAll = binding?.tvSeeAll
        layoutShimmering = binding?.carouselShimmering?.carouselShimmeringLayout
        rvProduct = binding?.rvProduct
    }

    override fun bind(data: TokoNowRepurchaseUiModel) {
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

    private fun initView(data: TokoNowRepurchaseUiModel) {
        tvTitle?.text = data.title
        tvTitle?.setType(Typography.HEADING_4)

        tvSeeAll?.setOnClickListener {
            goToRepurchasePage()
        }

        rvProduct?.apply {
            adapter = this@TokoNowRepurchaseViewHolder.adapter
            linearLayoutManager = createLinearLayoutManager()
            layoutManager = linearLayoutManager
            addOnScrollListener(createScrollListener())
        }
        adapter.submitList(data.productList)
    }

    private fun showAllView() {
        tvTitle?.show()
        rvProduct?.show()
        tvSeeAll?.show()
    }

    private fun hideAllView() {
        tvTitle?.hide()
        rvProduct?.hide()
        tvSeeAll?.hide()
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
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollState = linearLayoutManager?.onSaveInstanceState()
                tokoNowView?.saveScrollState(adapterPosition, scrollState)
            }
        }
    }

    private fun goToRepurchasePage() {
        RouteManager.route(itemView.context, ApplinkConstInternalTokopediaNow.REPURCHASE)
    }

    private fun restoreScrollState() {
        val scrollState = tokoNowView?.getScrollState(adapterPosition)
        linearLayoutManager?.onRestoreInstanceState(scrollState)
    }
}
