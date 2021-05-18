package com.tokopedia.shop.common.util

import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.shop.common.data.model.MerchantVoucherCouponUiModel
import com.tokopedia.shop.common.data.model.ResultStatus
import com.tokopedia.shop.common.data.model.Titles

object ShopPageMapper {
    fun mapToVoucherCouponUiModel(data: TokopointsCatalogMVCSummary?, shopId: String?): MerchantVoucherCouponUiModel? {
        data?.let {
            return MerchantVoucherCouponUiModel(
                    resultStatus = ResultStatus(
                            code = data.resultStatus?.code,
                            message = data.resultStatus?.message,
                            status = data.resultStatus?.status
                    ),
                    titles = listOf(Titles(
                            text = data.titles?.firstOrNull()?.text,
                            icon = data.titles?.firstOrNull()?.icon
                    )),
                    isShown = data.isShown,
                    subTitle = data.subTitle,
                    imageURL = data.imageURL,
                    shopId = shopId,
                    animatedInfos = data.animatedInfos
            )
        }
        return null
    }
}