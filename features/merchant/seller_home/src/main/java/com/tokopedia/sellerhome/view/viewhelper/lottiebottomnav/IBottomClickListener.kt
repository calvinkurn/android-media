package com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav

/**
 * Created By @ilhamsuaib on 15/10/20
 */

interface IBottomClickListener {
    fun menuClicked(position: Int, id: Int): Boolean
    fun menuReselected(position: Int, id: Int)
}