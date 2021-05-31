package com.tokopedia.oneclickcheckout.common.view.model

import androidx.lifecycle.LiveData
import com.tokopedia.oneclickcheckout.common.view.model.preference.AddressModel
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccErrorData
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.PriceChangeMessage
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import java.util.*

sealed class OccState<out T: Any> {
    data class FirstLoad<out T: Any>(val data: T) : OccState<T>()
    data class Success<out T: Any>(val data: T) : OccState<T>()
    object Loading : OccState<Nothing>()
    data class Failed(private val failure: Failure): OccState<Nothing>() {
        private val eventFailure: OccEvent<Failure> = OccEvent(failure)
        fun getFailure(): Failure? {
            return eventFailure.getData()
        }
    }
}

sealed class OccGlobalEvent {
    object Normal : OccGlobalEvent()
    object Loading : OccGlobalEvent()
    data class Error(val throwable: Throwable? = null, val errorMessage: String = "") : OccGlobalEvent()
    data class CheckoutError(val error: CheckoutOccErrorData) : OccGlobalEvent()
    data class PriceChangeError(val message: PriceChangeMessage) : OccGlobalEvent()
    data class TriggerRefresh(val isFullRefresh: Boolean = true, val throwable: Throwable? = null, val errorMessage: String = "") : OccGlobalEvent()
    data class PromoClashing(val notEligiblePromoHolderDataList: ArrayList<NotEligiblePromoHolderdata>) : OccGlobalEvent()
    data class AtcError(val throwable: Throwable? = null, val errorMessage: String = "") : OccGlobalEvent()
    data class AtcSuccess(val message: String = ""): OccGlobalEvent()
    data class Prompt(val prompt: OccPrompt): OccGlobalEvent()
    data class ForceOnboarding(val onboarding: OccMainOnboarding): OccGlobalEvent()
    data class UpdateLocalCacheAddress(val addressModel: AddressModel): OccGlobalEvent()
}

data class OccEvent<out T: Any>(private val data: T) {
    private var isConsumed: Boolean = false

    fun getData(): T? {
        if (isConsumed) {
            return null
        }
        isConsumed = true
        return data
    }
}

data class Failure(val throwable: Throwable? = null, val errorMessage: String = "")

@Suppress("UNCHECKED_CAST")
class OccMutableLiveData<T>(initialValue: T): LiveData<T>(initialValue) {

    override fun getValue(): T = super.getValue() as T

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}