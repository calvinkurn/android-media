package com.tokopedia.localizationchooseaddress.ui.listener

import androidx.fragment.app.Fragment

interface ChooseAddressListener {
    /**
     * Action choosen address from user by widget / bottomshet
     * Host must update content UI
     */
    fun onLocalizingAddressUpdatedFromWidget();

    /**
     * Address updated from background if device have not address saved in local cache.
     * this first user rollout
     * host can ignore this. optional to update UI
     */
    fun onLocalizingAddressUpdatedFromBackground();

    /**
     * this listen if we get server down on widget/bottomshet.
     * Host mandatory to GONE LocalizingAddressWidget
     */
    fun onLocalizingAddressServerDown();

    /**
     * this trigger to Host this feature active or not
     * Host must GONE widget if isRollOutUser == false
     * Host must VISIBLE widget if isRollOutUser == true
     */
    fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean)

    /**
     * We need Object Host Fragment to get viewmodel
     */
    fun getLocalizingAddressHostFragment(): Fragment

    /**
     * String Source of Host Page
     */
    fun getLocalizingAddressHostSourceData(): String


}
