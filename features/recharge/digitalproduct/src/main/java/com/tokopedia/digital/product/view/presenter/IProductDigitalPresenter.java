package com.tokopedia.digital.product.view.presenter;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {

    void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    );

    void getCategoryData(String categoryId, String operatorId, String productId, String clientNumber);

    void processStateDataToReRender();
}
