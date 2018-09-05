package com.tokopedia.reksadana.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.reksadana.view.adapter.BuySellTabAdapter;

public class BuySellTabActivity extends BaseTabActivity {
    private static final String TAB_TYPE = "TAB_TYPE";
    private static final String BUY_TAB = "BUY";

    public static Intent createInstance(Context context, String tabType) {
        Intent intent = new Intent(context, BuySellTabActivity.class);
        intent.putExtra(TAB_TYPE,tabType);
        return intent;
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new BuySellTabAdapter(getSupportFragmentManager(),
                (getIntent()!=null && getIntent().getStringExtra(TAB_TYPE).equals(BUY_TAB)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent()!=null && getIntent().getStringExtra(TAB_TYPE).equals(BUY_TAB)))
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(1);
    }

    @Override
    protected int getPageLimit() {
        return 2;
    }
}
