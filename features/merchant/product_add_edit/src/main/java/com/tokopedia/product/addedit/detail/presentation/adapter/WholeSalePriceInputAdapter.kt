package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_WHOLESALE_PRICES
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder

class WholeSalePriceInputAdapter : RecyclerView.Adapter<WholeSaleInputViewHolder>(), WholeSaleInputViewHolder.OnDeleteButtonClickListener {

    private var wholeSaleInputModelList: MutableList<WholeSaleInputModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeSaleInputViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.wholesale_input_item, parent, false)
        return WholeSaleInputViewHolder(rootView, this)
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
        val price = ""
        val quantity = ""
        val wholeSaleInputModel = WholeSaleInputModel(price, quantity)
        wholeSaleInputModelList.add(wholeSaleInputModel)
        notifyItemInserted(wholeSaleInputModelList.lastIndex)
    }

    fun setWholeSaleInputModels(draftInputModelList: List<WholeSaleInputModel>) {
        wholeSaleInputModelList = draftInputModelList.toMutableList()
    }

    override fun onDeleteButtonClicked(position: Int) {
        wholeSaleInputModelList.removeAt(position)
        notifyDataSetChanged()
    }
}