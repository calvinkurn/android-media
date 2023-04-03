package com.tokopedia.minicart.common.widget.viewmodel.utils

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.data.model.response.AddToCartBundleResponse
import com.tokopedia.cartcommon.data.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils
    private val miniCartSimplifiedMapper = MiniCartSimplifiedMapper()
    private val miniCartListUiModelMapper = MiniCartListUiModelMapper()

    fun provideGetMiniCartListSuccessAllAvailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_all_available"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartBundleListSuccessAllAvailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_bundle_success_all_available"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartBundleVariantSuccessAllAvailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_bundle_variant_success_all_available"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessAvailableAndUnavailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_available_and_unavailable"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessMultipleAvailableAndUnavailableOneCart(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_multiple_available_unavailable_one_cart"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartBundleListSuccessAvailableAndUnavailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_bundle_success_available_and_unavailable"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessAllUnavailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessOutOfService(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_out_of_service"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_available"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartBundleSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_bundle_success_all_available"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartBundleVariantSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_bundle_variant_success_all_available"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessAllUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessAvailableAndUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_available_and_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedBundleSuccessAvailableAndUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_bundle_success_available_and_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessEmptyData(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_empty"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideMiniCartListUiModelAllAvailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartListSuccessAllAvailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartBundleListUiModelAllAvailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartBundleListSuccessAllAvailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartBundleVariantListUiModelAllAvailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartBundleVariantSuccessAllAvailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartListUiModelAvailableAndUnavailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartListSuccessAvailableAndUnavailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartBundleListUiModelAvailableAndUnavailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartBundleListSuccessAvailableAndUnavailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartListUiModelAllUnavailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartListSuccessAllUnavailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartSimplifiedDataAllAvailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedSuccessAllAvailable()
        return miniCartSimplifiedData
    }

    fun provideMiniCartSimplifiedDataAllUnavailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedSuccessAllUnavailable()
        return miniCartSimplifiedData
    }

    fun provideMiniCartSimplifiedDataAvailableAndUnavailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedSuccessAvailableAndUnavailable()
        return miniCartSimplifiedData
    }

    fun provideMiniCartSimplifiedDataBundleAvailableAndUnavailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedBundleSuccessAvailableAndUnavailable()
        return miniCartSimplifiedData
    }

    fun provideGetMiniCartListSuccessWithSingleAvailableItem(): MiniCartListUiModel {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_single_available_item"), MiniCartGqlResponse::class.java)
        return miniCartListUiModelMapper.mapUiModel(json.miniCart)
    }

    fun provideGetMiniCartListSuccessWithWholesaleVariant(): MiniCartListUiModel {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_wholesale_variant"), MiniCartGqlResponse::class.java)
        return miniCartListUiModelMapper.mapUiModel(json.miniCart)
    }

    fun provideGetMiniCartListSuccessWithSlashPrice(): MiniCartListUiModel {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_slash_price"), MiniCartGqlResponse::class.java)
        return miniCartListUiModelMapper.mapUiModel(json.miniCart)
    }

    fun provideDeleteFromCartSuccess(): RemoveFromCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/remove_from_cart_success"), DeleteCartGqlResponse::class.java)
        return json.removeFromCart
    }

    fun provideUndoDeleteFromCartSuccess(): UndoDeleteCartDataResponse {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/undo_delete_cart_item_success"), UndoDeleteCartGqlResponse::class.java)
        return json.undoDeleteCartDataResponse
    }

    fun provideUpdateCartSuccess(): UpdateCartV2Data {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/update_cart_success"), UpdateCartGqlResponse::class.java)
        return json.updateCartData
    }

    fun provideUpdateCartFailed(): UpdateCartV2Data {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/update_cart_failed"), UpdateCartGqlResponse::class.java)
        return json.updateCartData
    }

    fun provideProductBundleRecomResponse(isEmptyList: Boolean): ProductBundleRecomResponse {
        return gson.fromJson(
            fileUtil.getJsonFromAsset(if (isEmptyList) "assets/get_product_bundle_recommendation_empty_list" else "assets/get_product_bundle_recommendation"),
            ProductBundleRecomResponse::class.java
        )
    }

    fun provideAddToCartBundleResponse(isSuccess: Boolean, isEmptyData: Boolean = false, isOkStatus: Boolean = true): AddToCartBundleResponse {
        return gson.fromJson(
            fileUtil.getJsonFromAsset(if (isSuccess) if (isEmptyData) "assets/add_to_cart_bundle_empty_data" else "assets/add_to_cart_bundle" else if (isOkStatus) "assets/add_to_cart_bundle_failed" else "assets/add_to_cart_bundle_failed_not_ok"),
            AddToCartBundleResponse::class.java
        )
    }

    fun provideProductBundleRecomData(response: ProductBundleRecomResponse): MiniCartProductBundleRecomUiModel {
        return miniCartListUiModelMapper.mapToProductBundleUiModel(
            widgetResponse = response
        )
    }

    fun provideAddToCartBundleProductDetailParam(productDetails: List<ShopHomeBundleProductUiModel>, quantity: Int, shopId: String, userId: String): List<ProductDetail> {
        return miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(
            productDetails = productDetails,
            quantity = quantity,
            shopId = shopId,
            userId = userId
        )
    }
}
