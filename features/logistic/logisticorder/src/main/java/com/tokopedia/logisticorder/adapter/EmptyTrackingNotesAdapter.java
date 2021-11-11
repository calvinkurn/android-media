package com.tokopedia.logisticorder.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.logisticorder.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class EmptyTrackingNotesAdapter extends
        RecyclerView.Adapter<EmptyTrackingNotesAdapter.EmptyTrackingNotesViewHolder> {
    private static final int DEFAULT_SIZE = 3;

    @NonNull
    @Override
    public EmptyTrackingNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_adapter_layout, parent, false);
        return new EmptyTrackingNotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyTrackingNotesViewHolder holder, int position) {
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
        return DEFAULT_SIZE;
    }

    class EmptyTrackingNotesViewHolder extends RecyclerView.ViewHolder {

        private Typography notesText;

        EmptyTrackingNotesViewHolder(View itemView) {
            super(itemView);
            notesText = itemView.findViewById(R.id.empty_tracking_notes);
        }

    }

}
