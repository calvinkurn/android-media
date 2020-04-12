package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_WHOLESALE_PRICES
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import java.math.BigInteger

class WholeSalePriceInputAdapter(private val listener: WholeSaleInputViewHolder.TextChangedListener,
                                 private val onAddWholesale: (() -> Unit)? = null,
                                 private val onDeleteWholesale: (() -> Unit)? = null) :
    RecyclerView.Adapter<WholeSaleInputViewHolder>(), WholeSaleInputViewHolder.OnDeleteButtonClickListener {

    private var wholeSaleInputModelList: MutableList<WholeSaleInputModel> = mutableListOf()

    private var price: BigInteger = 0.toBigInteger()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeSaleInputViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.wholesale_input_item, parent, false)
        return WholeSaleInputViewHolder(rootView, this, listener)
    }

    override fun getItemCount(): Int {
        return wholeSaleInputModelList.size
    }

    override fun onBindViewHolder(holder: WholeSaleInputViewHolder, position: Int) {
        val wholeSaleInputModel = wholeSaleInputModelList[position]
        if (price > 0.toBigInteger()) {
            var priceResult = (price - (position + 1).toBigInteger())
            if (priceResult < 0.toBigInteger()) {
                priceResult = 0.toBigInteger()
            }
            wholeSaleInputModel.price = priceResult.toString()
        }
        wholeSaleInputModel.quantity = (position + 1).toString()
        holder.bindData(wholeSaleInputModel)
    }

    fun addNewWholeSalePriceForm() {
        val wholeSaleInputModel = WholeSaleInputModel()
        wholeSaleInputModelList.add(wholeSaleInputModel)
        onAddWholesale?.invoke()
        notifyItemInserted(wholeSaleInputModelList.lastIndex)
    }

    fun addNewWholeSalePrice(wholeSaleInputModels: List<WholeSaleInputModel>) {
        wholeSaleInputModels.forEach {
            wholeSaleInputModelList.add(it)
        }
        notifyDataSetChanged()
    }

    fun setWholeSaleInputModels(drafts: List<WholeSaleInputModel>) {
        if (drafts.size == MAX_WHOLESALE_PRICES) return
        wholeSaleInputModelList = drafts.toMutableList()
    }

    fun setPrice(price: BigInteger) {
        this.price = price
    }

    override fun onDeleteButtonClicked(position: Int) {
        onDeleteWholesale?.invoke()
        wholeSaleInputModelList.removeAt(position)
        notifyDataSetChanged()
    }
}