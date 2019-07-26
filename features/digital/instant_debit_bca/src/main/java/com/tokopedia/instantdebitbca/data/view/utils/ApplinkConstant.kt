package com.tokopedia.instantdebitbca.data.view.utils

import com.tokopedia.abstraction.constant.TkpdAppLink

/**
 * Created by nabillasabbaha on 25/03/19.
 */
class ApplinkConstant : TkpdAppLink() {
    companion object {
        const val INSTANT_DEBIT_BCA_APPLINK = "tokopedia://instantdebitbca"
        const val INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK = "tokopedia://editbcaoneklik"
    }
}
