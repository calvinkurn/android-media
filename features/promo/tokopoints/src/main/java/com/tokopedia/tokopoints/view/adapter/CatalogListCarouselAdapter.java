package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.activity.CouponCatalogDetailsActivity;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogListCarouselAdapter extends RecyclerView.Adapter<CatalogListCarouselAdapter.ViewHolder> {

    private List<CatalogsValueEntity> mItems;
    private CatalogPurchaseRedemptionPresenter mPresenter;
    private boolean mIsLimitEnable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quota, description, pointLabel, pointValue,
                timeLabel, timeValue, disabledError, btnContinue,
                labelPoint, textDiscount;
        ImageView imgBanner, imgTime, imgPoint;
        ProgressBar pbQuota;
        boolean isVisited = false;

        public ViewHolder(View view) {
            super(view);
            quota = view.findViewById(R.id.text_quota_count);
            description = view.findViewById(R.id.text_description);
            pointLabel = view.findViewById(R.id.text_my_points_label);
            pointValue = view.findViewById(R.id.text_point_value);
            timeLabel = view.findViewById(R.id.text_time_label);
            timeValue = view.findViewById(R.id.text_time_value);
            disabledError = view.findViewById(R.id.text_disabled_error);
            btnContinue = view.findViewById(R.id.button_continue);
            imgBanner = view.findViewById(R.id.img_banner);
            imgTime = view.findViewById(R.id.img_time);
            imgPoint = view.findViewById(R.id.img_points_stack);
            labelPoint = view.findViewById(R.id.text_point_label);
            textDiscount = view.findViewById(R.id.text_point_discount);
            pbQuota = view.findViewById(R.id.progress_timer_quota);
        }
    }

    public CatalogListCarouselAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CatalogsValueEntity> items) {
        this.mPresenter = presenter;
        this.mItems = items;
    }

    public CatalogListCarouselAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CatalogsValueEntity> items, boolean isLimitEnable) {
        this.mPresenter = presenter;
        this.mItems = items;
        this.mIsLimitEnable = isLimitEnable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon_carousel, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatalogsValueEntity item = mItems.get(position);
        holder.btnContinue.setEnabled(!item.isDisabledButton());
        holder.description.setText(item.getTitle());
        holder.btnContinue.setText(R.string.tp_label_exchange); //TODO asked for server driven value
        ImageHandler.loadImageFitCenter(holder.imgBanner.getContext(), holder.imgBanner, item.getThumbnailUrlMobile());
        //setting points info if exist in response
        if (item.getPointsStr() == null || item.getPointsStr().isEmpty()) {
            holder.pointValue.setVisibility(View.GONE);
            holder.imgPoint.setVisibility(View.GONE);
        } else {
            holder.pointValue.setVisibility(View.VISIBLE);
            holder.imgPoint.setVisibility(View.VISIBLE);
            holder.pointValue.setText(item.getPointsStr());
        }

        //setting expiry time info if exist in response
        if (item.getExpiredLabel() == null || item.getExpiredLabel().isEmpty()) {
            holder.timeLabel.setVisibility(View.GONE);
            holder.timeValue.setVisibility(View.GONE);
            holder.imgTime.setVisibility(View.GONE);
        } else {
            holder.timeLabel.setVisibility(View.VISIBLE);
            holder.timeValue.setVisibility(View.VISIBLE);
            holder.imgTime.setVisibility(View.VISIBLE);
            holder.timeLabel.setText(item.getExpiredLabel());
            holder.timeValue.setText(item.getExpiredStr());
        }

        //Quota text handling
        if (item.getUpperTextDesc() == null || item.getUpperTextDesc().isEmpty()) {
            holder.quota.setVisibility(View.GONE);
            holder.pbQuota.setVisibility(View.GONE);
        } else {
            holder.quota.setVisibility(View.VISIBLE);
            holder.pbQuota.setVisibility(View.VISIBLE);
            holder.pbQuota.setProgress(0);
            StringBuilder upperText = new StringBuilder();

            if (item.getCatalogType() == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.getContext(), R.color.red_150));
            } else {
                holder.quota.setTextColor(ContextCompat.getColor(holder.quota.getContext(), R.color.black_38));
            }

            for (int i = 0; i < item.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    if (item.getCatalogType() == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                        //for flash sale progress bar handling
                        holder.pbQuota.setProgress(item.getQuota());
                        upperText.append(item.getUpperTextDesc().get(i));
                    } else {
                        //exclusive case for handling font color of second index.
                        upperText.append("<font color='#ff5722'>" + item.getUpperTextDesc().get(i) + "</font>");
                    }
                } else {
                    upperText.append(item.getUpperTextDesc().get(i)).append(" ");
                }
            }

            holder.quota.setText(MethodChecker.fromHtml(upperText.toString()));
        }

        //Quota text handling
        if (item.getDisableErrorMessage() == null || item.getDisableErrorMessage().isEmpty()) {
            holder.disabledError.setVisibility(View.GONE);
        } else {
            holder.disabledError.setVisibility(View.VISIBLE);
            holder.disabledError.setText(item.getDisableErrorMessage());
        }

        //disabling the coupons if not eligible for current membership
        if (item.isDisabled()) {
            ImageUtil.dimImage(holder.imgBanner);
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(holder.imgBanner);
            holder.pointValue.setTextColor(ContextCompat.getColor(holder.pointValue.getContext(), R.color.orange_red));
        }

        if (item.isDisabledButton()) {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.black_12));
        } else {
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.white));
        }

        if (item.getPointsSlash() <= 0) {
            holder.labelPoint.setVisibility(View.GONE);
        } else {
            holder.labelPoint.setVisibility(View.VISIBLE);
            holder.labelPoint.setText(item.getPointsSlashStr());
            holder.labelPoint.setPaintFlags(holder.labelPoint.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (item.getDiscountPercentage() <= 0) {
            holder.textDiscount.setVisibility(View.GONE);
        } else {
            holder.textDiscount.setVisibility(View.VISIBLE);
            holder.textDiscount.setText(item.getDiscountPercentageStr());
        }

        holder.btnContinue.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(item);

            AnalyticsTrackerUtil.sendEvent(holder.btnContinue.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    item.getBaseCode());
        });

        holder.imgBanner.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConstant.EXTRA_CATALOG_CODE, mItems.get(position).getSlug());
            holder.imgBanner.getContext().startActivity(CouponCatalogDetailsActivity.getCatalogDetail(holder.imgBanner.getContext(), bundle), bundle);
            sendClickEvent(holder.imgBanner.getContext(), item, position);
        });


        holder.btnContinue.setVisibility(item.isShowTukarButton() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (mIsLimitEnable) {
            return mItems.size() > CommonConstant.HOMEPAGE_PAGE_SIZE ? CommonConstant.HOMEPAGE_PAGE_SIZE : mItems.size();
        } else {
            return mItems.size();
        }
    }

    public void updateItems(List<CatalogsValueEntity> items) {
        this.mItems = items;
    }

    public List<CatalogsValueEntity> getItems() {
        return this.mItems;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        CatalogsValueEntity data = mItems.get(holder.getAdapterPosition());
        if (data == null) {
            return;
        }

        if (!holder.isVisited) {
            Map<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(data.getId()));
            item.put("name", data.getTitle());
            item.put("position", String.valueOf(holder.getAdapterPosition()));
            item.put("creative", data.getTitle());
            item.put("creative_url", data.getImageUrlMobile());
            item.put("promo_code", data.getBaseCode());

            Map<String, List<Map<String, String>>> promotions = new HashMap<>();
            promotions.put("promotions", Arrays.asList(item));

            Map<String, Map<String, List<Map<String, String>>>> promoView = new HashMap<>();
            promoView.put("promoView", promotions);

            AnalyticsTrackerUtil.sendECommerceEvent(holder.btnContinue.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
                    data.getTitle(), promoView);

            holder.isVisited = true;
        }
    }

    private void sendClickEvent(Context context, CatalogsValueEntity data, int position) {
        Map<String, String> item = new HashMap<>();
        item.put("id", String.valueOf(data.getId()));
        item.put("name", data.getTitle());
        item.put("position", String.valueOf(position));
        item.put("creative", data.getTitle());
        item.put("creative_url", data.getImageUrlMobile());
        item.put("promo_code", data.getBaseCode());

        Map<String, List<Map<String, String>>> promotions = new HashMap<>();
        promotions.put("promotions", Arrays.asList(item));

        Map<String, Map<String, List<Map<String, String>>>> promoClick = new HashMap<>();
        promoClick.put("promoClick", promotions);

        AnalyticsTrackerUtil.sendECommerceEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
                data.getTitle(), promoClick);
    }
}
