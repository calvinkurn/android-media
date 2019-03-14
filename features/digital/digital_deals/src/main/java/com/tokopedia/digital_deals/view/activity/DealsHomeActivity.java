package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.fragment.TrendingDealsFragment;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.utils.TrendingDealsCallBacks;

import java.util.List;

public class DealsHomeActivity extends DealsBaseActivity implements TrendingDealsCallBacks, DealsHomeFragment.OpenTrendingDeals {


    public static final int REQUEST_CODE_DEALSLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_DEALSSEARCHACTIVITY = 102;
    public final static int REQUEST_CODE_DEALDETAILACTIVITY=103;
    public final static int REQUEST_CODE_LOGIN=104;

    List<ProductItem> trendingDeals;

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
        return DealsHomeFragment.createInstance();
    }

    @Override
    public List<ProductItem> getTrendingDeals() {
        return this.trendingDeals;
    }

    @Override
    public void replaceFragment(List<ProductItem> trendingDeals, int flag) {
        this.trendingDeals = trendingDeals;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, TrendingDealsFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
