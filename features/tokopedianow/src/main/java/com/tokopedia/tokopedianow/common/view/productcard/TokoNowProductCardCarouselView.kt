package com.tokopedia.tokopedianow.common.view.productcard

import android.content.Context
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.TokoNowProductCardCarouselAdapter
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowProductCardCarouselDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductCardCarouselTypeFactoryImpl
import com.tokopedia.tokopedianow.common.decoration.ProductCardCarouselDecoration
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardCarouselItemViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowSeeMoreCardCarouselViewHolder
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCardCarouselViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TokoNowProductCardCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): BaseCustomView(context, attrs),
    TokoNowProductCardCarouselItemViewHolder.TokoNowCarouselProductCardItemListener,
    TokoNowSeeMoreCardCarouselViewHolder.TokoNowCarouselProductCardSeeMoreListener,
    CoroutineScope
{

    private val adapter: TokoNowProductCardCarouselAdapter by lazy {
        TokoNowProductCardCarouselAdapter(
            TokoNowProductCardCarouselDiffer(),
            TokoNowProductCardCarouselTypeFactoryImpl(
                productCardCarouselItemListener = this,
                productCardCarouselSeeMoreListener = this
            )
        )
    }

    private var layoutManager: LinearLayoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
        override fun requestChildRectangleOnScreen(
            parent: RecyclerView,
            child: View,
            rect: Rect,
            immediate: Boolean,
            focusedChildVisible: Boolean
        ): Boolean = false
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            saveInstanceStateToLayoutManager(recyclerView)
        }
    }

    private var binding: LayoutTokopedianowProductCardCarouselViewBinding
    private var listener: TokoNowProductCardCarouselListener? = null

    init {
        binding = LayoutTokopedianowProductCardCarouselViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            root.addOnScrollListener(scrollListener)
            root.addItemDecoration(ProductCardCarouselDecoration(context))
            root.layoutManager = layoutManager
            root.itemAnimator = null
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

    override fun onProductCardAnimationFinished(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductCardAnimationFinished(
            position = position,
            product = product,
            quantity = quantity
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

    private fun saveInstanceStateToLayoutManager(
        recyclerView: RecyclerView
    ) {
        launch {
            val scrollState = recyclerView.layoutManager?.onSaveInstanceState()
            listener?.saveScrollState(scrollState)
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
            val scrollState = listener?.getScrollState()
            layoutManager.onRestoreInstanceState(scrollState)
        }
    }

    fun bindItems(
        items: List<Visitable<*>>,
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel? = null
    ) {
        binding.root.adapter = adapter
        if (seeMoreUiModel != null && seeMoreUiModel.appLink.isNotBlank()) {
            val newItems = items.toMutableList()
            newItems.add(seeMoreUiModel)
            adapter.submitList(newItems)
        } else {
            adapter.submitList(items)
        }
        restoreInstanceStateToLayoutManager()
    }

    fun setListener(
        productCardCarouselListener: TokoNowProductCardCarouselListener? = null
    ) {
        listener = productCardCarouselListener
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
        fun onProductCardAnimationFinished(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int,
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int,
        )
        fun onProductCardAddVariantClicked(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onSeeMoreClicked(
            seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
        )
        fun saveScrollState(
            state: Parcelable?
        )
        fun getScrollState(): Parcelable?
    }
}
