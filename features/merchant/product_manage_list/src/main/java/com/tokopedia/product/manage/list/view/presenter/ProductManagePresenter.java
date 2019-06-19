package com.tokopedia.product.manage.list.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.product.manage.list.view.listener.ProductManageView;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManagePresenter extends CustomerPresenter<ProductManageView> {

    void getGoldMerchantStatus();

    void getListFeaturedProduct();

    void getListProduct(int page, String keywordFilter, @CatalogProductOption String catalogOption,
                        @ConditionProductOption String conditionOption, int etalaseId,
                        @PictureStatusProductOption String pictureOption, @SortProductOption String sortOption,
                        String categoryId);

    void editPrice(String productId, String price, String currencyId, String currencyText);

    void setCashback(String productId, int cashback);

    void deleteProduct(List<String> productIds);

    void saveSourceTagging(boolean isSellerApp);

    void getFreeClaim(String graphqlQuery, String shopId);

    void needGetPopupsInfo();

    void getPopupsInfo(String productId);
}
