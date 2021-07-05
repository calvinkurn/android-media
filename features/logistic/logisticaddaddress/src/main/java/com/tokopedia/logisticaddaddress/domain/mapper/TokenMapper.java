package com.tokopedia.logisticaddaddress.domain.mapper;

import com.tokopedia.logisticaddaddress.domain.model.Token;

import javax.inject.Inject;

public class TokenMapper {

    @Inject
    public TokenMapper() {

    }

    public Token reverseTokenModel(com.tokopedia.logisticCommon.data.entity.address.Token token) {
        Token tokenModel = new Token();
        tokenModel.setUt(token.getUt());
        tokenModel.setDistrictRecommendation(token.getDistrictRecommendation());

        return tokenModel;
    }

    public com.tokopedia.logisticCommon.data.entity.address.Token convertTokenModel(Token token) {
        com.tokopedia.logisticCommon.data.entity.address.Token result = new com.tokopedia.logisticCommon.data.entity.address.Token();
        result.setUt(token.getUt());
        result.setDistrictRecommendation(token.getDistrictRecommendation());

        return result;
    }

}
