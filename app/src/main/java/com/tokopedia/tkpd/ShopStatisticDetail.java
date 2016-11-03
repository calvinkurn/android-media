package com.tokopedia.tkpd;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.tkpd.app.TActivity;


public class ShopStatisticDetail extends TActivity {

    public static final String EXTRA_SHOP_INFO = "shop_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_statistic_detail);
        getFragmentManager().beginTransaction().replace(R.id.fragment, ShopStatisticDetailFragment.createInstance(getIntent().getStringExtra(EXTRA_SHOP_INFO))).commit();
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
