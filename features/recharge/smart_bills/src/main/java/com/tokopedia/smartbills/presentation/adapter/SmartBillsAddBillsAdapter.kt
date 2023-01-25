package com.tokopedia.smartbills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.smartbills.data.SmartBillsCatalogMenu
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemIconBottomSheetBinding

class SmartBillsAddBillsAdapter(private val listener: SmartBillsCatalogsListener) : RecyclerView.Adapter<SmartBillsAddBillsAdapter.SmartBillsAddBillViewHolder>() {

    var listCatalogMenu = emptyList<SmartBillsCatalogMenu>()

    inner class SmartBillsAddBillViewHolder(
        private val binding: ViewSmartBillsItemIconBottomSheetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(catalogMenu: SmartBillsCatalogMenu) {
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
        val binding = ViewSmartBillsItemIconBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SmartBillsAddBillViewHolder(binding)
    }

    interface SmartBillsCatalogsListener {
        fun onCatalogClicked(applink: String, category: String)
    }
}
