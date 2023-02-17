package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.analytics.SellerPersonaTracking
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.databinding.FragmentPersonaOpeningBinding
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaOpeningFragment : BaseFragment<FragmentPersonaOpeningBinding>() {

    private val impressHolder: ImpressHolder? by lazy {
        (activity as? SellerPersonaActivity)?.openingImpressHolder
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun bind(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentPersonaOpeningBinding {
        return FragmentPersonaOpeningBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            showIllustrationImage()
            setupQuestionnaireNavigation()
            btnOpeningTryLater.setOnClickListener {
                SellerPersonaTracking.sendClickSellerPersonaLaterEvent()
                activity?.finish()
            }
            sendImpressionEvent()
        }
    }

    private fun sendImpressionEvent() {
        if (!impressHolder?.isInvoke.orTrue()) {
            SellerPersonaTracking.sendImpressionSellerPersonaEvent()
            impressHolder?.invoke()
        }
    }

    private fun showIllustrationImage() {
        binding?.imgSpOpening?.loadImage(Constants.IMG_INTRODUCTION)
    }

    private fun setupQuestionnaireNavigation() {
        binding?.btnOpeningStartQuiz?.setOnClickListener { v ->
            SellerPersonaTracking.sendClickSellerPersonaStartQuizEvent()
            v.findNavController().navigate(R.id.actionOpeningFragmentToQuestionnaireFragment)
        }
    }
}