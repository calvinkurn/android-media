package com.tokopedia.topads.dashboard.view.activity;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.activity.TopAdsBaseActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddProductListFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

public class TopAdsAddProductListActivity extends TopAdsBaseActivity {

    @Override
    protected Fragment getNewFragment() {
        ArrayList<TopAdsProductViewModel> selectionParcel = getIntent().getExtras().getParcelableArrayList(
                TopAdsExtraConstant.EXTRA_SELECTIONS);
        boolean isExistingGroup = getIntent().getExtras().getBoolean(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        boolean isHideEtalase = getIntent().getExtras().getBoolean(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        int maxNumberSelection = getIntent().getExtras().getInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);
        return TopAdsAddProductListFragment.newInstance(maxNumberSelection, selectionParcel, isExistingGroup, isHideEtalase);
    }
}
