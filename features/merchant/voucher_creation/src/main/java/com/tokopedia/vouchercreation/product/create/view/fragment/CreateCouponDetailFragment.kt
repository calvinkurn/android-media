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
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getToday
import com.tokopedia.vouchercreation.databinding.FragmentMvcCreateCouponDetailBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.view.adapter.CreateCouponTargetAdapter
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.uimodel.convertToCouponInformationTarget
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CreateCouponDetailViewModel
import java.util.*
import javax.inject.Inject

class CreateCouponDetailFragment(
    private val couponInformationData: CouponInformation? = null
) : BaseDaggerFragment(){

    companion object {
        private const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm z"
    }

    private var binding by autoClearedNullable<FragmentMvcCreateCouponDetailBinding>()
    private val btnCouponCreateNext by lazy { binding?.btnCouponCreateNext }
    private val rvTarget by lazy { binding?.layoutCouponTarget?.rvTarget}
    private val tfuFillCouponName by lazy { binding?.layoutCouponName?.tfuFillCouponName }
    private val tfuFillCouponCode by lazy { binding?.layoutCouponName?.tfuFillCouponCode }
    private val tickerErrorCouponValidation by lazy { binding?.layoutCouponName?.tickerErrorCouponValidation}
    private val tfuCouponDateStart by lazy { binding?.layoutCouponDate?.tfuCouponDateStart }
    private val tfuCouponDateEnd by lazy { binding?.layoutCouponDate?.tfuCouponDateEnd }
    private var onCouponSaved: (CouponInformation) -> Unit = {}
    private val targetAdapter = CreateCouponTargetAdapter(::onCouponTargetChanged)

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
        setFragmentToUnifyBgColor()

        setupToolbar()
        setupRecyclerViewTarget()
        setupDateInput()
        setupNextButton()

        observeCouponTargetList()
        observeSelectedCouponTarget()
        observeInputDate()
        observeCouponValidationResult()
        observePeriodValidationResult()
        observeAllInputValid()

        viewModel.setStartDateCalendar(GregorianCalendar())
        viewModel.setEndDateCalendar(GregorianCalendar())
        viewModel.populateCouponTarget()

        if (couponInformationData != null) {
            viewModel.setCouponInformation(couponInformationData)
            tfuFillCouponName?.textFieldInput?.setText(couponInformationData.name)
            tfuFillCouponCode?.textFieldInput?.setText(couponInformationData.code)
        }
    }

    private fun observeCouponTargetList() {
        viewModel.couponTargetList.observe(viewLifecycleOwner) {
            targetAdapter.setData(it)
        }
    }

    private fun observeSelectedCouponTarget() {
        viewModel.selectedCouponTarget.observe(viewLifecycleOwner) {
            if (it == CouponTargetEnum.PUBLIC) tfuFillCouponCode?.textFieldInput?.setText("")
            tfuFillCouponCode?.isVisible = it == CouponTargetEnum.PRIVATE
            if (it != CouponTargetEnum.NOT_SELECTED) {
                btnCouponCreateNext?.isButtonEnabled = true
            }
        }
    }

    private fun observeInputDate() {
        viewModel.startDateCalendarLiveData.observe(viewLifecycleOwner) { startDate ->
            val formattedDate = startDate.time.toFormattedString(FULL_DAY_FORMAT, LocaleUtils.getIDLocale())
            tfuCouponDateStart?.textFieldInput?.setText(formattedDate)
        }

        viewModel.endDateCalendarLiveData.observe(viewLifecycleOwner) { endDate ->
            val formattedDate = endDate.time.toFormattedString(FULL_DAY_FORMAT, LocaleUtils.getIDLocale())
            tfuCouponDateEnd?.textFieldInput?.setText(formattedDate)
        }
    }

    private fun observeCouponValidationResult() {
        viewModel.couponValidationResult.observe(viewLifecycleOwner) {
            if (it is Success) {
                val validationResult = it.data
                if (!validationResult.checkHasError()) {
                    activity?.run {
                        KeyboardHandler.hideSoftKeyboard(this)
                    }
                } else {
                    when {
                        validationResult.isPublicError.isNotBlank() -> {
                            view?.showErrorToaster(validationResult.isPublicError)
                        }
                        else -> {
                            val isCouponNameError = validationResult.couponNameError.isNotBlank()
                            val isCouponCodeError = validationResult.codeError.isNotBlank()
                            val couponName = tfuFillCouponName?.textFieldInput?.text.toString()
                            val couponCode = tfuFillCouponCode?.textFieldInput?.text.toString()
                            tfuFillCouponName?.setError(isCouponNameError)
                            tfuFillCouponName?.setMessage(validationResult.couponNameError)
                            tfuFillCouponCode?.setError(isCouponCodeError)
                            tfuFillCouponCode?.setMessage(validationResult.codeError)
                            tickerErrorCouponValidation?.isVisible =
                                (isCouponCodeError && viewModel.validateMinCharCouponCode(couponCode)) ||
                                (isCouponNameError && viewModel.validateMinCharCouponName(couponName))
                        }
                    }
                }
            } else if (it is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                view?.showErrorToaster(errorMessage)
            }
        }
    }

    private fun observePeriodValidationResult() {
        viewModel.periodValidationLiveData.observe(viewLifecycleOwner) {
            if (it is Success) {
                val validationResult = it.data
                if (!validationResult.getIsHaveError()) {
                    activity?.run {
                        KeyboardHandler.hideSoftKeyboard(this)
                    }
                } else {
                     (validationResult.dateStartError.isNotBlank() ||
                             validationResult.hourStartError.isNotBlank()).run {
                        tfuCouponDateStart?.setError(this)
                        tfuCouponDateStart?.setMessage(validationResult.dateStartError)
                    }

                    (validationResult.dateEndError.isNotBlank() ||
                            validationResult.hourEndError.isNotBlank()).run {
                        tfuCouponDateEnd?.setError(this)
                        tfuCouponDateEnd?.setMessage(validationResult.dateEndError)
                    }
                }
            } else if (it is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                view?.showErrorToaster(errorMessage)
            }
        }
    }

    private fun observeAllInputValid() {
        viewModel.allInputValid.observe(viewLifecycleOwner) {
            if (it) {
                onCouponSaved.invoke(
                    CouponInformation(
                        viewModel.selectedCouponTarget.value.convertToCouponInformationTarget(),
                        tfuFillCouponName?.textFieldInput?.text.toString(),
                        tfuFillCouponCode?.textFieldInput?.text.toString(),
                        CouponInformation.Period(
                            viewModel.startDateCalendarLiveData.value?.time ?: Date(),
                            viewModel.endDateCalendarLiveData.value?.time ?: Date()
                        )
                    )
                )
            }
        }
    }

    private fun setupNextButton() {
        btnCouponCreateNext?.isButtonEnabled = false
        btnCouponCreateNext?.setOnClickListener {
            val promoCode = tfuFillCouponCode?.textFieldInput?.text?.toString().orEmpty()
            val couponName = tfuFillCouponName?.textFieldInput?.text?.toString().orEmpty()
            viewModel.validateCouponTarget(promoCode, couponName)
            viewModel.validateCouponPeriod()
            clearErrorMessage()
        }
    }

    private fun setupRecyclerViewTarget() {
        rvTarget?.setRecyclerViewToVertical()
        rvTarget?.adapter = targetAdapter
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
        val defaultDate = viewModel.startDateCalendarLiveData.value ?: GregorianCalendar()
        val maxDate = requireContext().getMaxStartDate()
        getStartDateTimePicker(title, info, minDate, defaultDate, maxDate) {
            viewModel.setStartDateCalendar(it)
        }
    }

    private fun pickEndDate() {
        val title = getString(R.string.mvc_start_date_title)
        val info = getString(R.string.mvc_create_coupon_date_desc).parseAsHtml()
        val minDate = DateTimeUtils.getMinEndDate(requireContext().getToday()) ?: GregorianCalendar()
        val defaultDate = viewModel.endDateCalendarLiveData.value ?: GregorianCalendar()
        val maxDate = DateTimeUtils.getMaxEndDate(requireContext().getToday()) ?: GregorianCalendar()
        getStartDateTimePicker(title, info, minDate, defaultDate, maxDate) {
            viewModel.setEndDateCalendar(it)
        }
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
        tickerErrorCouponValidation?.gone()
    }

    private fun setupToolbar() {
        val toolbar = binding?.toolbar
        toolbar?.headerTitle = getString(R.string.mvc_coupon_information_title)
        toolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun RecyclerView.setRecyclerViewToVertical() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setOnCouponSaved(onCouponSaved: (CouponInformation) -> Unit) {
        this.onCouponSaved = onCouponSaved
    }
}