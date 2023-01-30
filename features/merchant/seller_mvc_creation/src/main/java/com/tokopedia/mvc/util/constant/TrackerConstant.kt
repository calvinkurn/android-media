package com.tokopedia.mvc.util.constant

object TrackerConstant {
    const val EVENT = "clickPG"
    const val TRACKER_ID = "trackerId"
    const val BUSINESS_UNIT = "physical goods"
    const val CURRENT_SITE = "tokopediaseller"

    object ChangeQuota {
        const val event = "kupon toko saya - list kupon"
    }

    object StopVoucherPopUp {
        const val cancelEvent = "kupon toko saya - creation pop up batal"
        const val stopEvent = "kupon toko saya - creation pop up stop"
    }

    object ChangePeriod {
        const val event = "kupon toko saya - list kupon"
    }

    object CreationVoucherType {
        const val event = "kupon toko saya - creation jenis kupon"
    }

    object CreationVoucherInfo {
        const val event = "kupon toko saya - creation informasi kupon"
    }

    object RecurringVoucher {
        const val event = "kupon toko saya - creation periode kupon aktif"
    }
}
