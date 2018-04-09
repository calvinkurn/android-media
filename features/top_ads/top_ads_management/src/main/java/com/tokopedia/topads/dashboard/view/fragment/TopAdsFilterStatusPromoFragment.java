package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

public class TopAdsFilterStatusPromoFragment extends TopAdsFilterRadioButtonFragment {

    private int selectedStatusPromo;

    public static TopAdsFilterStatusPromoFragment createInstance(int status) {
        TopAdsFilterStatusPromoFragment fragment = new TopAdsFilterStatusPromoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_status_promo;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedStatusPromo = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedStatusPromo);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        String[] statusValueList = getResources().getStringArray(R.array.top_ads_filter_status_promo_list_values);
        String[] statusNameList = getResources().getStringArray(R.array.top_ads_filter_status_promo_list_entries);
        for (int i = 0; i < statusNameList.length; i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(statusNameList[i]);
            radioButtonItem.setValue(statusValueList[i]);
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }
        updateSelectedPosition(radioButtonItemList);
        return radioButtonItemList;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedAdapterPosition > 0) { // has been updated for first time only
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedStatusPromo) {
                selectedAdapterPosition = i;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_status);
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selectedAdapterPosition > -1) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, Integer.parseInt(getSelectedRadioValue()));
        }
        return intent;
    }
}