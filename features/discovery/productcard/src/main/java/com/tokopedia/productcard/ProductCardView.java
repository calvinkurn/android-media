package com.tokopedia.productcard;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.productcard.R;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

public class ProductCardView extends BaseCustomView {

    public TextView textName;
    public TextView textPrice;
    public ImpressedImageView imageView;
    public View topAdsIcon;
    public View wishlistButton;

    public ProductCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.product_card_layout, this);
        textName = view.findViewById(R.id.textName);
        textPrice = view.findViewById(R.id.textPrice);
        imageView = view.findViewById(R.id.image);
        topAdsIcon = view.findViewById(R.id.topAdsIcon);
        wishlistButton = view.findViewById(R.id.btn_wishlist);
    }

    public void setTitle(String title) {
        textName.setText(title);
    }

    public void setPrice(String price) {
        textPrice.setText(price);
    }

    public void setImageUrl(String imageUrl) {
        ImageHandler.loadImageFitCenter(getContext(), imageView, imageUrl);
    }

    public void setTopAdsVisible(boolean isVisible) {
        topAdsIcon.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setWishlistButtonVisible(boolean isVisible) {
        wishlistButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
