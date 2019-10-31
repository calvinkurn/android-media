package com.tokopedia.digital_deals.view.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.fragment.TrendingDealsFragment;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.utils.CuratedDealsView;
import com.tokopedia.digital_deals.view.utils.CurrentLocationCallBack;
import com.tokopedia.digital_deals.view.utils.TrendingDealsCallBacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.locationmanager.DeviceLocation;
import java.util.List;

public class DealsHomeActivity extends DealsBaseActivity implements TrendingDealsCallBacks, DealsHomeFragment.OpenTrendingDeals, SelectLocationBottomSheet.SelectedLocationListener, CurrentLocationCallBack {


    public static final int REQUEST_CODE_DEALSLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_DEALSSEARCHACTIVITY = 102;
    public final static int REQUEST_CODE_DEALDETAILACTIVITY=103;
    public final static int REQUEST_CODE_LOGIN=104;

    private boolean isLocationUpdated;
    private DealsHomeFragment dealsHomeFragment;
    private String title;
    private String url;
    private int position;

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    protected int getParentViewResourceID(){
        return com.tokopedia.digital_deals.R.id.deals_home_parent_view;
    }

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        toolbar.setVisibility(View.GONE);
        dealsHomeFragment = DealsHomeFragment.createInstance(isLocationUpdated);
        return dealsHomeFragment;
    }

    @Override
    public String getTrendingDealsUrl() {
        return url;
    }

    @Override
    public String getToolBarTitle() {
        return title;
    }

    @Override
    public int getHomePosition() {
        return position;
    }

    @Override
    public void replaceFragment(String url, String title, int position) {
        this.url = url;
        this.title = title;
        this.position = position;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_left_brands,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_right_brands);
        transaction.add(com.tokopedia.digital_deals.R.id.deals_home_parent_view, TrendingDealsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLocationItemUpdated(boolean isLocationUpdated) {
        this.isLocationUpdated = isLocationUpdated;
        dealsHomeFragment.refreshHomePage(isLocationUpdated);
    }

    @Override
    public void setDefaultLocationOnHomePage() {
        dealsHomeFragment.setDefaultLocationName();
    }

    @Override
    public void setCurrentLocation(DeviceLocation deviceLocation) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(this, TkpdCache.DEALS_LOCATION);
        localCacheHandler.putString(Utils.KEY_LOCATION_LAT, String.valueOf(deviceLocation.getLatitude()));
        localCacheHandler.putString(Utils.KEY_LOCATION_LONG, String.valueOf(deviceLocation.getLongitude()));
        localCacheHandler.applyEditor();
        dealsHomeFragment.setCurrentCoordinates();
    }
}
