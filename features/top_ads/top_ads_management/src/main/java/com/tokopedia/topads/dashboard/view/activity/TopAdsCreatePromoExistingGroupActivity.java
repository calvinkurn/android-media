package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoExistingGroupActivity extends BaseStepperActivity<TopAdsCreatePromoExistingGroupModel> implements HasComponent<AppComponent> {
    protected List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListExistingGroupFragment());
            return fragmentList;
        } else {
            return fragmentList;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public TopAdsCreatePromoExistingGroupModel createNewStepperModel() {
        String groupId = null;
        String itemIdToAdd = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            groupId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
        ((TopAdsCreatePromoExistingGroupModel) stepperModel).setGroupId(groupId);
        ((TopAdsCreatePromoExistingGroupModel) stepperModel).setIdToAdd(itemIdToAdd);
        return stepperModel;
    }


    public static Intent createIntent(Context context, String groupId, String itemIdToAdd) {
        Intent intent = new Intent(context, TopAdsCreatePromoExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        return intent;
    }

    @Override
    public void finishPage() {
        super.finishPage();
    }


    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
