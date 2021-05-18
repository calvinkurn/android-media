package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {

    void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    );

    void getCategoryData(String categoryId, String operatorId, String productId, String clientNumber);

    void processStateDataToReRender();

    void addToCart(DigitalCheckoutPassData digitalCheckoutPassData, RequestBodyIdentifier digitalIdentifierParam,
                   DigitalSubscriptionParams digitalSubscriptionParams);
}
