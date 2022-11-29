package com.tokopedia.smartbills.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.SmartBillsCatalogMenu
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemIconBottomSheetBinding

class SmartBillsAddBillsAdapter(private val listener: SmartBillsCatalogsListener) : RecyclerView.Adapter<SmartBillsAddBillsAdapter.SmartBillsAddBillViewHolder>() {

    var listCatalogMenu = emptyList<SmartBillsCatalogMenu>()

    inner class SmartBillsAddBillViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(catalogMenu: SmartBillsCatalogMenu) {
            val binding = ViewSmartBillsItemIconBottomSheetBinding.bind(itemView)
            with(binding) {
                root.setOnClickListener {
                    listener.onCatalogClicked(catalogMenu.applink, catalogMenu.label)
                }
                imgCatalogSmartBills.loadImage(catalogMenu.icon)
                txtCatalogTitleSmartBills.text = catalogMenu.label
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

    interface SmartBillsCatalogsListener {
        fun onCatalogClicked(applink: String, category: String)
    }
}
