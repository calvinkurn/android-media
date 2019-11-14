package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.activity.CouponDetailActivity;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CouponListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CouponValueEntity> mItems;
    private static final int VIEW_HEADER = 0;
    private static final int VIEW_DATA = 1;
    private RecyclerView mRecyclerView;

    // Define a ViewHolder for Footer view
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                // Do whatever you want on clicking the item
                RouteManager.route(v.getContext(), ApplinkConstInternalPromo.TOKOPOINTS_COUPON);
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView label, value, tvMinTxnValue, tvMinTxnLabel;
        ImageView imgBanner, imgLabel, ivMinTxn;
        public boolean isVisited = false;
        /*This section is exclusively for handling timer*/
        public CountDownTimer timer;
        public ProgressBar progressTimer;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_time_label);
            value = view.findViewById(R.id.text_time_value);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_time);
            ivMinTxn = view.findViewById(R.id.iv_rp);
            tvMinTxnValue = view.findViewById(R.id.tv_min_txn_value);
            tvMinTxnLabel = view.findViewById(R.id.tv_min_txn_label);
            progressTimer = view.findViewById(R.id.progress_timer);
        }

        public void onDetach() {
            if (timer != null){
                timer.cancel();
                timer = null;
            }
        }
    }

    public CouponListAdapter(List<CouponValueEntity> items) {
        this.mItems = items;

        if (mItems != null) {
            mItems.add(0, new CouponValueEntity());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == VIEW_HEADER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tp_item_my_coupon_section_header, parent, false);
            ;
            HeaderViewHolder vh = new HeaderViewHolder(itemView);
            return vh;
        }

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_my_coupon_section, parent, false);

        return new CouponListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pHolder, int position) {
        final CouponValueEntity item = mItems.get(position);

        if (pHolder instanceof CouponListAdapter.ViewHolder) {
            CouponListAdapter.ViewHolder holder = (CouponListAdapter.ViewHolder) pHolder;
            ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner,
                    TextUtils.isEmpty(item.getThumbnailUrlMobile()) ? item.getImageUrlMobile() : item.getThumbnailUrlMobile());

            if (item.getUsage() != null) {
                holder.label.setVisibility(View.VISIBLE);
                holder.value.setVisibility(View.VISIBLE);
                holder.imgLabel.setVisibility(View.VISIBLE);
                holder.value.setText(item.getUsage().getUsageStr().trim());
                holder.label.setText(item.getUsage().getText());
            }

            if (TextUtils.isEmpty(item.getMinimumUsageLabel())) {
                holder.tvMinTxnLabel.setVisibility(View.GONE);
                holder.ivMinTxn.setVisibility(View.GONE);
            } else {
                holder.ivMinTxn.setVisibility(View.VISIBLE);
                holder.tvMinTxnLabel.setVisibility(View.VISIBLE);
                holder.tvMinTxnLabel.setText(item.getMinimumUsageLabel());
            }

            if (TextUtils.isEmpty(item.getMinimumUsage())) {
                holder.tvMinTxnValue.setVisibility(View.GONE);
            } else {
                holder.tvMinTxnValue.setVisibility(View.VISIBLE);
                holder.tvMinTxnValue.setText(item.getMinimumUsage());
            }

            holder.imgBanner.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(CommonConstant.EXTRA_COUPON_CODE, mItems.get(position).getCode());
                holder.imgBanner.getContext().startActivity(CouponDetailActivity.getCouponDetail(holder.imgBanner.getContext(), bundle), bundle);
            });


            /*This section is exclusively for handling flash-sale timer*/
            if (holder.timer != null) {
                holder.timer.cancel();
            }

            if (item.getUsage().getActiveCountDown() < 1) {
                if (item.getUsage().getExpiredCountDown() > 0
                        && item.getUsage().getExpiredCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    holder.progressTimer.setMax((int) CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S);
                    holder.progressTimer.setVisibility(View.VISIBLE);
                    holder.value.setVisibility(View.VISIBLE);
                    if (holder.timer != null){
                        holder.timer.cancel();
                    }
                    holder.timer = new CountDownTimer(item.getUsage().getExpiredCountDown() * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            item.getUsage().setExpiredCountDown(l / 1000);
                            int seconds = (int) (l / 1000) % 60;
                            int minutes = (int) ((l / (1000 * 60)) % 60);
                            int hours = (int)  ((l / (1000 * 60 * 60)) % 24);
                            holder.value.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                            holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.medium_green));
                            holder.progressTimer.setProgress((int) l / 1000);
                            holder.value.setPadding(holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_small),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_small),
                                    holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall));
                        }

                        @Override
                        public void onFinish() {
                            holder.value.setText("00 : 00 : 00");
                        }
                    }.start();
                } else {
                    holder.progressTimer.setVisibility(View.GONE);
                    holder.value.setPadding(0, 0, 0, 0);
                    holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.black_70));

                }
            } else {
                holder.progressTimer.setVisibility(View.GONE);
                holder.value.setTextColor(ContextCompat.getColor(holder.value.getContext(), com.tokopedia.design.R.color.black_70));
            }

            enableOrDisableImages(holder, item);
        } else if (pHolder instanceof HeaderViewHolder) {
        }

    }

    private void enableOrDisableImages(ViewHolder holder, CouponValueEntity item) {
        if (item.getUsage() != null) {
            if (item.getUsage().getActiveCountDown() > 0
                    || item.getUsage().getExpiredCountDown() <= 0) {
                disableImages(holder);
            } else {
                enableImages(holder);
            }
        } else {
            disableImages(holder);
        }
    }

    private void disableImages(ViewHolder holder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(), R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void enableImages(ViewHolder holder) {
        holder.imgLabel.setColorFilter(ContextCompat.getColor(holder.imgLabel.getContext(),  com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.ivMinTxn.setColorFilter(ContextCompat.getColor(holder.ivMinTxn.getContext(), com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems == null) {
            return super.getItemViewType(position);
        }

        if (position == 0) {
            // This is where we'll add footer.
            return VIEW_HEADER;
        }

        return VIEW_DATA;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).onDetach();
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void onDestroyView() {
        if (mRecyclerView != null){
            for (int i = 0 ;i < mRecyclerView.getChildCount();i++){
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
                if (holder instanceof ViewHolder) {
                    ((ViewHolder) holder).onDetach();
                }
            }
        }

    }
}
