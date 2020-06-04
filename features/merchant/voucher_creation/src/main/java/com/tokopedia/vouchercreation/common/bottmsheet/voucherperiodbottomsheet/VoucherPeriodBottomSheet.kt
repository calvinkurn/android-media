package com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinStartDate
import com.tokopedia.vouchercreation.common.utils.convertUnsafeDateTime
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.ChangeVoucherPeriodViewModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_voucher_edit_period.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_voucher_edit_period.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 29/04/20
 */

class VoucherPeriodBottomSheet(
        parent: ViewGroup,
        private val voucher: VoucherUiModel) : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(parent: ViewGroup,
                           voucher: VoucherUiModel): VoucherPeriodBottomSheet =
                VoucherPeriodBottomSheet(parent, voucher)

        private const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm z"
        private const val DATE_OF_WEEK_FORMAT = "EEE, dd MMM yyyy"

        private const val START_DATE_TIME_PICKER_TAG = "startChangeDateTimePicker"
        private const val END_DATE_TIME_PICKER_TAG = "endDateChangeTimePicker"

        private const val MINUTE_INTERVAL = 30
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ChangeVoucherPeriodViewModel::class.java)
    }

    private val locale by lazy {
        LocaleUtils.getIDLocale()
    }

    private var onSuccessListener: () -> Unit = {}
    private var onFailListener: (String) -> Unit = {}

    private var startDateString = ""
    private var endDateString = ""

    private var startCalendar: GregorianCalendar? = null
    private var endCalendar: GregorianCalendar? = null

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_voucher_edit_period, parent, false)

        setTitle(parent.context.getString(R.string.mvc_edit_shown_period))
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLiveData()
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            setupView(view)
            view.setupBottomSheetChildNoMargin()
        }
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView(view: View) = with(view) {
        setImageVoucher(voucher.isPublic, voucher.type)

        tvMvcVoucherName.text = voucher.name
        tvMvcVoucherDescription.text = String.format(context?.getString(R.string.mvc_discount_formatted).toBlankOrString(), voucher.typeFormatted, voucher.discountAmtFormatted)

        resetDate()

        edtMvcStartDate?.run {
            startCalendar?.run {
                setDateText(this)
            }
            textFieldInput.run {
                setOnClickListener {
                    getStartDateTimePicker()?.show(childFragmentManager, START_DATE_TIME_PICKER_TAG)
                }
                isFocusable = false
                isClickable = true
                keyListener = null
            }
        }
        edtMvcEndDate?.run {
            endCalendar?.run {
                setDateText(this)
            }
            textFieldInput.run {
                setOnClickListener {
                    getEndDateTimePicker()?.show(childFragmentManager, END_DATE_TIME_PICKER_TAG)
                }
                isFocusable = false
                isClickable = true
                keyListener = null
            }
        }
        context?.getMinStartDate()?.let { calendar ->
            val initialTime = calendar.time?.toFormattedString(DATE_OF_WEEK_FORMAT, locale).toBlankOrString()
            startDateString = initialTime
            viewModel.setStartDateCalendar(calendar)
        }
        endCalendar?.let { calendar ->
            val initialTime = calendar.time.toFormattedString(DATE_OF_WEEK_FORMAT, locale)
            endDateString = initialTime
            viewModel.setEndDateCalendar(calendar)
        }

        btnMvcSavePeriod?.run {
            setOnClickListener {
                isLoading = true
                viewModel.validateVoucherPeriod(voucher)
            }
        }

        setAction(context?.getString(R.string.mvc_retry).toBlankOrString()) {
            resetDate()
            edtMvcStartDate?.run {
                startCalendar?.run {
                    setDateText(this)
                }
            }
            edtMvcEndDate?.run {
                endCalendar?.run {
                    setDateText(this)
                }
            }
        }
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.startDateCalendarLiveData) { startDate ->
                (startDate as? GregorianCalendar)?.run {
                    edtMvcStartDate?.setDateText(this)
                }
            }
            observe(viewModel.endDateCalendarLiveData) { endDate ->
                (endDate as? GregorianCalendar)?.run {
                    edtMvcEndDate?.setDateText(this)
                }
            }
            observe(viewModel.updateVoucherSuccessLiveData) { result ->
                when(result) {
                    is Success -> {
                        onSuccessListener()
                    }
                    is Fail -> {
                        onFailListener(result.throwable.message.toBlankOrString())
                    }
                }
                btnMvcSavePeriod?.isLoading = false
                dismiss()
            }
        }
    }

    private fun resetDate() {
        startCalendar = getGregorianDate(voucher.startTime)
        endCalendar = getGregorianDate(voucher.finishTime)
    }

    private fun getGregorianDate(date: String): GregorianCalendar {
        return GregorianCalendar().apply {
            time = date.convertUnsafeDateTime()
        }
    }

    private fun getStartDateTimePicker() =
            context?.run {
                getMinStartDate().let { minDate ->
                    startCalendar?.let { currentDate ->
                        getMaxStartDate().let { maxDate ->
                            val title = getString(R.string.mvc_start_date_title)
                            val info = String.format(getString(R.string.mvc_start_date_desc).toBlankOrString(), startDateString).parseAsHtml()
                            val buttonText = getString(R.string.mvc_pick).toBlankOrString()
                            DateTimePickerUnify(this, minDate, currentDate, maxDate, null, DateTimePickerUnify.TYPE_DATETIMEPICKER).apply {
                                setTitle(title)
                                setInfo(info)
                                setInfoVisible(true)
                                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                                minuteInterval = MINUTE_INTERVAL
                                datePickerButton.let { button ->
                                    button.text = buttonText
                                    button.setOnClickListener {
                                        viewModel.setStartDateCalendar(getDate())
                                        dismiss()
                                    }
                                }
                            }
                        }
                    }
                }
            }

    private fun getEndDateTimePicker() =
            context?.run {
                DateTimeUtils.getMinEndDate(startCalendar)?.let { minDate ->
                    endCalendar?.let { currentDate ->
                        DateTimeUtils.getMaxEndDate(startCalendar)?.let { maxDate ->
                            val title = getString(R.string.mvc_end_date_title)
                            val info = String.format(getString(R.string.mvc_end_date_desc).toBlankOrString(), startDateString).parseAsHtml()
                            val buttonText = getString(R.string.mvc_pick).toBlankOrString()
                            DateTimePickerUnify(this, minDate, currentDate, maxDate, null, DateTimePickerUnify.TYPE_DATETIMEPICKER).apply {
                                setTitle(title)
                                setInfo(info)
                                setInfoVisible(true)
                                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                                minuteInterval = MINUTE_INTERVAL
                                datePickerButton.let { button ->
                                    button.text = buttonText
                                    button.setOnClickListener {
                                        viewModel.setEndDateCalendar(getDate())
                                        dismiss()
                                    }
                                }
                            }
                        }
                    }
                }
            }

    private fun TextFieldUnify.setDateText(calendar: GregorianCalendar) {
        val formattedDate = calendar.time.toFormattedString(FULL_DAY_FORMAT, locale).toBlankOrString()
        textFieldInput.setText(formattedDate)
    }

    private fun setImageVoucher(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            view?.imgMvcVoucher?.run {
                val drawableRes = when {
                    isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_publik
                    !isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_khusus
                    isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_publik
                    !isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_khusus
                    else -> R.drawable.ic_mvc_cashback_publik
                }
                loadImageDrawable(drawableRes)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun setOnSuccessClickListener(callback: () -> Unit): VoucherPeriodBottomSheet {
        this.onSuccessListener = callback
        return this
    }

    fun setOnFailClickListener(callback: (String) -> Unit): VoucherPeriodBottomSheet {
        this.onFailListener = callback
        return this
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }
}