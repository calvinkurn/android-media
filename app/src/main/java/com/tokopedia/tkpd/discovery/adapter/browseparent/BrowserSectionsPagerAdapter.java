package com.tokopedia.tkpd.discovery.adapter.browseparent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;

import com.tokopedia.tkpd.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.tkpd.discovery.fragment.browseparent.CatalogFragment;
import com.tokopedia.tkpd.discovery.fragment.browseparent.ShopFragment;

import static com.tokopedia.tkpd.discovery.view.BrowseProductParentView.VISIBLE_ON;

import java.util.Map;

/**
 * Created by raditya.gumay on 21/03/2016.
 */
public class BrowserSectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = BrowserSectionsPagerAdapter.class.getSimpleName();
    public static final String PRODUK = "Produk";
    public static final String KATALOG = "Katalog";
    public static final String TOKO = "Toko";
    public static final int BROWSE_PRODUCT = 0;
    public static final int BROWSE_CATALOG = 1;
    public static final int BROWSE_SHOP = 2;

    private ArrayMap<String, String> mSectionViewPagerMaps;
    private Fragment[] fragments;
    public BrowserSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setSectionViewPager(ArrayMap<String, String> maps) {
        this.mSectionViewPagerMaps = maps;
        fragments = new Fragment[getCount()];
        refreshData();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BROWSE_PRODUCT:
                if(fragments[BROWSE_PRODUCT] == null){
                    fragments[BROWSE_PRODUCT] = ProductFragment.newInstance();
                }
                return fragments[BROWSE_PRODUCT];
            case BROWSE_CATALOG:
                if(fragments[BROWSE_CATALOG] == null) {
                    if(mSectionViewPagerMaps.get(KATALOG)!= null && mSectionViewPagerMaps.get(KATALOG).equals(VISIBLE_ON)){
                        fragments[BROWSE_CATALOG] = CatalogFragment.newInstance();
                    } else {
                        fragments[BROWSE_CATALOG] = ShopFragment.newInstance();
                    }
                }return fragments[BROWSE_CATALOG];
            case BROWSE_SHOP:
                if(fragments[BROWSE_SHOP] == null) {
                    fragments[BROWSE_SHOP] = ShopFragment.newInstance();
                }
                return fragments[BROWSE_SHOP];
            default: {
                return ProductFragment.newInstance();
            }
        }
    }

    @Override
    public int getCount() {
        return mSectionViewPagerMaps != null ? mSectionViewPagerMaps.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return PRODUK;
            case 1:
                if(mSectionViewPagerMaps.get(KATALOG)!= null && mSectionViewPagerMaps.get(KATALOG).equals(VISIBLE_ON)){
                    return KATALOG;
                }else{
                    return TOKO;
                }
            case 2:
                return TOKO;
            default:
                return PRODUK;
        }
    }

    public void refreshData() {
        this.notifyDataSetChanged();
    }
}