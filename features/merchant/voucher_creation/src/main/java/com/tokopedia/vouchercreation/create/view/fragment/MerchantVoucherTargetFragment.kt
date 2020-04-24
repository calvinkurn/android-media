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
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.VoucherTargetUiModel
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

    private val createPromoCodeBottomSheetFragment by lazy {
        CreatePromoCodeBottomSheetFragment.createInstance(context, ::onNextCreatePromoCode, ::getPromoCodeString)
    }

    private val voucherDisplayBottomSheetFragment by lazy {
        VoucherDisplayBottomSheetFragment.createInstance(context, ::getClickedVoucherDisplayType)
    }

    private val fillVoucherWidget by lazy {
        FillVoucherNameUiModel()
    }

    private var voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet)

    private var shouldReturnToInitialValue = true

    private var promoCodeText = ""

    private var lastClickedVoucherDisplayType = VoucherTargetCardType.PUBLIC

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
                    viewModel.setDefaultVoucherTargetListData()
                    super.setupView()
                }
            }
            else -> {
                dismissBottomSheet()
            }
        }
    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        when(bottomSheetType) {
            CreateVoucherBottomSheetType.VOUCHER_DISPLAY -> {
                val bottomSheetTitleRes = when(lastClickedVoucherDisplayType) {
                    VoucherTargetCardType.PUBLIC -> R.string.mvc_create_public_voucher_display_title
                    VoucherTargetCardType.PRIVATE -> R.string.mvc_create_private_voucher_display_title
                }
                voucherDisplayBottomSheetFragment.setTitle(
                        resources.getString(bottomSheetTitleRes).toBlankOrString()
                )
            }
            else -> {}
        }
    }

    private fun observeLiveData() {
        viewModel.run {
            voucherTargetListData.observe(viewLifecycleOwner, Observer { voucherTargetList ->
                dismissBottomSheet()
                voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet, voucherTargetList)
                extraWidget = listOf(voucherTargetWidget, fillVoucherWidget)
                super.setupView()
            })
            privateVoucherPromoCode.observe(viewLifecycleOwner, Observer { promoCode ->
                promoCodeText = promoCode
            })
            shouldReturnToInitialValue.observe(viewLifecycleOwner, Observer { shouldReturn ->
                this@MerchantVoucherTargetFragment.shouldReturnToInitialValue = shouldReturn
            })

        }
    }

    private fun setupBottomSheet() {
        context?.run {
            addBottomSheetView(CreateVoucherBottomSheetType.CREATE_PROMO_CODE, createPromoCodeBottomSheetFragment)
            addBottomSheetView(CreateVoucherBottomSheetType.VOUCHER_DISPLAY, voucherDisplayBottomSheetFragment)
        }
    }

    private fun openBottomSheet(bottomSheetType: CreateVoucherBottomSheetType,
                                voucherTargetCardType: VoucherTargetCardType? = null) {
        lastClickedVoucherDisplayType = voucherTargetCardType ?: lastClickedVoucherDisplayType
        showBottomSheet(bottomSheetType)
    }

    private fun onNextCreatePromoCode(promoCode: String) {
        viewModel.validatePromoCode(promoCode)
    }

    private fun getPromoCodeString() : String = promoCodeText

    private fun getClickedVoucherDisplayType() : VoucherTargetCardType = lastClickedVoucherDisplayType

}