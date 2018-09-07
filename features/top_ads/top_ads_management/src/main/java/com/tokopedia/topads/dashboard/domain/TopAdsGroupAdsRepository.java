package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;

import java.util.List;

import rx.Observable;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;


/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsGroupAdsRepository {
    Observable<List<GroupAd>> searchGroupAds(String string);

    Observable<DataResponseCreateGroup> createGroup(CreateGroupRequest createGroupRequest);

    Observable<TopAdsDetailGroupDomainModel> getDetailGroup(String groupId);

    Observable<TopAdsDetailGroupDomainModel> saveDetailGroup(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel);

    Observable<GetSuggestionResponse> getSuggestion(GetSuggestionBody getSuggestionBody, String shopId);
}
