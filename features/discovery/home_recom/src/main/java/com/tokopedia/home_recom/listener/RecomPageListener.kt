package com.tokopedia.home_recom.listener

import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget

/**
 * Created by yfsx on 01/09/21.
 */
interface RecomPageListener {

    fun onChooseAddressUpdated()

    fun onChooseAddressServerDown()
}