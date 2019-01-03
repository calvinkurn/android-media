package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAddingPromoOptionAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddingPromoOptionView;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddingPromoOptionPresenter;

import java.util.List;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionFragment extends BaseListFragment<TopAdsAddingPromoOptionPresenter, TopAdsAddingPromoOptionModel>
        implements TopAdsAddingPromoOptionView {

    public static final String EXTRA_SELECTED_OPTION = "selected_option";

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
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public void onItemClicked(TopAdsAddingPromoOptionModel topAdsAddingPromoOptionModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_OPTION, topAdsAddingPromoOptionModel.getOptionId());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onSuccessGetListTopAdsAddingOption(List<TopAdsAddingPromoOptionModel> optionModelList) {
        onSearchLoaded(optionModelList, optionModelList.size());
    }
}
