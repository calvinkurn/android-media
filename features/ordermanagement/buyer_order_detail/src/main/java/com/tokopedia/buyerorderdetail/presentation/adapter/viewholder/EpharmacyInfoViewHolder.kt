package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailEpharmacyContentBinding
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailEpharmacyInfoBinding
import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel

class EpharmacyInfoViewHolder(
    itemView: View
) : AbstractViewHolder<EpharmacyInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_epharmacy_info
    }

    private val binding = ItemBuyerOrderDetailEpharmacyInfoBinding.bind(itemView)


    override fun bind(element: EpharmacyInfoUiModel) {
        binding.run {
            containerBuyerOrderDetailEphar.removeAllViews()

            element.consultationName.addViewIfNotBlank("Konsultasi")
            element.consultationDoctorName.addViewIfNotBlank("Dokter")
            element.consultationPrescriptionNumber.addViewIfNotBlank("No. Resep")
            element.consultationDate.addViewIfNotBlank("Tanggal Dikeluarkan")
            element.consultationExpiryDate.addViewIfNotBlank("Masa Berlaku")
            element.consultationPatientName.addViewIfNotBlank("Nama Pasien")
        }
    }

    override fun bind(element: EpharmacyInfoUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun createContentEpharmacy(title: String, desc: String): View {
        return ItemBuyerOrderDetailEpharmacyContentBinding.inflate(
            LayoutInflater.from(binding.root.context),
            binding.root,
            false
        ).apply {
            tvBuyerOrderDetailEpharContentTitle.text = title
            tvBuyerOrderDetailEpharContentDesc.text = desc
        }.root
    }

    private fun String.addViewIfNotBlank(key: String) {
        if (this.isNotBlank()) {
            val content = createContentEpharmacy(key, this)
            binding.containerBuyerOrderDetailEphar.addView(content)
        }
    }
}