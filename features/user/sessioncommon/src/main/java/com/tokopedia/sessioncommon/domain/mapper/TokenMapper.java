package com.tokopedia.sessioncommon.domain.mapper;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class TokenMapper implements Func1<Response<TokenViewModel>, TokenViewModel> {

    @Inject
    public TokenMapper() {
    }

    @Override
    public TokenViewModel call(Response<TokenViewModel> response) {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
