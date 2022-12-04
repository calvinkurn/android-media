package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventRedeemParticipantBinding
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.ParticipantDetail
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

class EventRedeemVisitorAdapter: RecyclerView.Adapter<EventRedeemVisitorAdapter.ViewHolder>() {

    private var participants: List<ParticipantDetail> = mutableListOf()

    override fun getItemCount(): Int = participants.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventRedeemParticipantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(participants[position])
    }

    fun addParticipant(participants: List<ParticipantDetail>) {
        this.participants = participants
        notifyItemRangeChanged(Int.ZERO, (participants.size - Int.ONE))
    }

    inner class ViewHolder(private val binding: ItemEventRedeemParticipantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: ParticipantDetail) {
            with(binding) {
                tgTitle.text = participant.label
                tgValue.text = participant.value
            }
        }
    }
}
