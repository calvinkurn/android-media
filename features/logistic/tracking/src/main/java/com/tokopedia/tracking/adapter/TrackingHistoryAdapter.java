package com.tokopedia.tracking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tracking.R;
import com.tokopedia.tracking.utils.DateUtil;
import com.tokopedia.tracking.viewmodel.TrackingHistoryViewModel;

import java.util.List;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class TrackingHistoryAdapter extends RecyclerView.Adapter<TrackingHistoryAdapter.TrackingHistoryViewHolder> {

    private List<TrackingHistoryViewModel> trackingHistoryData;
    private DateUtil dateUtil;

    public TrackingHistoryAdapter(List<TrackingHistoryViewModel> trackingHistoryData, DateUtil dateUtil) {
        this.trackingHistoryData = trackingHistoryData;
        this.dateUtil = dateUtil;
    }

    @NonNull
    @Override
    public TrackingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tracking_history_view_holder, parent, false);
        return new TrackingHistoryViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingHistoryViewHolder holder, int position) {

        holder.title.setText(dateUtil.getFormattedDate(trackingHistoryData.get(position).getDate()));
        holder.time.setText(dateUtil.getFormattedTime(trackingHistoryData.get(position).getTime()));
        setTitleColor(holder, position);

        holder.comment.setVisibility(View.GONE);
        holder.description.setText(!TextUtils.isEmpty(trackingHistoryData.get(position).getStatus()) ?
                Html.fromHtml(trackingHistoryData.get(position).getStatus()) : "");
        holder.dot.setColorFilter(Color.parseColor(trackingHistoryData.get(position).getColor()));
        if (position == trackingHistoryData.size() - 1) {
            holder.dotTrail.setVisibility(View.GONE);
        } else {
            holder.dotTrail.setVisibility(View.VISIBLE);
            holder.dotTrail.setBackgroundColor(
                    Color.parseColor(trackingHistoryData.get(position).getColor())
            );
        }
    }

    private void setTitleColor(TrackingHistoryViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setTextColor((Color.parseColor(
                    trackingHistoryData.get(position).getColor()
            )));
        } else {
            holder.title.setTextColor(
                    holder.context.getResources().getColor(R.color.black_70));
        }
    }

    @Override
    public int getItemCount() {
        return trackingHistoryData.size();
    }

    class TrackingHistoryViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView title;

        private TextView time;

        private TextView description;

        private ImageView dot;

        private View dotTrail;

        private TextView comment;

        TrackingHistoryViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            title = itemView.findViewById(R.id.title);

            time = itemView.findViewById(R.id.date);

            description = itemView.findViewById(R.id.description);

            dot = itemView.findViewById(R.id.dot_image);

            dotTrail = itemView.findViewById(R.id.dot_trail);

            comment = itemView.findViewById(R.id.comment);

        }
    }
}
