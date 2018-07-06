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
import com.tokopedia.digital.product.view.model.GuideData;

import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class PromoPanduanPagerAdapter extends FragmentStatePagerAdapter
        implements DigitalPromoFragment.DigitalPromoConnector, DigitalPanduanFragment.DigitalPanduanConnector{

    public static final String PROMO_TAB = "promo_tab";
    public static final String PANDUAN_TAB = "panduan_tab";

    private Context context;
    private int currentPosition = -1;
    private int tabCount = 2;
    private String firstTab;

    private DigitalPromoFragment digitalPromoFragment;
    private DigitalPanduanFragment digitalPanduanFragment;

    private String bannerDataTitle;
    private List<BannerData> bannerDataList;
    private String otherBannerDataTitle;
    private List<BannerData> otherBannerDataList;
    private List<GuideData> guideDataList;

    public PromoPanduanPagerAdapter(FragmentManager fm, Context context, int tabCount, String firstTab) {
        super(fm);
        this.context = context;
        this.tabCount = tabCount;
        this.firstTab = firstTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                if (firstTab.equals(PROMO_TAB)) {
                    return context.getString(R.string.promo_tab_title);
                } else if (firstTab.equals(PANDUAN_TAB)) {
                    return context.getString(R.string.panduan_tab_title);
                }
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
                if (firstTab.equals(PROMO_TAB)) {
                    digitalPromoFragment = DigitalPromoFragment.createInstance();
                    digitalPromoFragment.setDigitalPromoConnector(this);
                    return digitalPromoFragment;
                } else if (firstTab.equals(PANDUAN_TAB)) {
                    digitalPanduanFragment = DigitalPanduanFragment.createInstance();
                    digitalPanduanFragment.setConnector(this);
                    return digitalPanduanFragment;
                }
            case 1 :
                digitalPanduanFragment = DigitalPanduanFragment.createInstance();
                digitalPanduanFragment.setConnector(this);
                return digitalPanduanFragment;
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setBannerDataList(String bannerDataTitle, List<BannerData> bannerDataList) {
        this.bannerDataTitle = bannerDataTitle;
        this.bannerDataList = bannerDataList;
    }

    public void setOtherBannerDataList(String bannerDataTitle, List<BannerData> bannerDataList) {
        this.otherBannerDataTitle = bannerDataTitle;
        this.otherBannerDataList = bannerDataList;
    }

    public void setGuideDataList(List<GuideData> guideDataList) {
        this.guideDataList = guideDataList;
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

    @Override
    public List<GuideData> getGuideDataList() {
        return guideDataList;
    }
}
