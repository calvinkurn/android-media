package com.tokopedia.posapp.auth.validatepassword.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.auth.validatepassword.data.model.ValidatePasswordResponse;
import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordMapper implements Func1<Response<TkpdResponse>, ValidatePasswordDomain> {
    @Override
    public ValidatePasswordDomain call(Response<TkpdResponse> tkpdResponse) {
        ValidatePasswordDomain validatePasswordDomain = new ValidatePasswordDomain();
        validatePasswordDomain.setStatus(false);
        validatePasswordDomain.setMessage(tkpdResponse.body().getErrorMessageJoined());

        if (tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ValidatePasswordResponse response = tkpdResponse.body().convertDataObj(ValidatePasswordResponse.class);

            if(response.getIsSuccess() != null && response.getIsSuccess() == 1) {
                validatePasswordDomain.setStatus(true);
                validatePasswordDomain.setMessage("OK");
            }
        }

        return validatePasswordDomain;
    }
}
