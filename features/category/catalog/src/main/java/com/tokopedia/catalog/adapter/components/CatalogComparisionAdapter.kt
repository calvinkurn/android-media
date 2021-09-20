package com.tokopedia.catalog.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.catalog.viewholder.components.ComparisionDetailViewHolder
import com.tokopedia.catalog.viewholder.components.ComparisionFeatureViewHolder

class CatalogComparisionAdapter (val list : List<String>, val baseCatalog : HashMap<String, ComparisionModel>,
                                 private val comparisionCatalog : HashMap<String, ComparisionModel>, private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object ViewType {
        const val CATALOG_DETAIL = 0
        const val CATALOG_FEATURE = 1
        const val FIRST_POSITION = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == CATALOG_DETAIL){
            ComparisionDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_detail, parent, false))
        }else {
            ComparisionFeatureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_feature, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == FIRST_POSITION){
            CATALOG_DETAIL
        } else {
            CATALOG_FEATURE
        }
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == FIRST_POSITION){
            (holder as ComparisionDetailViewHolder).bind(baseCatalog[list[position]],comparisionCatalog[list[position]],catalogDetailListener)
        }else {
            (holder as ComparisionFeatureViewHolder).bind(position,baseCatalog[list[position]],comparisionCatalog[list[position]],catalogDetailListener)
        }
    }
}