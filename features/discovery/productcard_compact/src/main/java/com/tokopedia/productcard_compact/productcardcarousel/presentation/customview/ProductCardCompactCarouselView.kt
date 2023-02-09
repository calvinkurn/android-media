package com.tokopedia.productcard_compact.productcardcarousel.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.databinding.LayoutTokopedianowProductCardCarouselViewBinding
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.ProductCardCompactCarouselAdapter
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.differ.ProductCardCompactCarouselDiffer
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactoryImpl
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.productcard_compact.productcardcarousel.helper.ProductCardCompactCarouselLinearLayoutManager
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselItemViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselSeeMoreViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.helper.ProductCardCompactCarouselDecoration
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ProductCardCompactCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs),
    ProductCardCompactCarouselItemViewHolder.TokoNowCarouselProductCardItemListener,
    ProductCardCompactCarouselSeeMoreViewHolder.TokoNowCarouselProductCardSeeMoreListener,
    CoroutineScope {

    private val adapter: ProductCardCompactCarouselAdapter by lazy {
        ProductCardCompactCarouselAdapter(
            ProductCardCompactCarouselDiffer(),
            ProductCardCompactCarouselTypeFactoryImpl(
                productCardCarouselItemListener = this,
                productCardCarouselSeeMoreListener = this
            )
        )
    }

    private var binding: LayoutTokopedianowProductCardCarouselViewBinding
    private var layoutManager: LinearLayoutManager = ProductCardCompactCarouselLinearLayoutManager(context)
    private var listener: TokoNowProductCardCarouselListener? = null

    init {
        binding = LayoutTokopedianowProductCardCarouselViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            root.addItemDecoration(ProductCardCompactCarouselDecoration(context))
            root.layoutManager = layoutManager
            root.adapter = adapter
        }
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductCardAddVariantClicked(
            position = position,
            product = product
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductCardQuantityChanged(
            position = position,
            product = product,
            quantity = quantity
        )
    }

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductCardClicked(
            position = position,
            product = product
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductCardImpressed(
            position = position,
            product = product
        )
    }

    override fun onProductCardSeeMoreClickListener(
        seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
    ) {
        listener?.onSeeMoreClicked(
            seeMoreUiModel = seeMoreUiModel
        )
    }

    fun bindItems(
        items: List<Visitable<*>>,
        seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel? = null
    ) {
        if (seeMoreModel != null && seeMoreModel.appLink.isNotBlank()) {
            val newItems = items.toMutableList()
            newItems.add(seeMoreModel)
            adapter.submitList(newItems)
        } else {
            adapter.submitList(items)
        }
    }

    fun updateItems(
        items: List<Visitable<*>>
    ) {
        adapter.submitList(ArrayList(items))
    }

    fun setListener(
        productCardCarouselListener: TokoNowProductCardCarouselListener? = null
    ) {
        listener = productCardCarouselListener
    }

    fun scrollToPosition(position: Int) {
        binding.root.post {
            layoutManager.scrollToPosition(position)
        }
    }

    interface TokoNowProductCardCarouselListener {
        fun onProductCardClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int
        )
        fun onProductCardAddVariantClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onSeeMoreClicked(
            seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
        )
    }

    interface TokoNowProductCardCarouseBasicListener: TokoNowProductCardCarouselListener {
        override fun onSeeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {}
    }
}
