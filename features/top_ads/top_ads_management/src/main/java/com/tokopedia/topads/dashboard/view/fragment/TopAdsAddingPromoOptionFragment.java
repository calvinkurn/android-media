package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsAddingOption;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCreatePromoShopActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAddingPromoOptionAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddingPromoOptionView;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddingPromoOptionPresenter;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;

import java.util.List;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionFragment extends BaseListFragment<TopAdsAddingPromoOptionPresenter, TopAdsAddingPromoOptionModel>
        implements TopAdsAddingPromoOptionView {

    private TopAdsAddingPromoOptionPresenter promoOptionPresenter;
    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        promoOptionPresenter = new TopAdsAddingPromoOptionPresenter();
        promoOptionPresenter.attachView(this);
    }

    @Override
    protected BaseListAdapter<TopAdsAddingPromoOptionModel> getNewAdapter() {
        return new TopAdsAddingPromoOptionAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        promoOptionPresenter.getListAddingOption(getResources().getStringArray(R.array.top_ads_adding_option_title),
                getResources().getStringArray(R.array.top_ads_adding_option_subtitle),
                getResources().getStringArray(R.array.top_ads_adding_option_value),
                getResources().obtainTypedArray(R.array.top_ads_adding_option_icon));
    }

    @Override
    public void onItemClicked(TopAdsAddingPromoOptionModel topAdsAddingPromoOptionModel) {
        Intent intent = null;
        switch (topAdsAddingPromoOptionModel.getOptionId()){
            case TopAdsAddingOption.SHOP_OPT:
                ShopAd shopAd = new ShopAd();
                intent = TopAdsCreatePromoShopActivity.createIntent(getActivity(), shopAd);
                break;
            case TopAdsAddingOption.PRODUCT_OPT: intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
                break;
            case TopAdsAddingOption.KEYWORDS_OPT: intent = TopAdsKeywordNewChooseGroupActivity.createIntent(getActivity(), true, null);
                break;
        }
        if (intent != null) {
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onSuccessGetListTopAdsAddingOption(List<TopAdsAddingPromoOptionModel> optionModelList) {
        onSearchLoaded(optionModelList, optionModelList.size());
    }
}
