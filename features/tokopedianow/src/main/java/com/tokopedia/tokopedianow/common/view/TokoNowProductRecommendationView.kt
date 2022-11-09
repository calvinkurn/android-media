package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductRecommendationViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TokoNowProductRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): BaseCustomView(context, attrs) {

    private var binding: LayoutTokopedianowProductRecommendationViewBinding

    init {
        binding = LayoutTokopedianowProductRecommendationViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setItems(
        items: List<Visitable<*>>,
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel? = null
    ) {
        binding.productCardCarousel.bindItems(
            items = items,
            seeMoreUiModel = seeMoreUiModel
        )
    }

    fun setHeader(
        header: TokoNowDynamicHeaderUiModel? = null
    ) {
        binding.header.showIfWithBlock(header != null) {
            header?.apply {
                setModel(this)
            }
        }
    }

    fun setListener(
        productCardCarouselListener: TokoNowProductCardCarouselListener? = null,
        headerCarouselListener: TokoNowDynamicHeaderListener? = null
    ) {
        binding.productCardCarousel.setListener(
            productCardCarouselListener = productCardCarouselListener,
        )
        binding.header.setListener(
            headerListener =  headerCarouselListener
        )
    }
}
