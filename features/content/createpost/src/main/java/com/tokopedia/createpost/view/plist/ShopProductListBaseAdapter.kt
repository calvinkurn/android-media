package com.tokopedia.createpost.view.plist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.common.view.plist.GetShopProduct
import com.tokopedia.createpost.common.view.plist.ShopPageListener
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

open class ShopProductListBaseAdapter(
    val viewModel: ShopPageProductListViewModel,
    val callback: AdapterCallback,
    val listener: ShopPageListener
) : BaseAdapter<ShopPageProduct>(callback) {

    protected var cList: MutableList<BaseItem>? = null

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var productView: ProductCardGridView = view.findViewById(R.id.product_card)
        var isVisited = false

        override fun bindView(item: ShopPageProduct, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_shop_plist_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int, vararg args: String?) {
        super.loadData(pageNumber, *args)
        viewModel.getList(pageNumber)
    }

    fun onSuccess(data: GetShopProduct) {
        loadCompleted(data.productList.data, data)
        isLastPage = data.productList.paging.next.isEmpty()
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ShopPageProduct) {
        val itemContext = holder.itemView.context
        holder.productView.setProductModel(toShopProductModel(item))

        if (holder.itemView != null) {
            holder.itemView.setOnClickListener { v ->
                viewModel.setNewProductValue(item)
                sendClickEvent(itemContext, item, holder.adapterPosition)
            }
        }
    }


    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
            listener.shopProductImpressed(holder.adapterPosition, data)
        }
    }

    private fun sendClickEvent(context: Context, data: ShopPageProduct, position: Int) {
        listener.shopProductClicked(position, data)
    }

    private fun toShopProductModel(item: ShopPageProduct): ProductCardModel {
        val isDiscount = !item.campaign?.dPrice?.toInt().isZero()
        return ProductCardModel(
            productImageUrl = item.pImage?.img!!,
            productName = item.name ?: "",
            formattedPrice = item.price?.priceIdr!!,
            discountPercentage = if (isDiscount)
                ("${item.campaign?.dPrice!!}%") else "",
            slashedPrice = if (isDiscount) item.campaign?.oPriceFormatted!! else ""
        )
    }
}

