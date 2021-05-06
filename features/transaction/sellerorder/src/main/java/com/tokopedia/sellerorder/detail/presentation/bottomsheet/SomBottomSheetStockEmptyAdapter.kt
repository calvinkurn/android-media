package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import kotlinx.android.synthetic.main.bottomsheet_empty_product_item.view.*

/**
 * Created by fwidjaja on 2019-11-05.
 */
class SomBottomSheetStockEmptyAdapter: RecyclerView.Adapter<SomBottomSheetStockEmptyAdapter.ViewHolder>() {
    var listProduct = mutableListOf<SomDetailOrder.Data.GetSomDetail.Products>()
    var listToBeEmptied = ArrayList<SomDetailOrder.Data.GetSomDetail.Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_empty_product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.iv_product.loadImageRounded(listProduct[position].thumbnail, holder.itemView.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
        holder.itemView.tv_product_name.text = listProduct[position].name
        holder.itemView.tv_product_price.text = listProduct[position].priceText
        holder.itemView.cb_product.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                listToBeEmptied.add(listProduct[position])
                holder.itemView.label_empty_stock.visibility = View.VISIBLE
            } else {
                listToBeEmptied.remove(listProduct[position])
                holder.itemView.label_empty_stock.visibility = View.GONE
            }
        }
    }

    fun getListProductEmptied() : ArrayList<SomDetailOrder.Data.GetSomDetail.Products> {
        return listToBeEmptied
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}