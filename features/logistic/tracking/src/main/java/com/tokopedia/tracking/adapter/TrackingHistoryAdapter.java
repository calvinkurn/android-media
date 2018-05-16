package com.tokopedia.tracking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tracking.R;
import com.tokopedia.tracking.viewmodel.TrackingHistoryViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class TrackingHistoryAdapter extends RecyclerView.Adapter<TrackingHistoryAdapter.TrackingHistoryViewHolder> {

    private List<TrackingHistoryViewModel> trackingHistoryData;

    public TrackingHistoryAdapter(List<TrackingHistoryViewModel> trackingHistoryData) {
        this.trackingHistoryData = trackingHistoryData;
    }

    @Override
    public TrackingHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_trailing_bullet, parent, false);
        return new TrackingHistoryViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(TrackingHistoryViewHolder holder, int position) {

        holder.title.setText(formattedDate(trackingHistoryData.get(position)));
        setTitleColor(holder, position);

        holder.comment.setVisibility(View.GONE);
        holder.description.setText(Html.fromHtml(trackingHistoryData.get(position).getStatus()));
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

    private String formattedDate(TrackingHistoryViewModel viewModel) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "EEEE, dd MMM YYY";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,
                new Locale("in", "ID"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,
                new Locale("in","ID"));

        try {
            Date date = inputFormat.parse(viewModel.getTime());
            return outputFormat.format(date);
        } catch(ParseException e) {
            return viewModel.getTime();
        }
    }

    private void setTitleColor(TrackingHistoryViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setTextColor((Color.parseColor(
                    trackingHistoryData.get(position).getColor()
            )));
        } else holder.title.setTextColor(
                holder.context.getResources().getColor(R.color.black_70));
    }

    @Override
    public int getItemCount() {
        return trackingHistoryData.size();
    }

    class TrackingHistoryViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView title;

        private TextView description;

        private TextView time;

        private ImageView dot;

        private View dotTrail;

        private TextView comment;

        TrackingHistoryViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            title = itemView.findViewById(R.id.title);

            description = itemView.findViewById(R.id.description);

            time = itemView.findViewById(R.id.date);

            dot = itemView.findViewById(R.id.dot_image);

            dotTrail = itemView.findViewById(R.id.dot_trail);

            comment = itemView.findViewById(R.id.comment);

        }
    }
}
