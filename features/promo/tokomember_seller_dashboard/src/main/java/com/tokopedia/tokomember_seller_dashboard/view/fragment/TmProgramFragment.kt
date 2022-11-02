package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.model.ProgramThreshold
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DURATION
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.LOADING_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.SIMPLE_DATE_FORMAT
import com.tokopedia.tokomember_seller_dashboard.util.TM_ERROR_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_PROGRAM_MIN_PURCHASE_ERROR
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTime
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getDayFromTimeWindow
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getDayOfWeekID
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDatePreview
import com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_program_form_container.*
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val COUNTRY_ID = "ID"
private const val LANGUAGE_ID = "in"
private val locale = Locale(LANGUAGE_ID, COUNTRY_ID)
private const val PROGRESS_33 = 33

class TmProgramFragment : BaseDaggerFragment(), ChipGroupCallback,
    BottomSheetClickListener {

    private var firstTime = false
    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    private var selectedTime = ""
    private var fromEdit = false
    private var programActionType = ProgramActionType.CREATE
    private var periodInMonth = 0
    private var selectedChipPosition = 0
    private var errorCount = 0
    private var programUpdateResponse = ProgramUpdateDataInput()
    private var tmTracker: TmTracker? = null
    private var programCreationId = 0
    private var loaderDialog: LoaderDialog? = null
    private var errorCodeProgramCreation = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }
    private var selectedCalendar: Calendar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getBoolean(BUNDLE_EDIT_PROGRAM, false)?.let {
            fromEdit = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_TYPE, 0)?.let {
            programActionType = it
        }

        if (context is TmOpenFragmentCallback) {
            tmOpenFragmentCallback = context
        } else {
            throw Exception(context.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_program_form_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmTracker = TmTracker()
        renderHeader()
        observeViewModel()
        callGQL(
            programActionType,
            arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
            arguments?.getInt(BUNDLE_PROGRAM_ID) ?: 0
        )
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun observeViewModel() {
        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        renderProgramUI(it.data.membershipGetProgramForm)
                    } else {
                        handleErrorOnDataError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleErrorOnDataError()
                }
            }
        })

        tmDashCreateViewModel.tokomemberProgramUpdateResultLiveData.observe(viewLifecycleOwner, {
            when (it.status) {

                TokoLiveDataResult.STATUS.LOADING -> {
                    openLoadingDialog()
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    closeLoadingDialog()
                    when (it.data?.membershipCreateEditProgram?.resultStatus?.code) {
                        CODE_SUCCESS -> {
                            programCreationId =
                                it?.data.membershipCreateEditProgram.programSeller?.id ?: 0
                            onProgramUpdateSuccess()
                        }
                        CODE_INVALID -> {
                            view?.let { it1 ->
                                Toaster.build(
                                    it1,
                                    "Cek dan pastikan semua informasi yang kamu isi sudah benar, ya.",
                                    Toaster.LENGTH_LONG,
                                    Toaster.TYPE_ERROR
                                ).show()
                            }
                        }
                        CODE_OUTSIDE_WINDOW, CODE_PROGRAM_OUTSIDE_V2 -> {
                            errorCodeProgramCreation = CODE_OUTSIDE_WINDOW
                            handleErrorOnUpdate(
                                it.data.membershipCreateEditProgram.resultStatus.message,
                                ERROR_CREATING_CTA
                            )
                        }
                        else -> {
                            handleErrorOnUpdate(null)
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    closeLoadingDialog()
                    view?.let { it1 ->
                        Toaster.build(
                            it1,
                            RETRY,
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun handleErrorOnDataError() {
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {
            callGQL(
                programActionType,
                arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
                arguments?.getInt(BUNDLE_PROGRAM_ID) ?: 0
            )
        }
    }

    private fun handleErrorOnUpdate(message: List<String?>?, ctaError: String = "") {
        closeLoadingDialog()
        val title = if (message.isNullOrEmpty()) {
            when (errorCount) {
                0 -> ERROR_CREATING_TITLE
                else -> ERROR_CREATING_TITLE_RETRY
            }
        } else {
            message.getOrNull(0) ?: ""
        }
        var desc = ERROR_CREATING_DESC
        if (!message.isNullOrEmpty()) {
            desc = message.getOrNull(1) ?: ""
        }

        val image: String
        val cta =
            if (ctaError.isEmpty()) {
                image = ""
                when (errorCount) {
                    0 -> RETRY
                    else -> ERROR_CREATING_CTA_RETRY
                }
            } else {
                image = TM_ERROR_PROGRAM
                ctaError
            }

        val bundle = Bundle()
        val tmIntroBottomSheetModel =
            TmIntroBottomsheetModel(title, desc, image, cta, errorCount = errorCount)
        bundle.putString(
            TokomemberBottomsheet.ARG_BOTTOMSHEET,
            Gson().toJson(tmIntroBottomSheetModel)
        )
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(this)
        bottomSheet.show(childFragmentManager, "")
        errorCount += 1
    }

    private fun callGQL(programType: Int, shopId: Int, programId: Int = 0) {
        when (programType) {
            ProgramActionType.CREATE -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_CREATE)
            }
            ProgramActionType.CREATE_FROM_COUPON -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_CREATE)
            }
            ProgramActionType.CREATE_BUAT -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_CREATE)
            }
            ProgramActionType.DETAIL -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_DETAIL)
            }
            ProgramActionType.EXTEND -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_EXTEND)
            }
            ProgramActionType.EDIT -> {
                tmDashCreateViewModel.getProgramInfo(programId, shopId, ACTION_EDIT)
            }
            ProgramActionType.CANCEL -> {
                //TODO
            }
        }
    }

    private fun onProgramUpdateSuccess() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_PROGRAM_TYPE, programActionType)
        bundle.putString(BUNDLE_SHOP_AVATAR, arguments?.getString(BUNDLE_SHOP_AVATAR))
        bundle.putInt(BUNDLE_CARD_ID_IN_TOOLS, arguments?.getInt(BUNDLE_CARD_ID_IN_TOOLS) ?: 0)
        bundle.putString(BUNDLE_SHOP_NAME, arguments?.getString(BUNDLE_SHOP_NAME))
        bundle.putInt(BUNDLE_SHOP_ID, arguments?.getInt(BUNDLE_SHOP_ID) ?: 0)
        bundle.putInt(BUNDLE_CARD_ID, arguments?.getInt(BUNDLE_CARD_ID) ?: 0)
        bundle.putInt(BUNDLE_PROGRAM_DURATION, periodInMonth)
        bundle.putInt(BUNDLE_PROGRAM_ID_IN_TOOLS, programCreationId)
        when (programActionType) {
            ProgramActionType.CREATE -> {
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE, bundle)
            }
            ProgramActionType.CREATE_FROM_COUPON -> {
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE, bundle)
            }
            ProgramActionType.EXTEND -> {
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE_EXTEND, bundle)
            }
            ProgramActionType.EDIT -> {
                val intent = Intent()
                intent.putExtra("REFRESH_STATE", REFRESH)
                activity?.setResult(RESULT_OK, intent)
                activity?.finish()
            }
            ProgramActionType.CREATE_BUAT -> {
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE, bundle)
            }
        }
    }

    private fun renderHeader() {

        headerProgram.setNavigationOnClickListener {
            when (programActionType) {
                ProgramActionType.CREATE -> {
                    tmTracker?.clickProgramCreationBack(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                ProgramActionType.CREATE_BUAT -> {
                    tmTracker?.clickProgramCreationBackFromProgramList(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString()
                    )
                }
                ProgramActionType.EXTEND -> {
                    tmTracker?.clickProgramExtensionBack(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
                ProgramActionType.EDIT -> {
                    tmTracker?.clickProgramEditBack(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
            }
            activity?.onBackPressed()
        }

        when (programActionType) {
            ProgramActionType.CREATE -> {
                btnCreateProgram.text = PROGRAM_CTA
                headerProgram?.apply {
                    title = HEADER_TITLE_CREATE
                    subtitle = HEADER_TITLE_DESC
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(50, false)
                }
            }
            ProgramActionType.CREATE_BUAT -> {
                btnCreateProgram.text = PROGRAM_CTA
                headerProgram?.apply {
                    title = HEADER_TITLE_CREATE_BUAT
                    subtitle = HEADER_TITLE_EXTEND_DESC
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(50, false)
                }
            }
            ProgramActionType.CREATE_FROM_COUPON -> {
                btnCreateProgram.text = PROGRAM_CTA
                headerProgram?.apply {
                    title = HEADER_TITLE_CREATE_BUAT
                    subtitle = HEADER_TITLE_EXTEND_DESC
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(33, false)
                }
            }
            ProgramActionType.EXTEND -> {
                headerProgram?.apply {
                    title = HEADER_TITLE_EXTEND
                    subtitle = HEADER_TITLE_EXTEND_DESC
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(PROGRESS_33, false)
                }
                btnCreateProgram.text = PROGRAM_CTA
                textFieldTranskVip.isEnabled = false
                textFieldTranskPremium.isEnabled = false
            }
            ProgramActionType.EDIT -> {
                //TODO actionType edit pending from backend
                headerProgram?.apply {
                    title = HEADER_TITLE_EDIT
                    isShowBackButton = true
                }
                btnCreateProgram.text = PROGRAM_CTA_EDIT
                progressProgram?.hide()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProgramUI(membershipGetProgramForm: MembershipGetProgramForm?) {
        textFieldDuration.editText.inputType = InputType.TYPE_NULL
        containerViewFlipper.displayedChild = DATA
        addPremiumTransactionTextListener(membershipGetProgramForm?.programThreshold)
        addVipTransactionTextListener(membershipGetProgramForm?.programThreshold)
        if (programActionType == ProgramActionType.EXTEND) {
            textFieldTranskVip.isEnabled = false
            textFieldTranskPremium.isEnabled = false
            cardEditInfo.show()
            textFieldDuration.isEnabled = false
            textFieldDuration.iconContainer.isEnabled = false
            textFieldTranskPremium.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel1.toString())
            textFieldTranskVip.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel2.toString())
        } else {
            textFieldTranskVip.isEnabled = true
            textFieldTranskPremium.isEnabled = true
            textFieldDuration.isEnabled = true
            textFieldDuration.iconContainer.isEnabled = true
            textFieldTranskPremium.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel1.toString())
            textFieldTranskVip.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel2.toString())
            cardEditInfo.hide()
        }
        membershipGetProgramForm?.timePeriodList?.getOrNull(0)?.months?.let {
            periodInMonth = it
        }
        if (programActionType == ProgramActionType.CREATE || programActionType == ProgramActionType.CREATE_BUAT || programActionType == ProgramActionType.CREATE_FROM_COUPON) {
            val currentDate = GregorianCalendar(locale)
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
            val dayInId = getDayOfWeekID(currentDate.get(Calendar.DAY_OF_WEEK))

            val month = currentDate.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                LocaleUtils.getIDLocale()
            )
            textFieldDuration.editText.setText(
                "$dayInId, ${currentDate.get(Calendar.DATE)} $month ${currentDate.get(Calendar.YEAR)} "
            )
            membershipGetProgramForm?.programForm?.timeWindow?.startTime =
                convertDateTime(currentDate.time)
        } else {
            val day = getDayFromTimeWindow(
                membershipGetProgramForm?.programForm?.timeWindow?.startTime ?: ""
            )
            membershipGetProgramForm?.programForm?.timeWindow?.startTime?.let {
                selectedTime = it
                textFieldDuration.editText.setText("$day, ${setDatePreview(selectedTime)}")
            }
        }
        membershipGetProgramForm?.timePeriodList?.forEach {
            if (it?.isSelected == true) {
                selectedChipPosition = membershipGetProgramForm.timePeriodList.indexOf(it)
            }
        }
        chipGroup.setCallback(this)
        chipGroup.setDefaultSelection(selectedChipPosition)
        membershipGetProgramForm?.timePeriodList?.forEach {
            if (it != null) {
                it.name?.let { it1 -> chipGroup.addChip(it1) }
            }
        }
        context?.let {
            textFieldDuration?.setFirstIcon(R.drawable.tm_dash_calender)
        }
        textFieldDuration.iconContainer.setOnClickListener {
            clickDatePicker()
        }
        btnCreateProgram?.setOnClickListener {
            when (programActionType) {
                ProgramActionType.CREATE -> {
                    tmTracker?.clickProgramCreationButton(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
                ProgramActionType.CREATE_FROM_COUPON -> {
                    tmTracker?.clickProgramCreationButton(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
                ProgramActionType.CREATE_BUAT -> {
                    tmTracker?.clickProgramCreationButtonFromProgramList(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
                ProgramActionType.EXTEND -> {
                    tmTracker?.clickProgramExtensionCreateButton(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
                ProgramActionType.EDIT -> {
                    tmTracker?.clickProgramEditButton(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                        arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                    )
                }
            }
            initCreateProgram(membershipGetProgramForm)
        }
    }

    private fun noInternetUi(action: () -> Unit) {
        //show no internet bottomsheet

        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            ERROR_CREATING_TITLE_NO_INTERNET,
            ERROR_CREATING_DESC_NO_INTERNET,
            "",
            RETRY,
            errorCount = 0,
            showSecondaryCta = true
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                action()
            }})
        if(programActionType == ProgramActionType.CREATE){
            bottomsheet.setSecondaryCta {
                arguments?.getInt(BUNDLE_SHOP_ID)?.let {
                    TokomemberDashIntroActivity.openActivity(
                        it,
                        arguments?.getString(BUNDLE_SHOP_AVATAR).toString(),
                        arguments?.getString(BUNDLE_SHOP_NAME).toString(),
                        context = context
                    )
                }
                activity?.finish()
            }
        }
        bottomsheet.show(childFragmentManager,"")
    }
    private fun addPremiumTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskPremium.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText) {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    if (number > programThreshold?.maxThresholdLevel1 ?: 0) {
                        textFieldTranskPremium.isInputError = true
                        textFieldTranskPremium.setMessage(
                            "Maks. Rp${
                                CurrencyFormatHelper.convertToRupiah(
                                    programThreshold?.maxThresholdLevel1.toString()
                                )
                            }"
                        )
                    } else if (number <= programThreshold?.maxThresholdLevel1 ?: 0 && number > 0) {
                        tickerInfo.show()
                        if (textFieldTranskVip.editText.text.toString()
                                .isNotEmpty() && number >= CurrencyFormatHelper.convertRupiahToInt(
                                textFieldTranskVip.editText.text.toString()
                            )
                        ) {
                            textFieldTranskPremium.isInputError = true
                            textFieldTranskPremium.setMessage(TM_PROGRAM_MIN_PURCHASE_ERROR)
                        } else {
                            textFieldTranskPremium.isInputError = false
                            textFieldTranskPremium.setMessage("")
                            textFieldTranskVip.isInputError = false
                            textFieldTranskVip.setMessage("")
                        }
                    } else {
                        tickerInfo.hide()
                    }
                }
            })
        }
    }

    private fun addVipTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskVip.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText) {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number > programThreshold?.maxThresholdLevel2 ?: 0 -> {
                            textFieldTranskVip.isInputError = true
                            textFieldTranskVip.setMessage(
                                "Maks. Rp${
                                    CurrencyFormatHelper.convertToRupiah(
                                        programThreshold?.maxThresholdLevel2.toString()
                                    )
                                }"
                            )
                        }
                        number < programThreshold?.minThresholdLevel2 ?: 0 -> {
                            textFieldTranskVip.isInputError = true
                            textFieldTranskVip.setMessage(
                                "Min. Rp${
                                    CurrencyFormatHelper.convertToRupiah(
                                        programThreshold?.minThresholdLevel2.toString()
                                    )
                                }"
                            )
                        }
                        else -> {
                            textFieldTranskVip.isInputError = false
                            textFieldTranskVip.setMessage("")
                            textFieldTranskPremium.isInputError = false
                            textFieldTranskPremium.setMessage("")
                        }
                    }
                }
            })
        }
    }

    private fun initCreateProgram(membershipGetProgramForm: MembershipGetProgramForm?) {
        val pre = textFieldTranskPremium.editText.text.toString()
        val vip = textFieldTranskVip.editText.text.toString()
        if (membershipGetProgramForm != null) {
            membershipGetProgramForm.programForm?.tierLevels?.getOrNull(0)?.threshold =
                pre.replace(".", "").toIntSafely()
            membershipGetProgramForm.programForm?.tierLevels?.getOrNull(1)?.threshold =
                vip.replace(".", "").toIntSafely()
        }
        if (selectedCalendar != null) {
            membershipGetProgramForm?.programForm?.timeWindow?.startTime =
                selectedCalendar?.time?.let {
                    TmDateUtil.convertDateTimeRemoveTimeDiff(it)
                }
        }
        membershipGetProgramForm?.timePeriodList?.getOrNull(selectedChipPosition)?.months?.let {
            periodInMonth = it
        }
        if (programActionType == ProgramActionType.EDIT) {
            openLoadingDialog()
        }
        programUpdateResponse = ProgramUpdateMapper.formToUpdateMapper(
            membershipGetProgramForm,
            arguments?.getInt(BUNDLE_PROGRAM_TYPE) ?: 0,
            periodInMonth,
            arguments?.getInt(BUNDLE_CARD_ID) ?: 0,
            arguments?.getInt(BUNDLE_CARD_ID_IN_TOOLS) ?: 0
        )
        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.updateProgram(programUpdateResponse)
        }
        else{
            noInternetUi {
                initCreateProgram(membershipGetProgramForm)
            }
        }
    }

    private fun clickDatePicker() {
        var date = ""
        var month = ""
        var year = ""
        var day = 1
        var dayInId = ""
        context?.let {
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.MONTH, 3)
            calMax.add(Calendar.DAY_OF_MONTH, 1)
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val minDate = GregorianCalendar(locale)
            val currentDate = GregorianCalendar(locale)
            if (programActionType == ProgramActionType.EDIT) {
                val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                try {
                    minDate.time = sdf.parse(selectedTime + "00") ?: Date()
                    currentDate.time = sdf.parse(selectedTime + "00") ?: Date()
                } catch (e: Exception) {
                }
                maxDate.time = sdf.parse(selectedTime + "00") ?: Date()
                maxDate.add(Calendar.MONTH, 3)
                maxDate.add(Calendar.DAY_OF_MONTH, 1)
            }
            minDate.add(Calendar.DAY_OF_MONTH, 1)
            currentDate.add(Calendar.DAY_OF_MONTH, 1)

            if(selectedCalendar != null && firstTime){
                currentDate.time = selectedCalendar?.time
            }

            val datepickerObject =
                DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                    setTitle(DATE_TITLE)
                    setInfo(DATE_DESC)
                    setInfoVisible(true)
                    datePickerButton.let { button ->
                        button.setOnClickListener {
                            firstTime = true
                            selectedCalendar = getDate()
                            selectedTime = selectedCalendar?.time?.let { it1 ->
                                TmDateUtil.convertDateTimeRemoveTimeDiff(it1)
                            }.toString()
                            date = selectedCalendar?.get(Calendar.DATE).toString()
                            month = selectedCalendar?.getDisplayName(
                                Calendar.MONTH,
                                Calendar.LONG,
                                LocaleUtils.getIDLocale()
                            ).toString()
                            year = selectedCalendar?.get(Calendar.YEAR).toString()
                            day = selectedCalendar?.get(Calendar.DAY_OF_WEEK) ?: 0
                            dayInId = getDayOfWeekID(day)
                            dismiss()
                        }
                    }
                }

            datepickerObject.setOnDismissListener {
                if (dayInId.isNotEmpty() && date.isNotEmpty() && month.isNotEmpty() && year.isNotEmpty()) {
                    textFieldDuration?.textInputLayout?.editText?.setText(("$dayInId, $date $month $year"))
                }
            }
            childFragmentManager.let {
                datepickerObject.show(it, "")
            }
        }
    }

    companion object {
        const val MAX_YEAR = 10
        const val MIN_YEAR = -90
        const val HEADER_TITLE_CREATE = "Daftar TokoMember"
        const val HEADER_TITLE_CREATE_BUAT = "Buat Program"
        const val HEADER_TITLE_DESC = "Langkah 2 dari 4"
        const val HEADER_TITLE_EXTEND = "Perpanjang TokoMember"
        const val HEADER_TITLE_EXTEND_DESC = "Langkah 1 dari 3"
        const val HEADER_TITLE_EDIT = "Ubah Program"
        const val CODE_SUCCESS = "200"
        const val CODE_INVALID = "41002"
        const val CODE_OUTSIDE_WINDOW = "42039"
        const val CODE_PROGRAM_OUTSIDE_V2 = "42049"

        const val CODE_ERROR = "50001"

        const val SHIMMER = 0
        const val DATA = 1
        const val ERROR = 2
        fun newInstance(extras: Bundle?) = TmProgramFragment().apply {
            arguments = extras
        }
    }

    override fun chipSelected(position: Int) {
        this.selectedChipPosition = position
    }

    override fun onButtonClick(errorCount: Int) {
        if (errorCodeProgramCreation == CODE_OUTSIDE_WINDOW) {
        } else {
            if (errorCount == 0) {
                tmDashCreateViewModel.updateProgram(programUpdateResponse)
            } else {
                if (programActionType == ProgramActionType.CREATE) {
                    TokomemberDashIntroActivity.openActivity(
                        arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
                        arguments?.getString(BUNDLE_SHOP_AVATAR) ?: "",
                        arguments?.getString(
                            BUNDLE_SHOP_NAME
                        ) ?: "",
                        false,
                        this.context
                    )
                    activity?.finish()
                } else {
                    activity?.finish()
                }
            }
        }
    }

    private fun openLoadingDialog() {

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

}
