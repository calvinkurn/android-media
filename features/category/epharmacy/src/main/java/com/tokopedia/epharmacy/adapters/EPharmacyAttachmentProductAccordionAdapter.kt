package com.tokopedia.epharmacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.viewholder.EPharmacyProductsAccordionItemViewHolder

class EPharmacyAttachmentProductAccordionAdapter(var products : ArrayList<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?>):
    RecyclerView.Adapter<EPharmacyProductsAccordionItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EPharmacyProductsAccordionItemViewHolder {
        return EPharmacyProductsAccordionItemViewHolder(LayoutInflater.from(parent.context).inflate(com.tokopedia.epharmacy.R.layout.epharmacy_prescription_attachment_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: EPharmacyProductsAccordionItemViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}


