package com.tokopedia.product.edit.price

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.ProductCatalog
import kotlinx.android.synthetic.main.item_product_edit_catalog.view.*

class ProductCatalogAdapter(private var catatogList: ArrayList<ProductCatalog>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var selectedPosition = -1

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_edit_catalog, parent, false)
        return ProductCategoryRecommendationViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewName.text = catatogList[position].catalogName
        if(selectedPosition == position)
            holder.itemView.imageViewCheck.visibility = View.VISIBLE
        else
            holder.itemView.imageViewCheck.visibility = View.GONE
    }

    override fun getItemCount() = catatogList.size

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun replaceData(catatogList: ArrayList<ProductCatalog>) {
        this.catatogList = catatogList
        notifyDataSetChanged()
    }

    fun getSelectedCatalog(): ProductCatalog{
        return catatogList[selectedPosition]
    }

    inner class ProductCategoryRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            setView()
        }
        fun setView(){
            itemView.setOnClickListener({
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            })
        }
    }
}