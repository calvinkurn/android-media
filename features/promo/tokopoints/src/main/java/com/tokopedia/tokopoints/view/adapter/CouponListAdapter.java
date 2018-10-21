package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.activity.CouponCatalogDetailsActivity;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.ViewHolder> {

    private List<CouponValueEntity> mItems;
    private CatalogPurchaseRedemptionPresenter mPresenter;
    private boolean mIsLimitEnable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, label, value, btnContinue;
        ImageView imgBanner, imgLabel;
        public boolean isVisited = false;
        /*This section is exclusively for handling timer*/
        public CountDownTimer timer;
        public ProgressBar progressTimer;

        public ViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.text_description);
            label = view.findViewById(R.id.text_time_label);
            value = view.findViewById(R.id.text_time_value);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgLabel = view.findViewById(R.id.img_time);
            progressTimer = view.findViewById(R.id.progress_timer);
        }
    }

    public CouponListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CouponValueEntity> items) {
        this.mItems = items;
        this.mPresenter = presenter;
    }

    public CouponListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CouponValueEntity> items, boolean isLimitEnable) {
        this.mItems = items;
        this.mPresenter = presenter;
        this.mIsLimitEnable = isLimitEnable;

    }

    @Override
    public CouponListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon, parent, false);

        return new CouponListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CouponListAdapter.ViewHolder holder, int position) {
        final CouponValueEntity item = mItems.get(position);
        holder.description.setText(item.getTitle());
        ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner, item.getThumbnailUrlMobile());

        if (item.getUsage() != null) {
            holder.imgLabel.setImageResource(R.drawable.ic_tp_time);
            holder.label.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.imgLabel.setVisibility(View.VISIBLE);
            holder.value.setText(item.getUsage().getUsageStr());
            holder.label.setText(item.getUsage().getText());
            if (item.getUsage().getBtnUsage() != null) {
                holder.btnContinue.setText(item.getUsage().getBtnUsage().getText());

                if(item.getUsage().getBtnUsage().getType().equalsIgnoreCase("invisible")) {
                    holder.btnContinue.setVisibility(View.GONE);
                } else {
                    holder.btnContinue.setVisibility(View.VISIBLE);
                }
            }
        }

        holder.btnContinue.setBackgroundResource(R.drawable.bg_button_green);
        holder.value.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.medium_green));
        holder.imgLabel.setImageResource(R.drawable.bg_tp_time_greeen);

        holder.btnContinue.setOnClickListener(v -> {
            mPresenter.showRedeemCouponDialog(item.getCta(), item.getCode(), item.getTitle());

            AnalyticsTrackerUtil.sendEvent(holder.imgBanner.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    item.getTitle());
        });
        holder.imgBanner.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConstant.EXTRA_COUPON_CODE, mItems.get(position).getCode());
            holder.imgBanner.getContext().startActivity(CouponCatalogDetailsActivity.getCouponDetail(holder.imgBanner.getContext(), bundle), bundle);

            //TODO need to add transectinal ga
            AnalyticsTrackerUtil.sendEvent(holder.imgBanner.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                    AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL);
        });


        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer.cancel();
        }

        holder.btnContinue.setEnabled(true);
        if (item.getUsage().getActiveCountDown() < 1) {
            if (item.getUsage().getExpiredCountDown() > 0
                    && item.getUsage().getExpiredCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                holder.progressTimer.setMax((int) CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S);
                holder.progressTimer.setVisibility(View.VISIBLE);
                holder.timer = new CountDownTimer(item.getUsage().getExpiredCountDown() * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        item.getUsage().setExpiredCountDown(l / 1000);
                        int seconds = (int) (l / 1000) % 60;
                        int minutes = (int) ((l / (1000 * 60)) % 60);
                        int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                        holder.value.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                        holder.progressTimer.setProgress((int) l / 1000);
                        holder.value.setPadding(holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                holder.label.getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall));
                    }

                    @Override
                    public void onFinish() {
                        holder.value.setText("00 : 00 : 00");
                        holder.btnContinue.setText("Expired");
                        holder.btnContinue.setEnabled(false);
                        holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.black_12));
                    }
                }.start();
            } else {
                holder.progressTimer.setVisibility(View.GONE);
                holder.btnContinue.setText(item.getUsage().getBtnUsage().getText());
                holder.btnContinue.setEnabled(true);
                holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.white));
                holder.value.setPadding(0, 0, 0, 0);
            }
        } else {
            if (item.getUsage().getActiveCountDown() > 0) {
                holder.btnContinue.setEnabled(false);
                if (item.getUsage().getActiveCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    holder.timer = new CountDownTimer(item.getUsage().getActiveCountDown() * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            item.getUsage().setActiveCountDown(l / 1000);
                            int seconds = (int) (l / 1000) % 60;
                            int minutes = (int) ((l / (1000 * 60)) % 60);
                            int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                            holder.btnContinue.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {
                            holder.btnContinue.setText(item.getUsage().getBtnUsage().getText());
                            holder.btnContinue.setEnabled(true);
                        }
                    }.start();
                }
            } else {
                holder.btnContinue.setText(item.getUsage().getUsageStr());
                holder.btnContinue.setEnabled(true);
                holder.progressTimer.setVisibility(View.GONE);
                holder.value.setPadding(0, 0, 0, 0);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mIsLimitEnable) {
            return mItems.size() > CommonConstant.HOMEPAGE_PAGE_SIZE ? CommonConstant.HOMEPAGE_PAGE_SIZE : mItems.size();
        } else {
            return mItems.size();
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        CouponValueEntity data = mItems.get(holder.getAdapterPosition());
        if (data == null) {
            return;
        }

        if (!holder.isVisited) {
            Map<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(data.getCatalogId()));
            item.put("name", data.getTitle());
            item.put("position", String.valueOf(holder.getAdapterPosition()));
            item.put("creative", data.getTitle());
            item.put("creative_url", data.getImageUrlMobile());
            item.put("promo_code", data.getCode());

            Map<String, List<Map<String, String>>> promotions = new HashMap<>();
            promotions.put("promotions", Arrays.asList(item));

            Map<String, Map<String, List<Map<String, String>>>> promoView = new HashMap<>();
            promoView.put("promoView", promotions);

            AnalyticsTrackerUtil.sendECommerceEvent(holder.btnContinue.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                    AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                    data.getTitle(), promoView);

            holder.isVisited = true;
        }
    }

    private void sendClickEvent(Context context, CouponValueEntity data, int position) {
        Map<String, String> item = new HashMap<>();
        item.put("id", String.valueOf(data.getCatalogId()));
        item.put("name", data.getTitle());
        item.put("position", String.valueOf(position));
        item.put("creative", data.getTitle());
        item.put("creative_url", data.getImageUrlMobile());
        item.put("promo_code", data.getCode());

        Map<String, List<Map<String, String>>> promotions = new HashMap<>();
        promotions.put("promotions", Arrays.asList(item));

        Map<String, Map<String, List<Map<String, String>>>> promoClick = new HashMap<>();
        promoClick.put("promoClick", promotions);

        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.getTitle(), promoClick);
    }
}
