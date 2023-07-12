package com.tokopedia.sellerpersona.view.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.analytics.SellerPersonaTracking
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.compose.screen.OpeningScreen

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ComposeOpeningFragment : Fragment() {

    private val impressHolder: ImpressHolder? by lazy {
        (activity as? SellerPersonaActivity)?.openingImpressHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {
                NestTheme {
                    OpeningScreen(
                        onImpressed = ::sendImpressionEvent,
                        onNext = ::onNextClicked,
                        onBack = ::onBackClicked
                    )
                }
            }
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