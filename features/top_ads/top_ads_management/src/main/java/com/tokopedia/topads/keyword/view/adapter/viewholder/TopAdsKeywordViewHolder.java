package com.tokopedia.topads.keyword.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.model.NegativeKeywordAd;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordViewHolder extends BaseMultipleCheckViewHolder<KeywordAd> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_top_ads_keyword_main;

    private final TextView titleProduct;
    private final TextView statusActive;
    private final TextView pricePromoPerClick;
    private final TextView promoPriceUsed;
    private final TextView groupName;
    private final View statusActiveDot;
    private final LinearLayout promoPriceUsedContainer;
    private final LinearLayout promoPerClickContainer;
    private final TextView keywordTypeDescription;
    private final LinearLayout statusActiveContainer;
    private View optionImageButton;
    private CheckBox checkBox;

    public TopAdsKeywordViewHolder(View itemView) {
        super(itemView);
        titleProduct = (TextView) itemView.findViewById(R.id.title_product);
        statusActive = (TextView) itemView.findViewById(R.id.status_active);
        pricePromoPerClick = (TextView) itemView.findViewById(R.id.price_promo_per_click);
        promoPriceUsed = (TextView) itemView.findViewById(R.id.promo_price_used);
        groupName = (TextView) itemView.findViewById(R.id.group_name);
        statusActiveDot = itemView.findViewById(R.id.status_active_dot);
        promoPriceUsedContainer = (LinearLayout) itemView.findViewById(R.id.promo_price_used_container);
        promoPerClickContainer = (LinearLayout) itemView.findViewById(R.id.promo_per_click_container);
        keywordTypeDescription = (TextView) itemView.findViewById(R.id.title_keyword_type_description);
        statusActiveContainer = (LinearLayout) itemView.findViewById(R.id.status_active_container);
        optionImageButton = itemView.findViewById(R.id.image_button_option);
        checkBox = (CheckBox) itemView.findViewById(R.id.check_box_product);
    }

    @Override
    public void bind(final KeywordAd keywordAd) {
        if (keywordAd != null && keywordAd instanceof NegativeKeywordAd) {
            promoPriceUsedContainer.setVisibility(View.GONE);
            pricePromoPerClick.setVisibility(View.GONE);
            statusActiveContainer.setVisibility(View.GONE);
            promoPerClickContainer.setVisibility(View.GONE);
        } else {
            promoPriceUsedContainer.setVisibility(View.VISIBLE);
            pricePromoPerClick.setVisibility(View.VISIBLE);
            statusActiveContainer.setVisibility(View.VISIBLE);
            promoPerClickContainer.setVisibility(View.VISIBLE);
        }
        keywordTypeDescription.setText(keywordAd.getKeywordTypeDesc());
        titleProduct.setText(keywordAd.getKeywordTag());
        statusActive.setText(keywordAd.getStatusDesc());
        pricePromoPerClick.setText(itemView.getContext()
                .getString(R.string.top_ads_bid_format_text, keywordAd.getPriceBidFmt(), keywordAd.getLabelPerClick()));
        promoPriceUsed.setText(keywordAd.getStatTotalSpent());
        groupName.setText(itemView.getContext().getString(R.string.top_ads_keywords_groups_format, keywordAd.getGroupName()));
        switch (keywordAd.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.ic_status_green);
                break;
            default:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
        }

        optionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionMoreCallback != null){
                    optionMoreCallback.onClickMore(keywordAd);
                }
            }
        });
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
    public void bindObject(final KeywordAd item, boolean isChecked) {
        bind(item);
        setChecked(isChecked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedCallback != null){
                    checkedCallback.onItemChecked(item, checkBox.isChecked());
                }
                setChecked(checkBox.isChecked());
            }
        });
    }

    @Override
    public void showCheckButton(boolean isInActionMode, boolean isAutoAds) {
        if (isInActionMode) {
            checkBox.setVisibility(isAutoAds ? View.GONE : View.VISIBLE);
            optionImageButton.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.GONE);
            optionImageButton.setVisibility(isAutoAds ? View.GONE : View.VISIBLE);
        }
    }

    public void setBackground(boolean isChecked) {
        if (isChecked) {
            if (itemView instanceof CardView){
                ((CardView)itemView).setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
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
}
