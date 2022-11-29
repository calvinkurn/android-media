package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 28/04/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(
        SocmedType.COPY_LINK, SocmedType.INSTAGRAM, SocmedType.FACEBOOK,
        SocmedType.FACEBOOK_MESSENGER, SocmedType.WHATSAPP, SocmedType.LINE,
        SocmedType.TWITTER, SocmedType.BROADCAST, SocmedType.LAINNYA
)
annotation class SocmedType {

    companion object {
        const val COPY_LINK = 0
        const val INSTAGRAM = 1
        const val FACEBOOK = 2
        const val FACEBOOK_MESSENGER = 3
        const val WHATSAPP = 4
        const val LINE = 5
        const val TWITTER = 6
        const val BROADCAST = 7
        const val LAINNYA = 8
        const val TELEGRAM = 9
    }
}
