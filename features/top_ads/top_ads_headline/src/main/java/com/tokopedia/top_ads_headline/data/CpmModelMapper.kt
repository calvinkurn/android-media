package com.tokopedia.top_ads_headline.data

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val SHOP_BADGE = "https://ecs7-p.tokopedia.net/ta/icon/badge/OS-Badge-80.png"

class CpmModelMapper @Inject constructor(@ApplicationContext private val context: Context,
                                         private val userSession: UserSessionInterface) {

    fun getCpmModelResponse(response: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>): CpmModel {
        return CpmModel().apply {
            data.add(CpmData().apply {
                cpm = Cpm().apply {
                    adClickUrl = ""
                    cta = context.getString(R.string.topads_headline_check_now)
                    applinks = "tokopedia://shop/${userSession.shopId}"
                    name = userSession.shopName
                    promotedText = context.getString(R.string.topads_headline_promoted_by)
                    badges = listOf(Badge(SHOP_BADGE))
                    cpmShop = mapCpmShop(response)
                    cpmImage = CpmImage().apply { fullEcs = userSession.profilePicture }
                }
            })
        }
    }

    private fun mapCpmShop(response: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>): CpmShop {
        return CpmShop().apply {
            id = userSession.shopId
            name = userSession.shopName
            slogan = context.getString(R.string.topads_headline_promotional_dummy_message)
            isPowerMerchant = userSession.isPowerMerchantIdle
            isOfficial = userSession.isShopOfficialStore
            imageShop = ImageShop().apply {
                setsEcs(userSession.profilePicture)
            }
            if(response.isNotEmpty()){
                val productsList = ArrayList<Product>()
                response.getOrNull(0)?.let {
                    productsList.add(mapProducts(it))
                }
                response.getOrNull(1)?.let {
                    productsList.add(mapProducts(it))
                }
                response.getOrNull(2)?.let {
                    productsList.add(mapProducts(it))
                }
                products = productsList
            }
        }

    }

    private fun mapProducts(data: ResponseProductList.Result.TopadsGetListProduct.Data): Product {
        return Product().apply {
            data.let {
                id = it.productID.toString()
                name = it.productName
                priceFormat = it.productPrice
                applinks = it.productUri
                countReviewFormat = it.productReviewCount.toString()
                productRating = it.productRating
                imageProduct = ImageProduct().apply {
                    id = it.productID.toString()
                    name = it.productName
                    imageUrl = it.productImage
                }
            }
        }
    }
}