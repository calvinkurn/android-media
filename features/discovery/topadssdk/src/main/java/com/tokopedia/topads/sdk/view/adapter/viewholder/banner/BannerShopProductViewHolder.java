package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;

import androidx.annotation.LayoutRes;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewHolder extends AbstractViewHolder<BannerShopProductViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_ads_banner_shop_a_product;
    private static final String TAG = BannerShopProductViewHolder.class.getSimpleName();
    private ImpressedImageView imageView;
    private TextView descTxt;
    private TextView priceTxt;
    private TextView reviewCountTxt, textViewRatingCount;
    private TextView discountTxt;
    private TextView newLabelTxt;
    private TextView slashedPrice;
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener impressionListener;
    private Context context;
    private View container;
    private ImageView rating1, rating2, rating3, rating4, rating5;
    private static final String className = "com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder";


    public BannerShopProductViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener,
                                       TopAdsItemImpressionListener itemImpressionListener) {
        super(itemView);
        this.container = itemView;
        this.context = itemView.getContext();
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.impressionListener = itemImpressionListener;
        imageView = itemView.findViewById(R.id.icon);
        descTxt = itemView.findViewById(R.id.description);
        priceTxt = itemView.findViewById(R.id.price);
        rating1 = itemView.findViewById(R.id.imageViewRating1);
        rating2 = itemView.findViewById(R.id.imageViewRating2);
        rating3 = itemView.findViewById(R.id.imageViewRating3);
        rating4 = itemView.findViewById(R.id.imageViewRating4);
        rating5 = itemView.findViewById(R.id.imageViewRating5);
        reviewCountTxt = itemView.findViewById(R.id.textViewReviewCount);
        textViewRatingCount = itemView.findViewById(R.id.textViewRatingCount);
        discountTxt = itemView.findViewById(R.id.discount_txt);
        newLabelTxt = itemView.findViewById(R.id.label_new);
        slashedPrice = itemView.findViewById(R.id.slashed_price);
    }

    @Override
    public void bind(final BannerShopProductViewModel element) {
        final Product product = element.getProduct();
        imageView.setImage(product.getImageProduct());
        imageView.setViewHintListener(new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                if (impressionListener != null) {
                    impressionListener.onImpressionHeadlineAdsItem(getAdapterPosition(), element.getCpmData());
                }
            }
        });
        descTxt.setText(TopAdsBannerView.Companion.escapeHTML(element.getProduct().getName()));
        priceTxt.setText(element.getProduct().getPriceFormat());
        String reviewCount = element.getProduct().getCountReviewFormat();
        reviewCountTxt.setText(String.format("(%s)", (reviewCount.isEmpty()) ? "0" : reviewCount));
        if (element.getProduct().isProductNewLabel()) {
            newLabelTxt.setVisibility(View.VISIBLE);
        } else {
            newLabelTxt.setVisibility(View.GONE);
        }

        if (hasDiscount(element)) {
            discountTxt.setText(String.format("%d%%", element.getProduct().getCampaign().getDiscountPercentage()));
            discountTxt.setVisibility(View.VISIBLE);
            slashedPrice.setVisibility(View.VISIBLE);
            initSlashedPrice(product.getCampaign().getOriginalPrice());
        } else {
            discountTxt.setVisibility(View.GONE);
            slashedPrice.setVisibility(View.INVISIBLE);
        }
        setRating(element.getProduct().getProductRating(), !hasDiscount(element));
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topAdsBannerClickListener != null) {
                    topAdsBannerClickListener.onBannerAdsClicked((getAdapterPosition()),
                            element.getProduct().getApplinks(), element.getCpmData());
                    new ImpresionTask(className).execute(element.getProduct().getImageProduct().getImageClickUrl());
                }
            }
        });
    }

    private boolean hasDiscount(BannerShopProductViewModel element) {
        return element.getProduct().getCampaign().getDiscountPercentage() > 0;
    }

    private void setRating(int productRating, boolean isVisible) {
        int rating = Math.round(productRating / 20f);
        rating1.setImageResource(getRatingDrawable(rating >= 1));
        rating1.setVisibility((isVisible && (rating > 0)) ? View.VISIBLE : View.GONE);
        textViewRatingCount.setText(String.valueOf(productRating / 20f));
        textViewRatingCount.setVisibility((isVisible && (rating >0)) ? View.VISIBLE : View.GONE);
        reviewCountTxt.setVisibility((isVisible && (rating > 0)) ? View.VISIBLE : View.GONE);
    }

    private int getRatingDrawable(Boolean isActive) {
        if (isActive) {
            return R.drawable.topads_ic_rating_active;
        } else {
            return R.drawable.topads_ic_rating_default;
        }
    }

    private void initSlashedPrice(String price) {
        if (price.trim().isEmpty()) {
            slashedPrice.setVisibility(View.GONE);
        } else {
            slashedPrice.setVisibility(View.VISIBLE);
            slashedPrice.setText(price);
            slashedPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
