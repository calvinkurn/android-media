package com.tokopedia.homecredit.applink

object Constants {
    /*
     * no type => default cut out camera
     * KTP1 => no overlay on camera KTP
     * KTP2 => custom overlay camera KTP
     * SLFE1 => no overlay on camera Selfie
     * SLFE2 => custom overlay camera Selfie
     * */
    const val CAMERA_TYPE = "type"
    const val KTP_NO_OVERLAY = "KTP1"
    const val CUST_OVERLAY_URL = "imgurl"
    const val CUST_HEADER = "header_text"
    const val SLFE_NO_OVERLAY = "SLFE1"
    const val SLFE_CUST_OVERLAY = "SLFE2"
}