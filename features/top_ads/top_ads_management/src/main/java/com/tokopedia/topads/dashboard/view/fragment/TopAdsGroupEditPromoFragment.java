package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.user.session.UserSession;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupEditPromoFragment extends TopAdsBaseGroupEditPromoFragment{

    public static TopAdsGroupEditPromoFragment createInstance(String adId, int choosenOption,
                                                              String groupId, String groupName) {
        TopAdsGroupEditPromoFragment fragment = new TopAdsGroupEditPromoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putInt(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, choosenOption);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        fragment.setupArguments(bundle);
        return fragment;
    }

    @Override
    protected void onSubmitFormNewGroup(String groupName) {
        presenter.moveToNewProductGroup(adId, groupName, new UserSession(getActivity()).getShopId());
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        presenter.moveOutProductGroup(new UserSession(getActivity()).getShopId(),adId);
    }

    @Override
    protected void onSubmitFormChooseGroup(String choosenId) {
        presenter.moveToExistProductGroup(adId, choosenId , new UserSession(getActivity()).getShopId());
    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_save);
    }

}
