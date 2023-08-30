package com.tokopedia.shop.home.view.customview.directpurchase

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.ColorPallete
import com.tokopedia.kotlin.extensions.view.ZERO

class ProductCarouselDirectPurchaseAdapter(
    private val contentListener: ProductDirectPurchaseViewHolder.ProductDirectPurchaseContentVHListener,
    private val errorListener: ProductDirectPurchaseViewHolder.ProductDirectPurchaseErrorVHListener,
    private val seeMoreListener: ProductDirectPurchaseViewHolder.ProductDirectPurchaseSeeMoreVHListener,
    private var maxProductShown: Int
) :
    ListAdapter<ProductCarouselDirectPurchaseAdapter.Model, RecyclerView.ViewHolder>(
        DirectPurchaseDiffCallback()
    ) {

    var fullData: List<Model> = emptyList()
    private var recyclerView: RecyclerView? = null
    private var colorPallete: ColorPallete? = null
    private var seeAllCardModeType: Int? = null
    private var isAdaptiveLabelDiscount: Boolean = true

    fun setMaxProductShown(maxProductShownParam: Int) {
        if (maxProductShown != maxProductShownParam) {
            maxProductShown = maxProductShownParam
            if (this.itemCount > 0 && this.getItem(0) is Model.Content) {
                setProductData(fullData)
            }
        }
    }

    fun setWidgetColor(colorPallete: ColorPallete?) {
        this.colorPallete = colorPallete
    }

    // MODE_INVERT(8) or MODE_NORMAL(6)
    fun setSeeAllCardModeType(mode: Int) {
        this.seeAllCardModeType = mode
    }

    //set adaptive label discount
    fun setAdaptiveLabelDiscount(isAdaptive: Boolean) {
        this.isAdaptiveLabelDiscount = isAdaptive
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> ProductDirectPurchaseViewHolder.ContentVH.create(
                parent,
                colorPallete,
                isAdaptiveLabelDiscount,
                contentListener
            )

            TYPE_EMPTY -> ProductDirectPurchaseViewHolder.EmptyVH.create(parent, colorPallete)
            TYPE_LOADING -> ProductDirectPurchaseViewHolder.LoadingVH.create(parent)
            TYPE_ERROR -> ProductDirectPurchaseViewHolder.ErrorVH.create(
                parent,
                colorPallete,
                errorListener
            )

            TYPE_SEEMORE -> ProductDirectPurchaseViewHolder.SeeMoreVH.create(
                parent, colorPallete,
                seeAllCardModeType, seeMoreListener
            )

            else -> error("View Type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        return ProductDirectPurchaseViewHolder.bind(holder, data, colorPallete)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Model.Content -> TYPE_CONTENT
            is Model.Empty -> TYPE_EMPTY
            is Model.Loading -> TYPE_LOADING
            is Model.Error -> TYPE_ERROR
            is Model.SeeMore -> TYPE_SEEMORE
            else -> TYPE_EMPTY
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun setLoading() {
        if (this.itemCount == 1 && getItem(0) is Model.Loading) {
            return
        }
        this.submitList(listOf(Model.Loading))
    }

    fun setError(errorMessage: String) {
        if (this.itemCount == 1 && getItem(0) is Model.Error
            && (getItem(0) as Model.Error).errorMessage == errorMessage
        ) {
            return
        }
        this.submitList(listOf(Model.Error(errorMessage)))
    }

    fun setEmpty() {
        if (this.itemCount == 1 && getItem(0) is Model.Empty) {
            return
        }
        this.submitList(listOf(Model.Empty))
    }

    fun setProductData(contentData: List<Model>) {
        fullData = contentData
        val diff = contentData.size - maxProductShown
        if (diff > 0) {
            submitList(contentData.take(maxProductShown).toMutableList().apply {
                add(Model.SeeMore(diff))
            }){
                resetScrollPosition()
            }
        } else {
            submitList(contentData){
                resetScrollPosition()
            }
        }
    }

    private fun resetScrollPosition() {
        recyclerView?.scrollToPosition(Int.ZERO)
    }

    sealed interface Model {
        data class Content(val data: ProductCardDirectPurchaseDataModel) : Model
        object Empty : Model
        data class SeeMore(val counter: Int) : Model
        object Loading : Model
        data class Error(val errorMessage: String) : Model
    }

    companion object {
        private const val TYPE_CONTENT = 0
        private const val TYPE_EMPTY = 1
        private const val TYPE_LOADING = 2
        private const val TYPE_ERROR = 3
        private const val TYPE_SEEMORE = 4
    }
}

internal class DirectPurchaseDiffCallback :
    DiffUtil.ItemCallback<ProductCarouselDirectPurchaseAdapter.Model>() {

    override fun areItemsTheSame(
        oldItem: ProductCarouselDirectPurchaseAdapter.Model,
        newItem: ProductCarouselDirectPurchaseAdapter.Model
    ): Boolean {
        return when {
            oldItem is ProductCarouselDirectPurchaseAdapter.Model.Content && newItem is ProductCarouselDirectPurchaseAdapter.Model.Content -> oldItem.data.productId == newItem.data.productId
            oldItem is ProductCarouselDirectPurchaseAdapter.Model.Loading && newItem is ProductCarouselDirectPurchaseAdapter.Model.Loading -> false
            oldItem is ProductCarouselDirectPurchaseAdapter.Model.Empty && newItem is ProductCarouselDirectPurchaseAdapter.Model.Empty -> false
            oldItem is ProductCarouselDirectPurchaseAdapter.Model.Error && newItem is ProductCarouselDirectPurchaseAdapter.Model.Error -> false
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: ProductCarouselDirectPurchaseAdapter.Model,
        newItem: ProductCarouselDirectPurchaseAdapter.Model
    ): Boolean {
        return oldItem == newItem
    }

}
