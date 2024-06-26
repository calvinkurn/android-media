package com.tokopedia.tokopedianow.home.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.adapter.TokoNowChipListAdapter
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListAdapterTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.decoration.ChipListHorizontalDecoration
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCarouselChipsViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify

class HomeProductCarouselChipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs),
    ProductCardCompactCarouselView.ProductCardCompactCarouselBasicListener,
    ChipListener {

    companion object {
        private const val FIRST_POSITION_INDEX = 0
    }

    private var channelId = ""
    private var headerName = ""
    private var chipList = emptyList<TokoNowChipUiModel>()

    private var previousState: TokoNowProductRecommendationState? = null
    private var listener: HomeProductCarouselChipsViewListener? = null

    private val chipAdapter by lazy {
        TokoNowChipListAdapter(
            TokoNowChipListAdapterTypeFactory(
                this@HomeProductCarouselChipsView
            )
        )
    }

    private val binding = LayoutTokopedianowProductCarouselChipsViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.chipsTab.apply {
            adapter = chipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ChipListHorizontalDecoration(context))
        }
    }

    fun bind(
        channelId: String,
        chipList: List<TokoNowChipUiModel> = emptyList(),
        carouselItems: List<Visitable<*>> = listOf(),
        seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel? = null,
        header: TokoNowDynamicHeaderUiModel? = null,
        state: TokoNowProductRecommendationState = TokoNowProductRecommendationState.LOADING
    ) {
        val title = header?.title.orEmpty()
        val loading = TokoNowProductRecommendationState.LOADING
        val loaded = TokoNowProductRecommendationState.LOADED

        initData(channelId, chipList, title)

        binding.header.showIfWithBlock(header != null) {
            header?.let { setModel(it) }
        }

        binding.productCardCarousel.showIfWithBlock(state == loaded) {
            bindItems(items = carouselItems, seeMoreModel = seeMoreModel)
        }
        binding.productCardCarousel.setListener(this@HomeProductCarouselChipsView)

        binding.productCardShimmering.root.showWithCondition(state == loading)

        submitChipList(chipList)
        previousState = state
    }

    fun bind(
        carouselItemList: List<Visitable<*>>,
        chipList: List<TokoNowChipUiModel>,
        state: TokoNowProductRecommendationState
    ) {
        val loading = TokoNowProductRecommendationState.LOADING
        val loaded = TokoNowProductRecommendationState.LOADED

        binding.productCardCarousel.bindItems(items = carouselItemList)

        if (previousState == TokoNowProductRecommendationState.LOADING) {
            binding.productCardCarousel.scrollToPosition(FIRST_POSITION_INDEX)
        }

        binding.productCardCarousel.showWithCondition(state == loaded)
        binding.productCardShimmering.root.showWithCondition(state == loading)

        submitChipList(chipList)
        previousState = state
    }

    fun setListener(listener: HomeProductCarouselChipsViewListener?) {
        this.listener = listener
    }

    private fun initData(
        channelId: String,
        chipList: List<TokoNowChipUiModel>,
        headerName: String
    ) {
        this.channelId = channelId
        this.chipList = chipList
        this.headerName = headerName
    }

    private fun submitChipList(chipList: List<TokoNowChipUiModel>) {
        chipAdapter.submitList(chipList)
    }

    private fun getSelectedChipName() = chipList.first { it.selected }.text

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onClickProductCard(position, channelId, getSelectedChipName(), product)
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductCardImpressed(position, channelId, getSelectedChipName(), product)
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductCardQuantityChanged(position, product, quantity)
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onClickVariantAddToCart(product.getProductId(), product.shopId)
    }

    override fun onProductCardAddToCartBlocked() {
        listener?.onAddToCartBlocked()
    }

    override fun onClickChipItem(chip: TokoNowChipUiModel) {
        val selectedChipIndex = chipList.indexOf(chip)

        chipList.forEachIndexed { index, _ ->
            val type = if (index == selectedChipIndex) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }

            val viewHolder = binding.chipsTab.findViewHolderForAdapterPosition(index)
            (viewHolder as? TokoNowChipViewHolder)?.setChipType(type)
        }

        listener?.onClickChipItem(channelId, headerName, chip)
    }

    interface HomeProductCarouselChipsViewListener {
        fun onProductCardQuantityChanged(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int
        )
        fun onClickVariantAddToCart(
            productId: String,
            shopId: String
        )
        fun onClickProductCard(
            position: Int,
            channelId: String,
            chipName: String,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            channelId: String,
            chipName: String,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onClickChipItem(channelId: String, headerName: String, chip: TokoNowChipUiModel)
        fun onWidgetImpressed()
        fun onAddToCartBlocked()
    }
}
