package com.tokopedia.product.detail.view.util

import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

class DynamicProductDetailHashMap(private val mapOfData: Map<String, DynamicPDPDataModel>) {

    companion object {
        private const val SOCIAL_PROOF = "social_proof"
        private const val PRODUCT_SNAPSHOT = "product_snapshot"
        private const val SHOP_INFO = "shop_info"
        private const val PRODUCT_INFO = "product_info"
        private const val DISCUSSION = "discussion"
    }

    val socialProofMap: ProductSocialProofDataModel
        get() = mapOfData[SOCIAL_PROOF] as ProductSocialProofDataModel

    val snapShotMap: ProductSnapshotDataModel
        get() = mapOfData[PRODUCT_SNAPSHOT] as ProductSnapshotDataModel

    val shopInfoMap: ProductShopInfoDataModel
        get() = mapOfData[SHOP_INFO] as ProductShopInfoDataModel

    val productInfoMap: ProductInfoDataModel
        get() = mapOfData[PRODUCT_INFO] as ProductInfoDataModel

    val productDiscussionMap: ProductDiscussionDataModel
        get() = mapOfData[DISCUSSION] as ProductDiscussionDataModel

    fun updateDataP1(data: ProductInfoP1?) {
        data?.let {
            snapShotMap.productInfoP1 = it.productInfo
            snapShotMap.media = it.productInfo.media
            socialProofMap.productInfo = it.productInfo
            productInfoMap.productInfo = it.productInfo
        }
    }

    fun updateDataP2Shop(data: ProductInfoP2ShopData?) {
        data?.let {
            shopInfoMap.shopInfo = it.shopInfo
            snapShotMap.shopInfo = it.shopInfo ?: ShopInfo()
            snapShotMap.nearestWarehouse = it.nearestWarehouse
            productInfoMap.shopInfo = it.shopInfo
        }
    }

    fun updateDataP2General(data: ProductInfoP2General?){
        data?.let {
            productInfoMap.productSpecification = it.productSpecificationResponse
            productDiscussionMap.latestTalk = it.latestTalk
            productDiscussionMap.shopId = productInfoMap.productInfo?.basic?.shopID.toString()
            productDiscussionMap.talkCount = productInfoMap.productInfo?.stats?.countTalk ?: 0
        }
    }

}