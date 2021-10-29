package com.tokopedia.smartbills.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.SmartBillsCatalogMenu
import kotlinx.android.synthetic.main.view_smart_bills_item_icon_bottom_sheet.view.*

class SmartBillsAddBillsAdapter(private val listener: SmartBillsCatalogsListener) : RecyclerView.Adapter<SmartBillsAddBillsAdapter.SmartBillsAddBillViewHolder>() {

    var listCatalogMenu = emptyList<SmartBillsCatalogMenu>()

    inner class SmartBillsAddBillViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(catalogMenu: SmartBillsCatalogMenu) {
            with(itemView) {
                setOnClickListener {
                    listener.onCatalogClicked(catalogMenu.applink, catalogMenu.label)
                }
                img_catalog_smart_bills.loadImage(catalogMenu.icon)
                txt_catalog_title_smart_bills.text = catalogMenu.label
            }
        }
    }

    override fun getItemCount(): Int {
        return listCatalogMenu.size
    }

    override fun onBindViewHolder(holder: SmartBillsAddBillViewHolder, position: Int) {
        holder.bind(listCatalogMenu[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartBillsAddBillViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_smart_bills_item_icon_bottom_sheet, parent, false)
        return SmartBillsAddBillViewHolder(itemView)
    }

    interface SmartBillsCatalogsListener{
        fun onCatalogClicked(applink: String, category: String)
    }
}