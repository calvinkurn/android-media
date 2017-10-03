package com.tokopedia.sellerapp.dashboard.model;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

/**
 * Created by User on 9/12/2017.
 */

public class ShopModelWithScore {
    private ShopModel shopModel;
    private ShopScoreMainDomainModel shopScoreMainDomainModel;

    public ShopModelWithScore(ShopModel shopModel,
                              ShopScoreMainDomainModel shopScoreMainDomainModel){
        this.shopModel = shopModel;
        this.shopScoreMainDomainModel = shopScoreMainDomainModel;
    }
    public ShopModel getShopModel() {
        return shopModel;
    }

    public ShopScoreMainDomainModel getShopScoreMainDomainModel() {
        return shopScoreMainDomainModel;
    }

    public void setShopModel(ShopModel shopModel) {
        this.shopModel = shopModel;
    }

    public void setShopScoreMainDomainModel(ShopScoreMainDomainModel shopScoreMainDomainModel) {
        this.shopScoreMainDomainModel = shopScoreMainDomainModel;
    }
}
