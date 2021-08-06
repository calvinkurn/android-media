package com.tokopedia.promocheckout.mockdata

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.list.model.listcoupon.DataPromoCheckoutList
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

/**
 * @author: astidhiyaa on 05/08/21.
 */

val SERVICE_ID = "3"

val PAGE = 1

val CATEGORY_ID = 35

val CART_ID = "123"

val PROMO_CODE = "diskoncode"

val HEX_COLOR = "#yrghs"

val DUMMY_DATA_UI_MODEL = DataUiModel()

val FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION = Throwable(MessageErrorException("Minimal pembelian 500.000"))

val FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION = Throwable(message = "Telah terjadi kesalahan")

val SUCCESS_PROMO_LIST = DataPromoCheckoutList()

val SUCCESS_PROMO_LAST_SEEN = listOf<PromoCheckoutLastSeenModel>(PromoCheckoutLastSeenModel
    (id = 1, title = "Diskon khusus Android", subtitle = "Android", promoCode = "ANDROIDDISKON"))

val SUCCESS_DETAIL_PROMO = PromoCheckoutDetailModel()

val SUCCESS_CANCEL_VOUCHER = FlightCancelVoucher.Response(FlightCancelVoucher(
    type = "", attributes = FlightCancelVoucher.Attributes(success = true)
))