package com.tokopedia.oldcatalog.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.ComponentData
import com.tokopedia.oldcatalog.viewholder.components.ComparisonDetailNewViewHolder
import com.tokopedia.oldcatalog.viewholder.components.ComparisonFeatureNewViewHolder

class CatalogComparisonNewAdapter(
    private val specsList: ArrayList<ComponentData.SpecList>,
    val catalogDetailListener: CatalogDetailListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object ViewType {
        const val CATALOG_DETAIL = 0
        const val CATALOG_FEATURE = 1
        const val FIRST_POSITION = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CATALOG_DETAIL) {
            ComparisonDetailNewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_detail, parent, false))
        } else {
            ComparisonFeatureNewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_accordion, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == FIRST_POSITION) {
            CATALOG_DETAIL
        } else {
            CATALOG_FEATURE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == FIRST_POSITION) {
            (holder as ComparisonDetailNewViewHolder).bind(
                specsList[position].subcard?.firstOrNull()?.featureLeftData,
                specsList[position].subcard?.firstOrNull()?.featureRightData,
                catalogDetailListener
            )
        } else {
            (holder as ComparisonFeatureNewViewHolder).bind(
                specsList[position], catalogDetailListener
            )
        }
    }

    override fun getItemCount() = specsList.size
}
