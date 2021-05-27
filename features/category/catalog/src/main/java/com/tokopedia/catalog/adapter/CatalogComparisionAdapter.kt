package com.tokopedia.catalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogComparisionAdapter (val list : List<String>, val baseCatalog : HashMap<String, ComparisionModel>,
                                 val comparisionCatalog : HashMap<String, ComparisionModel>, private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object ViewType {
        const val CATALOG_DETAIL = 0
        const val CATALOG_FEATURE = 1
        const val FIRST_POSITION = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == CATALOG_DETAIL){
            ComparisionDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_detail, parent,false))
        }else {
            ComparisionFeatureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_feature, parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == FIRST_POSITION){ CATALOG_DETAIL } else { CATALOG_FEATURE }
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == FIRST_POSITION){
            (holder as ComparisionDetailViewHolder).bind(baseCatalog[list[position]],comparisionCatalog[list[position]],catalogDetailListener)
        }else {
            (holder as ComparisionFeatureViewHolder).bind(position,baseCatalog[list[position]],comparisionCatalog[list[position]],catalogDetailListener)
        }
    }

    inner class ComparisionDetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(baseCatalog: ComparisionModel?,comparisionCatalog: ComparisionModel?, catalogDetailListener: CatalogDetailListener) {
            baseCatalog?.run {
                itemView.findViewById<Typography>(R.id.first_catalog_product_brand).displayTextOrHide(brand ?: "")
                itemView.findViewById<Typography>(R.id.first_catalog_product_name).displayTextOrHide(name ?: "")
                itemView.findViewById<Typography>(R.id.first_catalog_product_price).displayTextOrHide(price ?: "")
                url?.let {imageUrl ->
                    itemView.findViewById<ImageUnify>(R.id.first_catalog_image).loadImageWithoutPlaceholder(imageUrl)
                }
            }
            comparisionCatalog?.run {
                itemView.findViewById<Typography>(R.id.second_catalog_product_brand).displayTextOrHide(brand ?: "")
                itemView.findViewById<Typography>(R.id.second_catalog_product_name).displayTextOrHide(name ?: "")
                itemView.findViewById<Typography>(R.id.second_catalog_product_price).displayTextOrHide(price ?: "")
                url?.let {imageUrl ->
                    itemView.findViewById<ImageUnify>(R.id.second_catalog_image).loadImageWithoutPlaceholder(imageUrl)
                }
            }

            itemView.findViewById<CardUnify>(R.id.comparision_card).setOnClickListener {
                comparisionCatalog?.id?.let {catalogId ->
                    if(catalogId.isNotEmpty()){
                        catalogDetailListener.comparisionCatalogClicked(catalogId)
                    }
                }
            }
        }
    }

    inner class ComparisionFeatureViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(position : Int , baseCatalog: ComparisionModel?, comparisionCatalog: ComparisionModel?, catalogDetailListener: CatalogDetailListener) {
            setKeyOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.first_key),baseCatalog)
            setValueOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.first_value),baseCatalog)
            setKeyOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.second_key),baseCatalog)
            setValueOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.second_value),comparisionCatalog)

            if(isColoredTile(position)){
                itemView.findViewById<ImageUnify>(R.id.catalog_feature_bg_image).run {
                    show()
                    setImageDrawable(MethodChecker.getDrawable(itemView.context,R.drawable.catalog_top_specs_gray_bg))
                }
            }else {
                itemView.findViewById<Typography>(R.id.catalog_feature_bg_image).hide()
            }
        }
    }

    private fun isColoredTile(position: Int) : Boolean{
        if(position % 2 == 0){ return false }
        return true
    }

    private fun setKeyOrHyphen(context : Context, typography: Typography, model : ComparisionModel?){
        if(model != null && !model.key.isNullOrEmpty()){
            typography.text = model.key
        }else {
            typography.text = context.getString(R.string.catalog_feature_not_available)
        }
    }

    private fun setValueOrHyphen(context : Context, typography: Typography , model : ComparisionModel?){
        if(model != null && !model.value.isNullOrEmpty()){
            typography.text = model.value
        }else {
            typography.text = context.getString(R.string.catalog_feature_not_available)
        }
    }
}