package com.tokopedia.play.analytic.tokonow

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayTokonowAnalytic {
    fun impressAddressWidget()
    fun impressChooseAddress()
    fun clickChooseAddress()
    fun clickInfoAddressWidget()
    fun impressInfoNow()
    fun clickInfoNow()

    /**
     * Can be merged with the existing
     */
    fun impressCarouselNow()
    fun clickCarouselNow()
    fun impressProductNow()
    fun clickProductNow()
    fun clickATNow()
    fun clickBuyNow()
    fun impressNowToaster()
    fun clickLihatNowToaster()
}