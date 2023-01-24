package com.tokopedia.sellerpersona.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.sellerpersona.databinding.FragmentPersonaQuestionnaireBinding

/**
 * Created by @ilhamsuaib on 24/01/23.
 */

class PersonaQuestionnaireFragment : BaseFragment<FragmentPersonaQuestionnaireBinding>() {

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaQuestionnaireBinding {
        return FragmentPersonaQuestionnaireBinding.inflate(layoutInflater, container, false)
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }
}