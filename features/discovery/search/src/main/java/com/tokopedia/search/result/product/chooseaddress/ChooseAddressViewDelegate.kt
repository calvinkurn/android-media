package com.tokopedia.search.result.product.chooseaddress

import android.content.Context
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import timber.log.Timber

class ChooseAddressViewDelegate(
    context: Context,
): ChooseAddressView,
    ContextProvider by WeakReferenceContextProvider(context) {

    @Deprecated("isEnableChooseAddress is always true")
    override val isChooseAddressWidgetEnabled: Boolean = true

    override val chooseAddressData: LocalCacheModel
        get() = context?.let {
            try {
                ChooseAddressUtils.getLocalizingAddressData(it)
            } catch (e: Throwable) {
                Timber.w(e)
                ChooseAddressConstant.emptyAddress
            }
        } ?: ChooseAddressConstant.emptyAddress

    override fun getIsLocalizingAddressHasUpdated(
        currentChooseAddressData: LocalCacheModel
    ): Boolean {
        return context?.let {
            try {
                ChooseAddressUtils.isLocalizingAddressHasUpdated(it, currentChooseAddressData)
            } catch (e: Throwable) {
                Timber.w(e)
                false
            }
        } ?: false
    }
}