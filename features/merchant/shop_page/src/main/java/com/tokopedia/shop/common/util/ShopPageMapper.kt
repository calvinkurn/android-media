package com.tokopedia.shop.common.util

import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.shop.common.data.model.MerchantVoucherCouponUiModel
import com.tokopedia.shop.common.data.model.ResultStatus

object ShopPageMapper {
    fun mapToVoucherCouponUiModel(data: TokopointsCatalogMVCSummary?, shopId: String?): MerchantVoucherCouponUiModel? {
        data?.let {
            return MerchantVoucherCouponUiModel(
                    resultStatus = ResultStatus(
                            code = data.resultStatus?.code,
                            message = data.resultStatus?.message,
                            status = data.resultStatus?.status
                    ),
                    isShown = data.isShown,
                    shopId = shopId,
                    animatedInfos = data.animatedInfos
            )
        }
        return null
    }
}