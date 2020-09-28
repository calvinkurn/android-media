package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.topads.common.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsCreatePromoNewGroupActivity extends BaseStepperActivity implements HasComponent<BaseAppComponent> {
    private List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListNewGroupFragment());
            fragmentList.add(new TopAdsNewCostNewGroupFragment());
            fragmentList.add(new TopAdsNewScheduleNewGroupFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepperModel = createNewStepperModel();
    }

    public TopAdsCreatePromoNewGroupModel createNewStepperModel() {
        String name = null;
        String itemIdToAdd = null;
        String source = "";
        if (getIntent() != null && getIntent().getExtras() != null) {
            name = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_NAME);
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
            source = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_SOURCE);
        }
        stepperModel = new TopAdsCreatePromoNewGroupModel();
        ((TopAdsCreatePromoNewGroupModel)stepperModel).setGroupName(name);
        ((TopAdsCreatePromoNewGroupModel)stepperModel).setIdToAdd(itemIdToAdd);
        ((TopAdsCreatePromoNewGroupModel)stepperModel).setSource(source);
        return (TopAdsCreatePromoNewGroupModel)stepperModel;
    }


    public static Intent createIntent(Context context, String name, String itemIdToAdd, String source){
        Intent intent = new Intent(context, TopAdsCreatePromoNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, name);
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
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
}
