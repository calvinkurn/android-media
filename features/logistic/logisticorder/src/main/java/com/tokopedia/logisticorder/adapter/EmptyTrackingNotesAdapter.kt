package com.tokopedia.logisticorder.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.EmptyAdapterLayoutBinding

/**
 * Created by kris on 5/11/18. Tokopedia
 */
class EmptyTrackingNotesAdapter : RecyclerView.Adapter<EmptyTrackingNotesAdapter.EmptyTrackingNotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyTrackingNotesViewHolder {
        val binding = EmptyAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmptyTrackingNotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmptyTrackingNotesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return DEFAULT_SIZE
    }

    inner class EmptyTrackingNotesViewHolder(private val binding: EmptyAdapterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            when (position) {
                0 -> binding.emptyTrackingNotes.setText(R.string.empty_notes_1)
                1 -> binding.emptyTrackingNotes.setText(R.string.empty_notes_2)
                2 -> binding.emptyTrackingNotes.setText(R.string.empty_notes_3)
            }
        }

    }

    companion object {
        private const val DEFAULT_SIZE = 3
    }
}