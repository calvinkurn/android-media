package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsViewHolder extends BaseViewHolder<Ad> {

    private TextView titleProduct;
    private View statusActiveDot;
    private TextView statusActive;
    private TextView promoPriceUsed;
    private TextView dailySpentTextView;
    private TextView dailyTotalTextView;
    private TextView pricePromoPerClick;
    private View progressBarLayout;
    private ProgressBar progressBarPromo;
    private TextView groupNameView;

    public TopAdsViewHolder(View view) {
        super(view);
        titleProduct = (TextView) view.findViewById(R.id.title_product);
        statusActiveDot = view.findViewById(R.id.status_active_dot);
        statusActive = (TextView) view.findViewById(R.id.status_active);
        promoPriceUsed = (TextView) view.findViewById(R.id.promo_price_used);
        dailySpentTextView = (TextView) view.findViewById(R.id.text_view_daily_spent);
        dailyTotalTextView = (TextView) view.findViewById(R.id.text_view_daily_total);
        pricePromoPerClick = (TextView) view.findViewById(R.id.price_promo_per_click);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progressBarPromo = (ProgressBar) view.findViewById(R.id.progress_bar);
        groupNameView = (TextView) view.findViewById(R.id.group_name);

        // programmatically styling for ProgressBar
        // http://stackoverflow.com/questions/16893209/how-to-customize-a-progress-bar-in-android
        Drawable draw=view.getContext().getResources().getDrawable(R.drawable.top_ads_progressbar);
        progressBarPromo.setProgressDrawable(draw);
    }

    @Override
    public void bindObject(Ad ad) {
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
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
        if(ad instanceof ProductAd){
            groupId = ((ProductAd) ad).getGroupId();
            groupName = ((ProductAd) ad).getGroupName();
        }
        if (TextUtils.isEmpty(ad.getPriceDailyBar()) || groupId > 0) {
            progressBarLayout.setVisibility(View.GONE);
            if(groupId > 0){
                groupNameView.setVisibility(View.VISIBLE);
                groupNameView.setText(promoPriceUsed.getContext().getString(R.string.top_ads_group_name_format_text,
                        promoPriceUsed.getContext().getString(R.string.label_top_ads_groups), groupName));
            }else{
                groupNameView.setVisibility(View.GONE);
            }
        } else {
            groupNameView.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBarPromo.setProgress((int) Double.parseDouble(ad.getPriceDailyBar()));
            dailySpentTextView.setText(ad.getPriceDailySpentFmt());
            dailyTotalTextView.setText(ad.getPriceDailyFmt());
        }
    }
}