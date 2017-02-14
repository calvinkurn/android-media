package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.sellerapp.R;
import com.tkpd.library.utils.image.ImageHandler;

/**
 * Created by normansyahputa on 9/1/16.
 */

public class BadgeContainer extends FrameLayout implements BaseView<BadgeContainer.BadgeContainerModel> {

    LinearLayout badgeContainerLinLay;
    ImageView luckyShop;

    public BadgeContainer(Context context) {
        super(context);
    }

    public BadgeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.badge_container, this);

        badgeContainerLinLay  = (LinearLayout) findViewById(R.id.badge_container_linlay);
        luckyShop = (ImageView) findViewById(R.id.lucky_shop);
    }

    public void init(BadgeContainerModel data, ImageHandler imageHandler){
        if(data.isGold) {
            final float scale = getResources().getDisplayMetrics().density;
            int dpWidthInPx  = (int) (16 * scale);
            int dpHeightInPx = (int) (16 * scale);
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(dpWidthInPx, dpHeightInPx);

//            int sizeInDP = 1;
//            int marginInDp = (int) TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
//                            .getDisplayMetrics());
//            params.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);

            ImageView goldMerchant = new ImageView(getContext());
            goldMerchant.setLayoutParams(params);
            goldMerchant.setImageResource(R.drawable.ic_shop_gold);
//            imageHandler.loadImageWithIdWithoutPlaceholder(goldMerchant, R.drawable.ic_shop_gold);

            badgeContainerLinLay.removeAllViews();
            badgeContainerLinLay.addView(goldMerchant);
        }

        luckyShop.setVisibility(View.GONE);

        if(data.luckyUrl != null && !data.luckyUrl.isEmpty()){
//            final ImageView luckMerchant = new ImageView(getContext());
//            luckMerchant.setLayoutParams(new LinearLayout.LayoutParams(10,10));
//            badgeContainerLinLay.addView(luckMerchant);

            imageHandler.loadImage(luckyShop, data.luckyUrl, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Log.d(ImageHandler.class.getSimpleName(),
                            "width "+resource.getWidth()+" vs height "+resource.getHeight());
                    if(resource.getWidth() > 1 ){
                        luckyShop.setImageBitmap(resource);
                        luckyShop.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    Log.e(ImageHandler.class.getSimpleName(), e.toString());
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    Log.d(ImageHandler.class.getSimpleName(), "start load url");
                }
            });
        }
    }

    @Override
    public void init(BadgeContainerModel data) {


    }

    public static class BadgeContainerModel{
        boolean isGold;
        boolean isLucky;
        String luckyUrl;
    }
}
