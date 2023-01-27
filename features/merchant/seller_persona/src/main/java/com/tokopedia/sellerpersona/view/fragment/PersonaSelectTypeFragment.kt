package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerpersona.databinding.FragmentPersonaSelectTypeBinding

/**
 * Created by @ilhamsuaib on 26/01/23.
 */

class PersonaSelectTypeFragment : BaseFragment<FragmentPersonaSelectTypeBinding>() {

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaSelectTypeBinding {
        return FragmentPersonaSelectTypeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }
}