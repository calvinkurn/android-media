package com.tokopedia.topupbills.telco.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.model.DigitalProductTelco

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductGridAdapter(val productList: List<DigitalProductTelco>)
    : RecyclerView.Adapter<DigitalProductGridAdapter.ProductGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_grid, parent, false)
        return ProductGridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size;
    }

    override fun onBindViewHolder(holder: ProductGridViewHolder, position: Int) {

    }

    inner class ProductGridViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        init {

        }
    }

}
