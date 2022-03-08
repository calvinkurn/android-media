package com.tokopedia.vouchercreation.product.update.period

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.toCalendar
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.isBeforeRollout
import com.tokopedia.vouchercreation.databinding.BottomsheetUpdateCouponPeriodBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import java.util.*
import javax.inject.Inject


class UpdateCouponPeriodBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_COUPON_ID = "coupon-id"
        private const val ERROR_MESSAGE = "Error edit coupon quota"
        private const val TIME_PICKER_TIME_INTERVAL_IN_MINUTE = 30
        private const val COUPON_START_DATE_OFFSET_IN_HOUR = 3
        private const val COUPON_END_DATE_OFFSET_IN_DAYS = 30
        private const val DATE_TIME_FORMAT_FULL = "EEE, dd MMM yyyy, HH:mm z"

        @JvmStatic
        fun newInstance(couponId: Long): UpdateCouponPeriodBottomSheet {
            val args = Bundle()
            args.putLong(BUNDLE_KEY_COUPON_ID, couponId)

            val bottomSheet = UpdateCouponPeriodBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private var nullableBinding: BottomsheetUpdateCouponPeriodBinding? = null
    private val binding: BottomsheetUpdateCouponPeriodBinding
        get() = requireNotNull(nullableBinding)

    private val couponId by lazy { arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(UpdateCouponPeriodViewModel::class.java) }

    private var onUpdatePeriodSuccess: () -> Unit = {}
    private var onUpdatePeriodError: (String) -> Unit = {}

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
        observeCouponDetail()
        observeUpdateResult()
        observeStartDateChange()
        observeEndDateChange()
        viewModel.getCouponDetail(couponId)
    }

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            binding.loader.gone()

            when(result) {
                is Success -> {
                    viewModel.setCurrentlySelectedStartDate(result.data.coupon.information.period.startDate)
                    viewModel.setCurrentlySelectedEndDate(result.data.coupon.information.period.endDate)
                    viewModel.setCouponData(result.data.coupon)

                    displayCouponInformation(
                        result.data.coupon.information.period.startDate,
                        result.data.coupon.information.period.endDate,
                        result.data.coupon.information.name
                    )
                    displayCouponImage(
                        result.data.coupon.information.target,
                        result.data.coupon.settings.type
                    )

                    binding.groupContent.visible()
                }
                is Fail -> {
                    binding.groupContent.gone()
                    showError(result.throwable)
                }
            }
        })
    }


    private fun setupView() {
        with(binding) {

            textFieldStartDate.isFocusable = false
            textFieldStartDate.isClickable = true
            textFieldStartDate.textFieldInput.keyListener = null
            textFieldStartDate.textFieldInput.setOnClickListener {
                viewModel.openStartDateTimePicker()
            }

            textFieldEndDate.isFocusable = false
            textFieldEndDate.isClickable = true
            textFieldEndDate.textFieldInput.keyListener = null
            textFieldEndDate.textFieldInput.setOnClickListener {
                viewModel.openEndDateTimePicker()
            }

            btnSave.setOnClickListener { updateCoupon() }
        }
    }


    private fun observeUpdateResult() {
        viewModel.updateCouponResult.observe(viewLifecycleOwner) { result ->
            binding.btnSave.isLoading = false
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

    private fun observeStartDateChange() {
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
            showStartDateTimePicker(startDate)
        }
    }

    private fun observeEndDateChange() {
        viewModel.endDate.observe(viewLifecycleOwner) { date ->
            val (startDate, endDate) = date
            showEndDateTimePicker(startDate, endDate)
        }
    }

    private fun displayCouponInformation(startDate : Date, endDate : Date, couponName : String) {
        binding.textFieldStartDate.textFieldInput.setText(startDate.parseTo(DATE_TIME_FORMAT_FULL))
        binding.textFieldEndDate.textFieldInput.setText(endDate.parseTo(DATE_TIME_FORMAT_FULL))
        binding.tpgCouponName.text = couponName
    }

    private fun showStartDateTimePicker(currentStartDate : Date) {
        val formattedStartDate = getCouponStartDate().time.parseTo(DateTimeUtils.DATE_FORMAT)
        val title = getString(R.string.mvc_start_date_title)
        val info = String.format(
            getString(R.string.mvc_start_date_desc),
            formattedStartDate
        ).parseAsHtml()
        val buttonText = getString(R.string.mvc_pick).toBlankOrString()

        val dateTimePicker = DateTimePickerUnify(
            requireActivity(),
            getCouponStartDate(),
            currentStartDate.toCalendar(),
            getCouponEndDate(currentStartDate),
            null,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )

        dateTimePicker.apply {
            setTitle(title)
            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            minuteInterval = TIME_PICKER_TIME_INTERVAL_IN_MINUTE
            datePickerButton.text = buttonText
            datePickerButton.setOnClickListener {
                viewModel.setCurrentlySelectedStartDate(getDate().time)
                val formattedNewStartDate = getDate().time.parseTo(DATE_TIME_FORMAT_FULL)
                binding.textFieldStartDate.textFieldInput.setText(formattedNewStartDate)
                dismiss()
            }
        }
        dateTimePicker.show(childFragmentManager, dateTimePicker.tag)
    }

    private fun showEndDateTimePicker(currentStartDate: Date, currentEndDate : Date ) {
        val title = getString(R.string.mvc_end_date_title)
        val buttonText = getString(R.string.mvc_pick).toBlankOrString()

        val formattedStartDate = currentStartDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val info = String.format(getString(R.string.mvc_end_date_desc), formattedStartDate).parseAsHtml()

        val dateTimePicker = DateTimePickerUnify(
            requireActivity(),
            getCouponStartDate(),
            currentEndDate.toCalendar(),
            getCouponEndDate(currentStartDate),
            null,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )


        dateTimePicker.apply {
            setTitle(title)
            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            minuteInterval = TIME_PICKER_TIME_INTERVAL_IN_MINUTE
            datePickerButton.text = buttonText
            datePickerButton.setOnClickListener {
                viewModel.setCurrentlySelectedEndDate(getDate().time)
                val formattedNewEndDate = getDate().time.parseTo(DATE_TIME_FORMAT_FULL)
                binding.textFieldEndDate.textFieldInput.setText(formattedNewEndDate)
                dismiss()
            }

        }
        dateTimePicker.show(childFragmentManager, dateTimePicker.tag)
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
        binding.imgCoupon.loadImageDrawable(drawableRes)
    }


    fun setOnSuccessClickListener(callback: () -> Unit) {
        this.onUpdatePeriodSuccess = callback
    }

    fun setOnFailClickListener(callback: (String) -> Unit) {
        this.onUpdatePeriodError = callback
    }

    private fun getCouponStartDate(): Calendar {
        if (requireContext().isBeforeRollout()) return getCouponStartDateBeforeRollout()
        val calendar = Calendar.getInstance()
        calendar.add(
            Calendar.HOUR_OF_DAY,
            COUPON_START_DATE_OFFSET_IN_HOUR
        )
        return calendar
    }

    private fun getCouponEndDate(currentStartDate: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentStartDate.time
        calendar.add(
            Calendar.DAY_OF_MONTH,
            COUPON_END_DATE_OFFSET_IN_DAYS
        )
        return calendar
    }

    private fun getCouponStartDateBeforeRollout(): Calendar {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = DateTimeUtils.ROLLOUT_DATE_THRESHOLD_TIME
        }
        return calendar
    }

    private fun updateCoupon() {
        binding.btnSave.isLoading = true
        binding.btnSave.loadingText = getString(R.string.mvc_please_wait)

        viewModel.updateCoupon()
    }


    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }


}