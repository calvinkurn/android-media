package com.tokopedia.topads.keyword.data.mapper;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.keyword.data.model.EditTopAdsKeywordDetailDataModel;
import com.tokopedia.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailDataMapper implements Func1<Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>>, EditTopAdsKeywordDetailDomainModel> {

    public static final String ON = "on";
    public static final String OFF = "off";

    @Inject
    public TopAdsKeywordEditDetailDataMapper() {
    }

    public static TopAdsKeywordEditDetailInputDataModel mapDomainToData(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        TopAdsKeywordEditDetailInputDataModel dataModel = new TopAdsKeywordEditDetailInputDataModel();
        dataModel.setKeywordId(modelInput.getKeywordId());
        dataModel.setGroupId(modelInput.getGroupId());
        dataModel.setKeywordTag(modelInput.getKeywordTag());
        dataModel.setKeywordTypeId(modelInput.getKeywordTypeId());
        dataModel.setPriceBid(modelInput.getPriceBid());
        dataModel.setShopId(modelInput.getShopId());
        dataModel.setSource(TopAdsNetworkConstant.SOURCE_DASHBOARD_USER_MAIN);
        if (modelInput.getToggle() == 1) {
            dataModel.setToggle(ON);
        } else {
            dataModel.setToggle(OFF);
        }
        return dataModel;
    }

    @Override
    public EditTopAdsKeywordDetailDomainModel call(Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>> dataResponse) {
        EditTopAdsKeywordDetailDomainModel domainModel = new EditTopAdsKeywordDetailDomainModel();
        EditTopAdsKeywordDetailDataModel dataModel = dataResponse.body().getData().get(0);
        domainModel.setGroupId(dataModel.getGroupId());
        domainModel.setKeywordId(dataModel.getKeywordId());
        domainModel.setKeywordTag(dataModel.getKeywordTag());
        domainModel.setKeywordTypeId(dataModel.getKeywordTypeId());
        domainModel.setPriceBid(dataModel.getPriceBid());
        domainModel.setShopId(dataModel.getShopId());
        domainModel.setSource(dataModel.getSource());
        int toggle = (dataModel.getToggle().equals(ON)) ? 1 : 0;
        domainModel.setToggle(toggle);
        return domainModel;
    }
}
