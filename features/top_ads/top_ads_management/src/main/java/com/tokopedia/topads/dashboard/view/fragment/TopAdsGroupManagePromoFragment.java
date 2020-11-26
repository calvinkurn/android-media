package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupManagePromoFragment extends TopAdsBaseGroupEditPromoFragment {

    private String shopId = "";

    public static TopAdsGroupManagePromoFragment createInstance(String adId, int choosenOption,
                                                              String groupId, String groupName) {
        TopAdsGroupManagePromoFragment fragment = new TopAdsGroupManagePromoFragment();
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
        UnifyTracking.eventTopAdsProductEditGrupManage(getActivity(), AppEventTracking.EventLabel.GROUP_PRODUCT_OPTION_NEW_GROUP);
        presenter.moveToNewProductGroup(adId, groupName, getShopId());
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        UnifyTracking.eventTopAdsProductEditGrupManage(getActivity(), AppEventTracking.EventLabel.GROUP_PRODUCT_OPTION_WITHOUT_GROUP);
        finishAndSetResult();
    }

    @Override
    protected void onSubmitFormChooseGroup(String choosenId) {
        UnifyTracking.eventTopAdsProductEditGrupManage(getActivity(), AppEventTracking.EventLabel.GROUP_PRODUCT_OPTION_EXISTING_GROUP);
        presenter.moveToExistProductGroup(adId, String.valueOf(choosenId), getShopId());
    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_save);
    }

    private String getShopId() {
        if(!TextUtils.isEmpty(shopId)) {
            return shopId;
        } else {
            UserSessionInterface userSession = new UserSession(getActivity());
            shopId = userSession.getShopId();
            return shopId;
        }
    }

}
