package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxEndDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinEndDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getToday
import com.tokopedia.vouchercreation.common.utils.convertUnsafeDateTime
import com.tokopedia.vouchercreation.common.utils.dismissBottomSheetWithTags
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.painter.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.SetVoucherPeriodViewModel
import kotlinx.android.synthetic.main.mvc_set_voucher_period_fragment.*
import java.util.*
import javax.inject.Inject

class SetVoucherPeriodFragment : Fragment() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: (String, String, String, String) -> Unit,
                           getVoucherBanner: () -> BannerVoucherUiModel,
                           getBannerBaseUiModel: () -> BannerBaseUiModel,
                           onSuccessGetBannerBitmap: (Bitmap) -> Unit,
                           getVoucherReviewData: () -> VoucherReviewUiModel,
                           isCreateNew: Boolean,
                           isEdit: Boolean) = SetVoucherPeriodFragment().apply {
            this.onNext = onNext
            this.getVoucherBanner = getVoucherBanner
            this.getBannerBaseUiModel = getBannerBaseUiModel
            this.onSuccessGetBannerBitmap = onSuccessGetBannerBitmap
            this.getVoucherReviewData = getVoucherReviewData
            this.isCreateNew = isCreateNew
            this.isEdit = isEdit
        }

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"

        private const val START_DATE_TIME_PICKER_TAG = "startDateTimePicker"
        private const val END_DATE_TIME_PICKER_TAG = "endDateTimePicker"

        private const val EXTRA_HOUR = 1
        private const val EXTRA_WEEK = 7
        private const val MINUTE_INTERVAL = 30

        private const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm z"
        private const val DATE_OF_WEEK_FORMAT = "EEE, dd MMM yyyy"

        private const val COMBINED_DATE = "dd MMM yyyy HH:mm"
        private const val COMBINED_DASHED_DATE = "yyyy-MM-dd HH:mm"

        private const val ERROR_MESSAGE = "Error validate voucher period"
    }

    private var onNext: (String, String, String, String) -> Unit = { _,_,_,_ -> }
    private var getVoucherBanner: () -> BannerVoucherUiModel = {
        BannerVoucherUiModel(
                VoucherImageType.FreeDelivery(0),
                "",
                "",
                "")
    }
    private var getBannerBaseUiModel: () -> BannerBaseUiModel = {
        BannerBaseUiModel(
                VoucherUrl.BANNER_BASE_URL,
                VoucherUrl.FREE_DELIVERY_URL,
                VoucherUrl.CASHBACK_URL,
                VoucherUrl.CASHBACK_UNTIL_URL
        )}
    private var onSuccessGetBannerBitmap: (Bitmap) -> Unit = { _ -> }
    private var getVoucherReviewData: () -> VoucherReviewUiModel? = { null }
    private var isCreateNew: Boolean = true
    private var isEdit: Boolean = false

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(SetVoucherPeriodViewModel::class.java)
    }

    private val locale by lazy {
        LocaleUtils.getIDLocale()
    }

    private val impressHolder = ImpressHolder()

    private var bannerVoucherUiModel: BannerVoucherUiModel = getVoucherBanner()

    private var bannerBitmap: Bitmap? = null

    private var isWaitingForValidation = false

    private var startDateString = ""
    private var endDateString = ""

    private var startCalendar: GregorianCalendar? = null
    private var endCalendar: GregorianCalendar? = null

    private var startDate = ""
    private var endDate = ""
    private var startHour = ""
    private var endHour = ""

    override fun onResume() {
        super.onResume()
        bannerVoucherUiModel = getVoucherBanner()
        drawBanner()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_set_voucher_period_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        view.addOnImpressionListener(impressHolder) {
            VoucherCreationTracking.sendOpenScreenTracking(
                    VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.PERIOD,
                    userSession.isLoggedIn,
                    userSession.userId)
        }
    }

    override fun onPause() {
        super.onPause()
        childFragmentManager.dismissBottomSheetWithTags(
                START_DATE_TIME_PICKER_TAG,
                END_DATE_TIME_PICKER_TAG
        )
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        startDateTextField?.textFieldInput?.run{
            // Fix blank color when dark mode activated.
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700))

            setOnClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.PERIOD,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CALENDAR_START,
                        userId = userSession.userId
                )
                getStartDateTimePicker()?.show(childFragmentManager, START_DATE_TIME_PICKER_TAG)
            }
            isFocusable = false
            isClickable = true
        }
        endDateTextField?.textFieldInput?.run {
            // Fix blank color when dark mode activated.
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700))

            setOnClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.PERIOD,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CALENDAR_END,
                        userId = userSession.userId
                )
                getEndDateTimePicker()?.show(childFragmentManager, END_DATE_TIME_PICKER_TAG)
            }
            isFocusable = false
            isClickable = true
        }
        observeLiveData()
        disableTextFieldEdit()
        setDateNextButton?.run {
            setOnClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.PERIOD,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CONTINUE,
                        userId = userSession.userId
                )
                isLoading = true
                onNextClicked()
            }
        }
        setInitialDate()
    }

    private fun onSuccessGetBitmap(bitmap: Bitmap) {
        activity?.runOnUiThread {
            periodBannerImage?.setImageBitmap(bitmap)
            onSuccessGetBannerBitmap(bitmap)
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.periodValidationLiveData) { result ->
                if (isWaitingForValidation) {
                    when(result) {
                        is Success -> {
                            val validation = result.data
                            if (!validation.getIsHaveError()) {
                                startDateTextField?.removeError()
                                endDateTextField?.removeError()
                                activity?.run {
                                    KeyboardHandler.hideSoftKeyboard(this)
                                }
                                onNext(startDate, endDate, startHour, endHour)
                            } else {
                                if (validation.dateStartError.isNotBlank() || validation.hourStartError.isNotBlank()) {
                                    startDateTextField?.run {
                                        setError(true)
                                        setMessage(validation.dateStartError)
                                    }
                                } else {
                                    startDateTextField?.removeError()
                                }
                                if (validation.dateEndError.isNotBlank() || validation.hourEndError.isNotBlank()) {
                                    endDateTextField?.run {
                                        setError(true)
                                        setMessage(validation.dateEndError)
                                    }
                                } else {
                                    endDateTextField?.removeError()
                                }
                            }
                        }
                        is Fail -> {
                            val error = result.throwable.message.toBlankOrString()
                            view?.showErrorToaster(error)
                            MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                        }
                    }
                    isWaitingForValidation = false
                    setDateNextButton?.isLoading = false
                }
            }

            observe(viewModel.startDateCalendarLiveData) { startDate ->
                startCalendar = startDate as? GregorianCalendar
                endDateString = startDate.time.toFormattedString(DATE_OF_WEEK_FORMAT, locale)
                val formattedDate = startDate.time.toFormattedString(FULL_DAY_FORMAT, locale)
                startDateTextField?.textFieldInput?.setText(formattedDate)
            }

            observe(viewModel.endDateCalendarLiveData) { endDate ->
                endCalendar = endDate as? GregorianCalendar
                val formattedDate = endDate.time.toFormattedString(FULL_DAY_FORMAT, locale)
                endDateTextField?.textFieldInput?.setText(formattedDate)
            }

            observe(viewModel.dateStartLiveData) { date ->
                startDate = date
            }
            observe(viewModel.dateEndLiveData) { date ->
                endDate = date
            }
            observe(viewModel.hourStartLiveData) { hour ->
                startHour = hour
            }
            observe(viewModel.hourEndLiveData) { hour ->
                endHour = hour
            }
        }
    }

    private fun disableTextFieldEdit() {
        startDateTextField?.textFieldInput?.keyListener = null
        endDateTextField?.textFieldInput?.keyListener = null
    }

    private fun setInitialDate() {
        context?.run {
            endCalendar = context?.run { getToday().apply {
                roundingDate()
                add(Calendar.DATE, EXTRA_WEEK)}}
            getMinStartDate().let { calendar ->
                calendar.roundingDate()
                val initialTime = calendar.time.toFormattedString(DATE_OF_WEEK_FORMAT, locale)
                startDateString = initialTime
                viewModel.setStartDateCalendar(calendar)
            }
            endCalendar?.let { calendar ->
                val initialTime = calendar.time.toFormattedString(DATE_OF_WEEK_FORMAT, locale)
                endDateString = initialTime
                viewModel.setEndDateCalendar(calendar)
            }
            getVoucherReviewData()?.let { uiModel ->
                if (!isCreateNew) {
                    if (uiModel.startDate.isNotEmpty() && uiModel.endDate.isNotEmpty()) {
                        val startTime = "${uiModel.startDate} ${uiModel.startHour}"
                        val endTime = "${uiModel.endDate} ${uiModel.endHour}"
                        val dateFormat =
                                if (isEdit) {
                                    COMBINED_DASHED_DATE
                                } else {
                                    COMBINED_DATE
                                }
                        startCalendar = getGregorianDate(startTime, dateFormat)
                        endCalendar = getGregorianDate(endTime, dateFormat)
                        startCalendar?.run {
                            viewModel.setStartDateCalendar(this)
                        }
                        endCalendar?.run {
                            viewModel.setEndDateCalendar(this)
                        }
                    }
                }
            }
        }
    }

    private fun drawBanner() {
        if (bannerBitmap == null) {
            context?.run {
                Glide.with(this)
                        .asDrawable()
                        .load(getBannerBaseUiModel().bannerBaseUrl)
                        .signature(ObjectKey(System.currentTimeMillis().toString()))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                activity?.runOnUiThread {
                                    val bitmap = resource.toBitmap()
                                    val painter = VoucherPreviewPainter(this@run, bitmap, ::onSuccessGetBitmap, getBannerBaseUiModel())
                                    painter.drawFull(bannerVoucherUiModel, bitmap)
                                }
                                return false
                            }
                        })
                        .submit()
            }
        }
    }

    private fun onNextClicked() {
        isWaitingForValidation = true
        viewModel.validateVoucherPeriod()
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
                getMinEndDate(startCalendar)?.let { minDate ->
                    endCalendar?.let { currentDate ->
                        getMaxEndDate(startCalendar)?.let { maxDate ->
                            val title = getString(R.string.mvc_end_date_title)
                            val info = String.format(getString(R.string.mvc_end_date_desc).toBlankOrString(), endDateString).parseAsHtml()
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

    private fun TextFieldUnify.removeError() {
        setError(false)
        setMessage("")
    }

    private fun GregorianCalendar.roundingDate() {
        val minute = get(Calendar.MINUTE)
        if (minute <= MINUTE_INTERVAL) {
            set(Calendar.MINUTE, MINUTE_INTERVAL)
        } else {
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR, EXTRA_HOUR)
        }
    }

    private fun getGregorianDate(date: String): GregorianCalendar {
        return GregorianCalendar().apply {
            time = date.convertUnsafeDateTime()
        }
    }

    private fun getGregorianDate(date: String, format: String): GregorianCalendar {
        return GregorianCalendar().apply {
            time = date.convertToDate(format)
        }
    }
}