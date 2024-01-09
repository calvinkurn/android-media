package com.tokopedia.hotel.common.presentation

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * @author by jessica on 12/06/19
 */

abstract class HotelBaseFragment: BaseDaggerFragment() {

    abstract fun onErrorRetryClicked()
}
