package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightCancellationEstimationNotesBinding

/**
 * @author by furqan on 11/08/2020
 */
class FlightCancellationReviewEstimationNotesViewHolder(val binding: ItemFlightCancellationEstimationNotesBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: String) {
        with(binding) {
            tvFlightEstimationNotes.text = note
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_estimation_notes
    }

}