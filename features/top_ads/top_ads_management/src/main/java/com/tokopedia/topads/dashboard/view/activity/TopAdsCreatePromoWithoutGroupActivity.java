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
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoWithoutGroupActivity extends BaseStepperActivity implements HasComponent<BaseAppComponent> {
    private List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListWithoutGroupFragment());
            fragmentList.add(new TopAdsNewCostWithoutGroupFragment());
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

    public TopAdsCreatePromoWithoutGroupModel createNewStepperModel() {
        String itemIdToAdd = null;
        String source = "";
        if (getIntent() != null && getIntent().getExtras() != null) {
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
            source = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_SOURCE);
        }
        stepperModel = new TopAdsCreatePromoWithoutGroupModel();
        ((TopAdsCreatePromoWithoutGroupModel)stepperModel).setIdToAdd(itemIdToAdd);
        ((TopAdsCreatePromoWithoutGroupModel)stepperModel).setSource(source);
        return (TopAdsCreatePromoWithoutGroupModel) stepperModel;
    }

    public static Intent createIntent(Context context, String itemIdToAdd, String source){
        Intent intent = new Intent(context, TopAdsCreatePromoWithoutGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_SOURCE, source);
        return intent;
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }
}
