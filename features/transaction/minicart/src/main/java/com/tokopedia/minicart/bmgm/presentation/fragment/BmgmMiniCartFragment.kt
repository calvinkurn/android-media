package com.tokopedia.minicart.bmgm.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.minicart.databinding.FragmentBmgmMiniCartWidgetBinding
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartFragment : Fragment() {

    companion object {

        const val TAG = "BmgmMiniCartWidgetFragment"
        fun getInstance(fm: FragmentManager): BmgmMiniCartFragment {
            return (fm.findFragmentByTag(TAG) as? BmgmMiniCartFragment)
                ?: BmgmMiniCartFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: FragmentBmgmMiniCartWidgetBinding? = null
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter() }
    private val viewModel: BmgmMiniCartViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BmgmMiniCartViewModel::class.java]
    }

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
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = miniCartAdapter
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