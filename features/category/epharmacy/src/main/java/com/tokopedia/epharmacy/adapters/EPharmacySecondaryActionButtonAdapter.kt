package com.tokopedia.epharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.epharmacy.ui.fragment.EPharmacySecondaryActionButtonBottomSheet
import com.tokopedia.unifyprinciples.Typography

class EPharmacySecondaryActionButtonAdapter(private val listener: EPharmacySecondaryActionButtonBottomSheet.ActionButtonClickListener?) : RecyclerView.Adapter<EPharmacySecondaryActionButtonAdapter.ViewHolder>() {

    private var secondaryActionButtons: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_epharmacy_order_detail_secondary_action_button, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return secondaryActionButtons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(secondaryActionButtons.getOrNull(position))
    }

    fun setSecondaryActionButtons(secondaryActionButtons: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>) {
        this.secondaryActionButtons = secondaryActionButtons
        notifyItemRangeChanged(0, secondaryActionButtons.size)
    }

    class ViewHolder(itemView: View, private val listener: EPharmacySecondaryActionButtonBottomSheet.ActionButtonClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val tvBuyerOrderDetailSecondaryActionButton = itemView.findViewById<Typography>(R.id.tvEPharmacyOrderButton)

        fun bind(button: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?) {
            tvBuyerOrderDetailSecondaryActionButton.text = button?.label.orEmpty()
            tvBuyerOrderDetailSecondaryActionButton.setOnClickListener {
                button?.let {
                    listener?.onActionButtonClicked(false, button)
                }
            }
        }
    }
}
