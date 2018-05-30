package com.tokopedia.district_recommendation.domain.mapper;

import com.tokopedia.district_recommendation.domain.model.Token;

import javax.inject.Inject;

public class TokenMapper {

    @Inject
    public TokenMapper() {

    }

    public Token convertTokenModel(com.tokopedia.core.manage.people.address.model.Token token) {
        Token tokenModel = new Token();
        tokenModel.setUnixTime(token.getUt());
        tokenModel.setDistrictRecommendation(token.getDistrictRecommendation());

        return tokenModel;
    }

}
