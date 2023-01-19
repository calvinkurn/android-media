package com.tokopedia.sellerpersona.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.sellerpersona.databinding.FragmentPersonaOpeningBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class PersonaOpeningFragment : BaseFragment<FragmentPersonaOpeningBinding>() {

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaOpeningBinding {
        return FragmentPersonaOpeningBinding.inflate(layoutInflater, container, false)
    }
}