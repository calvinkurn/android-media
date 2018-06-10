package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsDetailShopMapper implements Func1<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>, TopAdsDetailShopDomainModel> {

    @Inject
    public TopAdsDetailShopMapper() {
    }

    @Override
    public TopAdsDetailShopDomainModel call(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private TopAdsDetailShopDomainModel mappingResponse(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> response) {
        TopAdsProductDetailDataSourceModel dataModel = response.body().getData().get(0);
        TopAdsDetailShopDomainModel domainModel = new TopAdsDetailShopDomainModel();

        domainModel.setAdId(dataModel.getAdId());
        domainModel.setAdType(dataModel.getAdType());
        domainModel.setGroupId(dataModel.getGroupId());
        domainModel.setShopId(dataModel.getShopId());
        domainModel.setItemId(dataModel.getItemId());
        domainModel.setStatus(dataModel.getStatus());
        domainModel.setPriceBid(dataModel.getPriceBid());
        domainModel.setAdBudget(dataModel.getAdBudget());
        domainModel.setPriceDaily(dataModel.getPriceDaily());
        domainModel.setStickerId(dataModel.getStickerId());
        domainModel.setAdSchedule(dataModel.getAdSchedule());
        domainModel.setAdStartDate(dataModel.getAdStartDate());
        domainModel.setAdStartTime(dataModel.getAdStartTime());
        domainModel.setAdEndDate(dataModel.getAdEndDate());
        domainModel.setAdEndTime(dataModel.getAdEndTime());
        domainModel.setAdImage(dataModel.getAdImage());
        domainModel.setAdTitle(dataModel.getAdTitle());
        domainModel.setEnoughDeposit(dataModel.isEnoughDeposit());
        return domainModel;
    }
}
