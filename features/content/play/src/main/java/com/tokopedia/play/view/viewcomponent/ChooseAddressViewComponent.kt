package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_choose_address_widget) {

    interface Listener {}
}