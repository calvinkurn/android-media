package com.tokopedia.search.result.product.inspirationcarousel

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
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardModel
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardModel
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.home_component_header.view.HomeComponentHeaderListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup.*
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.utils.SEARCH_PAGE_RESULT_MAX_LINE
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.search.utils.convertToChannelHeader
import com.tokopedia.search.utils.getHorizontalShadowOffset
import com.tokopedia.search.utils.getVerticalShadowOffset
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.tokopedia.carouselproductcard.R as carouselproductcardR
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup as LabelGroupReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class InspirationCarouselViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationCarouselListener,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val reimagineSearch2Component: Search2Component,
) : AbstractViewHolder<InspirationCarouselDataView>(itemView), CoroutineScope {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel

        private const val LEFT_OFFSET_NOT_FIRST_DIVISOR = 4
        private const val RIGHT_OFFSET_NOT_LAST_DIVISOR = 4
    }

    private var binding: SearchInspirationCarouselBinding? by viewBinding()

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    private val isReimagine: Boolean
        get() = reimagineSearch2Component.isReimagineCarousel()

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
        bindHeader(element)
        if (isChipsLayout(element))
            bindChipsCarousel(element)
        else
            bindContent(element)
    }

    private fun bindHeader(element: InspirationCarouselDataView) {
        if (isReimagine) {
            setHeaderRevamp(element)
        } else {
            setOldHeader(element)
        }
    }

    private fun setHeaderRevamp(element: InspirationCarouselDataView) {
        showRevampHeader()
        hideOldHeader()
        hideSeparator()
        bindHeaderRevamp(element)
    }

    private fun setOldHeader(element: InspirationCarouselDataView) {
        showOldHeader()
        hideRevampHeader()
        showSeparator()
        bindTitle(element)
    }

    private fun isChipsLayout(element: InspirationCarouselDataView): Boolean {
        return element.layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS
    }

    private fun bindTitle(element: InspirationCarouselDataView) {
        binding?.inspirationCarouselTitle?.text = element.title
    }

    private fun showRevampHeader() {
        binding?.inspirationCarouselHeaderView?.visible()
    }

    private fun hideRevampHeader() {
        binding?.inspirationCarouselHeaderView?.gone()
    }

    private fun showSeparator(){
        binding?.viewSeparatorTop?.visible()
        binding?.viewSeparatorBottom?.visible()
    }

    private fun hideSeparator(){
        binding?.viewSeparatorTop?.hide()
        binding?.viewSeparatorBottom?.hide()
    }

    private fun showOldHeader() {
        binding?.inspirationCarouselTitle?.visible()
        binding?.inspirationCarouselSeeAllButton?.visible()
    }

    private fun hideOldHeader() {
        binding?.inspirationCarouselTitle?.gone()
        binding?.inspirationCarouselSeeAllButton?.gone()
    }

    private fun bindHeaderRevamp(element: InspirationCarouselDataView) {
        val headerView = binding?.inspirationCarouselHeaderView ?: return
        val option = element.options.getOrNull(0) ?: return
        headerView.bind(
            channelHeader = element.convertToChannelHeader(),
            listener = object : HomeComponentHeaderListener {
                override fun onSeeAllClick(link: String) {
                    inspirationCarouselListener.onInspirationCarouselSeeAllClicked(option)
                }
            },
            maxLines = SEARCH_PAGE_RESULT_MAX_LINE
        )
    }

    private fun bindChipsCarousel(element: InspirationCarouselDataView) {
        configureInspirationCarouselChipsVisibility()

        bindCarouselChipsList(element)
        bindChipsCarouselProducts(element)
    }

    private fun configureInspirationCarouselChipsVisibility() {
        binding?.let {
            it.inspirationCarouselSeeAllButton.gone()
            it.inspirationCarouselOptionList.gone()
        }
    }

    private fun bindCarouselChipsList(element: InspirationCarouselDataView) {
        if (element.options.size == 1) {
            binding?.inspirationCarouselChipsList?.gone()
            return
        }

        binding?.inspirationCarouselChipsList?.let {
            it.visible()
            setMarginChipsList(it)
            it.layoutManager = createLayoutManager()
            it.adapter = InspirationCarouselChipsAdapter(
                adapterPosition, element, inspirationCarouselListener
            )
            it.addItemDecorationIfNotExists(
                InspirationCarouselChipsListItemDecoration(
                    getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
                    getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
                )
            )

            val scrollPosition = element.options.indexOfFirst { option -> option.isChipsActive }
            it.scrollToPosition(scrollPosition)
        }
    }

    private fun setMarginChipsList(inspirationCarouselChipsList: RecyclerView) {
        if (isReimagine) {
            val resource = inspirationCarouselChipsList.context.resources
            val topMargin = resource.getDimensionPixelSize(R.dimen.search_inspiration_chips_margin_top_revamp)
            inspirationCarouselChipsList.setMargin(0, topMargin, 0, 0)
        } else {
            val resource = inspirationCarouselChipsList.context.resources
            val topMargin = resource.getDimensionPixelSize(R.dimen.search_inspiration_chips_margin_top_control)
            inspirationCarouselChipsList.setMargin(0, topMargin, 0, 0)
        }
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
        binding?.inspirationCarouselChipsShimmeringView?.root?.gone()

        if (isReimagine)
            bindInspirationCarouselChipsReimagine(activeOption)
        else
            bindInspirationCarouselChipsControl(activeOption)
    }

    private fun bindInspirationCarouselChipsReimagine(
        activeOption: InspirationCarouselDataView.Option
    ) {
        binding?.run {
            inspirationCarouselChipsContent.gone()
            inspirationCarouselChipsContentReimagine.visible()

            val carouselProductCardList = productCardReimagineList(activeOption)
            val viewAllCardModel = viewAllCardReimagine(activeOption)

            inspirationCarouselChipsContentReimagine.bind(CarouselProductCardModel(
                itemList = carouselProductCardList + listOfNotNull(viewAllCardModel),
                recycledViewPool = recycledViewPool,
            ))
        }
    }

    private fun productCardReimagineList(activeOption: InspirationCarouselDataView.Option) =
        activeOption.product.map { product ->
            val shopBadge = product.badgeItemDataViewList.firstOrNull()
            CarouselProductCardGridModel(
                productCardModel = ProductCardModelReimagine(
                    imageUrl = product.imgUrl,
                    isAds = product.isOrganicAds,
                    name = product.name,
                    price = product.priceStr,
                    rating = product.ratingAverage,
                    slashedPrice = product.originalPrice,
                    discountPercentage = product.discountPercentage,
                    labelGroupList = product.labelGroupDataList.map { labelGroup ->
                        LabelGroupReimagine(
                            title = labelGroup.title,
                            position = labelGroup.position,
                            type = labelGroup.type,
                            imageUrl = labelGroup.imageUrl,
                            styles = labelGroup.style.map { item ->
                                Style(item.key, item.value)
                            }
                        )
                    },
                    shopBadge = ProductCardModelReimagine.ShopBadge(
                        title = shopBadge?.title ?: "",
                        imageUrl = shopBadge?.imageUrl ?: "",
                    ),
                    hasMultilineName = reimagineSearch2Component.hasMultilineProductName(),
                ),
                impressHolder = { product },
                onImpressed = {
                    inspirationCarouselListener.onImpressedInspirationCarouselChipsProduct(product)
                },
                onClick = {
                    inspirationCarouselListener.onInspirationCarouselChipsProductClicked(product)
                }
            )
        }

    private fun viewAllCardReimagine(activeOption: InspirationCarouselDataView.Option) =
        if (activeOption.applink.isNotEmpty())
            CarouselProductCardViewAllCardModel(
                ctaText = getString(carouselproductcardR.string.see_more_card_see_all),
                onClick = {
                    inspirationCarouselListener.onInspirationCarouselChipsSeeAllClicked(
                        activeOption)
                },
            )
        else
            null

    private fun bindInspirationCarouselChipsControl(activeOption: InspirationCarouselDataView.Option) {
        binding?.let {
            it.inspirationCarouselChipsContentReimagine.gone()
            it.inspirationCarouselChipsContent.visible()

            val activeOptionsProducts = activeOption.product
            val chipsProductCardModels = activeOptionsProducts.map { product ->
                product.toProductCardModel()
            }

            it.inspirationCarouselChipsContent.bindCarouselProductCardViewGrid(
                productCardModelList = chipsProductCardModels,
                recyclerViewPool = recycledViewPool,
                showSeeMoreCard = activeOption.applink.isNotEmpty(),
                carouselProductCardOnItemClickListener = object :
                    CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        val product =
                            activeOptionsProducts.getOrNull(carouselProductCardPosition) ?: return
                        inspirationCarouselListener.onInspirationCarouselChipsProductClicked(product)
                    }
                },
                carouselProductCardOnItemImpressedListener = object :
                    CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        val product =
                            activeOptionsProducts.getOrNull(carouselProductCardPosition) ?: return

                        inspirationCarouselListener.onImpressedInspirationCarouselChipsProduct(
                            product
                        )
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return if (carouselProductCardPosition < activeOptionsProducts.size)
                            activeOptionsProducts[carouselProductCardPosition]
                        else null
                    }
                },
                carouselSeeMoreClickListener = object :
                    CarouselProductCardListener.OnSeeMoreClickListener {
                    override fun onSeeMoreClick() {
                        inspirationCarouselListener.onInspirationCarouselChipsSeeAllClicked(
                            activeOption
                        )
                    }
                }
            )
        }
    }

    private fun bindInspirationCarouselChipProductsLoading() {
        binding?.let {
            it.inspirationCarouselChipsShimmeringView.root.visible()
            it.inspirationCarouselChipsContent.gone()
            it.inspirationCarouselChipsContentReimagine.gone()
        }
    }

    private fun bindContent(element: InspirationCarouselDataView) {
        configureInspirationCarouselNonChipsVisibility()

        binding?.inspirationCarouselOptionList?.let {
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
        binding?.let {
            it.inspirationCarouselSeeAllButton.gone()
            it.inspirationCarouselOptionList.visible()
            it.inspirationCarouselChipsShimmeringView.root.gone()
            it.inspirationCarouselChipsList.gone()
            it.inspirationCarouselChipsContent.gone()
            it.inspirationCarouselChipsContentReimagine.gone()
        }
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

    private fun createGridProductList(option: InspirationCarouselDataView.Option): List<Visitable<InspirationCarouselOptionTypeFactory>> {
        val list = mutableListOf<Visitable<InspirationCarouselOptionTypeFactory>>()
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
        val productCardHeight =
            if (isReimagine) RecyclerView.LayoutParams.WRAP_CONTENT
            else getProductCardMaxHeight(list)

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
            ) },
            shopLocation = shopLocation,
            shopBadgeList = badgeItemDataViewList.toProductCardModelShopBadges(),
            isTopAds = isOrganicAds,
            cardInteraction = true,
        )
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private suspend fun getProductCardMaxHeight(list: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(productcardR.dimen.carousel_product_card_grid_width)
        return list.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun configureSeeAllButton(option: InspirationCarouselDataView.Option) {
        showSeeAllButton()
        bindSeeAllButtonListener(option)
    }

    private fun showSeeAllButton() {
        binding?.inspirationCarouselSeeAllButton?.visibility = View.VISIBLE
    }

    private fun bindSeeAllButtonListener(option: InspirationCarouselDataView.Option) {
        binding?.inspirationCarouselSeeAllButton?.setOnClickListener {
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
        list: List<Visitable<InspirationCarouselOptionTypeFactory>>
    ): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>> {
        val typeFactory = InspirationCarouselOptionAdapterTypeFactory(
            inspirationCarouselListener,
            reimagineSearch2Component,
        )
        val inspirationCarouselProductAdapter = InspirationCarouselOptionAdapter(typeFactory)
        inspirationCarouselProductAdapter.clearData()
        inspirationCarouselProductAdapter.addAll(list)

        return inspirationCarouselProductAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return InspirationCarouselItemDecoration(
            getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
            getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
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

        private fun getLeftOffsetNotFirstItem(): Int { return (left / LEFT_OFFSET_NOT_FIRST_DIVISOR) - (cardViewHorizontalOffset / 2) }

        private fun getRightOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) getRightOffsetLastItem()
            else getRightOffsetNotLastItem()
        }

        private fun getRightOffsetLastItem(): Int { return right - (cardViewHorizontalOffset / 2) }

        private fun getRightOffsetNotLastItem(): Int { return (right / RIGHT_OFFSET_NOT_LAST_DIVISOR) - (cardViewHorizontalOffset / 2) }
    }
}
