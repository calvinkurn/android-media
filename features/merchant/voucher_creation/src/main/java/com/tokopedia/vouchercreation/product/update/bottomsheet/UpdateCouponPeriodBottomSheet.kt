package com.tokopedia.vouchercreation.product.update.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.toDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.BottomsheetUpdateCouponPeriodBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import java.util.*
import javax.inject.Inject


class UpdateCouponPeriodBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"
        private const val ERROR_MESSAGE = "Error edit coupon quota"
        private const val TIME_PICKER_TIME_INTERVAL_IN_MINUTE = 30
        private const val COUPON_START_DATE_OFFSET_IN_HOUR = 3
        private const val COUPON_END_DATE_OFFSET_IN_DAYS = 30
        private const val DATE_TIME_FORMAT_FULL = "EEE, dd MMM yyyy, HH:mm z"

        @JvmStatic
        fun newInstance(coupon: Coupon): UpdateCouponPeriodBottomSheet {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_COUPON, coupon)

            val bottomSheet = UpdateCouponPeriodBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private var nullableBinding: BottomsheetUpdateCouponPeriodBinding? = null
    private val binding: BottomsheetUpdateCouponPeriodBinding
        get() = requireNotNull(nullableBinding)

    private val coupon by lazy { arguments?.getParcelable(BUNDLE_KEY_COUPON) as? Coupon }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(UpdateCouponPeriodViewModel::class.java) }

    private var onUpdatePeriodSuccess: () -> Unit = {}
    private var onUpdatePeriodError: (String) -> Unit = {}
    private var startDateMillis: Long = 0
    private var endDateMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
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
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        nullableBinding = BottomsheetUpdateCouponPeriodBinding.inflate(inflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding.root)
        setTitle(getString(R.string.update_active_coupon_period))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUpdateResult()
    }

    private fun setupView() {
        with(binding) {
            displayCouponImage(
                coupon?.information?.target ?: return,
                coupon?.settings?.type ?: return
            )

            tvMvcVoucherName.text = coupon?.information?.name

            edtMvcStartDate.isFocusable = false
            edtMvcStartDate.isClickable = true
            edtMvcStartDate.textFieldInput.keyListener = null
            edtMvcStartDate.textFieldInput.setOnClickListener {
                showStartDateTimePicker()
            }

            edtMvcEndDate.isFocusable = false
            edtMvcEndDate.isClickable = true
            edtMvcEndDate.textFieldInput.keyListener = null
            edtMvcEndDate.textFieldInput.setOnClickListener {
                showEndDateTimePicker()
            }

            btnMvcSavePeriod.setOnClickListener {
                updateCoupon()
            }
        }
    }


    private fun observeUpdateResult() {
        viewModel.updateCouponResult.observe(viewLifecycleOwner) { result ->
            binding.btnMvcSavePeriod.isLoading = false
            when (result) {
                is Success -> {
                    onUpdatePeriodSuccess()
                }
                is Fail -> {
                    onUpdatePeriodError(result.throwable.message.toBlankOrString())
                    MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                }
            }
            dismiss()
        }
    }


    private fun showStartDateTimePicker() {
        val formattedStartDate = getCouponStartDate().time.parseTo(DateTimeUtils.DATE_FORMAT)
        val title = getString(R.string.mvc_start_date_title)
        val info = String.format(
            getString(R.string.mvc_start_date_desc),
            formattedStartDate
        ).parseAsHtml()

        val buttonText = getString(R.string.mvc_pick).toBlankOrString()
        val dateChangeListener = object : OnDateChangedListener {
            override fun onDateChanged(date: Long) {
                startDateMillis = date
            }
        }

        val datePicker = DateTimePickerUnify(
            requireActivity(),
            getCouponStartDate(),
            Calendar.getInstance(),
            getCouponEndDate(),
            dateChangeListener,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )

        datePicker.apply {
            setTitle(title)
            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            minuteInterval = TIME_PICKER_TIME_INTERVAL_IN_MINUTE
            datePickerButton.text = buttonText
            datePickerButton.setOnClickListener {
                val formattedNewStartDate = startDateMillis.toDate().parseTo(DATE_TIME_FORMAT_FULL)
                binding.edtMvcStartDate.textFieldInput.setText(formattedNewStartDate)
                dismiss()
            }
        }
        datePicker.show(childFragmentManager, datePicker.tag)
    }

    private fun showEndDateTimePicker() {
        val formattedEndDate = getCouponEndDate().time.parseTo(DateTimeUtils.DATE_FORMAT)
        val title = getString(R.string.mvc_end_date_title)
        val info = String.format(getString(R.string.mvc_end_date_desc).toBlankOrString(), formattedEndDate)
                .parseAsHtml()
        val buttonText = getString(R.string.mvc_pick).toBlankOrString()
        val dateChangeListener = object : OnDateChangedListener {
            override fun onDateChanged(date: Long) {
                endDateMillis = date
            }
        }

        val datePicker = DateTimePickerUnify(
            requireActivity(),
            getCouponStartDate(),
            Calendar.getInstance(),
            getCouponEndDate(),
            dateChangeListener,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )

        datePicker.apply {
            setTitle(title)
            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            minuteInterval = TIME_PICKER_TIME_INTERVAL_IN_MINUTE
            datePickerButton.text = buttonText
            datePickerButton.setOnClickListener {
                val formattedNewEndDate = endDateMillis.toDate().parseTo(DATE_TIME_FORMAT_FULL)
                binding.edtMvcEndDate.textFieldInput.setText(formattedNewEndDate)
                dismiss()
            }
        }
        datePicker.show(childFragmentManager, datePicker.tag)
    }

    private fun displayCouponImage(target: CouponInformation.Target, couponType: CouponType) {
        val isPublic = target == CouponInformation.Target.PUBLIC
        val isCashbackCoupon = couponType == CouponType.CASHBACK
        val isFreeShippingCoupon = couponType == CouponType.FREE_SHIPPING
        val drawableRes = when {
            isPublic && isCashbackCoupon -> R.drawable.ic_mvc_cashback_publik
            !isPublic && isCashbackCoupon -> R.drawable.ic_mvc_cashback_khusus
            isPublic && isFreeShippingCoupon -> R.drawable.ic_mvc_ongkir_publik
            !isPublic && isFreeShippingCoupon -> R.drawable.ic_mvc_ongkir_khusus
            else -> R.drawable.ic_mvc_cashback_publik
        }
        binding.imgMvcVoucher.loadImageDrawable(drawableRes)
    }


    fun setOnSuccessClickListener(callback: () -> Unit) {
        this.onUpdatePeriodSuccess = callback
    }

    fun setOnFailClickListener(callback: (String) -> Unit) {
        this.onUpdatePeriodError = callback
    }

    private fun getCouponStartDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(
            Calendar.HOUR_OF_DAY,
            COUPON_START_DATE_OFFSET_IN_HOUR
        )
        return calendar
    }

    private fun getCouponEndDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(
            Calendar.DAY_OF_MONTH,
            COUPON_END_DATE_OFFSET_IN_DAYS
        )
        return calendar
    }

    private fun updateCoupon() {
        val coupon = coupon ?: return

        binding.btnMvcSavePeriod.isLoading = true
        binding.btnMvcSavePeriod.loadingText = getString(R.string.please_wait)

        val newStartDate = startDateMillis.toDate()
        val newEndDate = endDateMillis.toDate()
        val newPeriod = CouponInformation.Period(newStartDate, newEndDate)

        val updatedCouponInformation = coupon.information.copy(
            target = coupon.information.target,
            name = coupon.information.name,
            code = coupon.information.code,
            period = newPeriod
        )

        viewModel.updateCoupon(
            coupon.id,
            updatedCouponInformation,
            coupon.settings,
            coupon.products
        )
    }
}