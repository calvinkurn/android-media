package com.tokopedia.topads.dashboard.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.base.view.activity.BaseFilterActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsFilterStatusFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public class TopAdsFilterGroupActivity extends BaseFilterActivity {

    private int selectedFilterStatus;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();
        TopAdsFilterStatusFragment topAdsFilterStatusFragment = TopAdsProductFilterStatusFragment.createInstance(selectedFilterStatus);
        topAdsFilterStatusFragment.setActive(selectedFilterStatus > 0);
        filterContentFragmentList.add(topAdsFilterStatusFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedFilterStatus);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected Intent setFilterChangedResult() {
        Intent intent = super.setFilterChangedResult();
        int filterStatus = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, -1);
        switch (filterStatus){
            case 0:
                UnifyTracking.eventTopAdsProductGroupsFilter(this,AppEventTracking.EventLabel.GROUP_STATUS_FILTER_ALL);
                break;
            case 1:
                UnifyTracking.eventTopAdsProductGroupsFilter(this,AppEventTracking.EventLabel.GROUP_STATUS_FILTER_ACTIVE);
                break;
            case 2:
                UnifyTracking.eventTopAdsProductGroupsFilter(this,AppEventTracking.EventLabel.GROUP_STATUS_FILTER_NOT_SEND);
                break;
            case 3:
                UnifyTracking.eventTopAdsProductGroupsFilter(this,AppEventTracking.EventLabel.GROUP_STATUS_FILTER_INACTIVE);
                break;
            default:
                break;
        }
        return intent;
    }
}