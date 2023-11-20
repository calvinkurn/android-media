package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.model.raw.ComponentData
import com.tokopedia.unifyprinciples.Typography

class CatalogAccordionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(subCard: ComponentData.SpecList.Subcard) {
        view.findViewById<Typography>(R.id.subcategory_title).text = subCard.subTitle
        view.findViewById<Typography>(R.id.first_content).text = subCard.leftData
        view.findViewById<Typography>(R.id.second_content).text = subCard.rightData
    }
}
