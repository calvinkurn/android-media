package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerpersona.databinding.FragmentPersonaManualSelectBinding

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaManualSelectFragment : BaseFragment<FragmentPersonaManualSelectBinding>() {

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaManualSelectBinding {
        return FragmentPersonaManualSelectBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }
}