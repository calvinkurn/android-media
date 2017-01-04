package com.tokopedia.sellerapp.gmstat.presenters;

import android.content.res.AssetManager;
import android.support.annotation.IntRange;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;
import com.tokopedia.sellerapp.gmstat.models.GetPopularProduct;
import com.tokopedia.sellerapp.gmstat.models.GetProductGraph;
import com.tokopedia.sellerapp.gmstat.models.GetShopCategory;
import com.tokopedia.sellerapp.gmstat.models.GetTransactionGraph;
import com.tokopedia.sellerapp.gmstat.utils.GMStatNetworkController;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by normansyahputa on 1/2/17.
 */

public class GMFragmentPresenterImpl implements GMFragmentPresenter {

    private long sDate = -1, eDate = -1;
    boolean isFetchData = false, isFirstTime = false;
    private long shopId;
    private float[] mValues = new float[10];
    private String[] mLabels = new String[10];

    @IntRange(from = 0, to = 2)
    int lastSelectionPeriod = 1;
    private int selectionType;


    private GMFragmentView gmFragmentView;
    private GMStat gmStat;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public GMFragmentPresenterImpl(GMFragmentView gmFragmentView, GMStat gmStat, long shopId){
        this.gmFragmentView = gmFragmentView;
        this.gmStat = gmStat;
        this.shopId = shopId;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public void setFetchData(boolean fetchData) {
        isFetchData = fetchData;
    }

    public void initInstance(){
        for(int i=0;i<mLabels.length;i++){
            mLabels[i] = "";
        }
    }

    public float[] getmValues() {
        return mValues;
    }

    public String[] getmLabels() {
        return mLabels;
    }

    GMStatNetworkController.GetGMStat gmStatListener = new GMStatNetworkController.GetGMStat() {
        @Override
        public void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
            gmFragmentView.onSuccessGetShopCategory(getShopCategory);
        }

        @Override
        public void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph) {
            gmFragmentView.onSuccessTransactionGraph(getTransactionGraph, sDate, eDate, lastSelectionPeriod, selectionType);
        }

        @Override
        public void onSuccessProductnGraph(GetProductGraph getProductGraph) {
            gmFragmentView.onSuccessProductnGraph(getProductGraph, isFirstTime);
        }

        @Override
        public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
            gmFragmentView.onSuccessPopularProduct(getPopularProduct);
        }

        @Override
        public void onSuccessBuyerData(GetBuyerData getBuyerData) {
            gmFragmentView.onSuccessBuyerData(getBuyerData);
        }

        @Override
        public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
            gmFragmentView.onSuccessGetKeyword(getKeywords);
        }

        @Override
        public void onSuccessGetCategory(List<HadesV1Model> hadesV1Models) {
            gmFragmentView.onSuccessGetCategory(hadesV1Models);
        }

        @Override
        public void onComplete() {
            gmFragmentView.onComplete();
        }

        @Override
        public void onError(Throwable e) {
            gmFragmentView.onError(e);
        }

        @Override
        public void onFailure() {

        }
    };

    @Override
    public void fetchData() {
        if(isFetchData) {
            isFetchData = false;
            gmFragmentView.resetToLoading();
            gmStat.getGmStatNetworkController().fetchData(shopId, sDate, eDate, compositeSubscription, gmStatListener);
        }else if(!isFirstTime && !isFetchData){
            //[START] real network
            gmStat.getGmStatNetworkController().fetchData(shopId, compositeSubscription, gmStatListener);
            //[END] real network
        }
    }

    @Override
    public void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType){
        this.lastSelectionPeriod = lastSelectionPeriod;
        this.selectionType = selectionType;
        isFetchData = true;
        this.sDate = sDate;
        this.eDate = eDate;
        gmFragmentView.bindHeader(sDate, eDate, lastSelectionPeriod, selectionType);

        //[START] dummy data
//        gmstat.getGmStatNetworkController().fetchData(gmStatListener, getActivity().getAssets());
        //[END] dummy data
    }

    @Override
    public void onResume() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        fetchData();
    }

    @Override
    public void onPause() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    public void displayDefaultValue(AssetManager assets){
        if(assets == null)
            return;

        gmStat.getGmStatNetworkController().fetchDataEmptyState(gmStatListener, assets);
    }
}
