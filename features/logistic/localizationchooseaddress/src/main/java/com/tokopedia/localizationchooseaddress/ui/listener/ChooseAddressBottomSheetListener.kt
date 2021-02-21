package com.tokopedia.localizationchooseaddress.ui.listener

interface ChooseAddressBottomSheetListener {
    /**
     * this listen if we get server down on widget/bottomshet.
     * Host mandatory to GONE LocalizingAddressWidget
     */
    fun onLocalizingAddressServerDown();

    /**
     * Only use by bottomsheet
     */
    fun onAddressChosen()
}