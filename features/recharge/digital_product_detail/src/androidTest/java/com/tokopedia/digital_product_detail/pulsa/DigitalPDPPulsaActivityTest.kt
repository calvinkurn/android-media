package com.tokopedia.digital_product_detail.pulsa

class DigitalPDPPulsaActivityTest: BaseDigitalPDPPulsaTest() {

    override fun getApplink(): String = APPLINK

    companion object {
        const val APPLINK = "tokopedia://digital/form?category_id=1&menu_id=289&template=pulsav2"
    }
}