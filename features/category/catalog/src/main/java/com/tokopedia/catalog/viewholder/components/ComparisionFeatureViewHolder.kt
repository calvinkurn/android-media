package com.tokopedia.catalog.viewholder.components

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ComparisionFeatureViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(position : Int, baseCatalog: ComparisionModel?, comparisionCatalog: ComparisionModel?, catalogDetailListener: CatalogDetailListener) {
        setKeyOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.first_key),baseCatalog)
        setValueOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.first_value),baseCatalog)
        setKeyOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.second_key),baseCatalog)
        setValueOrHyphen(itemView.context,itemView.findViewById<Typography>(R.id.second_value),comparisionCatalog)

        if(isColoredTile(position)){
            itemView.findViewById<ImageUnify>(R.id.catalog_feature_bg_image).run {
                show()
                setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.catalog_top_specs_gray_bg))
            }
        }else {
            itemView.findViewById<Typography>(R.id.catalog_feature_bg_image).hide()
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

    private fun setValueOrHyphen(context : Context, typography: Typography, model : ComparisionModel?){
        if(model != null && !model.value.isNullOrEmpty()){
            typography.text = model.value
        }else {
            typography.text = context.getString(R.string.catalog_feature_not_available)
        }
    }
}
