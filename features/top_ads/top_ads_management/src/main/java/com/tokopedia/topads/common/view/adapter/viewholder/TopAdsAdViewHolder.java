package com.tokopedia.topads.common.view.adapter.viewholder;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.design.image.ImageLoader;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by hadi.putra on 07/05/18.
 */

public class TopAdsAdViewHolder<T extends Ad & Visitable> extends BaseMultipleCheckViewHolder<T> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_top_ads_ad;

    private TextView titleProduct;
    private TextView totalSoldTxt;
    private TextView profitTxt;
    private View statusActiveDot;
    private TextView statusActive;
    private TextView promoPriceUsed;
    private TextView dailySpentTextView;
    private TextView dailyTotalTextView;
    private TextView pricePromoPerClick;
    private View progressBarLayout;
    private ProgressBar progressBarPromo;
    private TextView groupNameView;
    private View optionImageButton;
    private CheckBox checkBox;
    private ImageView thumb;
    private View promoPerClickContainer;
    private View promoPriceUsedContainer;
    private View statusActiveContainer;

    public TopAdsAdViewHolder(View view) {
        super(view);
        titleProduct = (TextView) view.findViewById(R.id.title_product);
        totalSoldTxt = (TextView) view.findViewById(R.id.total_sold);
        profitTxt = (TextView) view.findViewById(R.id.profit);
        statusActiveDot = view.findViewById(R.id.status_active_dot);
        statusActive = (TextView) view.findViewById(R.id.status_active);
        promoPriceUsed = (TextView) view.findViewById(R.id.promo_price_used);
        dailySpentTextView = (TextView) view.findViewById(R.id.text_view_daily_spent);
        dailyTotalTextView = (TextView) view.findViewById(R.id.text_view_daily_total);
        pricePromoPerClick = (TextView) view.findViewById(R.id.price_promo_per_click);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progressBarPromo = (ProgressBar) view.findViewById(R.id.progress_bar);
        groupNameView = (TextView) view.findViewById(R.id.group_name);
        optionImageButton = view.findViewById(R.id.image_button_option);
        checkBox = (CheckBox) view.findViewById(R.id.check_box_product);
        thumb = (ImageView) view.findViewById(R.id.thumb);
        promoPerClickContainer = (View) view.findViewById(R.id.promo_per_click_container);
        promoPriceUsedContainer = (View) view.findViewById(R.id.promo_price_used_container);
        statusActiveContainer = (View) view.findViewById(R.id.status_active_container);

        // programmatically styling for ProgressBar
        // http://stackoverflow.com/questions/16893209/how-to-customize-a-progress-bar-in-android
        Drawable draw = view.getContext().getResources().getDrawable(R.drawable.top_ads_progressbar);
        progressBarPromo.setProgressDrawable(draw);
    }

    @Override
    public void bind(final T ad) {
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
        ImageLoader.LoadImage(thumb, ad.getProductImageUrl());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.ic_status_green);
                break;
            default:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
        }
        pricePromoPerClick.setText(promoPriceUsed.getContext().getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        promoPriceUsed.setText(ad.getStatTotalSpent());

        long groupId = -1;
        String groupName = "";
        if (ad instanceof ProductAd) {
            groupId = ((ProductAd) ad).getGroupId();
            groupName = ((ProductAd) ad).getGroupName();
        }
        if (TextUtils.isEmpty(ad.getPriceDailyBar()) || groupId > 0) {
            progressBarLayout.setVisibility(View.GONE);
            if (groupId > 0) {
                groupNameView.setVisibility(View.VISIBLE);
                groupNameView.setText(promoPriceUsed.getContext().getString(R.string.top_ads_group_name_format_text,
                        promoPriceUsed.getContext().getString(R.string.label_top_ads_groups), groupName));
            } else {
                groupNameView.setVisibility(View.GONE);
            }
        } else {
            groupNameView.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBarPromo.setProgress((int) Double.parseDouble(ad.getPriceDailyBar()));
            dailySpentTextView.setText(ad.getPriceDailySpentFmt());
            dailyTotalTextView.setText(ad.getPriceDailyFmt());
        }

        optionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionMoreCallback != null) {
                    optionMoreCallback.onClickMore(ad);
                }
            }
        });
        if(ad.isAutoAds()){
            totalSoldTxt.setVisibility(View.VISIBLE);
            profitTxt.setVisibility(View.VISIBLE);
            totalSoldTxt.setText(String.format(getString(R.string.label_total_gross_profit), ad.getStatTotalGrossProfit()));
            profitTxt.setText(String.format(getString(R.string.label_total_sold), ad.getStatTotalSold()));
            groupNameView.setVisibility(View.GONE);
            promoPerClickContainer.setVisibility(View.GONE);
            promoPriceUsedContainer.setVisibility(View.GONE);
            statusActiveContainer.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.GONE);
        } else {
            totalSoldTxt.setVisibility(View.GONE);
            profitTxt.setVisibility(View.GONE);
            promoPerClickContainer.setVisibility(View.VISIBLE);
            promoPriceUsedContainer.setVisibility(View.VISIBLE);
            statusActiveContainer.setVisibility(View.VISIBLE);
            progressBarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
        setBackground(checked);
    }

    @Override
    public void showCheckButton(boolean isInActionMode) {
        if (isInActionMode) {
            checkBox.setVisibility(View.VISIBLE);
            optionImageButton.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.GONE);
            optionImageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showOptionButton(boolean enable) {
        optionImageButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void setBackground(boolean isChecked) {
        if (isChecked) {
            if (itemView instanceof CardView) {
                ((CardView) itemView).setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
            }
        } else {
            if (itemView instanceof CardView) {
                ((CardView) itemView).setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            }
        }
    }

    @Override
    public void bindObject(final T item, boolean isChecked) {
        bind(item);
        setChecked(isChecked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(item, checkBox.isChecked());
                }
                setChecked(checkBox.isChecked());
            }
        });
    }
}
