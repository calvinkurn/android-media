package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.*
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselChipsAdapter
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapter
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapterTypeFactory
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.InspirationCarouselChipsListItemDecoration
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.search.utils.getHorizontalShadowOffset
import com.tokopedia.search.utils.getVerticalShadowOffset
import kotlinx.android.synthetic.main.search_inspiration_carousel.view.*
import kotlinx.coroutines.*

class InspirationCarouselViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView>(itemView), CoroutineScope {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    override fun onViewRecycled() {
        cancelJobs()
        super.onViewRecycled()
    }

    private fun cancelJobs() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }

    override fun bind(element: InspirationCarouselDataView) {
        bindTitle(element)

        if (element.layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS)
            bindChipsCarousel(element)
        else
            bindContent(element)
    }

    private fun bindTitle(element: InspirationCarouselDataView) {
        itemView.inspirationCarousel?.inspirationCarouselTitle?.text = element.title
    }

    private fun bindChipsCarousel(element: InspirationCarouselDataView) {
        configureInspirationCarouselChipsVisibility()

        bindCarouselChipsList(element)
        bindChipsCarouselProducts(element)
    }

    private fun configureInspirationCarouselChipsVisibility() {
        itemView.inspirationCarousel?.inspirationCarouselSeeAllButton?.gone()
        itemView.inspirationCarousel?.inspirationCarouselOptionList?.gone()
    }

    private fun bindCarouselChipsList(element: InspirationCarouselDataView) {
        if (element.options.size == 1) {
            itemView.inspirationCarousel?.inspirationCarouselChipsList?.gone()
            return
        }

        itemView.inspirationCarousel?.inspirationCarouselChipsList?.visible()

        itemView.inspirationCarousel?.inspirationCarouselChipsList?.layoutManager = createLayoutManager()
        itemView.inspirationCarousel?.inspirationCarouselChipsList?.adapter = InspirationCarouselChipsAdapter(
                adapterPosition, element, inspirationCarouselListener
        )
        itemView.inspirationCarousel?.inspirationCarouselChipsList?.addItemDecorationIfNotExists(
                InspirationCarouselChipsListItemDecoration(
                        getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                        getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                )
        )

        val scrollPosition = element.options.indexOfFirst { it.isChipsActive }
        itemView.inspirationCarousel?.inspirationCarouselChipsList?.scrollToPosition(scrollPosition)
    }

    private fun bindChipsCarouselProducts(element: InspirationCarouselDataView) {
        val activeOption = element.options.find { it.isChipsActive } ?: return

        if (activeOption.hasProducts())
            bindInspirationCarouselChipProducts(activeOption)
        else
            bindInspirationCarouselChipProductsLoading()
    }

    private fun bindInspirationCarouselChipProducts(
            activeOption: InspirationCarouselDataView.Option
    ) {
        itemView.inspirationCarousel?.inspirationCarouselChipsShimmeringView?.gone()
        itemView.inspirationCarousel?.inspirationCarouselChipsContent?.visible()

        val activeOptionsProducts = activeOption.product
        val chipsProductCardModels = activeOptionsProducts.map { it.toProductCardModel() }

        itemView.inspirationCarousel?.inspirationCarouselChipsContent?.bindCarouselProductCardViewGrid(
                productCardModelList = chipsProductCardModels,
                showSeeMoreCard = activeOption.applink.isNotEmpty(),
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = activeOptionsProducts.getOrNull(carouselProductCardPosition) ?: return
                        inspirationCarouselListener.onInspirationCarouselChipsProductClicked(product)
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = activeOptionsProducts.getOrNull(carouselProductCardPosition) ?: return

                        inspirationCarouselListener.onImpressedInspirationCarouselChipsProduct(product)
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return if (carouselProductCardPosition < activeOptionsProducts.size)
                            activeOptionsProducts[carouselProductCardPosition]
                        else null
                    }
                },
                carouselSeeMoreClickListener = object : CarouselProductCardListener.OnSeeMoreClickListener {
                    override fun onSeeMoreClick() {
                        inspirationCarouselListener.onInspirationCarouselChipsSeeAllClicked(activeOption)
                    }
                }
        )
    }

    private fun bindInspirationCarouselChipProductsLoading() {
        itemView.inspirationCarousel?.inspirationCarouselChipsShimmeringView?.visible()
        itemView.inspirationCarousel?.inspirationCarouselChipsContent?.gone()
    }

    private fun bindContent(element: InspirationCarouselDataView) {
        configureInspirationCarouselNonChipsVisibility()

        itemView.inspirationCarousel?.inspirationCarouselOptionList?.let {
            if (it.itemDecorationCount == 0) it.addItemDecoration(createItemDecoration())

            if (element.layout == LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                val option = element.options.getOrNull(0) ?: return
                val productList = option.product.map{ product -> product.toProductCardModel() }
                it.initRecyclerViewForGrid(option, productList)
                configureSeeAllButton(option)
            } else {
                it.setDefaultHeightInspirationCarouselOptionList()
                it.layoutManager = createLayoutManager()
                it.adapter = createAdapter(element.options)
            }
        }
    }

    private fun configureInspirationCarouselNonChipsVisibility() {
        itemView.inspirationCarousel?.inspirationCarouselSeeAllButton?.gone()
        itemView.inspirationCarousel?.inspirationCarouselOptionList?.visible()
        itemView.inspirationCarousel?.inspirationCarouselChipsShimmeringView?.gone()
        itemView.inspirationCarousel?.inspirationCarouselChipsList?.gone()
        itemView.inspirationCarousel?.inspirationCarouselChipsContent?.gone()
    }

    private fun RecyclerView.initRecyclerViewForGrid(option: InspirationCarouselDataView.Option, productList: List<ProductCardModel>) {
        launch {
            try {
                layoutManager = createLayoutManager()
                adapter = createAdapter(createGridProductList(option))
                setHeightBasedOnProductCardMaxHeight(productList)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun createGridProductList(option: InspirationCarouselDataView.Option): List<Visitable<*>> {
        val list = mutableListOf<Visitable<*>>()
        if(option.shouldAddBannerCard()) list.add(createBannerOption(option))
        list.addAll(option.product)
        return list
    }

    private fun createBannerOption(option: InspirationCarouselDataView.Option): InspirationCarouselDataView.Option {
        return InspirationCarouselDataView.Option(
                title = option.title,
                layout = LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER,
                bannerImageUrl = option.bannerImageUrl,
                bannerLinkUrl = option.bannerLinkUrl,
                bannerApplinkUrl = option.bannerApplinkUrl,
                inspirationCarouselType = option.inspirationCarouselType,
                position = option.position,
                carouselTitle = option.carouselTitle
        )
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            list: List<ProductCardModel>
    ) {
        val productCardHeight = getProductCardMaxHeight(list)
        val carouselLayoutParams = layoutParams
        carouselLayoutParams?.height = productCardHeight
        layoutParams = carouselLayoutParams
    }

    private fun InspirationCarouselDataView.Option.Product.toProductCardModel(): ProductCardModel {
        return ProductCardModel(
                productImageUrl = imgUrl,
                productName = name,
                formattedPrice = priceStr,
                countSoldRating = ratingAverage,
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                labelGroupList = labelGroupDataList.map { ProductCardModel.LabelGroup(
                        title = it.title,
                        position = it.position,
                        type = it.type,
                        imageUrl = it.imageUrl,
                ) }
        )
    }

    private suspend fun getProductCardMaxHeight(list: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.carousel_product_card_grid_width)
        return list.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun configureSeeAllButton(option: InspirationCarouselDataView.Option) {
        showSeeAllButton()
        bindSeeAllButtonListener(option)
    }

    private fun showSeeAllButton() {
        itemView.inspirationCarouselSeeAllButton?.visibility = View.VISIBLE
    }

    private fun bindSeeAllButtonListener(option: InspirationCarouselDataView.Option) {
        itemView.inspirationCarouselSeeAllButton?.setOnClickListener {
            inspirationCarouselListener.onInspirationCarouselSeeAllClicked(option)
        }
    }

    private fun RecyclerView.setDefaultHeightInspirationCarouselOptionList() {
        val carouselLayoutParams = layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        layoutParams = carouselLayoutParams
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
            list: List<Visitable<*>>
    ): RecyclerView.Adapter<AbstractViewHolder<*>> {
        val typeFactory = InspirationCarouselOptionAdapterTypeFactory(inspirationCarouselListener)
        val inspirationCarouselProductAdapter = InspirationCarouselOptionAdapter(typeFactory)
        inspirationCarouselProductAdapter.clearData()
        inspirationCarouselProductAdapter.addAll(list)

        return inspirationCarouselProductAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return InspirationCarouselItemDecoration(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
        )
    }

    private fun getDimensionPixelSize(@DimenRes resId: Int): Int {
        return itemView.context?.resources?.getDimensionPixelSize(resId) ?: 0
    }

    private class InspirationCarouselItemDecoration(
            private val left: Int,
            private val right: Int,
    ): RecyclerView.ItemDecoration() {

        private var cardViewHorizontalOffset = 0
        private var cardViewVerticalOffset = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (view is CardView) setCardViewOffsets(view)
            else if (view is ProductCardGridView) setProductCardViewOffsets(view)

            setOutRectOffSetForCardView(outRect, view, parent)
        }

        private fun setCardViewOffsets(cardView: CardView) {
            cardViewHorizontalOffset = cardView.getHorizontalShadowOffset()
            cardViewVerticalOffset = cardView.getVerticalShadowOffset()
        }

        private fun setProductCardViewOffsets(view: ProductCardGridView) {
            cardViewHorizontalOffset = view.getHorizontalShadowOffset()
            cardViewVerticalOffset = view.getVerticalShadowOffset()
        }

        private fun setOutRectOffSetForCardView(outRect: Rect, view: View, parent: RecyclerView) {
            outRect.left = getLeftOffset(view, parent)
            outRect.right = getRightOffset(view, parent)
        }

        private fun getLeftOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == 0) getLeftOffsetFirstItem()
            else getLeftOffsetNotFirstItem()
        }

        private fun getLeftOffsetFirstItem(): Int { return left - (cardViewHorizontalOffset / 2) }

        private fun getLeftOffsetNotFirstItem(): Int { return (left / 4) - (cardViewHorizontalOffset / 2) }

        private fun getRightOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) getRightOffsetLastItem()
            else getRightOffsetNotLastItem()
        }

        private fun getRightOffsetLastItem(): Int { return right - (cardViewHorizontalOffset / 2) }

        private fun getRightOffsetNotLastItem(): Int { return (right / 4) - (cardViewHorizontalOffset / 2) }
    }
}