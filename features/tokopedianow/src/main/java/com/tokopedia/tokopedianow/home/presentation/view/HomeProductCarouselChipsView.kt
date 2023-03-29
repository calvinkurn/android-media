package com.tokopedia.tokopedianow.home.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.common.adapter.TokoNowChipListAdapter
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListAdapterTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.decoration.ChipListHorizontalDecoration
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCarouselChipsViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class HomeProductCarouselChipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    private var listener: HomeProductCarouselChipsListener? = null
    private var chipListener: ChipListener? = null

    private val chipAdapter by lazy {
        TokoNowChipListAdapter(TokoNowChipListAdapterTypeFactory(chipListener))
    }

    private val binding = LayoutTokopedianowProductCarouselChipsViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        chipList: List<TokoNowChipUiModel> = emptyList(),
        carouselItems: List<Visitable<*>> = listOf(),
        seeMoreModel: TokoNowSeeMoreCardCarouselUiModel? = null,
        header: TokoNowDynamicHeaderUiModel? = null,
        state: TokoNowProductRecommendationState = TokoNowProductRecommendationState.LOADING
    ) {
        val loaded = TokoNowProductRecommendationState.LOADED
        val loading = TokoNowProductRecommendationState.LOADING

        binding.productCardShimmering.root.showWithCondition(state == loading)
        binding.productCardCarousel.showIfWithBlock(state == loaded) {
            bindItems(items = carouselItems, seeMoreModel = seeMoreModel)
        }
        binding.header.showIfWithBlock(header != null) {
            header?.let { setModel(it) }
        }

        renderChipList(chipList)
    }

    fun setListener(
        productCardCarouselListener: TokoNowProductCardCarouselListener? = null,
        headerCarouselListener: TokoNowDynamicHeaderListener? = null,
        chipListener: ChipListener? = null
    ) {
        binding.productCardCarousel.setListener(productCardCarouselListener,)
        binding.header.setListener(headerCarouselListener)
        this.chipListener = chipListener
    }

    private fun renderChipList(chipList: List<TokoNowChipUiModel>) {
        binding.chipsTab.apply {
            adapter = chipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ChipListHorizontalDecoration(context))
        }

        chipAdapter.submitList(chipList)
    }

    interface HomeProductCarouselChipsListener {
        fun onClickVariantAddToCart(
            productId: String,
            shopId: String
        )
        fun onClickProductCard(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        )
        fun onProductCardImpressed(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        )
        fun onSeeMoreClicked(
            seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
        )
        fun onSeeAllClicked(appLink: String)
    }
}
