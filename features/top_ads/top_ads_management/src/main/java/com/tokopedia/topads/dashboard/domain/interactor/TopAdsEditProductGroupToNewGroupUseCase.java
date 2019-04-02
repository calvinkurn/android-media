package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.model.request.AdCreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsEditProductGroupToNewGroupUseCase extends UseCase<Boolean> {

    public static final String AD_ID = "AD_ID";
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String GROUP_TOTAL = "1";
    public static final String SOURCE = "sellerapp";
    public static final String PRODUCT_AD_TYPE = "1";

    TopAdsShopAdsRepository topAdsShopAdsRepository;
    TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsEditProductGroupToNewGroupUseCase(TopAdsShopAdsRepository topAdsShopAdsRepository, TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super();
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.getDetail(requestParams.getString(AD_ID, ""))
                .flatMap(createNewGroup(requestParams))
                .map(new Func1<DataResponseCreateGroup, Boolean>() {
                    @Override
                    public Boolean call(DataResponseCreateGroup dataResponseCreateGroup) {
                        if(dataResponseCreateGroup != null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
    }

    private Func1<TopAdsDetailShopDomainModel, Observable<DataResponseCreateGroup>> createNewGroup(final RequestParams requestParams) {
        return new Func1<TopAdsDetailShopDomainModel, Observable<DataResponseCreateGroup>>() {
            @Override
            public Observable<DataResponseCreateGroup> call(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
                CreateGroupRequest createGroupRequest = convertRequest(topAdsDetailShopDomainModel, requestParams);
                return topAdsGroupAdsRepository.createGroup(createGroupRequest);
            }
        };
    }

    private CreateGroupRequest convertRequest(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel, RequestParams requestParams) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(requestParams.getString(GROUP_NAME, ""));
        createGroupRequest.setShopId(requestParams.getString(SHOP_ID, ""));
        createGroupRequest.setStatus(topAdsDetailShopDomainModel.getStatus());
        createGroupRequest.setPriceBid(Math.round(topAdsDetailShopDomainModel.getPriceBid()));
        createGroupRequest.setPriceDaily(Math.round(topAdsDetailShopDomainModel.getPriceDaily()));
        createGroupRequest.setGroupBudget(topAdsDetailShopDomainModel.getAdBudget());
        createGroupRequest.setGroupSchedule(topAdsDetailShopDomainModel.getAdSchedule());
        createGroupRequest.setGroupStartDate(topAdsDetailShopDomainModel.getAdStartDate());
        createGroupRequest.setGroupEndDate(topAdsDetailShopDomainModel.getAdEndDate());
        createGroupRequest.setGroupStartTime(topAdsDetailShopDomainModel.getAdStartTime());
        createGroupRequest.setGroupEndTime(topAdsDetailShopDomainModel.getAdEndTime());
        createGroupRequest.setStickerId(topAdsDetailShopDomainModel.getStickerId());
        createGroupRequest.setGroupTotal(GROUP_TOTAL);
        createGroupRequest.setSource(requestParams.getString(TopAdsSourceTaggingConstant.KEY_TAGGING_SOURCE, SOURCE));
        AdCreateGroupRequest adCreateGroupRequest = new AdCreateGroupRequest();
        adCreateGroupRequest.setAdId(topAdsDetailShopDomainModel.getAdId());
        adCreateGroupRequest.setItemId(topAdsDetailShopDomainModel.getItemId());
        adCreateGroupRequest.setGroupId(topAdsDetailShopDomainModel.getGroupId());
        adCreateGroupRequest.setAdType(PRODUCT_AD_TYPE);
        List<AdCreateGroupRequest> ads = new ArrayList<>();
        ads.add(adCreateGroupRequest);
        createGroupRequest.setAds(ads);
        return createGroupRequest;
    }

    public static RequestParams createRequestParams(String adId, String groupName, String shopId, String source){
        RequestParams params = RequestParams.create();
        params.putString(AD_ID, adId);
        params.putString(GROUP_NAME, groupName);
        params.putString(SHOP_ID, shopId);
        params.putString(TopAdsSourceTaggingConstant.KEY_TAGGING_SOURCE, source);
        return params;
    }
}
