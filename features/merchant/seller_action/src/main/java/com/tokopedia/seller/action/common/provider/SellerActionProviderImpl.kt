package com.tokopedia.seller.action.common.provider

import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.common.const.SellerActionConst
import java.util.*

class SellerActionProviderImpl: SellerActionProvider {

    override fun getDefaultDate(): String =
            Date().toFormattedString(SellerActionConst.REQUEST_DATE_FORMAT)

    override fun getFormattedDate(date: String): String =
            convertFormatDate(date, SellerActionConst.SLICE_DATE_FORMAT, SellerActionConst.REQUEST_DATE_FORMAT)

}