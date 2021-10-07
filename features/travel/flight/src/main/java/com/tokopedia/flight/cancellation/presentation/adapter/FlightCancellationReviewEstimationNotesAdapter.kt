package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationReviewEstimationNotesViewHolder
import com.tokopedia.flight.databinding.ItemFlightCancellationEstimationNotesBinding

/**
 * @author by furqan on 11/08/2020
 */
class FlightCancellationReviewEstimationNotesAdapter : RecyclerView.Adapter<FlightCancellationReviewEstimationNotesViewHolder>() {

    private val estimationNotes: MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCancellationReviewEstimationNotesViewHolder =
            FlightCancellationReviewEstimationNotesViewHolder(
                ItemFlightCancellationEstimationNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun getItemCount(): Int = estimationNotes.size

    override fun onBindViewHolder(holder: FlightCancellationReviewEstimationNotesViewHolder, position: Int) {
        holder.bind(estimationNotes[position])
    }

    fun setData(estimatesNotes: List<String>) {
        this.estimationNotes.clear()
        this.estimationNotes.addAll(estimatesNotes)
        notifyDataSetChanged()
    }

}