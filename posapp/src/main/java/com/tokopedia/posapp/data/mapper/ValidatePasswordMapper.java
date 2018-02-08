package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.ValidatePasswordResponse;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordMapper implements Func1<Response<TkpdResponse>, CheckPasswordDomain> {
    @Override
    public CheckPasswordDomain call(Response<TkpdResponse> tkpdResponse) {
        CheckPasswordDomain checkPasswordDomain = new CheckPasswordDomain();
        checkPasswordDomain.setStatus(false);
        checkPasswordDomain.setMessage(tkpdResponse.body().getErrorMessageJoined());

        if (tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ValidatePasswordResponse response = tkpdResponse.body().convertDataObj(ValidatePasswordResponse.class);

            if(response.getIsSuccess() != null && response.getIsSuccess() == 1) {
                checkPasswordDomain.setStatus(true);
                checkPasswordDomain.setMessage("OK");
            }
        }

        return checkPasswordDomain;
    }
}
