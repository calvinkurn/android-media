package com.tokopedia.pdpsimulation.common.helper

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardsListBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.bottomsheet.CreditCardAvailableBanksBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterSignupBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterVerificationBottomSheet

class BottomSheetNavigator(val childFragmentManager: FragmentManager) {

    fun <T : Any> showBottomSheet(modelClass: Class<T>, bundle: Bundle, pdpSimulationCallback: PdpSimulationCallback, productUrl: String) {
        when {
            modelClass.isAssignableFrom(PayLaterSignupBottomSheet::class.java) ->
                PayLaterSignupBottomSheet.show(bundle, pdpSimulationCallback, childFragmentManager)

            modelClass.isAssignableFrom(PayLaterActionStepsBottomSheet::class.java) -> {
                bundle.putString(PayLaterActionStepsBottomSheet.PRODUCT_URL, productUrl)
                PayLaterActionStepsBottomSheet.show(bundle, pdpSimulationCallback, childFragmentManager)
            }
            modelClass.isAssignableFrom(PayLaterVerificationBottomSheet::class.java) ->
                PayLaterVerificationBottomSheet.show(bundle, childFragmentManager)

            modelClass.isAssignableFrom(PayLaterFaqBottomSheet::class.java) ->
                PayLaterFaqBottomSheet.show(bundle, childFragmentManager)

            modelClass.isAssignableFrom(CreditCardsListBottomSheet::class.java) ->
                CreditCardsListBottomSheet.show(bundle, pdpSimulationCallback, childFragmentManager)

            modelClass.isAssignableFrom(CreditCardRegistrationBottomSheet::class.java) ->
                CreditCardRegistrationBottomSheet.show(pdpSimulationCallback, childFragmentManager)

            modelClass.isAssignableFrom(CreditCardAvailableBanksBottomSheet::class.java) ->
                CreditCardAvailableBanksBottomSheet.show(bundle, childFragmentManager)
        }
    }
}