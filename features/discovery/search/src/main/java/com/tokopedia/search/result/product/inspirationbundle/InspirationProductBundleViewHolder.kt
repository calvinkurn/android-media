package com.tokopedia.search.result.product.inspirationbundle

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselBundleBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class InspirationProductBundleViewHolder(
    itemView: View,
    private val listener: InspirationBundleListener,
    private val isReimagine: Boolean
) : AbstractViewHolder<InspirationProductBundleDataView>(itemView),
    ProductBundleListener {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel_bundle

        private const val DEFAULT_OFFSET = 0
    }

    private var binding: SearchInspirationCarouselBundleBinding? by viewBinding()
    private var productBundle: InspirationProductBundleDataView? = null

    override fun bind(element: InspirationProductBundleDataView) {
        setPadding()
        productBundle = element
        bindTitle(element)
        bindContent(element)
    }

    override fun onViewRecycled() {
        productBundle = null
        binding?.rvProductBundle?.adapter = null
        super.onViewRecycled()
    }

    private fun bindTitle(element: InspirationProductBundleDataView) {
        binding?.tgProductBundleTitle?.text = element.title
    }

    private fun bindContent(element: InspirationProductBundleDataView) {
        setMarginProductCardList()
        binding?.rvProductBundle?.let {
            if (it.itemDecorationCount == 0) it.addItemDecoration(createItemDecoration())

            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.asBundleUiModel())
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
        list: List<BundleUiModel>
    ): ProductBundleWidgetAdapter {
        val inspirationCarouselProductAdapter = ProductBundleWidgetAdapter()
        inspirationCarouselProductAdapter.updateDataSet(list)
        inspirationCarouselProductAdapter.setListener(this)

        return inspirationCarouselProductAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return InspirationProductBundleItemDecoration(
            getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
            getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
        )
    }

    private fun getDimensionPixelSize(@DimenRes resId: Int): Int {
        return itemView.context?.resources?.getDimensionPixelSize(resId) ?: 0
    }

    override fun onBundleProductClicked(
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        productItemPosition: Int
    ) {
        val productBundle = productBundle ?: return
        val bundle = productBundle.bundleList.getSelectedBundle(selectedMultipleBundle) ?: return
        listener.onBundleProductClicked(bundle, selectedProduct)
    }

    private fun List<InspirationProductBundleDataView.BundleDataView>.getSelectedBundle(
        selectedBundleDetail: BundleDetailUiModel
    ): InspirationProductBundleDataView.BundleDataView? {
        forEach { bundle ->
            val bundleDetail = bundle.bundle.bundleDetails.firstOrNull { bundleDetail ->
                bundleDetail == selectedBundleDetail
            }
            bundleDetail?.let {
                return bundle
            }
        }
        return null
    }

    override fun addMultipleBundleToCart(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    ) {
        val productBundle = productBundle ?: return
        val bundle = productBundle.bundleList.getSelectedBundle(selectedMultipleBundle) ?: return
        listener.onSeeBundleClicked(
            bundle,
            productDetails,
        )
    }

    override fun addSingleBundleToCart(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
    ) {
        val productBundle = productBundle ?: return
        val bundle = productBundle.bundleList.getSelectedBundle(selectedBundle) ?: return
        listener.onSeeBundleClicked(
            bundle,
            listOf(bundleProducts),
        )
    }

    override fun onTrackSingleVariantChange(
        selectedProduct: BundleProductUiModel,
        selectedSingleBundle: BundleDetailUiModel,
        bundleName: String
    ) {

    }

    override fun impressionProductBundleSingle(
        selectedSingleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        val productBundle = productBundle ?: return
        val selectedBundle = productBundle.bundleList[bundlePosition]
        listener.onBundleImpressed(selectedBundle)
        listener.onBundleProductImpressed(
            selectedBundle,
            selectedProduct,
        )
    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        val productBundle = productBundle ?: return
        listener.onBundleImpressed(productBundle.bundleList[bundlePosition])
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    ) {
        val productBundle = productBundle ?: return
        val selectedBundle = productBundle.bundleList.getSelectedBundle(selectedMultipleBundle) ?: return
        listener.onBundleProductImpressed(
            selectedBundle,
            selectedProduct,
        )
    }

    private class InspirationProductBundleItemDecoration(
        private val left: Int,
        private val right: Int,
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            setOutRectOffSetForCardView(outRect, view, parent)
        }

        private fun setOutRectOffSetForCardView(outRect: Rect, view: View, parent: RecyclerView) {
            outRect.left = getLeftOffset(view, parent)
            outRect.right = getRightOffset(view, parent)
        }

        private fun getLeftOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == 0) getLeftOffsetFirstItem()
            else getLeftOffsetNotFirstItem()
        }

        private fun getLeftOffsetFirstItem(): Int {
            return left
        }

        private fun getLeftOffsetNotFirstItem(): Int {
            return DEFAULT_OFFSET
        }

        private fun getRightOffset(view: View, parent: RecyclerView): Int {
            val itemCount = parent.adapter?.itemCount ?: 0
            return if (parent.getChildAdapterPosition(view) == itemCount - 1) getRightOffsetLastItem()
            else getRightOffsetNotLastItem()
        }

        private fun getRightOffsetLastItem(): Int {
            return right
        }

        private fun getRightOffsetNotLastItem(): Int {
            return DEFAULT_OFFSET
        }
    }

    private fun setPadding() {
        if (isReimagine) {
            paddingContainerReimagineVersion()
        } else {
            paddingContainerControlVersion()
        }
    }

    private fun paddingContainerReimagineVersion() {
        val suggestionTextView = binding?.constraintContainerCarouselBundle ?: return
        val contextResource = suggestionTextView.context.resources
        val paddingTop = contextResource.getDimensionPixelSize(R.dimen.search_bundle_carousel_padding_top)
        suggestionTextView.setPadding(0, paddingTop, 0, 0)
    }

    private fun paddingContainerControlVersion() {
        val suggestionTextView = binding?.constraintContainerCarouselBundle ?: return
        suggestionTextView.setPadding(0, 0, 0, 0)
    }

    private fun setMarginProductCardList() {
        if (isReimagine) {
            setMarginProductCardListReimagine()
        } else {
            setMarginProductCardListControl()
        }
    }

    private fun setMarginProductCardListReimagine() {
        val tgProductBundleTitle = binding?.rvProductBundle ?: return
        val contextResource = tgProductBundleTitle.context.resources
        val marginTop = contextResource.getDimensionPixelSize(R.dimen.search_bundle_carousel_bundle_list_margin_top_reimagine)
        tgProductBundleTitle.setMargin(0, marginTop,0, 0)
    }

    private fun setMarginProductCardListControl() {
        val tgProductBundleTitle = binding?.rvProductBundle ?: return
        val contextResource = tgProductBundleTitle.context.resources
        val marginTop = contextResource.getDimensionPixelSize(R.dimen.search_bundle_carousel_bundle_list_margin_control)
        tgProductBundleTitle.setMargin(0, marginTop,0,0)
    }
}
