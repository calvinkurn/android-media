package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.activity.TopAdsBaseActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleWithoutGroupFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditScheduleProductActivity extends TopAdsBaseActivity implements HasComponent<BaseAppComponent> {
    public static Intent createIntent(Context context, String adId){
        Intent intent = new Intent(context, TopAdsEditScheduleProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            String adId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditScheduleWithoutGroupFragment.createInstance(adId);
            return fragment;
        }
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }
}
