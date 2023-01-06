package com.tokopedia.mvc.presentation.creation.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_TIME_MINUTE_PRECISION
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_YEAR_PRECISION
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.customview.RecurringDateScheduleCustomView
import com.tokopedia.mvc.databinding.SmvcFragmentCreationVoucherInformationBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoButtonSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherCodeSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherNameSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherTargetSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.bottomsheet.SelectRepeatPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.editperiod.VoucherEditCalendarBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.VoucherPeriodBottomSheet
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeActivity
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.mvc.util.DateTimeUtils
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.mvc.util.convertUnsafeDateTime
import com.tokopedia.mvc.util.extension.setToAllCapsMode
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@FlowPreview
class VoucherInformationFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration
        ): VoucherInformationFragment {
            return VoucherInformationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                }
            }
        }

        private const val DEBOUNCE = 300L
        private const val minimumActivePeriodCountToShowFullSchedule = 2
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentCreationVoucherInformationBinding>()
    private var voucherTargetSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherTargetSectionBinding>()
    private var voucherNameSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherNameSectionBinding>()
    private var voucherCodeSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherCodeSectionBinding>()
    private var voucherPeriodSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding>()
    private var buttonSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoButtonSectionBinding>()

    // viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherInformationViewModel::class.java) }

    // data
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
            ?: VoucherConfiguration()
    }
    private var startCalendar: GregorianCalendar? = null
    private var endCalendar: GregorianCalendar? = null
    private var startHour: Int = 0
    private var startMinute: Int = 0

    private var getSelectedDateStarting: (Calendar) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherStartDateChanged(it))
    }

    private var getSelectedDateEnding: (Calendar) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherEndDateChanged(it))
    }

    private var getSelectedRecurringPeriod: (Int) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherRecurringPeriodSelected(it))
    }

    // bottom sheet
    private var voucherEditCalendarBottomSheet: VoucherEditCalendarBottomSheet? = null
    private var repeatPeriodBottomSheet: SelectRepeatPeriodBottomSheet? = null
    private var voucherRecurringPeriodBottomSheet: VoucherPeriodBottomSheet? = null

    override fun getScreenName(): String =
        VoucherInformationFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentCreationVoucherInformationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(
            VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                voucherConfiguration
            )
        )
        setupTime()
        setupView()
        observeUiState()
        observeUiAction()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiAction.collect { action -> handleAction(action) }
        }
    }

    private fun handleUiState(state: VoucherCreationStepTwoUiState) {
        renderVoucherTargetSelection(state.voucherConfiguration.isVoucherPublic)
        renderVoucherNameValidation(state.isVoucherNameError, state.voucherNameErrorMsg)
        renderVoucherCodeValidation(
            state.voucherConfiguration,
            state.isVoucherCodeError,
            state.voucherCodeErrorMsg
        )
        renderVoucherRecurringToggleChanges(state.voucherConfiguration)
        renderVoucherStartPeriodSelection(
            state.voucherConfiguration,
            state.isStartDateError,
            state.startDateErrorMsg
        )
        renderVoucherEndPeriodSelection(
            state.voucherConfiguration,
            state.isEndDateError,
            state.endDateErrorMsg
        )
        renderVoucherRecurringPeriodSelection(state.voucherConfiguration)
        renderAvailableRecurringPeriod(state.voucherConfiguration, state.validationDate)
        renderButtonValidation(state.isInputValid())
    }

    private fun handleAction(action: VoucherCreationStepTwoAction) {
        when (action) {
            is VoucherCreationStepTwoAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepTwoAction.ContinueToNextStep -> TODO()
            is VoucherCreationStepTwoAction.ValidateVoucherNameInput -> TODO()
            is VoucherCreationStepTwoAction.ShowError -> TODO()
        }
    }

    private fun setupView() {
        binding?.run {
            viewVoucherTarget.setOnInflateListener { _, view ->
                voucherTargetSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherTargetSectionBinding.bind(view)
            }
            viewVoucherName.setOnInflateListener { _, view ->
                voucherNameSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherNameSectionBinding.bind(view)
            }
            viewVoucherCode.setOnInflateListener { _, view ->
                voucherCodeSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherCodeSectionBinding.bind(view)
            }
            viewVoucherActivePeriod.setOnInflateListener { _, view ->
                voucherPeriodSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding.bind(view)
            }
            viewButton.setOnInflateListener { _, view ->
                buttonSectionBinding =
                    SmvcVoucherCreationStepTwoButtonSectionBinding.bind(view)
            }
        }
        setupHeader()
        setupVoucherTargetSection()
        setupVoucherNameSection()
        setupVoucherCodeSection()
        setupVoucherPeriodSection()
        setupButtonSection()
    }

    private fun setupHeader() {
        binding?.header?.apply {
            headerSubTitle = if (voucherConfiguration.isVoucherProduct) {
                getString(R.string.smvc_creation_step_two_out_of_four_sub_title_label)
            } else {
                getString(R.string.smvc_creation_step_two_out_of_three_sub_title_label)
            }
            setNavigationOnClickListener {
                viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton)
            }
        }
    }

    private fun backToPreviousStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            VoucherTypeActivity.start(requireContext(), voucherConfiguration)
            activity?.finish()
        } else {
            // TODO: navigate back to summary page
        }
    }

    // Voucher target section
    private fun setupVoucherTargetSection() {
        binding?.run {
            if (viewVoucherTarget.parent != null) {
                viewVoucherTarget.inflate()
            }
        }
        setupVoucherTargetSelection()
    }

    private fun setupVoucherTargetSelection() {
        setVoucherTarget(voucherConfiguration.isVoucherPublic)
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PUBLIC_VOUCHER)
                cardParent?.setOnClickListener {
                    setVoucherTarget(true)
                }
            }
            viewVoucherTargetPrivate.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PRIVATE_VOUCHER)
                cardParent?.setOnClickListener {
                    setVoucherTarget(false)
                }
            }
        }
    }

    private fun setVoucherTarget(isPublic: Boolean) {
        viewModel.processEvent(VoucherCreationStepTwoEvent.ChooseVoucherTarget(isPublic))
    }

    private fun renderVoucherTargetSelection(isPublic: Boolean) {
        if (isPublic) {
            setVoucherPublicSelected()
        } else {
            setVoucherPrivateSelected()
        }
    }

    private fun setVoucherPublicSelected() {
        binding?.run {
            viewVoucherCode.gone()
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = true
            viewVoucherTargetPrivate.isActive = false
        }
    }

    private fun setVoucherPrivateSelected() {
        binding?.run {
            viewVoucherCode.visible()
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = false
            viewVoucherTargetPrivate.isActive = true
        }
    }

    // Voucher name section
    private fun setupVoucherNameSection() {
        binding?.run {
            if (viewVoucherName.parent != null) {
                viewVoucherName.inflate()
            }
        }

        voucherNameSectionBinding?.run {
            tfVoucherName.editText.setText(voucherConfiguration.voucherName)
            tfVoucherName.editText.textChangesAsFlow()
                .filterNot { it.isEmpty() }
                .debounce { DEBOUNCE }
                .distinctUntilChanged()
                .onEach {
                    val formattedText = it.uppercase(Locale.getDefault()).trim()
                    viewModel.processEvent(
                        VoucherCreationStepTwoEvent.OnVoucherNameChanged(formattedText)
                    )
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun renderVoucherNameValidation(
        isVoucherNameError: Boolean,
        voucherNameErrorMsg: String
    ) {
        voucherNameSectionBinding?.run {
            tfVoucherName.isInputError = isVoucherNameError
            tfVoucherName.setMessage(voucherNameErrorMsg)
        }
    }

    // Voucher code section
    private fun setupVoucherCodeSection() {
        binding?.run {
            if (viewVoucherCode.parent != null) {
                viewVoucherCode.inflate()
            }
        }

        voucherCodeSectionBinding?.run {
            tfVoucherCode.apply {
                prependText(voucherConfiguration.voucherCodePrefix)
                editText.setText(voucherConfiguration.voucherCode)
                editText.setToAllCapsMode()
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce { DEBOUNCE }
                    .distinctUntilChanged()
                    .onEach {
                        val formattedText = it.uppercase(Locale.getDefault()).trim()
                        viewModel.processEvent(
                            VoucherCreationStepTwoEvent.OnVoucherCodeChanged(formattedText)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun renderVoucherCodeValidation(
        voucherConfiguration: VoucherConfiguration,
        isVoucherCodeError: Boolean,
        voucherCodeErrorMsg: String
    ) {
        voucherCodeSectionBinding?.run {
            tfVoucherCode.editText.setText(voucherConfiguration.voucherCode)
            tfVoucherCode.isInputError = isVoucherCodeError
            tfVoucherCode.setMessage(voucherCodeErrorMsg)
        }
    }

    // Voucher period section
    private fun setupVoucherPeriodSection() {
        binding?.run {
            if (viewVoucherActivePeriod.parent != null) {
                viewVoucherActivePeriod.inflate()
            }
        }

        voucherPeriodSectionBinding?.run {
            tfVoucherStartPeriod.run {
                disableText(editText)
                editText.setOnClickListener { onClickListenerForStartDate() }
            }
            tfVoucherEndPeriod.run {
                disableText(editText)
                editText.setOnClickListener { onClickListenerForEndDate() }
            }
            cbRepeatPeriod.setOnCheckedChangeListener { _, isChecked ->
                viewModel.processEvent(
                    VoucherCreationStepTwoEvent.OnVoucherRecurringToggled(
                        isChecked
                    )
                )
            }

            tfRepeat.run {
                disableText(editText)
                editText.setOnClickListener { onClickListenerForRecurringPeriod() }
            }
        }
    }

    private fun onClickListenerForStartDate() {
        context?.run {
            startCalendar?.let { minDate ->
                DateTimeUtils.getMaxDate(startCalendar)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            startCalendar,
                            minDate,
                            maxDate,
                            startHour,
                            startMinute,
                            getSelectedDateStarting
                        )
                    voucherEditCalendarBottomSheet?.show(
                        childFragmentManager,
                        ""
                    )
                }
            }
        }
    }

    private fun onClickListenerForEndDate() {
        context?.run {
            startCalendar?.let { minDate ->
                DateTimeUtils.getMaxDate(startCalendar)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            endCalendar,
                            minDate,
                            maxDate,
                            startHour,
                            startMinute,
                            getSelectedDateEnding
                        )
                    voucherEditCalendarBottomSheet?.show(
                        childFragmentManager,
                        ""
                    )
                }
            }
        }
    }

    private fun onClickListenerForRecurringPeriod() {
        val currentVoucherConfig = viewModel.getCurrentVoucherConfiguration()
        repeatPeriodBottomSheet = SelectRepeatPeriodBottomSheet.newInstance(
            currentVoucherConfig.totalPeriod,
            isShowTicker = false
        )
        repeatPeriodBottomSheet?.show(
            childFragmentManager,
            getSelectedRecurringPeriod
        )
    }

    private fun setupTime() {
        val voucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        val startDate =
            voucherConfiguration.startPeriod.formatTo(DATE_WITH_SECOND_PRECISION_ISO_8601)
        val endDate = voucherConfiguration.endPeriod.formatTo(DATE_WITH_SECOND_PRECISION_ISO_8601)
        startCalendar = getGregorianDate(startDate)
        endCalendar = getGregorianDate(endDate)
    }

    private fun renderVoucherRecurringToggleChanges(voucherConfiguration: VoucherConfiguration) {
        voucherPeriodSectionBinding?.run {
            tfRepeat.isVisible = voucherConfiguration.isPeriod
        }
    }

    private fun renderVoucherStartPeriodSelection(
        voucherConfiguration: VoucherConfiguration,
        isStartDateError: Boolean,
        startDateErrorMsg: String
    ) {
        voucherPeriodSectionBinding?.run {
            tfVoucherStartPeriod.run {
                isInputError = isStartDateError
                setMessage(startDateErrorMsg)
                editText.setText(
                    voucherConfiguration.startPeriod.formatTo(
                        DATE_TIME_MINUTE_PRECISION
                    )
                )
            }
        }
        setupTime()
    }

    private fun renderVoucherEndPeriodSelection(
        voucherConfiguration: VoucherConfiguration,
        isEndDateError: Boolean,
        endDateErrorMsg: String
    ) {
        voucherPeriodSectionBinding?.run {
            tfVoucherEndPeriod.run {
                isInputError = isEndDateError
                setMessage(endDateErrorMsg)
                editText.setText(voucherConfiguration.endPeriod.formatTo(DATE_TIME_MINUTE_PRECISION))
            }
        }
        setupTime()
    }

    private fun renderVoucherRecurringPeriodSelection(voucherConfiguration: VoucherConfiguration) {
        voucherPeriodSectionBinding?.run {
            tfRepeat.run {
                editText.setText(
                    getString(
                        R.string.smvc_recurring_period_placeholder_value,
                        voucherConfiguration.totalPeriod
                    )
                )
            }
        }
    }

    private fun renderAvailableRecurringPeriod(
        voucherConfiguration: VoucherConfiguration,
        validationDate: List<VoucherValidationResult.ValidationDate>
    ) {
        voucherPeriodSectionBinding?.run {
            val availableDate = validationDate.filter { it.available }
            val unAvailableDate = validationDate.filter { !it.available }
            if (availableDate.isNotEmpty()) {
                recurringPeriodView.run {
                    type = RecurringDateScheduleCustomView.TYPE_NORMAL
                    title = getString(R.string.smvc_voucher_active_label)
                    isVisible = voucherConfiguration.isPeriod

                    val firstPeriodStartDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        availableDate.first().dateStart
                    )
                    val firstPeriodStartHour = availableDate.first().hourStart
                    val firstPeriodEndDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        availableDate.first().dateEnd
                    )
                    val firstPeriodEndHour = availableDate.first().hourEnd
                    firstSchedule = getString(
                        R.string.smvc_recurring_date_palceholder_value,
                        "$firstPeriodStartDate, $firstPeriodStartHour - $firstPeriodEndDate, $firstPeriodEndHour"
                    )

                    val secondPeriodStartDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        availableDate[Int.ONE].dateStart
                    )
                    val secondPeriodStartHour = availableDate[Int.ONE].hourStart
                    val secondPeriodEndDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        availableDate[Int.ONE].dateEnd
                    )
                    val secondPeriodEndHour = availableDate[Int.ONE].hourEnd
                    secondSchedule = getString(
                        R.string.smvc_recurring_date_palceholder_value,
                        "$secondPeriodStartDate, $secondPeriodStartHour - $secondPeriodEndDate, $secondPeriodEndHour"
                    )

                    isShowOtherScheduleButton =
                        availableDate.size > minimumActivePeriodCountToShowFullSchedule

                    btnSeeOtherSchedule?.setOnClickListener {
                        onClickSeeOtherRecurringPeriod(
                            getString(R.string.smvc_voucher_active_label),
                            validationDate
                        )
                    }
                }
            }
        }
    }

    private fun onClickSeeOtherRecurringPeriod(
        title: String,
        validationDate: List<VoucherValidationResult.ValidationDate>
    ) {
        val voucherRecurringData = viewModel.mapVoucherRecurringPeriodData(validationDate)
        voucherRecurringPeriodBottomSheet =
            VoucherPeriodBottomSheet.newInstance(title, voucherRecurringData)
        voucherRecurringPeriodBottomSheet?.show(childFragmentManager, "")
    }

    private fun disableText(autoCompleteTextView: AutoCompleteTextView) {
        autoCompleteTextView.apply {
            isFocusable = false
            isClickable = true
            keyListener = null
        }
    }

    private fun getGregorianDate(date: String): GregorianCalendar {
        return GregorianCalendar().apply {
            time = date.convertUnsafeDateTime()
        }
    }

    // Button section
    private fun setupButtonSection() {
        binding?.run {
            if (viewButton.parent != null) {
                viewButton.inflate()
            }
        }

        buttonSectionBinding?.run {
            btnBack.setOnClickListener { viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton) }
        }
    }

    private fun renderButtonValidation(isEnabled: Boolean) {
        buttonSectionBinding?.run {
            btnContinue.apply {
                this.isEnabled = isEnabled
                setOnClickListener { viewModel.processEvent(VoucherCreationStepTwoEvent.NavigateToNextStep) }
            }
        }
    }
}
