package com.tokopedia.posapp.auth.login.data.mapper;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.posapp.auth.login.domain.usecase.PosGetTokenUseCase;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * @author okasurya on 3/9/18.
 */

public class PosTokenMapper extends TokenMapper {
    @Inject
    public PosTokenMapper() {

    }

    @Override
    public TokenViewModel call(Response<String> response) {
        TokenViewModel tokenViewModel = super.call(response);
        if(tokenViewModel.getScope() != null &&
                tokenViewModel.getScope().contains(PosGetTokenUseCase.O2O_DELEGATE)) {
            return tokenViewModel;
        } else {
            throw new ErrorMessageException("User doesnt have o2o scope");
        }
    }
}
