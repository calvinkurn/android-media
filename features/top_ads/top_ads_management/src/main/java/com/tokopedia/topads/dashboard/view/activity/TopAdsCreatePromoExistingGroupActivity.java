package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.topads.common.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoExistingGroupActivity extends BaseStepperActivity implements HasComponent<BaseAppComponent> {
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
        stepperModel = createNewStepperModel();
    }

    public TopAdsCreatePromoExistingGroupModel createNewStepperModel() {
        String groupId = null;
        String itemIdToAdd = null;
        String source = "";
        if (getIntent() != null && getIntent().getExtras() != null) {
            groupId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
            source = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_SOURCE);
        }
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
        ((TopAdsCreatePromoExistingGroupModel) stepperModel).setGroupId(groupId);
        ((TopAdsCreatePromoExistingGroupModel) stepperModel).setIdToAdd(itemIdToAdd);
        ((TopAdsCreatePromoExistingGroupModel) stepperModel).setSource(source);
        return (TopAdsCreatePromoExistingGroupModel)stepperModel;
    }


    public static Intent createIntent(Context context, String groupId, String itemIdToAdd, String source) {
        Intent intent = new Intent(context, TopAdsCreatePromoExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_SOURCE, source);
        return intent;
    }

    @Override
    public void finishPage() {
        super.finishPage();
    }


    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }
}
