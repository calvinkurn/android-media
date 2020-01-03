package com.tokopedia.opportunity.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.opportunity.data.OpportunityModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/3/17.
 */
public class OpportunityListMapper implements Func1<Response<TkpdResponse>, OpportunityModel> {

    @Override
    public OpportunityModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private OpportunityModel mappingResponse(Response<TkpdResponse> response) {
        OpportunityModel model = new OpportunityModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                OpportunityData data = response.body().convertDataObj(OpportunityData.class);
                model.setSuccess(true);
                model.setOpportunityDataData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));

        }
        return model;
    }
}
