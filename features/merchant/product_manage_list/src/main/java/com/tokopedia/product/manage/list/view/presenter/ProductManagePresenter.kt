package com.tokopedia.product.manage.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.product.manage.list.data.ConfirmationProductData
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.product.manage.list.view.listener.ProductManageView
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel
import com.tokopedia.seller.product.manage.constant.CatalogProductOption
import com.tokopedia.seller.product.manage.constant.ConditionProductOption
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption
import com.tokopedia.seller.product.manage.constant.SortProductOption
import java.util.*

interface ProductManagePresenter : CustomerPresenter<ProductManageView> {

    fun isIdlePowerMerchant() : Boolean

    fun isPowerMerchant(): Boolean

    fun getGoldMerchantStatus()

    fun bulkUpdateProduct(listUpdateResponse: MutableList<ConfirmationProductData>)

    fun getProductList(page: Int, keywordFilter: String, @CatalogProductOption catalogOption: String,
                       @ConditionProductOption conditionOption: String, etalaseId: Int,
                       @PictureStatusProductOption pictureOption: String, @SortProductOption sortOption: String,
                       categoryId: String)

    fun editPrice(productId: String, price: String, currencyId: String, currencyText: String)

    fun setCashback(productId: String, cashback: Int)

    fun deleteSingleProduct(productIds: String)

    fun getFreeClaim(graphqlQuery: String, shopId: String)

    fun getPopupsInfo(productId: String)

    fun mapToProductConfirmationData(isActionDelete: Boolean, stockType: BulkBottomSheetType.StockType, etalaseType: BulkBottomSheetType.EtalaseType, productManageViewModels: List<ProductManageViewModel>): ArrayList<ConfirmationProductData>

    fun failedBulkDataMapper(failData: List<ProductUpdateV3Response>, confirmationProductDataList: List<ConfirmationProductData>): MutableList<ConfirmationProductData>
}