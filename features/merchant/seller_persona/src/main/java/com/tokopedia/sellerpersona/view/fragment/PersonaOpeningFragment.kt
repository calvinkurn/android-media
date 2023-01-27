package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.local.PersonaSharedPreference
import com.tokopedia.sellerpersona.databinding.FragmentPersonaOpeningBinding
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaOpeningFragment : BaseFragment<FragmentPersonaOpeningBinding>() {

    @Inject
    lateinit var sharedPref: PersonaSharedPreference

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaOpeningBinding {
        return FragmentPersonaOpeningBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasFirstVisit()
        setupView()
    }

    private fun setHasFirstVisit() {
        sharedPref.setHasFirstVisit()
    }

    private fun setupView() {
        binding?.run {
            showIllustrationImage()
            setupQuestionnaireNavigation()
            btnOpeningTryLater.setOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun showIllustrationImage() {
        binding?.imgSpOpening?.loadImage(Constants.IMG_INTRODUCTION)
    }

    private fun setupQuestionnaireNavigation() {
        binding?.btnOpeningStartQuiz?.setOnClickListener { v ->
            v.findNavController().navigate(R.id.actionOpeningFragmentToQuestionnaireFragment)
        }
    }
}