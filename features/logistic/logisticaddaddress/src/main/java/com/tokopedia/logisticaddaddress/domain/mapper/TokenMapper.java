package com.tokopedia.logisticaddaddress.domain.mapper;

import com.tokopedia.logisticaddaddress.domain.model.Token;

import javax.inject.Inject;

public class TokenMapper {

    @Inject
    public TokenMapper() {

    }

    public Token reverseTokenModel(com.tokopedia.logisticdata.data.entity.address.Token token) {
        Token tokenModel = new Token();
        tokenModel.setUnixTime(token.getUt());
        tokenModel.setDistrictRecommendation(token.getDistrictRecommendation());

        return tokenModel;
    }

    public com.tokopedia.logisticdata.data.entity.address.Token convertTokenModel(Token token) {
        com.tokopedia.logisticdata.data.entity.address.Token result = new com.tokopedia.logisticdata.data.entity.address.Token();
        result.setUt(token.getUnixTime());
        result.setDistrictRecommendation(token.getDistrictRecommendation());

        return result;
    }

}
