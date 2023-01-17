package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerpersona.databinding.FragmentResultBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ResultFragment : BaseFragment<FragmentResultBinding>() {

    companion object {
        fun newInstance(): ResultFragment {
            return ResultFragment()
        }
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResultBinding {
        return FragmentResultBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding?.run {

        }
    }
}