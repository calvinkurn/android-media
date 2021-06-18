package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import kotlinx.android.synthetic.main.item_flight_cancellation_estimation_notes.view.*

/**
 * @author by furqan on 11/08/2020
 */
class FlightCancellationReviewEstimationNotesViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    fun bind(note: String) {
        with(itemView) {
            tvFlightEstimationNotes.text = note
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_estimation_notes
    }

}