package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.model.StepperModel;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.topads.common.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoShopActivity extends BaseStepperActivity implements HasComponent<BaseAppComponent> {
    private List<Fragment> fragmentList;
    private ShopAd shopAd;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewCostShopFragment());
            fragmentList.add(new TopAdsNewScheduleShopFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewStepperModel();
    }

    public StepperModel createNewStepperModel() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            shopAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        }
        return null;
    }

    public static Intent createIntent(Context context, ShopAd shopAd){
        Intent intent = new Intent(context, TopAdsCreatePromoShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, shopAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, shopAd.getName());
        return intent;
    }


    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }

    public ShopAd getShopAd() {
        return shopAd;
    }
}
