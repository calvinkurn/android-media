package com.tokopedia.tracking.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tracking.R;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class EmptyTrackingNotesAdapter extends RecyclerView.Adapter<EmptyTrackingNotesAdapter.EmptyTrackingNotesViewHolder> {

    @Override
    public EmptyTrackingNotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_adapter_layout, parent, false);
        return new EmptyTrackingNotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmptyTrackingNotesViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.notesText.setText(R.string.empty_notes_1);
                break;
            case 1:
                holder.notesText.setText(R.string.empty_notes_2);
                break;
            case 2:
                holder.notesText.setText(R.string.empty_notes_3);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class EmptyTrackingNotesViewHolder extends RecyclerView.ViewHolder {

        private TextView notesText;

        EmptyTrackingNotesViewHolder(View itemView) {
            super(itemView);
            notesText = itemView.findViewById(R.id.empty_tracking_notes);
        }

    }

}
