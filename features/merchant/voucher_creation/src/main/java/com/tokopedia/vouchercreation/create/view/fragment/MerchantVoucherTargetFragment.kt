package com.tokopedia.vouchercreation.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.bottomsheet.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheet
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.MerchantVoucherTargetViewModel
import com.tokopedia.vouchercreation.di.component.DaggerVoucherCreationComponent
import javax.inject.Inject

class MerchantVoucherTargetFragment(onNextInvoker: () -> Unit = {})
    : BaseCreateMerchantVoucherFragment<VoucherTargetTypeFactory, VoucherTargetAdapterTypeFactory>(onNextInvoker) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = MerchantVoucherTargetFragment(onNext)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantVoucherTargetViewModel::class.java)
    }

    private val createPromoCodeBottomSheet by lazy {
        CreatePromoCodeBottomSheet.createInstance(context, ::onNextCreatePromoCode, ::getPromoCodeString)
    }

    private val fillVoucherWidget by lazy {
        FillVoucherNameUiModel()
    }

    private var voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet)

    private var shouldReturnToInitialValue = true

    private var promoCodeText = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_target, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun getAdapterTypeFactory(): VoucherTargetAdapterTypeFactory = VoucherTargetAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun loadData(page: Int) {}

    override var extraWidget: List<Visitable<VoucherTargetTypeFactory>> =
            listOf(voucherTargetWidget, fillVoucherWidget)

    override fun setupView() {
        super.setupView()
        observeLiveData()
        setupBottomSheet()
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        when(bottomSheetType) {
            CreateVoucherBottomSheetType.CREATE_PROMO_CODE -> {
                if (shouldReturnToInitialValue) {
                    voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet)
                    extraWidget = listOf(voucherTargetWidget, fillVoucherWidget)
                    super.setupView()
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.run {
            voucherTargetListData.observe(viewLifecycleOwner, Observer { voucherTargetList ->
                shouldReturnToInitialValue = false
                dismissBottomSheet()
                voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet, voucherTargetList)
                extraWidget = listOf(voucherTargetWidget, fillVoucherWidget)
                super.setupView()
            })
            specialVoucherPromoCode.observe(viewLifecycleOwner, Observer { promoCode ->
                promoCodeText = promoCode
            })

        }
    }

    private fun setupBottomSheet() {
        context?.run {
            addBottomSheetView(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, createPromoCodeBottomSheet)
        }
    }

    private fun openBottomSheet() {
        showBottomSheet(CreateVoucherBottomSheetType.CREATE_PROMO_CODE)
    }

    private fun onNextCreatePromoCode(promoCode: String) {
        viewModel.validatePromoCode(promoCode)
    }

    private fun getPromoCodeString() : String = promoCodeText

}