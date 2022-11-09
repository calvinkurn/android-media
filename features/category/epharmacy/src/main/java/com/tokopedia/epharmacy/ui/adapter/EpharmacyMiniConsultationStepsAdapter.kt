package com.tokopedia.epharmacy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.databinding.EpharmacyItemMasterMiniConsultationBsBinding
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse.*
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.loader.loadImage

class EpharmacyMiniConsultationStepsAdapter(private val items:MutableList<EPharmacyMiniConsultationData.ConsultationSteps?>): RecyclerView.Adapter<EpharmacyMiniConsultationStepsAdapter.ViewHolder>() {

    fun setStepList(steps: List<EPharmacyMiniConsultationData.ConsultationSteps?>) {
        if(steps.isNullOrEmpty()) return
        items.clear()
        items.addAll(steps)
        notifyDataSetChanged()
    }

    fun clearStepsList() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EpharmacyItemMasterMiniConsultationBsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: EpharmacyItemMasterMiniConsultationBsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: EPharmacyMiniConsultationData.ConsultationSteps?){
            with(binding){
                stepIcon.loadImage(data?.imageUrl)
                headingTitle.text = data?.title.toEmptyStringIfNull()
                paraSubtitle.text = data?.subtitle.toEmptyStringIfNull()
            }
        }
    }
}
