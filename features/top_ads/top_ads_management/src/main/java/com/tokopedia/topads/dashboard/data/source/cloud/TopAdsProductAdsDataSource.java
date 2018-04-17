package com.tokopedia.topads.dashboard.data.source.cloud;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.topads.dashboard.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;import com.tokopedia.topads.dashboard.data.model.data.ProductAdAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsProductAdsDataSource {

    private final TopAdsDetailProductMapper topAdsDetailProductMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;
    private final TopAdsBulkActionMapper topAdsBulkActionMapper;

    public TopAdsProductAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi,
                                      TopAdsDetailProductMapper topAdsDetailProductMapper,
                                      TopAdsBulkActionMapper topAdsBulkActionMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsDetailProductMapper = topAdsDetailProductMapper;
        this.topAdsBulkActionMapper = topAdsBulkActionMapper;
    }

    public Observable<TopAdsDetailProductDomainModel> getDetailProduct(String adId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return topAdsManagementApi.getDetailProduct(param).map(topAdsDetailProductMapper);
    }

    public Observable<TopAdsDetailProductDomainModel> saveDetailProduct(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel) {
        return topAdsManagementApi.editProductAd(getSaveProductDetailRequest(topAdsDetailProductDomainModel)).map(topAdsDetailProductMapper);
    }

    public Observable<TopAdsDetailProductDomainModel> createDetailProductList(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels, String source){
        return topAdsManagementApi.createProductAd(getSaveProductDetailRequestList(topAdsDetailProductDomainModels, source)).map(topAdsDetailProductMapper);
    }

    private DataRequest<List<TopAdsProductDetailDataSourceModel>> getSaveProductDetailRequestList(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels, String source) {
        DataRequest<List<TopAdsProductDetailDataSourceModel>> dataRequest = new DataRequest<>();
        List<TopAdsProductDetailDataSourceModel> dataRequestList = new ArrayList<>();
        for(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel : topAdsDetailProductDomainModels) {
            dataRequestList.add(convert(topAdsDetailProductDomainModel, source));
        }
        dataRequest.setData(dataRequestList);
        return dataRequest;
    }

    private DataRequest<List<TopAdsProductDetailDataSourceModel>> getSaveProductDetailRequest(TopAdsDetailProductDomainModel topAdsDetailShopDomainModel) {
        DataRequest<List<TopAdsProductDetailDataSourceModel>> dataRequest = new DataRequest<>();
        List<TopAdsProductDetailDataSourceModel> dataRequestList = new ArrayList<>();
        dataRequestList.add(convert(topAdsDetailShopDomainModel, ""));
        dataRequest.setData(dataRequestList);
        return dataRequest;
    }

    private TopAdsProductDetailDataSourceModel convert(TopAdsDetailProductDomainModel domainModel, String source) {
        TopAdsProductDetailDataSourceModel dataModel = new TopAdsProductDetailDataSourceModel();
        dataModel.setAdId(domainModel.getAdId());
        dataModel.setAdType(domainModel.getAdType());
        dataModel.setGroupId(domainModel.getGroupId());
        dataModel.setShopId(domainModel.getShopId());
        dataModel.setItemId(domainModel.getItemId());
        dataModel.setStatus(domainModel.getStatus());
        dataModel.setPriceBid(domainModel.getPriceBid());
        dataModel.setAdBudget(domainModel.getAdBudget());
        dataModel.setPriceDaily(domainModel.getPriceDaily());
        dataModel.setStickerId(domainModel.getStickerId());
        dataModel.setAdSchedule(domainModel.getAdSchedule());
        dataModel.setAdStartDate(domainModel.getAdStartDate());
        dataModel.setAdStartTime(domainModel.getAdStartTime());
        dataModel.setAdEndDate(domainModel.getAdEndDate());
        dataModel.setAdEndTime(domainModel.getAdEndTime());
        dataModel.setAdImage(domainModel.getAdImage());
        dataModel.setAdTitle(domainModel.getAdTitle());
        dataModel.setSuggestionBidValue(domainModel.getSuggestionBidValue());
        dataModel.setSuggestionBidButton(domainModel.getSuggestionBidButton());
        if(TextUtils.isEmpty(source)) {
            dataModel.setSource(TopAdsNetworkConstant.VALUE_SOURCE_ANDROID);
        }else{
            dataModel.setSource(source);
        }
        return dataModel;
    }

    public Observable<ProductAdBulkAction> moveProductGroup(String adId, String groupId, String shopId) {
        DataRequest<ProductAdBulkAction> productAdBulkActionDataRequest = convertToProductBulkAction(adId, groupId, shopId);
        return topAdsManagementApi.bulkActionProductAd(productAdBulkActionDataRequest).map(topAdsBulkActionMapper);
    }

    private DataRequest<ProductAdBulkAction> convertToProductBulkAction(String adId, String groupId, String shopId) {
        DataRequest<ProductAdBulkAction> productAdBulkActionDataRequest = new DataRequest<>();
        ProductAdBulkAction productAdBulkAction = new ProductAdBulkAction();
        productAdBulkAction.setAction(TopAdsNetworkConstant.ACTION_BULK_MOVE_AD);
        productAdBulkAction.setShopId(shopId);
        ProductAdAction productAdAction = new ProductAdAction();
        productAdAction.setId(adId);
        productAdAction.setGroupId(groupId);
        List<ProductAdAction> adActions = new ArrayList<>();
        adActions.add(productAdAction);
        productAdBulkAction.setAds(adActions);
        productAdBulkActionDataRequest.setData(productAdBulkAction);
        return productAdBulkActionDataRequest;
    }
}