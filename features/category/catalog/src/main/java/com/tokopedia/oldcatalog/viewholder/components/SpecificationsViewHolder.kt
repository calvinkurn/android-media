package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_catalog_specification.view.*

class SpecificationsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(model: TopSpecificationsComponentData?, catalogDetailListener : CatalogDetailListener) {
        catalogDetailListener.sendWidgetImpressionEvent(
            CatalogDetailAnalytics.ActionKeys.SPECIFICATION_WIDGET_IMPRESSION,
            CatalogDetailAnalytics.ActionKeys.SPECIFICATION_WIDGET_IMPRESSION_ITEM_NAME,
            adapterPosition)
        itemView.run {
            if(model != null){
                specification_iv.show()
                specification_name.show()
                specification_description.show()
                lihat_logo.hide()
                lihat_text.hide()
                if(model.icon.isNullOrBlank())
                    specification_iv.loadImage(CatalogConstant.DEFAULT_SPECS_ICON_URL)
                else
                    specification_iv.loadImage(model.icon)
                specification_name.text = model.key
                specification_description.text = model.value
            }else {
                specification_iv.hide()
                specification_name.hide()
                specification_description.hide()
                lihat_logo.show()
                lihat_text.show()
            }
            setOnClickListener{
                catalogDetailListener.onViewMoreSpecificationsClick(model)
            }
        }
    }
}
