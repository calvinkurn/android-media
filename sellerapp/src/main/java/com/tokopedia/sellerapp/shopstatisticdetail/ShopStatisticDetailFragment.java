package com.tokopedia.sellerapp.shopstatisticdetail;

import android.os.Bundle;

import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticResponse;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticReview;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticSatisfaction;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.facade.FacadeShopStatistic;


/**
 * A placeholder fragment containing a simple view.
 */
public class ShopStatisticDetailFragment extends V2BaseFragment {

    private ShopStatisticSatisfaction shopStatisticSatisfaction;
    private ShopStatisticResponse shopStatisticResponse;
    private ShopStatisticTransaction shopStatisticTransaction;
    private ShopStatisticReview shopStatisticReview;
    private String shopInfoDashboardModelString;
    private FacadeShopStatistic facade;
    private Model model;

    public static ShopStatisticDetailFragment createInstance(String shopInfoDashboardModelString) {
        ShopStatisticDetailFragment fragment = new ShopStatisticDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopStatisticDetail.EXTRA_SHOP_INFO, shopInfoDashboardModelString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_shop_statistic_detail;
    }

    @Override
    protected void onCreateView() {
        getModelFromShopInfo();
        setModelToView();
    }

    @Override
    protected Object getHolder() {
        return null;
    }

    @Override
    protected void setHolder(Object holder) {

    }

    @Override
    protected void initView() {
        shopStatisticSatisfaction = new ShopStatisticSatisfaction(getActivity(), findViewById(R.id.satisfaction));
        shopStatisticResponse = new ShopStatisticResponse(findViewById(R.id.response));
        shopStatisticTransaction = new ShopStatisticTransaction(getActivity(), findViewById(R.id.transaction));
        shopStatisticReview = new ShopStatisticReview(getActivity(), findViewById(R.id.view_rating_stats));
    }

    @Override
    protected void setListener() {}

    private void initVar() {
        shopInfoDashboardModelString = getArguments().getString(ShopStatisticDetail.EXTRA_SHOP_INFO);
        facade = FacadeShopStatistic.createInstance(shopInfoDashboardModelString);
        model = new Model();
    }

    private void getModelFromShopInfo() {
        model.transaction = facade.getTransactionModel();
        model.satisfaction = facade.getSatisfactionModel();
        model.review = facade.getReviewModel();
    }

    private void setModelToView(){
        shopStatisticTransaction.setModel(model.transaction);
        shopStatisticSatisfaction.setModel(model.satisfaction);
        shopStatisticReview.setModel(model.review);
    }

    public static class Model {
        public ShopStatisticReview.Model review;
        public ShopStatisticTransaction.Model transaction;
        public ShopStatisticResponse.Model response;
        public ShopStatisticSatisfaction.Model satisfaction;
    }
}
