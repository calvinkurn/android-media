package com.tokopedia.shop.score.view.mapper;

import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop.score.view.model.ShopScoreDetailStateEnum;

/**
 * @author sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailStateMapper {
    public static ShopScoreDetailStateEnum map(ShopScoreDetailDomainModel domainModels) {
        switch (domainModels.getState()) {
            case ShopScoreDetailDomainModel.GOLD_MERCHANT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.NOT_GOLD_MERCHANT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            default:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_QUALIFIED_BADGE;
        }
    }
}
