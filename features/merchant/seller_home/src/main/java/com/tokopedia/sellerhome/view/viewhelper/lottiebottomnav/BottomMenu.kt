package com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav

/**
 * Created By @ilhamsuaib on 15/10/20
 */

data class BottomMenu(
        val id: Int,
        val title: String,
        val animName: Int? = null,
        val animToEnabledName: Int? = null,
        val imageName: Int? = null,
        val imageEnabledName: Int? = null,
        val activeButtonColor: Int,
        val useBadge: Boolean = true,
        val animSpeed: Float = 1f,
        val animToEnabledSpeed: Float = 1f
)