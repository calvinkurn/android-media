package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_catalog_specification.view.*

class CatalogSpecificationsAdapter (val list : ArrayList<TopSpecificationsComponentData>,
                                    private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<CatalogSpecificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_specification, null))
    }

    override fun getItemCount(): Int  = (list.size + 1)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == list.size)
            holder.bind(null,catalogDetailListener)
        else
            holder.bind(list[position],catalogDetailListener)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: TopSpecificationsComponentData?,catalogDetailListener : CatalogDetailListener) {
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
                    itemView.setOnClickListener(null)
                }else {
                    specification_iv.hide()
                    specification_name.hide()
                    specification_description.hide()
                    lihat_logo.show()
                    lihat_text.show()
                    itemView.setOnClickListener{
                        catalogDetailListener.onViewMoreSpecificationsClick()
                    }
                }
            }
        }
    }
}