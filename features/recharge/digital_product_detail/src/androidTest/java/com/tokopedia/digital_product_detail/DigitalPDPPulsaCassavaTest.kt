package com.tokopedia.digital_product_detail

import org.junit.Test

class DigitalPDPPulsaCassavaTest: BaseDigitalPDPPulsaTest() {

    @Test
    fun validate_cassava() {
        Thread.sleep(2000)
        interactWithClientNumberWidget()
    }

    private fun interactWithClientNumberWidget() {
        Thread.sleep(2000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("0812")
        clientNumberWidget_typeNumber("3456")
        clientNumberWidget_typeNumber("7890")

        Thread.sleep(2000)
    }

    override fun getApplink(): String = APPLINK

    companion object {
        const val APPLINK = "tokopedia://digital/form?category_id=1&menu_id=289&template=pulsav2"
    }
}