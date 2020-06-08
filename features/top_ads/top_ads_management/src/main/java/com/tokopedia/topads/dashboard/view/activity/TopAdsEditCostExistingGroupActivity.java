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
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostExistingGroupFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditCostExistingGroupActivity extends TopAdsBaseActivity implements HasComponent<BaseAppComponent> {
    public static Intent createIntent(Context context,String groupId, GroupAd groupAd){
        Intent intent = new Intent(context, TopAdsEditCostExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, groupAd);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            String groupId = null;
            GroupAd groupAd = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                groupId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
                groupAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
            }
            fragment = TopAdsEditCostExistingGroupFragment.createInstance(groupId, groupAd);
            return fragment;
        }
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }
}
