package com.tokopedia.shopdiscount.info.util

import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.info.data.request.GetSlashPriceTickerRequest
import com.tokopedia.shopdiscount.info.data.response.GetSlashPriceTickerResponse
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object ShopDiscountSellerInfoMapper {
    private const val PLATFORM = "android"

    fun mapToShopDiscountSellerInfoBenefitUiModel(
        response: GetSlashPriceBenefitResponse
    ): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            responseHeader = response.getSlashPriceBenefit.responseHeader,
            isUseVps = response.getSlashPriceBenefit.isUseVps,
            listSlashPriceBenefitData = mapToListSlashPriceBenefitData(response.getSlashPriceBenefit.slashPriceBenefits)
        )
    }

    fun mapToGetSlashPriceTickerRequest(): GetSlashPriceTickerRequest {
        return GetSlashPriceTickerRequest(
            requestHeader = getRequestHeader(),
            platform = PLATFORM
        )
    }

    private fun mapToListSlashPriceBenefitData(
        slashPriceBenefits: List<GetSlashPriceBenefitResponse.GetSlashPriceBenefit.SlashPriceBenefit>
    ): List<ShopDiscountSellerInfoUiModel.SlashPriceBenefitData> {
        return slashPriceBenefits.map {
            ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                packageId = it.packageId,
                packageName = it.packageName,
                remainingQuota = it.remainingQuota,
                maxQuota = it.maxQuota,
                expiredAt = it.expiredAt,
                expiredAtUnix = it.expiredAtUnix
            )
        }
    }

    private fun getRequestHeader(): RequestHeader {
        return RequestHeader(
            ip = "",
            usecase = ""
        )
    }

    fun mapToShopDiscountTickerUiModel(
        response: GetSlashPriceTickerResponse
    ): ShopDiscountTickerUiModel {
        return response.getSlashPriceTicker.let {
            ShopDiscountTickerUiModel(
                responseHeader = it.responseHeader,
                listTicker = it.listTicker.map { tickerString ->
                    TickerData(
                        "",
                        tickerString,
                        Ticker.TYPE_ANNOUNCEMENT,
                        true
                    )
                }
            )
        }
    }

}