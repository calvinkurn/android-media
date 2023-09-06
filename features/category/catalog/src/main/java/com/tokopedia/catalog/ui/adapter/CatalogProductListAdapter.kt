package com.tokopedia.catalog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.databinding.ItemCatalogPrefferedProductBinding
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.ui.mapper.ProductListMapper

class CatalogProductListAdapter(private val itemList: List<CatalogProductItem>) :
    RecyclerView.Adapter<CatalogProductListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatalogPrefferedProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUiModel = itemList[position]
        holder.bindToView(itemUiModel)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: ItemCatalogPrefferedProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val productCard = itemView.productCard

        fun bindToView(itemUiModel: CatalogProductItem) {
            productCard.apply {
                setProductModel(ProductListMapper.mapperToCatalogProductModel(itemUiModel))
            }
        }

    }
}
