package com.tokopedia.logisticaddaddress.domain.mapper;

import com.tokopedia.logisticaddaddress.domain.model.Token;

import javax.inject.Inject;

public class TokenMapper {

    @Inject
    public TokenMapper() {

    }

    public Token convertTokenModel(com.tokopedia.logisticdata.data.entity.address.Token token) {
        Token tokenModel = new Token();
        tokenModel.setUnixTime(token.getUt());
        tokenModel.setDistrictRecommendation(token.getDistrictRecommendation());

        return tokenModel;
    }

}
