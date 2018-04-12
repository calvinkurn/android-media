package com.tokopedia.shop.info.view.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.note.view.fragment.ShopNoteListFragment;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

import java.util.Arrays;


/**
 * Created by normansyahputa on 3/13/18.
 */

public class ShopInfoPagerAdapter extends FragmentStatePagerAdapter {
    private static final String STATES = "states";
    private final String[] titleArray;

    public ShopInfoPagerAdapter(FragmentManager fragmentManager, String[] titleArray) {
        super(fragmentManager);
        this.titleArray = titleArray;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            case 1:
                return titleArray[position];
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShopInfoFragment.createInstance();
            case 1:
                return ShopNoteListFragment.createInstance();
        }
        return ShopInfoFragment.createInstance();
    }

    @Override
    public int getCount() {
        return titleArray.length;
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        if (bundle != null) {
            Parcelable[] states = bundle.getParcelableArray(STATES); // Subset only last 3 states
            if (states != null)
                states = Arrays.copyOfRange(states, states.length > 3 ? states.length - 3 : 0, states.length - 1);
            bundle.putParcelableArray(STATES, states);
        } else {
            bundle = new Bundle();
        }
        return bundle;
    }
}
