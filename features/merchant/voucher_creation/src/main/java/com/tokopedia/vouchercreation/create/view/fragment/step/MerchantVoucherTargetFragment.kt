package com.tokopedia.vouchercreation.create.view.fragment.step

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.di.component.VoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTargetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.widgets.VoucherTargetUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertarget.widgets.FillVoucherNameViewHolder
import com.tokopedia.vouchercreation.create.view.viewmodel.MerchantVoucherTargetViewModel
import kotlinx.android.synthetic.main.fragment_merchant_voucher_target.*
import javax.inject.Inject

class MerchantVoucherTargetFragment : BaseCreateMerchantVoucherFragment<VoucherTargetTypeFactory, VoucherTargetAdapterTypeFactory>() {

    companion object {

        private const val MIN_TEXTFIELD_LENGTH = 5

        @JvmStatic
        fun createInstance(onNext: (Int, String, String) -> Unit,
                           getPromoCodePrefix: () -> String) = MerchantVoucherTargetFragment().apply {
            setOnNextClick(onNext)
            setGetPromoCodePrefix(getPromoCodePrefix)
        }
    }

    fun setOnNextClick(onNext: (Int, String, String) -> Unit) {
        this.onNext = onNext
    }

    fun setGetPromoCodePrefix(getPromoCodePrefix: () -> String) {
        this.getPromoCodePrefix = getPromoCodePrefix
    }

    private var onNext: (Int, String, String) -> Unit = { _,_,_ -> }
    private var getPromoCodePrefix: () -> String = {""}

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantVoucherTargetViewModel::class.java)
    }

    private val createPromoCodeBottomSheetFragment by lazy {
        CreatePromoCodeBottomSheetFragment.createInstance(context, ::onNextCreatePromoCode, ::getPromoCodeString, getPromoCodePrefix)
    }

    private val voucherDisplayBottomSheetFragment by lazy {
        VoucherDisplayBottomSheetFragment.createInstance(context, ::getClickedVoucherDisplayType)
    }

    private var alertMinimumMessage = ""

    private var voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet, ::onSetActiveVoucherTargetType)

    private var shouldReturnToInitialValue = true

    private var promoCodeText = ""

    private var couponName = ""

    private var lastClickedVoucherDisplayType = VoucherTargetCardType.PUBLIC
    private var currentTargetType = VoucherTargetType.PUBLIC

    override var layoutRes: Int = R.layout.fragment_merchant_voucher_target

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
            listOf(voucherTargetWidget)

    override fun setupView() {
        super.setupView()
        observeLiveData()
        setupTextFieldWidget()
        setupNextButton()
        setupBottomSheet()
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        when(bottomSheetType) {
            CreateVoucherBottomSheetType.CREATE_PROMO_CODE -> {
                if (shouldReturnToInitialValue) {
                    viewModel.setDefaultVoucherTargetListData()
                    viewModel.setActiveVoucherTargetType(VoucherTargetType.PUBLIC)
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

    override fun onFinishRenderInitial() {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.flush()
    }

    private fun observeLiveData() {
        viewModel.run {
            voucherTargetListData.observe(viewLifecycleOwner, Observer { voucherTargetList ->
                dismissBottomSheet()
                voucherTargetWidget = VoucherTargetUiModel(::openBottomSheet, ::onSetActiveVoucherTargetType, voucherTargetList)
                extraWidget = listOf(voucherTargetWidget)
                super.setupView()
            })
            privateVoucherPromoCode.observe(viewLifecycleOwner, Observer { promoCode ->
                promoCodeText = promoCode
            })
            shouldReturnToInitialValue.observe(viewLifecycleOwner, Observer { shouldReturn ->
                this@MerchantVoucherTargetFragment.shouldReturnToInitialValue = shouldReturn
            })
            voucherTargetValidationLiveData.observe(viewLifecycleOwner, Observer { result ->
                voucherTargetNextButton?.isLoading = false
                when(result) {
                    is Success -> {
                        val validation = result.data
                        if (!validation.checkHasError()) {
                            onNext(currentTargetType, couponName, promoCodeText)
                        } else {
                            validation.couponNameError.run {
                                if (isNotBlank()) {
                                    fillVoucherNameTextfield?.setError(true)
                                    fillVoucherNameTextfield?.setMessage(this)
                                    return@Observer
                                }
                            }
                            validation.isPublicError.let { error ->
                                if (error.isNotBlank()) {
                                    view?.showErrorToaster(error)
                                    return@Observer
                                }
                            }
                            validation.codeError.let { error ->
                                if (error.isNotBlank()) {
                                    view?.showErrorToaster(error)
                                    return@Observer
                                }
                            }
                        }
                    }
                    is Fail -> {
                        val error = result.throwable.message.toBlankOrString()
                        view?.showErrorToaster(error)
                    }
                }
            })
            voucherTargetTypeLiveData.observe(viewLifecycleOwner, Observer { type ->
                currentTargetType = type
            })
        }
    }

    private fun setupTextFieldWidget() {
        alertMinimumMessage = context?.getString(FillVoucherNameViewHolder.TEXFIELD_ALERT_MINIMUM).toBlankOrString()
        fillVoucherNameTextfield?.run {
            textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    //No op
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //No op
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    when {
                        s.length < MIN_TEXTFIELD_LENGTH -> {
                            setError(true)
                            setMessage(alertMinimumMessage)
                        }
                        else -> {
                            setError(false)
                            setMessage("")
                        }
                    }
                }
            })
        }
    }

    private fun setupNextButton() {
        voucherTargetNextButton?.run {
            setOnClickListener {
                if (!isLoading) {
                    isLoading = true
                    couponName = fillVoucherNameTextfield?.textFieldInput?.text?.toString().toBlankOrString()
                    viewModel.validateVoucherTarget(promoCodeText, couponName)
                }
            }
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

    private fun onSetActiveVoucherTargetType(@VoucherTargetType targetType: Int) {
        viewModel.setActiveVoucherTargetType(targetType)
    }

    private fun onNextCreatePromoCode(promoCode: String) {
        viewModel.setPromoCode(promoCode)
    }

    private fun getPromoCodeString() : String = promoCodeText

    private fun getClickedVoucherDisplayType() : VoucherTargetCardType = lastClickedVoucherDisplayType


}