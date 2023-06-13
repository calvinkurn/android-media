package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetVpsPackageBinding
import com.tokopedia.shop.flashsale.common.extension.attachDividerItemDecoration
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.VpsPackageAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.VpsPackageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VpsPackageBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_VPS_PACKAGE = "selected_vps_package_id"
        private const val BUNDLE_KEY_VPS_PACKAGES = "vps_packages"
        private const val ITEM_DIVIDER_INSET = 16

        @JvmStatic
        fun newInstance(
            selectedVpsPackageId: Long,
            vpsPackages: ArrayList<VpsPackageUiModel>
        ): VpsPackageBottomSheet {
            return VpsPackageBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(BUNDLE_KEY_SELECTED_VPS_PACKAGE, selectedVpsPackageId)
                    putParcelableArrayList(BUNDLE_KEY_VPS_PACKAGES, vpsPackages)
                }
            }
        }
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<SsfsBottomsheetVpsPackageBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VpsPackageViewModel::class.java) }
    private val vpsPackageAdapter = VpsPackageAdapter()
    private var onVpsPackageClicked : (VpsPackageUiModel) -> Unit = {}
    private val selectedVpsPackageId by lazy { arguments?.getLong(BUNDLE_KEY_SELECTED_VPS_PACKAGE).orZero() }
    private val vpsPackages by lazy { arguments?.getParcelableArrayList<VpsPackageUiModel>(BUNDLE_KEY_VPS_PACKAGES)?.toList().orEmpty() }

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
        setupView()
    }

    private fun setupView() {
        setupClickListeners()
        setupRecyclerView()
    }

    private fun setupClickListeners() {
        binding?.btnSave?.setOnClickListener {
            val vpsPackages = vpsPackageAdapter.snapshot()
            val selectedVpsPackage = viewModel.findSelectedVpsPackage(vpsPackages) ?: return@setOnClickListener
            onVpsPackageClicked(selectedVpsPackage)
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity ?: return)
            adapter = vpsPackageAdapter
            attachDividerItemDecoration(insetLeft = ITEM_DIVIDER_INSET, insetRight = ITEM_DIVIDER_INSET)
        }

        populateVpsPackages()

        vpsPackageAdapter.setOnVpsPackageClicked { selectedPackage ->
            handleVpsPackageSelection(selectedPackage)
        }
    }

    private fun populateVpsPackages() {
        val updatedVpsPackages = viewModel.markAsSelected(selectedVpsPackageId, vpsPackages)
        vpsPackageAdapter.submit(updatedVpsPackages)
    }

    fun setOnVpsPackageClicked(onVpsPackageClicked : (VpsPackageUiModel) -> Unit = {}) {
        this.onVpsPackageClicked = onVpsPackageClicked
    }

    private fun handleVpsPackageSelection(selectedPackage: VpsPackageUiModel) {
        val vpsPackages = vpsPackageAdapter.snapshot()
        val updatedVpsPackages = viewModel.markAsSelected(selectedPackage.packageId, vpsPackages)

        vpsPackageAdapter.submit(updatedVpsPackages)
    }
}