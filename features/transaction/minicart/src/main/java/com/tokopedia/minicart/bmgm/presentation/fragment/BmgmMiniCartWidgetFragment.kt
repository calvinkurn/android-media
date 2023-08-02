package com.tokopedia.minicart.bmgm.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.databinding.FragmentBmgmMiniCartWidgetBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartWidgetFragment : Fragment() {

    companion object {
        fun newInstance(): BmgmMiniCartWidgetFragment {
            return BmgmMiniCartWidgetFragment()
        }
    }

    private var binding: FragmentBmgmMiniCartWidgetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmgmMiniCartWidgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding?.let { v ->

        }
    }

    private fun initInjector() {
        context?.let {
            DaggerBmgmComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }
}