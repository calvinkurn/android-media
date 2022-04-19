package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import java.math.BigInteger

class WholeSalePriceInputAdapter(private val textListener: WholeSaleInputViewHolder.TextChangedListener,
                                 private val addListener: WholeSaleInputViewHolder.OnAddButtonClickListener,
                                 private val onDeleteWholesale: (() -> Unit)? = null) :
        RecyclerView.Adapter<WholeSaleInputViewHolder>(), WholeSaleInputViewHolder.OnDeleteButtonClickListener {

    private var wholeSaleInputModelList: MutableList<WholeSaleInputModel> = mutableListOf()

    private var productPrice: BigInteger = 0.toBigInteger()

    private var deletePosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeSaleInputViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.wholesale_input_item, parent, false)
        return WholeSaleInputViewHolder(rootView, this, addListener,  textListener)
    }

    override fun getItemCount(): Int {
        return wholeSaleInputModelList.size
    }

    override fun onBindViewHolder(holder: WholeSaleInputViewHolder, position: Int) {

        val wholeSaleInputModel = wholeSaleInputModelList[position]

        // wholesale quantity
        val isWholeSaleQuantityEmpty = wholeSaleInputModel.quantity.isBlank()
        // set wholesale quantity recommendation when the wholesale quantity is empty
        if (isWholeSaleQuantityEmpty) {
            if (position == 0) wholeSaleInputModel.quantity = (position + 1).toString()
            else {
                val previousInputModel = wholeSaleInputModelList[position - 1]
                val quantityRecommendation = previousInputModel.quantity.toBigInteger() + 1.toBigInteger()
                wholeSaleInputModel.quantity = quantityRecommendation.toString()
            }
        }

        // wholesale price
        val isWholeSalePriceEmpty = wholeSaleInputModel.price.isBlank()
        // set wholesale price recommendation when the wholesale price is empty
        if (isWholeSalePriceEmpty && productPrice > 0.toBigInteger()) {
            if (position == 0) wholeSaleInputModel.price = (productPrice - 1.toBigInteger()).toString()
            else {
                wholeSaleInputModel.price = try {
                    val previousInputModel = wholeSaleInputModelList[position - 1]
                    val priceRecommendation = previousInputModel.price.toBigInteger() - 1.toBigInteger()
                    priceRecommendation.toString()
                } catch (e: Exception) {
                    ""
                }
            }
        }

        holder.bindData(wholeSaleInputModel)
    }

    fun addNewWholeSalePriceForm() {
        val wholeSaleInputModel = WholeSaleInputModel()
        wholeSaleInputModelList.add(wholeSaleInputModel)
        notifyItemInserted(wholeSaleInputModelList.lastIndex)
    }

    fun setWholeSaleInputModels(wholeSaleInputModels: List<WholeSaleInputModel>) {
        this.wholeSaleInputModelList = wholeSaleInputModels.toMutableList()
        notifyDataSetChanged()
    }

    fun setProductPrice(productPriceInput: String) {
        this.productPrice = productPriceInput.toBigIntegerOrNull().orZero()
    }

    fun updateWholeSaleQuantityInputModel(position: Int, newQuantity: String) {
        wholeSaleInputModelList[position].quantity = newQuantity
    }

    fun updateWholeSalePriceInputModel(position: Int, newPrice: String) {
        wholeSaleInputModelList[position].price = newPrice
    }

    fun getPreviousQuantity(position: Int): String {
        return if (position <= 0) ""
        else wholeSaleInputModelList[position - 1].quantity
    }

    fun getPreviousPrice(position: Int): String {
        return if (position <= 0) ""
        else wholeSaleInputModelList[position - 1].price
    }

    fun getDeletePosition() : Int? {
        return deletePosition
    }

    override fun onDeleteButtonClicked(position: Int) {
        if (position != NO_POSITION) {
            deletePosition = position
            onDeleteWholesale?.invoke()
            wholeSaleInputModelList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}