package com.tokopedia.opportunity.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.AcceptReplacementData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.opportunity.data.AcceptReplacementModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/3/17.
 */
public class AcceptOpportunityMapper
        implements Func1<Response<TkpdResponse>, AcceptReplacementModel> {

    @Override
    public AcceptReplacementModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private AcceptReplacementModel mappingResponse(Response<TkpdResponse> response) {
        AcceptReplacementModel model = new AcceptReplacementModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                AcceptReplacementData data = response.body().convertDataObj(AcceptReplacementData.class);
                model.setSuccess(data != null && data.isSuccess());
                model.setMessage(data != null ? data.getMessage() : "");
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
