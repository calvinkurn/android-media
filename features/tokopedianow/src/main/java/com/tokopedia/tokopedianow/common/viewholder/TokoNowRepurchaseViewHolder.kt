package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.adapter.decoration.RepurchaseProductItemDecoration
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseProductAdapter
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseProductAdapter.TokoNowRepurchaseProductAdapterTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowRepurchaseSubtitleColor.NN500
import com.tokopedia.tokopedianow.common.constant.TokoNowRepurchaseSubtitleColor.YN500
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseProductViewHolder.TokoNowRepurchaseProductListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRepurchaseProductListBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowRepurchaseViewHolder(
    itemView: View,
    private val productCardListener: TokoNowRepurchaseProductListener?,
    private val listener: TokoNowRepurchaseListener?,
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<TokoNowRepurchaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_product_list
    }

    private val adapter by lazy {
        TokoNowRepurchaseProductAdapter(
            TokoNowRepurchaseProductAdapterTypeFactory(productCardListener)
        )
    }

    private var binding: ItemTokopedianowRepurchaseProductListBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private var vsTitle: ViewStub? = null
    private var tvTitle: Typography? = null
    private var cardViewChevron: CardView? = null
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
        cardViewChevron = binding?.cardViewChevron
        layoutShimmering = binding?.carouselShimmering?.carouselShimmeringLayout
        rvProduct = binding?.rvProduct

        rvProduct?.apply {
            adapter = this@TokoNowRepurchaseViewHolder.adapter
            linearLayoutManager = createLinearLayoutManager()
            layoutManager = linearLayoutManager
            addOnScrollListener(createScrollListener())
            addItemDecoration(RepurchaseProductItemDecoration())
        }
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
        initTitle(data)
        initSubtitle(data)

        cardViewChevron?.setOnClickListener {
            goToRepurchasePage()
            listener?.onChevronClicked()
        }

        adapter.submitList(data.productList)
    }

    private fun initTitle(data: TokoNowRepurchaseUiModel) {
        tvTitle?.text = data.title
        tvTitle?.setType(Typography.DISPLAY_2)
        tvTitle?.setWeight(Typography.BOLD)
    }

    private fun initSubtitle(data: TokoNowRepurchaseUiModel) {
        binding?.textSubtitle?.text = data.subtitle

        when(data.subtitleColor) {
            NN500.name -> setSubtitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500)
            YN500.name -> setSubtitleColor(com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        }
    }

    private fun showAllView() {
        vsTitle?.show()
        rvProduct?.show()
        cardViewChevron?.show()
    }

    private fun hideAllView() {
        vsTitle?.hide()
        rvProduct?.hide()
        cardViewChevron?.hide()
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

    private fun setSubtitleColor(resId: Int) {
        val color = ContextCompat.getColor(itemView.context, resId)
        binding?.textSubtitle?.setTextColor(color)
    }

    interface TokoNowRepurchaseListener {
        fun onChevronClicked()
    }
}
