package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditGroupNameFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditProductMainPageFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditGroupNameActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createIntent(Context context, String name, String adId){
        Intent intent = new Intent(context, TopAdsEditGroupNameActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, name);
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
            String groupName = null;
            String adId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                groupName = getIntent().getStringExtra(TopAdsExtraConstant.GROUP_NAME);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditGroupNameFragment.createInstance(groupName, adId);
            return fragment;
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
