package com.tokopedia.topupbills.telco.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelco

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductGridAdapter(val productList: List<DigitalProductTelco>)
    : RecyclerView.Adapter<com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridAdapter.ProductGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridAdapter.ProductGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_grid, parent, false)
        return ProductGridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size;
    }

    override fun onBindViewHolder(holder: com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridAdapter.ProductGridViewHolder, position: Int) {

    }

    inner class ProductGridViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        init {

        }
    }

}
