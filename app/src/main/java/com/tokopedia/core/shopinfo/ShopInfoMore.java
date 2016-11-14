package com.tokopedia.core.shopinfo;

import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.shopinfo.fragment.FragmentShopStatistic;

/**
 * Created by Tkpd_Eka on 11/5/2015.
 */
public class ShopInfoMore extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        FragmentShopStatistic fragment = new FragmentShopStatistic();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SHOP_INFO;
    }
}
