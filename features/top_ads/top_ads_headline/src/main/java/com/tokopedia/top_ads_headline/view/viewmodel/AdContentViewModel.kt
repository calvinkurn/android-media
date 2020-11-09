package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AdContentViewModel @Inject constructor(private var userSession: UserSessionInterface,
                                             private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : ViewModel() {

    fun getTopAdsProductList(shopId: Int, keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int,
                             onSuccess: ((cpmModel: CpmModel) -> Unit),
                             onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, shopId)
                    val response = topAdsGetListProductUseCase.executeOnBackground()
                    onSuccess(getCpmModelResponse(response))
                },
                onError = {
                    onError(it.message ?: "")
                    it.printStackTrace()
                }
        )
    }

    //to confirm the api to show the preview and refactor this code
    private fun getCpmModelResponse(response: ResponseProductList.Result): CpmModel {
        return CpmModel().apply {
            data.add(CpmData().apply {
                cpm = Cpm().apply {
                    adClickUrl = ""
                    cta = "Cek Sekarang"
                    applinks = "tokopedia://shop/${userSession.shopId}"
                    name = userSession.shopName
                    promotedText = "Dipromosikan oleh"
                    badges = listOf(Badge("https://ecs7-p.tokopedia.net/ta/icon/badge/OS-Badge-80.png"))
                    cpmShop = CpmShop().apply {
                        id = userSession.shopId
                        name = userSession.shopName
                        tagline = "LOGAM MULIA ANTAM UBS MURAH"
                        slogan = "Mau beli ${response.topadsGetListProduct.data.firstOrNull()?.productName ?: ""} kualitas terbaik? Cek toko ${userSession.shopName} sekarang!"
                        isPowerMerchant = userSession.isPowerMerchantIdle
                        isOfficial = userSession.isShopOfficialStore
                        cpmImage = CpmImage().apply { fullEcs = userSession.profilePicture }
                        products = listOf(Product().apply {
                            response.topadsGetListProduct.data.getOrNull(0)?.let {
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
                        }, Product().apply {
                            response.topadsGetListProduct.data.getOrNull(1)?.let {
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
                        }, Product().apply {
                            response.topadsGetListProduct.data.getOrNull(2)?.let {
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
                        })
                        imageShop = ImageShop().apply {
                            setsEcs(userSession.profilePicture)
                        }
                    }
                }
            })
        }
    }

}