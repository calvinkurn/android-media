package com.tokopedia.productcard_compact.productcardcarousel.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.databinding.LayoutTokopedianowProductCardCarouselViewBinding
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.TokoNowProductCardCarouselAdapter
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.differ.TokoNowProductCardCarouselDiffer
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.TokoNowProductCardCarouselTypeFactoryImpl
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.productcard_compact.productcardcarousel.util.CustomProductCardCarouselLinearLayoutManager
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.TokoNowProductCardCarouselItemViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.TokoNowSeeMoreCardCarouselViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.util.ProductCardCarouselDecoration
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TokoNowProductCardCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs),
    TokoNowProductCardCarouselItemViewHolder.TokoNowCarouselProductCardItemListener,
    TokoNowSeeMoreCardCarouselViewHolder.TokoNowCarouselProductCardSeeMoreListener,
    CoroutineScope {

    private val adapter: TokoNowProductCardCarouselAdapter by lazy {
        TokoNowProductCardCarouselAdapter(
            TokoNowProductCardCarouselDiffer(),
            TokoNowProductCardCarouselTypeFactoryImpl(
                productCardCarouselItemListener = this,
                productCardCarouselSeeMoreListener = this
            )
        )
    }

    private var binding: LayoutTokopedianowProductCardCarouselViewBinding
    private var layoutManager: LinearLayoutManager = CustomProductCardCarouselLinearLayoutManager(context)
    private var listener: TokoNowProductCardCarouselListener? = null

    init {
        binding = LayoutTokopedianowProductCardCarouselViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            root.addItemDecoration(ProductCardCarouselDecoration(context))
            root.layoutManager = layoutManager
            root.adapter = adapter
        }
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductCardAddVariantClicked(
            position = position,
            product = product
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
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
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductCardClicked(
            position = position,
            product = product
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductCardImpressed(
            position = position,
            product = product
        )
    }

    override fun onProductCardSeeMoreClickListener(
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
    ) {
        listener?.onSeeMoreClicked(
            seeMoreUiModel = seeMoreUiModel
        )
    }

    fun bindItems(
        items: List<Visitable<*>>,
        seeMoreModel: TokoNowSeeMoreCardCarouselUiModel? = null
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
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int
        )
        fun onProductCardAddVariantClicked(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onSeeMoreClicked(
            seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
        )
    }


    interface TokoNowProductCardCarouseBasicListener: TokoNowProductCardCarouselListener {
        override fun onSeeMoreClicked(seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel) {}
    }
}
