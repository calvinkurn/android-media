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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_TIME_MINUTE_PRECISION
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_YEAR_PRECISION
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.textChangesAsFlow
import com.tokopedia.kotlin.extensions.view.visible
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
import com.tokopedia.mvc.domain.entity.enums.UnavailableRecurringDateErrorType.NEW_FOLLOWER_VOUCHER_ALREADY_EXIST
import com.tokopedia.mvc.domain.entity.enums.UnavailableRecurringDateErrorType.SAME_DATE_VOUCHER_ALREADY_EXIST
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepTwoFieldValidation
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.ForbiddenWordsBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.SelectRepeatPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.editperiod.VoucherEditCalendarBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.VoucherPeriodBottomSheet
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeActivity
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.mvc.presentation.creation.step3.VoucherSettingActivity
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.DateTimeUtils
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.mvc.util.convertUnsafeDateTime
import com.tokopedia.mvc.util.decideCalendarPeriodEndDate
import com.tokopedia.mvc.util.decideCalendarPeriodStartDate
import com.tokopedia.mvc.util.extension.setToAllCapsMode
import com.tokopedia.mvc.util.tracker.VoucherInfoTracker
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private var minCalendar: GregorianCalendar? = null
    private var maxCalendar: GregorianCalendar? = null
    private var startHour: Int = 0
    private var startMinute: Int = 0

    private var getSelectedDateStarting: (Calendar) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherStartDateChanged(it))
        tracker.sendClickFieldDatePickerMulaiEvent()
    }

    private var getSelectedDateEnding: (Calendar) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherEndDateChanged(it))
        tracker.sendClickFieldDatePickerBerakhirEvent()
    }

    private var getSelectedRecurringPeriod: (Int) -> Unit = {
        viewModel.processEvent(VoucherCreationStepTwoEvent.OnVoucherRecurringPeriodSelected(it))
        tracker.sendClickDropdownEvent(it.toString())
    }

    // bottom sheet
    private var voucherEditCalendarBottomSheet: VoucherEditCalendarBottomSheet? = null
    private var repeatPeriodBottomSheet: SelectRepeatPeriodBottomSheet? = null
    private var voucherRecurringPeriodBottomSheet: VoucherPeriodBottomSheet? = null
    private var forbiddenWordsBottomSheet: ForbiddenWordsBottomSheet? = null

    // coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
    }

    // tracker
    @Inject
    lateinit var tracker: VoucherInfoTracker

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
                pageMode ?: PageMode.CREATE,
                voucherConfiguration
            )
        )
        setupTime()
        setupView()
        observeUiState()
        observeUiAction()
        viewModel.processEvent(VoucherCreationStepTwoEvent.HandleCoachMark)
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
        when (state.fieldValidated) {
            VoucherCreationStepTwoFieldValidation.VOUCHER_TARGET -> {
                renderVoucherTargetSelection(state.voucherConfiguration.isVoucherPublic)
            }
            VoucherCreationStepTwoFieldValidation.VOUCHER_NAME -> {
                renderVoucherNameValidation(state.isVoucherNameError, state.voucherNameErrorMsg)
            }
            VoucherCreationStepTwoFieldValidation.VOUCHER_CODE -> {
                renderVoucherCodeValidation(state.isVoucherCodeError, state.voucherCodeErrorMsg)
            }
            VoucherCreationStepTwoFieldValidation.VOUCHER_START_DATE -> {
                renderVoucherStartPeriodSelection(
                    state.voucherConfiguration,
                    state.isStartDateError,
                    state.startDateErrorMsg
                )
            }
            VoucherCreationStepTwoFieldValidation.VOUCHER_END_DATE -> {
                renderVoucherEndPeriodSelection(
                    state.voucherConfiguration,
                    state.isEndDateError,
                    state.endDateErrorMsg
                )
            }
            VoucherCreationStepTwoFieldValidation.ALL -> {
                renderVoucherTargetSelection(state.voucherConfiguration.isVoucherPublic)
                renderVoucherNameValidation(state.isVoucherNameError, state.voucherNameErrorMsg)
                renderVoucherCodeValidation(state.isVoucherCodeError, state.voucherCodeErrorMsg)
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
                renderVoucherRecurringToggleChanges(state.voucherConfiguration)
                renderVoucherRecurringPeriodSelection(state.voucherConfiguration)
                renderAvailableRecurringPeriod(state.validationDate)
            }
        }
        renderButtonValidation(
            state.voucherConfiguration,
            state.isInputValid(),
            state.validationDate
        )
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
    }

    private fun handleAction(action: VoucherCreationStepTwoAction) {
        when (action) {
            is VoucherCreationStepTwoAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepTwoAction.NavigateToNextStep -> navigateToNextStep(
                action.pageMode,
                action.voucherConfiguration
            )
            is VoucherCreationStepTwoAction.ShowError -> TODO()
            is VoucherCreationStepTwoAction.ShowCoachmark -> showCoachmark()
        }
    }

    private fun showCoachmark() {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        voucherPeriodSectionBinding?.run {
            coachMarkItem.add(
                CoachMark2Item(
                    cbRepeatPeriod,
                    getString(R.string.smvc_voucher_creation_step_two_coachmark_title),
                    getString(R.string.smvc_voucher_creation_step_two_coachmark_description),
                    CoachMark2.POSITION_TOP
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.onDismissListener = { viewModel.setSharedPrefCoachMarkAlreadyShown() }
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
        presetValue()
        hideTextFieldLabel()
    }

    private fun presetValue() {
        setVoucherTarget(voucherConfiguration.isVoucherPublic)
        voucherNameSectionBinding?.tfVoucherName?.run {
            if (voucherConfiguration.voucherName.isNotEmpty()) editText.setText(voucherConfiguration.voucherName)
        }
        voucherCodeSectionBinding?.tfVoucherCode?.run {
            if (voucherConfiguration.voucherCode.isNotEmpty()) editText.setText(voucherConfiguration.voucherCode)
        }
        if (pageMode == PageMode.EDIT) {
            voucherCodeSectionBinding?.run {
                tfVoucherCode.disable()
            }
            voucherPeriodSectionBinding?.run {
                cbRepeatPeriod.disable()
            }
        }
        voucherPeriodSectionBinding?.run {
            tfVoucherStartPeriod.editText.setText(
                voucherConfiguration.startPeriod.formatTo(
                    DATE_TIME_MINUTE_PRECISION
                )
            )
            tfVoucherEndPeriod.editText.setText(
                voucherConfiguration.endPeriod.formatTo(
                    DATE_TIME_MINUTE_PRECISION
                )
            )
        }
    }

    private fun hideTextFieldLabel() {
        voucherNameSectionBinding?.tfVoucherName?.labelText?.gone()
        voucherCodeSectionBinding?.tfVoucherCode?.labelText?.gone()
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
                tracker.sendClickKembaliArrowEvent()
            }
        }
    }

    // Voucher target region
    private fun setupVoucherTargetSection() {
        binding?.run {
            if (viewVoucherTarget.parent != null) {
                viewVoucherTarget.inflate()
            }
        }
        setupVoucherTargetSelection()
    }

    private fun setupVoucherTargetSelection() {
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PUBLIC_VOUCHER)
                cardParent?.setOnClickListener {
                    if (pageMode == PageMode.CREATE) setVoucherTarget(true)
                }
            }
            viewVoucherTargetPrivate.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PRIVATE_VOUCHER)
                cardParent?.setOnClickListener {
                    if (pageMode == PageMode.CREATE) setVoucherTarget(false)
                }
            }
        }
    }

    private fun setVoucherTarget(isPublic: Boolean) {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        if (!isPublic && currentVoucherConfiguration.targetBuyer == VoucherTargetBuyer.NEW_FOLLOWER) {
            showChangeTargetConfirmationDialog(isPublic)
        } else {
            viewModel.processEvent(VoucherCreationStepTwoEvent.ChooseVoucherTarget(isPublic))
            tracker.sendClickTargetKuponEvent(isPublic)
        }
    }

    private fun showChangeTargetConfirmationDialog(isPublic: Boolean) {
        val dialog = context?.let { ctx ->
            DialogUnify(
                ctx,
                DialogUnify.HORIZONTAL_ACTION,
                DialogUnify.NO_IMAGE
            )
        }
        dialog?.apply {
            setTitle(getString(R.string.smvc_change_voucher_target_confirmation_label))
            setDescription(
                MethodChecker.fromHtml(
                    getString(R.string.smvc_change_target_voucher_confirmation_description)
                )
            )
            setPrimaryCTAText(
                getString(R.string.smvc_voucher_target_confirmation_primary_cta_label)
            )
            setSecondaryCTAText(
                getString(R.string.smvc_voucher_target_confirmation_secondary_cta_label)
            )
            setPrimaryCTAClickListener {
                viewModel.processEvent(
                    VoucherCreationStepTwoEvent.ChooseVoucherTarget(
                        isPublic,
                        true
                    )
                )
                tracker.sendClickTargetKuponEvent(isPublic)
                dismiss()
            }
            setSecondaryCTAClickListener { dismiss() }
        }?.show()
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
            voucherCodeSectionBinding?.apply {
                tpgVoucherCodeTitle.gone()
                tfVoucherCode.gone()
            }
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = true
            viewVoucherTargetPrivate.isActive = false
            if (pageMode == PageMode.EDIT) viewVoucherTargetPrivate.disable()
        }
    }

    private fun setVoucherPrivateSelected() {
        binding?.run {
            voucherCodeSectionBinding?.apply {
                tpgVoucherCodeTitle.visible()
                tfVoucherCode.visible()
            }
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = false
            viewVoucherTargetPrivate.isActive = true
            if (pageMode == PageMode.EDIT) viewVoucherTargetPublic.disable()
        }
    }

    // Voucher name region
    private fun setupVoucherNameSection() {
        binding?.run {
            if (viewVoucherName.parent != null) {
                viewVoucherName.inflate()
            }
        }

        voucherNameSectionBinding?.run {
            tfVoucherName.editText.setOnFocusChangeListener { _, isFocus ->
                if (isFocus) tracker.sendClickFieldNamaKuponEvent()
            }
            tfVoucherName.editText.textChangesAsFlow()
                .debounce { DEBOUNCE }
                .distinctUntilChanged()
                .onEach {
                    viewModel.processEvent(
                        VoucherCreationStepTwoEvent.OnVoucherNameChanged(it)
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
            tpgPelajari.apply {
                isVisible = voucherNameErrorMsg.contains(
                    getString(R.string.smvc_larang_label),
                    ignoreCase = true
                )
                setOnClickListener { showForbiddenWordsBottomSheet() }
            }
        }
    }

    // Voucher code region
    private fun setupVoucherCodeSection() {
        binding?.run {
            if (viewVoucherCode.parent != null) {
                viewVoucherCode.inflate()
            }
        }

        voucherCodeSectionBinding?.run {
            tfVoucherCode.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldKodeKuponEvent()
                }
                prependText(voucherConfiguration.voucherCodePrefix)
                editText.setToAllCapsMode()
                editText.textChangesAsFlow()
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
        isVoucherCodeError: Boolean,
        voucherCodeErrorMsg: String
    ) {
        voucherCodeSectionBinding?.run {
            tfVoucherCode.isInputError = isVoucherCodeError
            tfVoucherCode.setMessage(voucherCodeErrorMsg)
            tpgPelajari.apply {
                isVisible = voucherCodeErrorMsg.contains(
                    getString(R.string.smvc_larang_label),
                    ignoreCase = true
                )
                setOnClickListener { showForbiddenWordsBottomSheet() }
            }
        }
    }

    private fun showForbiddenWordsBottomSheet() {
        forbiddenWordsBottomSheet = ForbiddenWordsBottomSheet()
        forbiddenWordsBottomSheet?.show(childFragmentManager)
    }

    // Voucher period region
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
                tracker.sendClickCheckBoxEvent()
            }

            tfRepeat.run {
                isVisible = voucherConfiguration.isPeriod
                disableText(editText)
                editText.setOnClickListener { onClickListenerForRecurringPeriod() }
            }
            iconChevronDown.isVisible = voucherConfiguration.isPeriod
        }
    }

    private fun onClickListenerForStartDate() {
        context?.run {
            decideCalendarPeriodStartDate(this, startCalendar)?.let { minDate ->
                DateTimeUtils.getMaxDate(minDate)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            startCalendar,
                            minDate,
                            maxDate,
                            startHour,
                            startMinute,
                            getSelectedDateStarting,
                            isFromVoucherCreation = true
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
            minCalendar?.let { minDate ->
                DateTimeUtils.getMaxDate(minCalendar)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            decideCalendarPeriodEndDate(minCalendar, maxCalendar),
                            minDate,
                            maxDate,
                            startHour,
                            startMinute,
                            getSelectedDateEnding,
                            isFromVoucherCreation = true
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
        minCalendar = getGregorianDate(startDate)
        maxCalendar = getGregorianDate(endDate)
    }

    private fun renderVoucherRecurringToggleChanges(voucherConfiguration: VoucherConfiguration) {
        voucherPeriodSectionBinding?.run {
            tfRepeat.isVisible = voucherConfiguration.isPeriod
            iconChevronDown.isVisible = voucherConfiguration.isPeriod
        }
    }

    private fun renderVoucherStartPeriodSelection(
        voucherConfiguration: VoucherConfiguration,
        isStartDateError: Boolean,
        startDateErrorMsg: String
    ) {
        voucherPeriodSectionBinding?.run {
            tfVoucherStartPeriod.run {
                try {
                    isInputError = isStartDateError
                    setMessage(startDateErrorMsg)
                    editText.setText(
                        voucherConfiguration.startPeriod.formatTo(
                            DATE_TIME_MINUTE_PRECISION
                        )
                    )
                    val startDate =
                        voucherConfiguration.startPeriod.formatTo(
                            DATE_WITH_SECOND_PRECISION_ISO_8601
                        )
                    startCalendar = getGregorianDate(startDate)
                } catch (_: Throwable) {
                }
            }
        }
    }

    private fun renderVoucherEndPeriodSelection(
        voucherConfiguration: VoucherConfiguration,
        isEndDateError: Boolean,
        endDateErrorMsg: String
    ) {
        voucherPeriodSectionBinding?.run {
            tfVoucherEndPeriod.run {
                try {
                    isInputError = isEndDateError
                    setMessage(endDateErrorMsg)
                    editText.setText(
                        voucherConfiguration.endPeriod.formatTo(DATE_TIME_MINUTE_PRECISION)
                    )

                    val endDate = voucherConfiguration.endPeriod.formatTo(
                        DATE_WITH_SECOND_PRECISION_ISO_8601
                    )
                    endCalendar = getGregorianDate(endDate)
                } catch (_: Throwable) {
                }
            }
        }
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
        validationDate: List<VoucherValidationResult.ValidationDate>
    ) {
        voucherPeriodSectionBinding?.run {
            val availableDate = validationDate.filter { it.available }
            val unAvailableDate = validationDate.filter { !it.available }
            if (availableDate.isNotEmpty()) {
                recurringPeriodView.run {
                    type = RecurringDateScheduleCustomView.TYPE_NORMAL
                    title = getString(R.string.smvc_voucher_active_label)
                    isVisible = tfRepeat.isVisible

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
                        firstPeriodStartDate,
                        firstPeriodStartHour,
                        firstPeriodEndDate,
                        firstPeriodEndHour
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
                        secondPeriodStartDate,
                        secondPeriodStartHour,
                        secondPeriodEndDate,
                        secondPeriodEndHour
                    )

                    isShowOtherScheduleButton =
                        availableDate.size > minimumActivePeriodCountToShowFullSchedule

                    btnSeeOtherSchedule?.setOnClickListener {
                        onClickSeeOtherRecurringPeriod(
                            getString(R.string.smvc_voucher_active_label),
                            availableDate
                        )
                    }
                }
            }

            if (unAvailableDate.isNotEmpty()) {
                unavailableRecurringPeriodView.run {
                    type = RecurringDateScheduleCustomView.TYPE_ERROR
                    title = when (unAvailableDate.first().type) {
                        SAME_DATE_VOUCHER_ALREADY_EXIST.type -> getString(
                            R.string.smvc_recurring_date_error_type_1_title
                        )
                        NEW_FOLLOWER_VOUCHER_ALREADY_EXIST.type -> getString(
                            R.string.smvc_recurring_date_error_type_2_title
                        )
                        else -> getString(R.string.smvc_recurring_date_error_type_3_title)
                    }
                    isVisible = tfRepeat.isVisible

                    val firstPeriodStartDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        unAvailableDate.first().dateStart
                    )
                    val firstPeriodStartHour = unAvailableDate.first().hourStart
                    val firstPeriodEndDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        unAvailableDate.first().dateEnd
                    )
                    val firstPeriodEndHour = unAvailableDate.first().hourEnd
                    firstSchedule = getString(
                        R.string.smvc_recurring_date_palceholder_value,
                        firstPeriodStartDate,
                        firstPeriodStartHour,
                        firstPeriodEndDate,
                        firstPeriodEndHour
                    )

                    val secondPeriodStartDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        unAvailableDate[Int.ONE].dateStart
                    )
                    val secondPeriodStartHour = unAvailableDate[Int.ONE].hourStart
                    val secondPeriodEndDate = DateUtil.formatDate(
                        DateConstant.DATE_MONTH_YEAR_BASIC,
                        DATE_YEAR_PRECISION,
                        unAvailableDate[Int.ONE].dateEnd
                    )
                    val secondPeriodEndHour = unAvailableDate[Int.ONE].hourEnd
                    secondSchedule = getString(
                        R.string.smvc_recurring_date_palceholder_value,
                        secondPeriodStartDate,
                        secondPeriodStartHour,
                        secondPeriodEndDate,
                        secondPeriodEndHour
                    )

                    isShowOtherScheduleButton =
                        unAvailableDate.size > minimumActivePeriodCountToShowFullSchedule

                    btnSeeOtherSchedule?.setOnClickListener {
                        onClickSeeOtherRecurringPeriod(
                            getString(R.string.smvc_voucher_unavailable_label),
                            unAvailableDate
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

    // Button region
    private fun setupButtonSection() {
        binding?.run {
            if (viewButton.parent != null) {
                viewButton.inflate()
            }
        }

        buttonSectionBinding?.run {
            btnBack.setOnClickListener {
                viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton)
                tracker.sendClickKembaliButtonEvent()
            }
            btnContinue.text = if (pageMode == PageMode.CREATE) {
                getString(R.string.smvc_continue)
            } else {
                getText(R.string.smvc_save)
            }
        }
    }

    private fun backToPreviousStep(currentVoucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            if (voucherConfiguration.isFinishedFillAllStep()) {
                navigateToVoucherSummaryPage(voucherConfiguration)
            } else {
                navigateToVoucherTypePage(currentVoucherConfiguration)
            }
        } else {
            navigateToVoucherSummaryPage(currentVoucherConfiguration)
        }
    }

    private fun navigateToNextStep(
        pageMode: PageMode,
        voucherConfiguration: VoucherConfiguration
    ) {
        if (pageMode == PageMode.CREATE) {
            if (voucherConfiguration.isFinishedFillAllStep()) {
                navigateToVoucherSummaryPage(voucherConfiguration)
            } else {
                navigateToVoucherSettingPage(pageMode, voucherConfiguration)
            }
        } else {
            navigateToVoucherSummaryPage(voucherConfiguration)
        }
    }

    private fun navigateToVoucherTypePage(currentVoucherConfiguration: VoucherConfiguration) {
        if (this.pageMode == PageMode.CREATE) {
            context?.let { ctx ->
                VoucherTypeActivity.buildCreateModeIntent(
                    ctx,
                    currentVoucherConfiguration
                )
            }
            activity?.finish()
        } else {
            context?.let { ctx ->
                VoucherTypeActivity.buildEditModeIntent(
                    ctx,
                    currentVoucherConfiguration
                )
            }
            activity?.finish()
        }
    }

    private fun navigateToVoucherSettingPage(
        pageMode: PageMode,
        currentVoucherConfiguration: VoucherConfiguration
    ) {
        if (pageMode == PageMode.CREATE) {
            context?.let { ctx ->
                VoucherSettingActivity.buildCreateModeIntent(
                    ctx,
                    currentVoucherConfiguration
                )
            }
            activity?.finish()
        } else {
            context?.let { ctx ->
                VoucherSettingActivity.buildEditModeIntent(
                    ctx,
                    currentVoucherConfiguration
                )
            }
            activity?.finish()
        }
    }

    private fun navigateToVoucherSummaryPage(currentVoucherConfiguration: VoucherConfiguration) {
        context?.let { ctx ->
            SummaryActivity.start(
                ctx,
                currentVoucherConfiguration
            )
        }
    }

    private fun showCreateVoucherConfirmationDialog(voucherConfiguration: VoucherConfiguration) {
        val dialog = context?.let { ctx ->
            DialogUnify(
                ctx,
                DialogUnify.HORIZONTAL_ACTION,
                DialogUnify.NO_IMAGE
            )
        }
        dialog?.apply {
            setTitle(getString(R.string.smvc_create_voucher_confirmation_title))
            setDescription(getString(R.string.smvc_create_voucher_confirmation_description))
            setPrimaryCTAText(
                getString(R.string.smvc_create_voucher_confirmation_primary_cta_label)
            )
            setSecondaryCTAText(
                getString(R.string.smvc_create_voucher_confirmation_secondary_cta_label)
            )
            setPrimaryCTAClickListener {
                viewModel.processEvent(
                    VoucherCreationStepTwoEvent.NavigateToNextStep(
                        voucherConfiguration
                    )
                )
                dismiss()
            }
            setSecondaryCTAClickListener { dismiss() }
        }?.show()
    }

    private fun renderButtonValidation(
        voucherConfiguration: VoucherConfiguration,
        isEnabled: Boolean,
        validationDate: List<VoucherValidationResult.ValidationDate>
    ) {
        buttonSectionBinding?.run {
            btnContinue.apply {
                val unAvailableDate = validationDate.filter { !it.available }
                this.isEnabled = isEnabled
                if (unAvailableDate.isNotEmpty()) {
                    showCreateVoucherConfirmationDialog(voucherConfiguration)
                } else {
                    setOnClickListener {
                        viewModel.processEvent(
                            VoucherCreationStepTwoEvent.NavigateToNextStep(
                                voucherConfiguration
                            )
                        )
                        tracker.sendClickLanjutEvent()
                    }
                }
            }
        }
    }
}
