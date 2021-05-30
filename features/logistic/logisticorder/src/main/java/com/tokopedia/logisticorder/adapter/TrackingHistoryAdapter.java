package com.tokopedia.logisticorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.logisticorder.R;
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel;
import com.tokopedia.logisticorder.utils.DateUtil;
import com.tokopedia.logisticorder.utils.TrackingPageUtil;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import timber.log.Timber;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class TrackingHistoryAdapter extends RecyclerView.Adapter<TrackingHistoryAdapter.TrackingHistoryViewHolder> {

    public interface OnImageClicked {
        void onImageItemClicked(String imageId, Long orderId);
    }

    private List<TrackHistoryModel> trackingHistoryData;
    private DateUtil dateUtil;
    private Long orderId;
    private OnImageClicked listener;

    public TrackingHistoryAdapter(List<TrackHistoryModel> trackingHistoryData, DateUtil dateUtil, Long orderId, OnImageClicked listener) {
        this.trackingHistoryData = trackingHistoryData;
        this.dateUtil = dateUtil;
        this.orderId = orderId;
        this.listener = listener;
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

        holder.description.setText(!TextUtils.isEmpty(trackingHistoryData.get(position).getStatus()) ?
                Html.fromHtml(trackingHistoryData.get(position).getStatus()) : "");

        if (position == 0) {
            holder.dot.setColorFilter(holder.context.getResources().getColor(R.color.tracking_primary_color));
            holder.dotTrail.setVisibility(View.VISIBLE);
            holder.dotTrail.setBackgroundColor(holder.context.getResources().getColor(R.color.tracking_primary_color));
        } else if (position == trackingHistoryData.size() - 1) {
            holder.dot.setColorFilter(holder.context.getResources().getColor(R.color.tracking_secondary_color));
            holder.dotTrail.setVisibility(View.GONE);
        } else {
            holder.dot.setColorFilter(holder.context.getResources().getColor(R.color.tracking_secondary_color));
            holder.dotTrail.setVisibility(View.VISIBLE);
            holder.dotTrail.setBackgroundColor(holder.context.getResources().getColor(R.color.tracking_secondary_color));
        }

        if (trackingHistoryData.get(position).getProof().getImageId().isEmpty()) {
            holder.imageProof.setVisibility(View.GONE);
        } else {
            holder.imageProof.setVisibility(View.VISIBLE);
            UserSessionInterface userSession = new UserSession(holder.context);
            String url = TrackingPageUtil.INSTANCE.getDeliveryImage(trackingHistoryData.get(position).getProof().getImageId(), orderId, "small",
                    userSession.getUserId(), 1, userSession.getDeviceId());

            Glide.with(holder.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(holder.context.getDrawable(R.drawable.ic_image_error))
                    .error(holder.context.getDrawable(R.drawable.ic_image_error))
                    .dontAnimate()
                    .into(holder.imageProof);

            holder.imageProof.setOnClickListener(view -> {
                listener.onImageItemClicked(trackingHistoryData.get(position).getProof().getImageId(), orderId);
            });
        }
    }

    private void setTitleColor(TrackingHistoryViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setTextColor(holder.context.getResources().getColor(R.color.tracking_primary_color));
        } else {
            holder.title.setTextColor(
                    holder.context.getResources().getColor(com.tokopedia.design.R.color.black_70));
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

        private ImageUnify imageProof;

        TrackingHistoryViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            title = itemView.findViewById(R.id.title);

            time = itemView.findViewById(R.id.date);

            description = itemView.findViewById(R.id.description);

            dot = itemView.findViewById(R.id.dot_image);

            dotTrail = itemView.findViewById(R.id.dot_trail);

            imageProof = itemView.findViewById(R.id.img_proof);

        }
    }
}
