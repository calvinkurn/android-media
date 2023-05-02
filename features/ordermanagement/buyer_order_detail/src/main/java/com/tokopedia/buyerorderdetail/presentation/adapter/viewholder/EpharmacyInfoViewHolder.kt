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
        private const val CONSULTATION_NAME_KEY = "Konsultasi"
        private const val DOCTOR_KEY = "Dokter"
        private const val PRESCRIPTION_NUMBER_KEY = "No. Resep"
        private const val CONSULTATION_DATE_KEY = "Tanggal Dikeluarkan"
        private const val CONSULTATION_EXPIRY_DATE_KEY = "Masa Berlaku"
        private const val CONSULTATION_PATIENT_NAME_KEY = "Nama Pasien"
    }

    private val binding = ItemBuyerOrderDetailEpharmacyInfoBinding.bind(itemView)


    override fun bind(element: EpharmacyInfoUiModel) {
        binding.run {
            containerBuyerOrderDetailEphar.removeAllViews()

            element.consultationName.addViewIfNotBlank(CONSULTATION_NAME_KEY)
            element.consultationDoctorName.addViewIfNotBlank(DOCTOR_KEY)
            element.consultationPrescriptionNumber.addViewIfNotBlank(PRESCRIPTION_NUMBER_KEY)
            element.consultationDate.addViewIfNotBlank(CONSULTATION_DATE_KEY)
            element.consultationExpiryDate.addViewIfNotBlank(CONSULTATION_EXPIRY_DATE_KEY)
            element.consultationPatientName.addViewIfNotBlank(CONSULTATION_PATIENT_NAME_KEY)
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