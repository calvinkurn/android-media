package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getToday
import com.tokopedia.vouchercreation.databinding.FragmentMvcCreateCouponDetailBinding
import com.tokopedia.vouchercreation.product.create.view.adapter.CreateCouponTargetAdapter
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CreateCouponDetailViewModel
import com.tokopedia.vouchercreation.shop.create.view.fragment.step.MerchantVoucherTargetFragment
import kotlinx.android.synthetic.main.fragment_merchant_voucher_target.*
import java.util.*
import javax.inject.Inject

class CreateCouponDetailFragment : BaseDaggerFragment(){

    private var binding by autoClearedNullable<FragmentMvcCreateCouponDetailBinding>()
    private val scrollViewCouponDetail by lazy { binding?.scrollViewCouponDetail }
    private val btnCouponCreateNext by lazy { binding?.btnCouponCreateNext }
    private val rvTarget by lazy { binding?.layoutCouponTarget?.rvTarget}
    private val tfuFillCouponName by lazy { binding?.layoutCouponName?.tfuFillCouponName }
    private val tfuFillCouponCode by lazy { binding?.layoutCouponName?.tfuFillCouponCode }
    private val tickerErrorCouponValidation by lazy { binding?.layoutCouponName?.tickerErrorCouponValidation}
    private val tfuCouponDateStart by lazy { binding?.layoutCouponDate?.tfuCouponDateStart }
    private val tfuCouponDateEnd by lazy { binding?.layoutCouponDate?.tfuCouponDateEnd }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CreateCouponDetailViewModel::class.java)
    }

    override fun getScreenName(): String = CreateCouponDetailFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMvcCreateCouponDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewTarget()
        setupNameInput()
        setupDateInput()
        setupNextButton()

        observeCouponTargetList()
        observeSelectedCouponTarget()
        observeSubmitValidationResult()
        viewModel.populateCouponTarget()
    }

    private fun observeSubmitValidationResult() {
        viewModel.couponValidationResult.observe(viewLifecycleOwner) {
            if (it is Success) {
                val validationResult = it.data
                if (!validationResult.checkHasError()) {
                    activity?.run {
                        KeyboardHandler.hideSoftKeyboard(this)
                    }
                    clearErrorMessage()
                    // TODO: navigate to next page
                } else {
                    when {
                        validationResult.isPublicError.isNotBlank() -> {
                            view?.showErrorToaster(validationResult.isPublicError)
                        }
                        else -> {
                            val isCouponNameError = validationResult.couponNameError.isNotBlank()
                            val isCouponCodeError = validationResult.codeError.isNotBlank()
                            tfuFillCouponName?.setError(isCouponNameError)
                            tfuFillCouponName?.setMessage(validationResult.couponNameError)
                            tfuFillCouponCode?.setError(isCouponCodeError)
                            tfuFillCouponCode?.setMessage(validationResult.codeError)
                            tickerErrorCouponValidation?.isVisible =
                                isCouponCodeError || isCouponNameError
                        }
                    }
                }
            } else if (it is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                view?.showErrorToaster(errorMessage)
            }
        }
    }

    private fun setupNextButton() {
        btnCouponCreateNext?.setOnClickListener {
            val promoCode = tfuFillCouponCode?.textFieldInput?.text?.toString().orEmpty()
            val couponName = tfuFillCouponName?.textFieldInput?.text?.toString().orEmpty()
            viewModel.validateCouponTarget(promoCode, couponName)
        }
    }

    private fun setupNameInput() {
        tfuFillCouponCode?.textFieldInput?.afterTextChanged {

        }
        tfuFillCouponName?.textFieldInput?.afterTextChanged {

        }
    }

    private fun observeSelectedCouponTarget() {
        viewModel.selectedCouponTarget.observe(viewLifecycleOwner) {
            tfuFillCouponCode?.isVisible = it == CouponTargetEnum.PRIVATE
        }
    }

    private fun observeCouponTargetList() {
        viewModel.couponTargetList.observe(viewLifecycleOwner) {
            rvTarget?.adapter = CreateCouponTargetAdapter(::onCouponTargetChanged).apply { setData(it) }
        }
    }

    private fun setupRecyclerViewTarget() {
        rvTarget?.setRecyclerViewToVertical()
    }

    private fun setupDateInput() {
        tfuCouponDateStart?.getFirstIcon()?.tintDrawableToBlack()
        tfuCouponDateEnd?.getFirstIcon()?.tintDrawableToBlack()

        tfuCouponDateStart?.setFieldOnClickListener {
            pickStartDate()
        }

        tfuCouponDateEnd?.setFieldOnClickListener {
            pickEndDate()
        }
    }

    private fun onCouponTargetChanged(target: CouponTargetEnum) {
        viewModel.setSelectedCouponTarget(target)
    }

    private fun pickStartDate() {
        val title = getString(R.string.mvc_start_date_title)
        val info = getString(R.string.mvc_create_coupon_date_desc).parseAsHtml()
        val minDate = requireContext().getMinStartDate()
        val defaultDate = requireContext().getToday()
        val maxDate = requireContext().getMaxStartDate()
        getStartDateTimePicker(title, info, minDate, defaultDate, maxDate)
    }

    private fun pickEndDate() {
        val title = getString(R.string.mvc_start_date_title)
        val info = getString(R.string.mvc_create_coupon_date_desc).parseAsHtml()
        val minDate = DateTimeUtils.getMinEndDate(requireContext().getToday()) ?: GregorianCalendar()
        val defaultDate = requireContext().getToday()
        val maxDate = DateTimeUtils.getMaxEndDate(requireContext().getToday()) ?: GregorianCalendar()
        getStartDateTimePicker(title, info, minDate, defaultDate, maxDate)
    }

    private fun clearErrorMessage() {
        tfuFillCouponName?.setError(false)
        tfuFillCouponName?.setMessage("")
        tfuFillCouponCode?.setError(false)
        tfuFillCouponCode?.setMessage("")
        tfuCouponDateStart?.setError(false)
        tfuCouponDateStart?.setMessage("")
        tfuCouponDateEnd?.setError(false)
        tfuCouponDateEnd?.setMessage("")
    }

    private fun RecyclerView.setRecyclerViewToVertical() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}