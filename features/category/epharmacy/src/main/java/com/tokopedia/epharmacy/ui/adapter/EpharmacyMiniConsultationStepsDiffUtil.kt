package com.tokopedia.epharmacy.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse

class EpharmacyMiniConsultationStepsDiffUtil(
    private val oldList: List<EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData.ConsultationSteps?>,
    private val newList: List<EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData.ConsultationSteps?>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.title == newList[newItemPosition]?.title
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.subtitle == newList[newItemPosition]?.subtitle
    }
}
