package com.tokopedia.recentview.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.recentview.R;
import com.tokopedia.recentview.view.viewmodel.LabelsDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/31/17.
 */

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.ViewHolder> {

    private static final double MEDIAN_VALUE = 135;
    private List<LabelsDataModel> listLabel;

    public LabelsAdapter() {
        this.listLabel = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label_recent_view,
                null);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.label.setText(listLabel.get(position).getTitle());
        LayerDrawable bgShape = (LayerDrawable) holder.label.getBackground();
        GradientDrawable background = (GradientDrawable) bgShape.findDrawableByLayerId(R.id
                .recent_view_container);
        String defaultWhiteColor = "#"+Integer.toHexString(
                ContextCompat.getColor(
                        holder.itemView.getContext(),
                        R.color.recentview_dms_label_background)
        );
        if (!listLabel.get(position).getColor().toLowerCase().equals(defaultWhiteColor)) {
            background.setColor(Color.parseColor(listLabel.get(position)
                    .getColor()));
            background.setStroke(0, 0);
        } else {
            if (holder.itemView.getContext() != null) {
                background.setColor(androidx.core.content.ContextCompat.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
                background.setStroke(1, androidx.core.content.ContextCompat.getColor(holder.itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N300));
            }

        }

        if (holder.itemView.getContext() == null) return;
        holder.label.setTextColor(getInverseColor(Color.parseColor(listLabel.get(position)
                .getColor()), holder.itemView.getContext()));
    }

    private int getInverseColor(int color, Context context) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= MEDIAN_VALUE ? androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700) : androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0);
    }

    @Override
    public int getItemCount() {
        return listLabel.size();
    }

    public void setList(List<LabelsDataModel> list) {
        this.listLabel = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
