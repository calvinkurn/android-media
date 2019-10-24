package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.fragment.TrendingDealsFragment;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.utils.CuratedDealsView;
import com.tokopedia.digital_deals.view.utils.TrendingDealsCallBacks;

import java.util.List;

public class DealsHomeActivity extends DealsBaseActivity implements TrendingDealsCallBacks, DealsHomeFragment.OpenTrendingDeals, SelectLocationBottomSheet.SelectedLocationListener {


    public static final int REQUEST_CODE_DEALSLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_DEALSSEARCHACTIVITY = 102;
    public final static int REQUEST_CODE_DEALDETAILACTIVITY=103;
    public final static int REQUEST_CODE_LOGIN=104;

    private boolean isLocationUpdated;
    private DealsHomeFragment dealsHomeFragment;
    private String title;
    private String url;
    private int position;

    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Intent destination;
        try {
            String deepLink = extras.getString(DeepLink.URI);

            Uri.Builder uri = Uri.parse(deepLink).buildUpon();
            destination = new Intent(context, DealsHomeActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

        } catch (Exception e) {
            destination = new Intent(context, DealsHomeActivity.class);
        }
        return destination;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
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
        transaction.setCustomAnimations(R.anim.slide_in_left_brands, R.anim.slide_out_right_brands);
        transaction.add(R.id.parent_view, TrendingDealsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLocationItemUpdated(boolean isLocationUpdated) {
        this.isLocationUpdated = isLocationUpdated;
        dealsHomeFragment.refreshHomePage(isLocationUpdated);
    }
}
