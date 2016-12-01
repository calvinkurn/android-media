package com.tokopedia.core.shop.presenter;

import android.content.Context;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.core.shipping.model.openshopshipping.OpenShopData;

/**
 * Created by Toped18 on 5/19/2016.
 */
public abstract class ShopCreatePresenter extends BaseImpl<ShopCreateView> {
    public static final int FragmentId = 1_343_434;

    public static final int DOMAIN_ERROR = 1;
    public static final int NAME_ERROR = 2;
    public static final int DESC_ERROR = 3;
    public static final int TAG_ERROR = 4;

    public static final String STUART = "STUART";
    public static final String SHOP_CREATE_FRAGMENT = "ShopCreateFragment ";
    public static final String DATA_VIEW = "DATA_VIEW";

    public ShopCreatePresenter(ShopCreateView view) {
        super(view);
    }


    public abstract void checkShopDomain(String domain);

    public abstract void checkShopName(String s);

    public abstract void sendShopRequest(Context context);

    public abstract void saveShippingData(OpenShopData shippingData);

    public abstract void saveShopAvatarUrl(String imagePath);

    public abstract boolean verifyData();

    public abstract void saveDescTag();

    public abstract void finalCheckDomainName(String domain, String name);
}
