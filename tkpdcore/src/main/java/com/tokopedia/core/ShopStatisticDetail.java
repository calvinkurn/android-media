package com.tokopedia.core;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core2.R;


public class ShopStatisticDetail extends TActivity {

    public static final String EXTRA_SHOP_INFO = "shop_info";

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_DETAIL_STATS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_statistic_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment,
                    ShopStatisticDetailFragment.createInstance(
                            getIntent().getStringExtra(EXTRA_SHOP_INFO))).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
