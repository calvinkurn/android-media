package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.unifyprinciples.Typography

class CatalogAccordionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(subcard: ComponentData.SpecList.Subcard) {
        view.findViewById<Typography>(R.id.subcategory_title).text = subcard.subTitle
        view.findViewById<Typography>(R.id.first_content).text = subcard.leftData
        view.findViewById<Typography>(R.id.second_content).text = subcard.rightData
    }
}
