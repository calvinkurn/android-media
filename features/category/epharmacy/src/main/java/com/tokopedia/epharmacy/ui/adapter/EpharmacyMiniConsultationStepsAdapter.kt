package com.tokopedia.epharmacy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.databinding.EpharmacyItemMasterMiniConsultationBsBinding
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse.*
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.loader.loadImage

class EpharmacyMiniConsultationStepsAdapter: RecyclerView.Adapter<EpharmacyMiniConsultationStepsAdapter.ViewHolder>() {
    private val stepsList = mutableListOf<EPharmacyMiniConsultationData.ConsultationSteps?>()

    fun submitList(list: List<EPharmacyMiniConsultationData.ConsultationSteps?>){
        val diffCallback = EpharmacyMiniConsultationStepsDiffUtil(stepsList.toMutableList(), list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        stepsList.clear()
        stepsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EpharmacyItemMasterMiniConsultationBsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stepsList[position])
    }

    override fun getItemCount(): Int = stepsList.size

    inner class ViewHolder(
        private val binding: EpharmacyItemMasterMiniConsultationBsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: EPharmacyMiniConsultationData.ConsultationSteps?){
            with(binding){
                stepIcon.loadImage(data?.imageUrl)
                headingTitle.text = data?.title.toEmptyStringIfNull()
                paraSubtitle.text = data?.subtitle?.parseAsHtml()
            }
        }
    }
}
