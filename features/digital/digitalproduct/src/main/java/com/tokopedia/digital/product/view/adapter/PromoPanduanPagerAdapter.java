package com.tokopedia.digital.product.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager;
import com.tokopedia.digital.product.view.fragment.DigitalPanduanFragment;
import com.tokopedia.digital.product.view.fragment.DigitalPromoFragment;
import com.tokopedia.digital.product.view.model.BannerData;

import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class PromoPanduanPagerAdapter extends FragmentStatePagerAdapter
        implements DigitalPromoFragment.DigitalPromoConnector{

    private Context context;
    private int currentPosition = -1;

    private DigitalPromoFragment digitalPromoFragment;
    private String bannerDataTitle;
    private List<BannerData> bannerDataList;
    private String otherBannerDataTitle;
    private List<BannerData> otherBannerDataList;

    public PromoPanduanPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return context.getString(R.string.promo_tab_title);
            case 1 :
                return context.getString(R.string.panduan_tab_title);
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            Fragment fragment = (Fragment) object;
            DigitalWrapContentViewPager pager = (DigitalWrapContentViewPager) container;
            if (fragment != null) {
                currentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                digitalPromoFragment = DigitalPromoFragment.createInstance();
                digitalPromoFragment.setDigitalPromoConnector(this);
                return digitalPromoFragment;
            case 1 :
                return DigitalPanduanFragment.createInstance();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void setBannerDataList(String bannerDataTitle, List<BannerData> bannerDataList) {
        this.bannerDataTitle = bannerDataTitle;
        this.bannerDataList = bannerDataList;
    }

    public void setOtherBannerDataList(String bannerDataTitle, List<BannerData> bannerDataList) {
        this.otherBannerDataTitle = bannerDataTitle;
        this.otherBannerDataList = bannerDataList;
    }

    @Override
    public String getBannerDataTitle() {
        return bannerDataTitle;
    }

    @Override
    public List<BannerData> getBannerDataList() {
        return bannerDataList;
    }

    @Override
    public String getOtherBannerDataTitle() {
        return otherBannerDataTitle;
    }

    @Override
    public List<BannerData> getOtherBannerDataList() {
        return otherBannerDataList;
    }
}
