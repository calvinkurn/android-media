package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupEditFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/29/17.
 */

public class TopAdsCreatePromoExistingGroupEditActivity extends TopAdsCreatePromoExistingGroupActivity implements HasComponent<AppComponent> {

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListExistingGroupEditFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    public static Intent createIntent(Context context, String groupId, String itemIdToAdd) {
        Intent intent = new Intent(context, TopAdsCreatePromoExistingGroupEditActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        return intent;
    }


    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
