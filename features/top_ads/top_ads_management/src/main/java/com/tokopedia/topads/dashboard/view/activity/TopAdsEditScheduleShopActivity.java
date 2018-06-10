package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleShopFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditScheduleShopActivity extends BaseSimpleActivity implements HasComponent<AppComponent>{
    public static Intent createIntent(Context context, String shopId){
        Intent intent = new Intent(context, TopAdsEditScheduleShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        return intent;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            String shopId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                shopId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditScheduleShopFragment.createInstance(shopId);
            return fragment;
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
