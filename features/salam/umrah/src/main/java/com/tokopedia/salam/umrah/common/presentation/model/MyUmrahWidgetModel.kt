package com.tokopedia.salam.umrah.common.presentation.model

/**
 * @author by furqan on 10/10/2019
 */
class MyUmrahWidgetModel (
        val header: String ="",
        val subHeader: String = "",
        val nextActionText: String = "",
        val mainButton: MainButton = MainButton()
) {
    class MainButton(val text: String = "", val link: String = "")
}