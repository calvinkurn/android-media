package com.tokopedia.product.manage.list.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.product.manage.list.data.ConfirmationProductData;
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType;
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Response;
import com.tokopedia.product.manage.list.view.listener.ProductManageView;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManagePresenter extends CustomerPresenter<ProductManageView> {

    void getGoldMerchantStatus();

    void bulkUpdateProduct(List<ConfirmationProductData> listUpdateResponse);

    void getProductList(int page, String keywordFilter, @CatalogProductOption String catalogOption,
                        @ConditionProductOption String conditionOption, int etalaseId,
                        @PictureStatusProductOption String pictureOption, @SortProductOption String sortOption,
                        String categoryId);

    void editPrice(String productId, String price, String currencyId, String currencyText);

    void setCashback(String productId, int cashback);

    void deleteSingleProduct(String productIds);

    void getFreeClaim(String graphqlQuery, String shopId);

    void getPopupsInfo(String productId);

    ArrayList<ConfirmationProductData> mapToProductConfirmationData(boolean isActionDelete, BulkBottomSheetType.StockType stockType, BulkBottomSheetType.EtalaseType etalaseType, List<ProductManageViewModel>  productManageViewModels);

    Pair<List<ProductUpdateV3Response>, List<ProductUpdateV3Response>> getBulkUpdateSuccessAndFailedList(List<ProductUpdateV3Response> listOfResponse);

}
