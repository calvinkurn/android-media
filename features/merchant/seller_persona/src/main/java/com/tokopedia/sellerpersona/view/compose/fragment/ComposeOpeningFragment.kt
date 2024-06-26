package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerpersona.analytics.SellerPersonaTracking
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.screen.PersonaOpeningScreen

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ComposeOpeningFragment : BaseComposeFragment() {

    private val impressHolder: ImpressHolder? by lazy {
        (activity as? SellerPersonaActivity)?.openingImpressHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return setComposeViewContent(inflater.context) {
            PersonaOpeningScreen(
                onImpressed = ::sendImpressionEvent,
                onNext = ::onNextClicked,
                onBack = ::onBackClicked
            )
        }
    }

    private fun onNextClicked() {
        SellerPersonaTracking.sendClickSellerPersonaStartQuizEvent()
        val action = ComposeOpeningFragmentDirections.actionComposeOpeningToQuestionnaire()
        view?.findNavController()?.navigate(action)
    }

    private fun onBackClicked() {
        SellerPersonaTracking.sendClickSellerPersonaLaterEvent()
        activity?.finish()
    }

    private fun sendImpressionEvent() {
        if (!impressHolder?.isInvoke.orTrue()) {
            SellerPersonaTracking.sendImpressionSellerPersonaEvent()
            impressHolder?.invoke()
        }
    }
}