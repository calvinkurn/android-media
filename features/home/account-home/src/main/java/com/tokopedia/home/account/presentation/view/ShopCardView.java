package com.tokopedia.home.account.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/26/18.
 */
public class ShopCardView extends BaseCustomView {
    private ImageView imageShop;
    private ImageView badge;
    private TextView textShopName;
    private ShopReputationView shopReputation;
    private TextView textSaldoAmount;

    public ShopCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShopCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShopCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_shop_card, this);
        imageShop = view.findViewById(R.id.image_shop);
        badge = view.findViewById(R.id.image_badge);
        textShopName = view.findViewById(R.id.text_shop_name);
        shopReputation = view.findViewById(R.id.shop_reputation);
        textSaldoAmount = view.findViewById(R.id.text_saldo_amount);
    }

    public void setShopImage(String url) {
        if(!TextUtils.isEmpty(url)) {
            ImageHandler.loadImage(getContext(), imageShop, url, new ColorDrawable(ContextCompat.getColor(getContext(), R.color.grey_500)));
        }
    }

    public void setBadgeImage(String url) {
        if(!TextUtils.isEmpty(url)) {
            ImageHandler.loadImage(getContext(), badge, url, new ColorDrawable(ContextCompat.getColor(getContext(), R.color.grey_500)));
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }
    }

    public void setBadgeImage(@DrawableRes int resource) {
        if(resource != 0) {
            badge.setImageResource(resource);
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }

    }

    public void setShopName(String shopName) {
        textShopName.setText(shopName);
    }

    public void setBalance(String balance) {
        textSaldoAmount.setText(balance);
    }

    public void setShopReputation(int medalType, int level) {
        shopReputation.setValue(medalType, level, "");
    }

    public void setOnClickShopAvatar(View.OnClickListener listener) {
        imageShop.setOnClickListener(listener);
    }

    public void setOnClickShopName(View.OnClickListener listener) {
        textShopName.setOnClickListener(listener);
    }
}