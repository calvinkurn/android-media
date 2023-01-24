package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerpersona.databinding.FragmentPersonaResultBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaResultFragment : BaseFragment<FragmentPersonaResultBinding>() {

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaResultBinding {
        return FragmentPersonaResultBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding?.run {

        }
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }
}