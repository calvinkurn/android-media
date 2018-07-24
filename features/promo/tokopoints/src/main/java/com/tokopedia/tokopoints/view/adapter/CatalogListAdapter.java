package com.tokopedia.tokopoints.view.adapter;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CatalogListAdapter extends RecyclerView.Adapter<CatalogListAdapter.ViewHolder> {

    private List<CatalogsValueEntity> mItems;
    private CatalogPurchaseRedemptionPresenter mPresenter;
    private boolean mIsLimitEnable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quota, description, pointLabel, pointValue,
                timeLabel, timeValue, disabledError, btnContinue;
        ImageView imgBanner, imgTime, imgPoint;

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
        }
    }

    public CatalogListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CatalogsValueEntity> items) {
        this.mPresenter = presenter;
        this.mItems = items;
    }

    public CatalogListAdapter(CatalogPurchaseRedemptionPresenter presenter, List<CatalogsValueEntity> items, boolean isLimitEnable) {
        this.mPresenter = presenter;
        this.mItems = items;
        this.mIsLimitEnable = isLimitEnable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_coupon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatalogsValueEntity item = mItems.get(position);
        holder.btnContinue.setEnabled(!item.isDisabledButton());
        holder.description.setText(item.getTitle());
        holder.btnContinue.setText(R.string.tp_label_exchange); //TODO asked for server driven value
        ImageHandler.loadImageFit2(holder.imgBanner.getContext(), holder.imgBanner, item.getImageUrlMobile());

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
        } else {
            holder.quota.setVisibility(View.VISIBLE);
            StringBuilder upperText = new StringBuilder();
            for (int i = 0; i < item.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    upperText.append("<font color='#ff5722'>" + item.getUpperTextDesc().get(i) + "</font>");
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
            holder.btnContinue.setBackgroundResource(R.drawable.bg_button_disabled);
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.black_12));
        } else {
            holder.btnContinue.setBackgroundResource(R.drawable.bg_button_primary);
            holder.btnContinue.setTextColor(ContextCompat.getColor(holder.btnContinue.getContext(), R.color.white));
        }

        holder.btnContinue.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(item);
        });

        holder.imgBanner.setOnClickListener(v -> mPresenter.navigateToWebView(CommonConstant.WebLink.COUPON_DETAIL + mItems.get(position).getSlug()));
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
}
