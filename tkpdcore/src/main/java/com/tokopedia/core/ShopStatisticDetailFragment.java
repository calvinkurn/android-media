package com.tokopedia.core;

import android.os.Bundle;

import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.facade.FacadeShopStatistic;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticResponse;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticReview;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticSatisfaction;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;


/**
 * A placeholder fragment containing a simple view.
 */
public class ShopStatisticDetailFragment extends V2BaseFragment {

    public static class Model {
        public ShopStatisticReview.Model review;
        public ShopStatisticTransaction.Model transaction;
        public ShopStatisticResponse.Model response;
        public ShopStatisticSatisfaction.Model satisfaction;
    }

    private ShopStatisticSatisfaction shopStatisticSatisfaction;
    private ShopStatisticResponse shopStatisticResponse;
    private ShopStatisticTransaction shopStatisticTransaction;
    private ShopStatisticReview shopStatisticReview;

    public static ShopStatisticDetailFragment createInstance(String shop_info) {
        ShopStatisticDetailFragment fragment = new ShopStatisticDetailFragment();
        fragment.shopInfo = shop_info;
        return fragment;
    }

    private String shopInfo;
    private FacadeShopStatistic facade;
    private Model model;

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
    protected void setListener() {
        shopStatisticReview.setListener();
    }

    private void initVar() {
        facade = FacadeShopStatistic.createInstance(shopInfo);
        model = new Model();
    }

    private void getModelFromShopInfo() {
        model.transaction = facade.getTransactionModel();
//        model.response = facade.getResponseModel(); // TODO Useless?
        model.satisfaction = facade.getSatisfactionModel(); // TODO DONE??
        model.review = facade.getReviewModel(); // TODO DONE?
    }

    private void setModelToView(){
        shopStatisticTransaction.setModel(model.transaction);
//        shopStatisticResponse.setModelToView(model.response);
        shopStatisticSatisfaction.setModel(model.satisfaction);
        shopStatisticReview.setModel(model.review);
    }
}
