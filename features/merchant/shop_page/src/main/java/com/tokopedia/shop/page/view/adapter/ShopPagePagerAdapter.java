package com.tokopedia.shop.page.view.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

import java.util.Arrays;

/**
 * Created by normansyahputa on 3/13/18.
 */

public class ShopPagePagerAdapter extends FragmentStatePagerAdapter {
    public static final String STATES = "states";
    private final String[] title;
    private final ShopModuleRouter shopModuleRouter;
    private final ShopPagePromoWebView.Listener listener;
    private final String shopId;
    private final String shopDomain;
    private final String shopAttribution;

    public ShopPagePagerAdapter(FragmentManager fragmentManager, String[] title, ShopModuleRouter shopModuleRouter,
                                ShopPagePromoWebView.Listener listener, String shopId, String shopDomain, String shopAttribution) {
        super(fragmentManager);
        this.title = title;
        this.shopModuleRouter = shopModuleRouter;
        this.listener = listener;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        this.shopAttribution = shopAttribution;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
                return title[position];
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public Fragment getItem(int position) {
        ShopProductListLimitedFragment shopProductListLimitedFragment = ShopProductListLimitedFragment.createInstance(shopAttribution);
        shopProductListLimitedFragment.setPromoWebViewListener(listener);
        switch (position) {
            case 0:
                return shopProductListLimitedFragment;
            case 1:
                if (shopModuleRouter != null) {
                    return shopModuleRouter.getShopReputationFragmentShop(shopId, shopDomain);
                }
                break;
            case 2:
                if (shopModuleRouter != null) {
                    return shopModuleRouter.getShopTalkFragment();
                }
                break;
        }
        return shopProductListLimitedFragment;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelableArray(STATES, null);
        return bundle;
    }
}
