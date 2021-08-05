package com.tokopedia.promocheckout.mockdata

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel

/**
 * @author: astidhiyaa on 05/08/21.
 */

val CART_ID = "123"

val PROMO_CODE = "diskoncode"

val HEX_COLOR = "#yrghs"

val DUMMY_DATA_UI_MODEL = DataUiModel()

val FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION = Throwable(MessageErrorException("Minimal pembelian 500.000"))

val FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION = Throwable(message = "Telah terjadi kesalahan")