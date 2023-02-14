package com.tokopedia.topads.debit.autotopup.view.sheet

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.common.extension.EIGHT
import com.tokopedia.topads.common.extension.ZERO
import com.tokopedia.topads.common.extension.createListOfSize
import com.tokopedia.topads.common.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.DEFAULT_TOP_UP_FREQUENCY
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.INVALID_NOMINAL_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.IS_EDIT_TOP_UP
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENCY_EIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENCY_FOUR
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.getTextFromFrequency
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAktifkanTambahKreditManualDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAktifkanTambahKreditOtomatisDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAktifkanTambahKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAmountOfAutoTopupDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAmountOfAutoTopupEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickAmountOfManualTopupDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickBatalPerubahanPengaturanKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickChecklistTncKreditOtomatisDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickClosePopupKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickInfoFrekuensiTambahKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickInfoMinimalTresholdKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickInformasiKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickPilihTambahKreditManualEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickPilihTambahKreditOtomatisEvent
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickUbahPengaturanKreditOtomatisDiModalTambahKreditEvent
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class TopAdsChooseCreditBottomSheet :
    BottomSheetUnify(),
    TopAdsCreditListAdapter.NominalClickListener {

    private var creditItemRecyclerView: RecyclerView? = null
    private var textPilihNominal: com.tokopedia.unifyprinciples.Typography? = null
    private var topCreditSheetScrollView: NestedScrollView? = null
    private var manualRadioButton: RadioButtonUnify? = null
    private var autoRadioButton: RadioButtonUnify? = null
    private var radioGroupAutoTopUpFrequency: RadioGroup? = null
    private var topUpSwitchRadioButtonGroup: RadioGroup? = null
    private var jarangRadioButton: RadioButtonUnify? = null
    private var normalRadioButton: RadioButtonUnify? = null
    private var seringRadioButton: RadioButtonUnify? = null
    private var cancelButton: UnifyButton? = null
    private var saveButton: UnifyButton? = null
    private var applyButton: UnifyButton? = null
    private var groupFrequencyAutoTopUp: Group? = null
    private var groupAutoTopUpCreditHistory: Group? = null
    private var autoTopUpCreditTips: TipsUnify? = null
    private var tncAutoCreditCheckBox: CheckboxUnify? = null
    private var autoTopUpActiveLabel: Label? = null
    private var autoRadioButtonTextDescription: com.tokopedia.unifyprinciples.Typography? = null
    private var selectedPrice: com.tokopedia.unifyprinciples.Typography? = null
    private var selectedBonus: com.tokopedia.unifyprinciples.Typography? = null
    private var maxCreditLimit: com.tokopedia.unifyprinciples.Typography? = null
    private var selectedFrequncyTambahCredit: com.tokopedia.unifyprinciples.Typography? = null
    private var creditHistoryShimmer: LoaderUnify? = null
    private var toolTipAuto: ImageUnify? = null
    private var toolTipFrequency: ImageUnify? = null
    private var toolTipMaxCreditLimit: ImageUnify? = null
    private var toolTipFrequencyHistory: ImageUnify? = null

    private var autoTopUpCreditHistoryTriple: Triple<String, String, Pair<String, Int>>? = null
    private var selectedNominal: TopUpCreditItemData? = null
    private var selectedNominalIndex: Int = INVALID_NOMINAL_INDEX
    private var creditResponse: CreditResponse? = null
    private var manualNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpAvailableNominalList: MutableList<AutoTopUpItem> = mutableListOf()
    private var autoTopUpMaxCreditLimit: Long = Long.ZERO
    private var autoTopUpFrequencySelected: Int = DEFAULT_TOP_UP_FREQUENCY
    var isAutoTopUpActive: Boolean = false
    var isAutoTopUpSelected: Boolean = false
    var isShowEditHistory: Boolean = false
    var isFromEditAutoTopUp: Boolean = false
    private var isCreditHistoryReceived: Boolean = false
    var onSaved: ((isAutoAdsSaved: Boolean) -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    private val adapter by lazy { TopAdsCreditListAdapter() }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    companion object {
        fun newInstance() = TopAdsChooseCreditBottomSheet()
    }

    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            ).get(TopAdsAutoTopUpViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_dash_choose_credit_sheet, null)
        setDefaultConfigs()
        setChild(contentView)
        initView(contentView)
    }

    private fun setDefaultConfigs() {
        clearContentPadding = true
        isDragable = true
        showCloseIcon = true
        setTitle(getString(R.string.title_top_ads_add_credit))
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun initView(contentView: View?) {
        topCreditSheetScrollView = contentView?.findViewById(R.id.topCreditSheetScrollView)
        manualRadioButton = contentView?.findViewById(R.id.manualRadioButton)
        autoRadioButton = contentView?.findViewById(R.id.autoRadioButton)
        creditItemRecyclerView = contentView?.findViewById(R.id.creditItemRecyclerView)
        saveButton = contentView?.findViewById(R.id.saveButton)
        cancelButton = contentView?.findViewById(R.id.cancelButton)
        applyButton = contentView?.findViewById(R.id.applyButton)
        groupFrequencyAutoTopUp = contentView?.findViewById(R.id.groupFrequencyAutoTopUp)
        groupAutoTopUpCreditHistory = contentView?.findViewById(R.id.autoTopUpCreditHistory)
        autoTopUpCreditTips = contentView?.findViewById(R.id.autoTopUpCreditTips)
        jarangRadioButton = contentView?.findViewById(R.id.jarangRadioButton)
        normalRadioButton = contentView?.findViewById(R.id.normalRadioButton)
        seringRadioButton = contentView?.findViewById(R.id.seringRadioButton)
        radioGroupAutoTopUpFrequency = contentView?.findViewById(R.id.radioGroupAutoTopUpFrequency)
        topUpSwitchRadioButtonGroup = contentView?.findViewById(R.id.topUpSwitchRadioButtonGroup)
        tncAutoCreditCheckBox = contentView?.findViewById(R.id.tncAutoCreditCheckBox)
        autoTopUpActiveLabel = contentView?.findViewById(R.id.autoTopUpActiveLabel)
        autoRadioButtonTextDescription =
            contentView?.findViewById(R.id.autoRadioButtonTextDescription)
        selectedPrice = contentView?.findViewById(R.id.selectedPrice)
        selectedBonus = contentView?.findViewById(R.id.selectedBonus)
        maxCreditLimit = contentView?.findViewById(R.id.maxCreditLimit)
        textPilihNominal = contentView?.findViewById(R.id.textPilihNominal)
        selectedFrequncyTambahCredit = contentView?.findViewById(R.id.selectedFrequncyTambahCredit)
        creditHistoryShimmer = contentView?.findViewById(R.id.creditHistoryShimmer)
        toolTipAuto = contentView?.findViewById(R.id.toolTipAuto)
        toolTipFrequency = contentView?.findViewById(R.id.toolTipFrequency)
        toolTipFrequencyHistory = contentView?.findViewById(R.id.toolTipFrequencyHistory)
        toolTipMaxCreditLimit = contentView?.findViewById(R.id.toolTipMaxCreditLimit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitialData()
        checkRadioDefaultTopUpType()
        setAutoRadioButtonLabel()
        manageAutoTopUpActiveState()
        setObserver()
        setUpRecyclerView()
        setDefaultState()
        setUpClickListener()
        setUpCheckChangeListener()
        setOnDismissListener {
            if (!isAutoTopUpActive) onCancel?.invoke()
            if (activity is TopAdsCreditTopUpActivity) activity?.finish()
        }
        setCloseClickListener {
            sendClickClosePopupKreditOtomatisEvent()
        }
    }

    private fun checkRadioDefaultTopUpType() {
        if (isAutoTopUpSelected) {
            autoRadioButton?.isChecked =
                true
        } else {
            manualRadioButton?.isChecked = true
        }
    }

    private fun setAutoRadioButtonLabel() {
        autoTopUpActiveLabel?.visibleWithCondition(isAutoTopUpActive)
        autoRadioButtonTextDescription?.visibleWithCondition(!isAutoTopUpActive)
    }

    private fun setDefaultState() {
        addNominalList(manualNominalList)
        changeButtonState(true)
        if (manualRadioButton?.isChecked == true) {
            changeToManualState()
        } else if (autoRadioButton?.isChecked == true) {
            changeToAutoState()
        }
    }

    private fun setUpRecyclerView() {
        adapter.setNominalClickListener(this)
        creditItemRecyclerView?.adapter = adapter
        creditItemRecyclerView?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun setUpClickListener() {
        saveButton?.setOnClickListener {
            if (it.tag == IS_EDIT_TOP_UP) {
                sendClickUbahPengaturanKreditOtomatisDiModalTambahKreditEvent()
                enableEditAutoTopUpState()
            } else if (manualRadioButton?.isChecked == true) {
                sendClickAktifkanTambahKreditManualDiModalTambahKreditEvent(
                    selectedNominal?.productPrice ?: ""
                )
                openManualAdsCreditWebView()
            } else {
                sendClickAktifkanTambahKreditOtomatisDiModalTambahKreditEvent(
                    "${selectedNominal?.productPrice}, ${
                    getTextFromFrequency(
                        context,
                        autoTopUpFrequencySelected
                    )
                    }"
                )
                saveAutoTopUp()
            }
        }

        cancelButton?.setOnClickListener {
            sendClickBatalPerubahanPengaturanKreditOtomatisEvent()
            resetToCreditHistoryState()
        }

        applyButton?.setOnClickListener {
            sendClickAktifkanTambahKreditOtomatisEvent()
            saveAutoTopUp()
        }

        toolTipAuto?.setOnClickListener {
            sendClickInformasiKreditOtomatisEvent()
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(context?.getString(R.string.label_topads_automatic_topup) ?: "")
                it.setDescription(
                    context?.getString(R.string.top_ads_tool_tip_auto_description) ?: ""
                )
            }.show(childFragmentManager)
        }

        toolTipFrequency?.setOnClickListener {
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(context?.getString(R.string.top_ads_frekuensi_tambah_kredit) ?: "")
                it.setDescription(
                    context?.getString(R.string.top_ads_tool_tip_frequency_description) ?: ""
                )
            }.show(childFragmentManager)
        }

        toolTipMaxCreditLimit?.setOnClickListener {
            sendClickInfoMinimalTresholdKreditOtomatisEvent()
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(context?.getString(R.string.toapds_dash_tooltip_title) ?: "")
                it.setDescription(
                    context?.getString(R.string.top_ads_tool_tip_max_credit_limit_description) ?: ""
                )
            }.show(childFragmentManager)
        }

        toolTipFrequencyHistory?.setOnClickListener {
            sendClickInfoFrekuensiTambahKreditOtomatisEvent()
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(context?.getString(R.string.top_ads_frekuensi_tambah_kredit) ?: "")
                it.setDescription(
                    context?.getString(R.string.top_ads_tool_tip_frequency_description) ?: ""
                )
            }.show(childFragmentManager)
        }
    }

    private fun resetToCreditHistoryState() {
        textPilihNominal?.hide()
        creditItemRecyclerView?.hide()
        groupFrequencyAutoTopUp?.hide()
        changeButtonState(true)
        tncAutoCreditCheckBox?.hide()
        groupAutoTopUpCreditHistory?.show()
        saveButton?.isEnabled = true
        saveButton?.tag = IS_EDIT_TOP_UP
    }

    private fun enableEditAutoTopUpState() {
        textPilihNominal?.show()
        creditItemRecyclerView?.show()
        groupFrequencyAutoTopUp?.show()
        groupAutoTopUpCreditHistory?.hide()
        when (autoTopUpCreditHistoryTriple?.third?.second) {
            TOP_UP_FREQUENCY_FOUR -> jarangRadioButton?.isChecked = true
            DEFAULT_TOP_UP_FREQUENCY -> normalRadioButton?.isChecked = true
            TOP_UP_FREQUENCY_EIGHT -> seringRadioButton?.isChecked = true
        }
        tncAutoCreditCheckBox?.isChecked = true
        tncAutoCreditCheckBox?.isEnabled = false
        tncAutoCreditCheckBox?.show()
        topCreditSheetScrollView?.post {
            topCreditSheetScrollView?.let { it1 ->
                it1.smoothScrollTo(
                    Int.ZERO,
                    it1.bottom
                )
            }
        }
        changeButtonState(false)

        val defaultEditList = autoTopUpCreditHistoryTriple?.let { triple ->
            viewModel?.getAutoTopUpCreditListFromSelected(
                triple.first,
                autoTopUpNominalList
            )
        }

        defaultEditList?.first?.let { list -> adapter.submitList(list) }
        selectedNominalIndex = defaultEditList?.second ?: INVALID_NOMINAL_INDEX
        applyButton?.isEnabled = false
        setCheckBoxText()
        resetSaveButtonTag()
        expandBottomSheet()
    }

    private fun resetSaveButtonTag() {
        saveButton?.tag = ""
    }

    private fun changeButtonState(isDefault: Boolean) {
        if (isDefault) {
            saveButton?.visible()
            cancelButton?.invisible()
            applyButton?.invisible()
        } else {
            saveButton?.invisible()
            cancelButton?.visible()
            applyButton?.visible()
        }
    }

    private fun setUpCheckChangeListener() {
        topUpSwitchRadioButtonGroup?.setOnCheckedChangeListener { _, id ->
            if (id == manualRadioButton?.id) {
                changeToManualState()
            } else {
                changeToAutoState()
            }
        }

        radioGroupAutoTopUpFrequency?.setOnCheckedChangeListener { _, id ->
            when (id) {
                jarangRadioButton?.id -> {
                    autoTopUpFrequencySelected = TOP_UP_FREQUENCY_FOUR
                    sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                        isFromEditAutoTopUp,
                        context?.getString(R.string.topads_frequency_four_text) ?: ""
                    )
                }
                normalRadioButton?.id -> {
                    autoTopUpFrequencySelected = DEFAULT_TOP_UP_FREQUENCY
                    sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                        isFromEditAutoTopUp,
                        context?.getString(R.string.topads_frequency_six_text) ?: ""
                    )
                }
                seringRadioButton?.id -> {
                    autoTopUpFrequencySelected = TOP_UP_FREQUENCY_EIGHT
                    sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                        isFromEditAutoTopUp,
                        context?.getString(R.string.topads_frequency_eight_text) ?: ""
                    )
                }
            }

            if (autoRadioButton?.isChecked == true) {
                setAutoStateFields()
                tncAutoCreditCheckBox?.isChecked = false
                applyButton?.isEnabled = tncAutoCreditCheckBox?.isChecked ?: false
            }
        }

        tncAutoCreditCheckBox?.setOnCheckedChangeListener { compoundButton, _ ->
            sendClickChecklistTncKreditOtomatisDiModalTambahKreditEvent(isFromEditAutoTopUp)
            saveButton?.isEnabled =
                compoundButton.isChecked && !selectedNominal?.productPrice.isNullOrEmpty()
            applyButton?.isEnabled =
                compoundButton.isChecked && !selectedNominal?.productPrice.isNullOrEmpty() && compoundButton?.isEnabled == true
        }
    }

    private fun saveAutoTopUp() {
        if (autoTopUpAvailableNominalList.isNotEmpty() && selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            viewModel?.saveSelection(
                true,
                autoTopUpAvailableNominalList[selectedNominalIndex],
                autoTopUpFrequencySelected.toString()
            )
        }
    }

    private fun openManualAdsCreditWebView() {
        if (selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            creditResponse?.credit?.getOrNull(selectedNominalIndex)?.productUrl?.let { productUrl ->
                chooseCredit(productUrl)
            }
        }
        dismiss()
    }

    private fun changeToManualState() {
        sendClickPilihTambahKreditManualEvent()
        manageAutoTopUpActiveState()
        addNominalList(manualNominalList)
        groupFrequencyAutoTopUp?.hide()
        autoTopUpCreditTips?.hide()
        tncAutoCreditCheckBox?.isChecked = false
        tncAutoCreditCheckBox?.hide()
        autoTopUpNominalList.reset()
        saveButton?.text = context?.getString(R.string.label_add_credit)
        saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
        changeButtonState(true)
        groupAutoTopUpCreditHistory?.hide()
        saveButton?.buttonVariant = UnifyButton.Variant.FILLED
        resetSaveButtonTag()
        collapseBottomSheet()
    }

    private fun changeToAutoState() {
        sendClickPilihTambahKreditOtomatisEvent()
        normalRadioButton?.isChecked = true
        groupFrequencyAutoTopUp?.showWithCondition(!isAutoTopUpActive)
        manualNominalList.reset()
        autoTopUpMaxCreditLimit = 0
        autoTopUpCreditTips?.hide()
        saveButton?.text = context?.getString(R.string.top_ads_auto_top_up_save_btn)
        saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
        manageAutoTopUpActiveState()
        addNominalList(autoTopUpNominalList)
        if (isAutoTopUpActive) collapseBottomSheet() else expandBottomSheet()
    }

    private fun manageAutoTopUpActiveState() {
        if (isAutoTopUpActive) {
            if (manualRadioButton?.isChecked == true) {
                resetManualState()
            } else {
                setCreditHistoryData()
            }
        } else {
            groupAutoTopUpCreditHistory?.hide()
        }
    }

    private fun setCreditHistoryData() {
        textPilihNominal?.hide()
        creditItemRecyclerView?.hide()
        selectedNominal = TopUpCreditItemData(
            autoTopUpCreditHistoryTriple?.first ?: "",
            autoTopUpCreditHistoryTriple?.second ?: ""
        )
        selectedPrice?.text = selectedNominal?.productPrice
        selectedBonus?.text = HtmlCompat.fromHtml(
            String.format(
                getString(R.string.topads_dash_auto_topup_bonus),
                selectedNominal?.bonus
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        maxCreditLimit?.text = autoTopUpCreditHistoryTriple?.third?.first
        val autoTopUpFrequencySelectedText =
            getTextFromFrequency(context, autoTopUpCreditHistoryTriple?.third?.second)
        selectedFrequncyTambahCredit?.text = HtmlCompat.fromHtml(
            String.format(
                getString(R.string.topads_dash_auto_topup_frequency),
                autoTopUpFrequencySelectedText,
                autoTopUpCreditHistoryTriple?.third?.second
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        autoTopUpMaxCreditLimit = viewModel?.getAutoTopUpMaxCreditLimit(
            autoTopUpFrequencySelected,
            autoTopUpCreditHistoryTriple?.first
        ) ?: 0L
        autoTopUpCreditTips?.description =
            context?.let { Utils.getSpannableForTips(it, autoTopUpMaxCreditLimit) } ?: ""
        saveButton?.text = context?.getString(R.string.topads_edit_auto_top_up)
        saveButton?.buttonVariant = UnifyButton.Variant.GHOST
        saveButton?.isEnabled = isCreditHistoryReceived
        creditHistoryShimmer?.showWithCondition(!isCreditHistoryReceived)
        saveButton?.tag = IS_EDIT_TOP_UP
        groupAutoTopUpCreditHistory?.showWithCondition(isCreditHistoryReceived)
        autoTopUpCreditTips?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (selectedFrequncyTambahCredit?.id != null) {
                topToBottom =
                    selectedFrequncyTambahCredit?.id!!
            }
        }
        autoTopUpCreditTips?.showWithCondition(isCreditHistoryReceived)
    }

    private fun resetManualState() {
        textPilihNominal?.show()
        creditItemRecyclerView?.show()
        saveButton?.text = context?.getString(R.string.label_add_credit)
    }

    private fun setAutoStateFields() {
        if (selectedNominal?.productPrice?.isNotEmpty() == true) {
            tncAutoCreditCheckBox?.isEnabled = true
        }
        autoTopUpMaxCreditLimit = viewModel?.getAutoTopUpMaxCreditLimit(
            autoTopUpFrequencySelected,
            selectedNominal?.productPrice
        ) ?: 0L
        autoTopUpCreditTips?.description =
            context?.let { Utils.getSpannableForTips(it, autoTopUpMaxCreditLimit) } ?: ""
        autoTopUpCreditTips?.show()
        if (selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            topCreditSheetScrollView?.post {
                topCreditSheetScrollView?.bottom?.let {
                    topCreditSheetScrollView?.smoothScrollTo(
                        Int.ZERO,
                        it
                    )
                }
            }
            setCheckBoxText()
            tncAutoCreditCheckBox?.show()
        }
    }

    private fun setCheckBoxText() {
        tncAutoCreditCheckBox?.text = context?.let {
            Utils.getTncSpannable(
                it,
                autoTopUpAvailableNominalList.getOrNull(selectedNominalIndex)?.minCreditFmt
                    ?: ""
            )
        }
    }

    private fun getInitialData() {
        viewModel?.populateCreditList(::onSuccessCreditInfo)
        viewModel?.getAutoTopUpStatusFull()
    }

    private fun setObserver() {
        viewModel?.getAutoTopUpStatus?.observe(viewLifecycleOwner) {
            if (it is Success) {
                onSuccessGetAutoCreditListData(it.data)
            }
        }

        viewModel?.statusSaveSelection?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseSaving -> {
                    saveButton?.isLoading = false
                    onSaved?.invoke(true)
                    isAutoTopUpActive = true
                    handleResponseSaving()
                    dismiss()
                }
                is Loading -> {
                    saveButton?.isLoading = true
                }
            }
        }
    }

    private fun handleResponseSaving() {
        activity?.setResult(Activity.RESULT_OK, Intent())
    }

    private fun onSuccessCreditInfo(data: CreditResponse) {
        creditResponse = data
        onSuccessGetCreditListData()
    }

    private fun onSuccessGetAutoCreditListData(data: AutoTopUpStatus) {
        val list = viewModel?.getAutoTopUpCreditList(data, isAutoTopUpActive)
        autoTopUpAvailableNominalList.clear()
        autoTopUpAvailableNominalList.addAll(data.availableNominals)
        autoTopUpNominalList.clear()
        list?.let { autoTopUpNominalList.addAll(it) }
        if (autoRadioButton?.isChecked == true) addNominalList(autoTopUpNominalList)
        autoTopUpCreditHistoryTriple = viewModel?.getAutoTopUpCreditHistoryData(data)
        isCreditHistoryReceived = true
        manageAutoTopUpActiveState()
        if (isShowEditHistory) enableEditAutoTopUpState()
    }

    private fun onSuccessGetCreditListData() {
        val list = viewModel?.getCreditItemDataList(
            creditResponse?.credit,
            creditResponse?.extraCreditPercent ?: 0.0f
        )
        manualNominalList.clear()
        list?.let { manualNominalList.addAll(it) }
        if (manualRadioButton?.isChecked == true) addNominalList(manualNominalList)
    }

    private fun addNominalList(list: MutableList<TopUpCreditItemData>?) {
        if (list.isNullOrEmpty()) {
            adapter.submitList(
                mutableListOf<TopUpCreditItemData>().createListOfSize(
                    TopUpCreditItemData(),
                    Int.EIGHT
                )
            )
        } else {
            adapter.submitList(list)
        }
    }

    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, "")
    }

    override fun onNominalClicked(nominalList: ArrayList<TopUpCreditItemData>, position: Int) {
        selectedNominal = nominalList.getOrNull(position)
        selectedNominalIndex = position
        nominalList.reset()
        selectedNominal?.clicked = true
        adapter.submitList(nominalList.toMutableList())
        if (autoRadioButton?.isChecked == true) {
            if (!isFromEditAutoTopUp) {
                sendClickAmountOfAutoTopupEvent(selectedNominal?.productPrice ?: "")
            } else {
                sendClickAmountOfAutoTopupDiModalTambahKreditEvent()
            }
            setAutoStateFields()
            tncAutoCreditCheckBox?.isEnabled = true
            tncAutoCreditCheckBox?.isChecked = false
            applyButton?.isEnabled = tncAutoCreditCheckBox?.isChecked ?: false
        } else {
            sendClickAmountOfManualTopupDiModalTambahKreditEvent(selectedNominal?.productPrice ?: "")
            saveButton?.isEnabled = true
        }
    }

    private fun collapseBottomSheet() {
        if (dialog?.isShowing == true) {
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun expandBottomSheet() {
        if (dialog?.isShowing == true) {
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun chooseCredit(productUrl: String) {
        activity?.let {
            val intent = Intent(context, TopAdsPaymentCreditActivity::class.java).apply {
                putExtra(KEY_URL, viewModel?.getUrl(productUrl))
                putExtra(KEY_TITLE, context?.getString(R.string.title_top_ads_add_credit))
            }
            startActivity(intent)
        }
    }
}

private fun MutableList<TopUpCreditItemData>.reset() {
    this.forEach { it.clicked = false }
}
