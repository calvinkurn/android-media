package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetVpsPackageBinding
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.VpsPackageAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.VpsPackageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VpsPackageBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_VPS_PACKAGE = "selected_vps_package"
        @JvmStatic
        fun newInstance(vpsPackage : VpsPackage?): VpsPackageBottomSheet {
            return VpsPackageBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_SELECTED_VPS_PACKAGE, vpsPackage)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetVpsPackageBinding>()


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VpsPackageViewModel::class.java) }
    private val adapter = VpsPackageAdapter()
    private var onVpsPackageClicked : (VpsPackage) -> Unit = {}
    private val selectedVpsPackage by lazy {
        arguments?.getParcelable(
            BUNDLE_KEY_SELECTED_VPS_PACKAGE
        ) as? VpsPackage
    }

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }


    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SsfsBottomsheetVpsPackageBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.sfs_quota_source))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeVpsPackages()
        viewModel.getVpsPackages()
    }


    private fun observeVpsPackages() {
        viewModel.vpsPackages.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()

            when (result) {
                is Success -> {
                    adapter.submit(result.data)
                }
                is Fail -> {
                    binding?.root showError result.throwable
                }
            }

        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(activity ?: return)
        binding?.recyclerView?.adapter = adapter
        adapter.setOnVpsPackageClicked { selectedPackage ->
            onVpsPackageClicked(selectedPackage)
        }
    }

    fun setOnVpsPackageClicked(onVpsPackageClicked : (VpsPackage) -> Unit = {}) {
        this.onVpsPackageClicked = onVpsPackageClicked
    }

}