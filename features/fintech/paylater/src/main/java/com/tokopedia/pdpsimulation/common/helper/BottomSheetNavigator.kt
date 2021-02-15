package com.tokopedia.pdpsimulation.common.helper

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.constants.PAYLATER_PRODUCT
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardsListBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.bottomsheet.CreditCardAvailableBanksBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterSignupBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterVerificationBottomSheet
import dagger.Lazy

class BottomSheetNavigator(val childFragmentManager: FragmentManager, private val pdpSimulationAnalytics: Lazy<PdpSimulationAnalytics>) {

    fun <T : Any> showBottomSheet(modelClass: Class<T>, bundle: Bundle, pdpSimulationCallback: PdpSimulationCallback) {
        when {
            modelClass.isAssignableFrom(PayLaterSignupBottomSheet::class.java) -> {
                pdpSimulationAnalytics.get().sendRegisterClickEvent()
                PayLaterSignupBottomSheet.show(bundle, pdpSimulationCallback, childFragmentManager)
            }
            modelClass.isAssignableFrom(PayLaterActionStepsBottomSheet::class.java) -> {
                pdpSimulationAnalytics.get().sendChoosePayLaterOptionClickEvent(bundle.getString(PAYLATER_PRODUCT) ?: "")
                PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
            }
            modelClass.isAssignableFrom(PayLaterVerificationBottomSheet::class.java) -> {
                pdpSimulationAnalytics.get().sendChoosePayLaterOptionClickEvent(bundle.getString(PAYLATER_PRODUCT) ?: "")
                PayLaterVerificationBottomSheet.show(bundle, childFragmentManager)
            }
            modelClass.isAssignableFrom(CreditCardsListBottomSheet::class.java) ->
                CreditCardsListBottomSheet.show(bundle, childFragmentManager)
            modelClass.isAssignableFrom(CreditCardRegistrationBottomSheet::class.java) ->
                CreditCardRegistrationBottomSheet.show(pdpSimulationCallback, childFragmentManager)
            modelClass.isAssignableFrom(CreditCardAvailableBanksBottomSheet::class.java) ->
                CreditCardAvailableBanksBottomSheet.show(bundle, childFragmentManager)
        }
    }
}