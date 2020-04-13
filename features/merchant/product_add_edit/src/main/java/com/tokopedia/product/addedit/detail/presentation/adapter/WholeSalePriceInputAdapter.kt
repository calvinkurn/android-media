package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_WHOLESALE_PRICES
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder

class WholeSalePriceInputAdapter(private val listener: WholeSaleInputViewHolder.TextChangedListener,
                                 private val onAddWholesale: (() -> Unit)? = null,
                                 private val onDeleteWholesale: (() -> Unit)? = null) :
    RecyclerView.Adapter<WholeSaleInputViewHolder>(), WholeSaleInputViewHolder.OnDeleteButtonClickListener {

    private var wholeSaleInputModelList: MutableList<WholeSaleInputModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeSaleInputViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.wholesale_input_item, parent, false)
        return WholeSaleInputViewHolder(rootView, this, listener)
    }

    override fun getItemCount(): Int {
        return wholeSaleInputModelList.size
    }

    override fun onBindViewHolder(holder: WholeSaleInputViewHolder, position: Int) {
        val wholeSaleInputModel = wholeSaleInputModelList[position]
        holder.bindData(wholeSaleInputModel)
    }

    fun addNewWholeSalePriceForm() {
        if (itemCount == MAX_WHOLESALE_PRICES) return
        val wholeSaleInputModel = WholeSaleInputModel()
        wholeSaleInputModelList.add(wholeSaleInputModel)
        onAddWholesale?.invoke()
        notifyItemInserted(wholeSaleInputModelList.lastIndex)
    }

    fun setWholeSaleInputModels(wholeSaleInputModels: List<WholeSaleInputModel>) {
        this.wholeSaleInputModelList = wholeSaleInputModels.toMutableList()
        notifyDataSetChanged()
    }

    override fun onDeleteButtonClicked(position: Int) {
        onDeleteWholesale?.invoke()
        wholeSaleInputModelList.removeAt(position)
        notifyItemRemoved(position)
    }
}