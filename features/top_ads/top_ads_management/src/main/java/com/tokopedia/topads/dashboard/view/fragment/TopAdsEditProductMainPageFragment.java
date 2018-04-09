package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditCostProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditScheduleProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupManagePromoActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailProductPresenterImpl;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditProductMainPageFragment extends TopAdsDetailEditMainPageFragment<ProductAd> {

    private static final int REQUEST_MANAGE_PRODUCT = 2;
    private LabelView manageGroup;

    public static Fragment createInstance(ProductAd ad, String adId) {
        Fragment fragment = new TopAdsEditProductMainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailProductPresenterImpl<ProductAd>(getActivity(), this, new TopAdsProductAdInteractorImpl(
                new TopAdsManagementService(new SessionHandler(getActivity())),
                new TopAdsCacheDataSourceImpl(getActivity())));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_product_main_page;
    }

    @Override
    protected void refreshAd() {
        if (ad != null) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        manageGroup = (LabelView) view.findViewById(R.id.manage_group);
    }

    @Override
    protected void updateMainView(ProductAd ad) {
        super.updateMainView(ad);
        if (isHasGroupAd()) {
            manageGroup.setContent(ad.getGroupName());
        } else {
            manageGroup.setContent(getString(R.string.label_top_ads_empty_group));
        }
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        manageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onManageGroupClicked();
            }
        });
    }

    private void onManageGroupClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsGroupManagePromoActivity.createIntent(getActivity(), String.valueOf(ad.getId()),
                    TopAdsGroupManagePromoFragment.NOT_IN_GROUP, ad.getGroupName(), String.valueOf(ad.getGroupId()));
        }else{
            intent = TopAdsGroupManagePromoActivity.createIntent(getActivity(), adId,
                    TopAdsGroupManagePromoFragment.NOT_IN_GROUP, "", "");
        }
        startActivityForResult(intent, REQUEST_MANAGE_PRODUCT);
    }

    @Override
    protected void onScheduleClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditScheduleProductActivity.createIntent(getActivity(), ad.getId());
        }else{
            intent = TopAdsEditScheduleProductActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void onCostClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditCostProductActivity.createIntent(getActivity(), ad.getId());
        }else{
            intent = TopAdsEditCostProductActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && requestCode == REQUEST_MANAGE_PRODUCT &&
                intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false)) {
            setResultAdDetailChanged();
            getActivity().finish();
        }
    }

    private boolean isHasGroupAd() {
        if (ad == null) {
            return false;
        }
        return !TextUtils.isEmpty(ad.getGroupName()) && ad.getGroupId() > 0;
    }
}
