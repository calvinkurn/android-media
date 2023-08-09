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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.minicart.databinding.FragmentBmgmMiniCartWidgetBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartFragment : Fragment(), BmgmMiniCartAdapter.Listener {

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
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter(this) }
    private val viewModel: BmgmMiniCartViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BmgmMiniCartViewModel::class.java]
    }

    private var data: BmgmCommonDataUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        fetchMiniCartData()
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
        observeCartData()
    }

    override fun setOnItemClickedListener() {
        context?.let {
            data?.let {
                RouteManager.route(context, ApplinkConstInternalGlobal.BMGM_MINI_CART)
            }
        }
    }

    private fun observeCartData() {
        viewLifecycleOwner.observe(viewModel.cartData) {
            when (it) {
                is Success -> setOnSuccessGetCartData(it.data)
                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        }
    }

    private fun setOnSuccessGetCartData(data: BmgmCommonDataUiModel) {
        this.data = data
        binding?.run {
            val checkoutView = ViewBmgmMiniCartSubTotalBinding.bind(root)
            if (data.products.isNotEmpty()) {
                tvBmgmCartDiscount.text = data.offerMessage.parseAsHtml()
                tvBmgmCartDiscount.visible()
                rvBmgmMiniCart.visible()
                checkoutView.bmgmBtnOpenCart.isEnabled = true
            } else {
                tvBmgmCartDiscount.gone()
                rvBmgmMiniCart.gone()
                checkoutView.bmgmBtnOpenCart.isEnabled = false
            }

            miniCartAdapter.data.clear()
            miniCartAdapter.data.addAll(data.products)
            val lastIndex = miniCartAdapter.itemCount.minus(Int.ONE)
            miniCartAdapter.notifyItemRangeChanged(Int.ZERO, lastIndex)
        }
    }

    private fun fetchMiniCartData() {
        val param = BmgmParamModel(0L, 0L, listOf())
        viewModel.getMiniCartData(param)
    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            addItemDecoration(BmgmMiniCartItemDecoration())
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