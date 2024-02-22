package com.tokopedia.topads.debit.autotopup.view.sheet

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cachemanager.SaveInstanceCacheManager
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
import com.tokopedia.topads.dashboard.databinding.TopadsDashChooseCreditSheetBinding
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.topads.debit.autotopup.view.uimodel.AutoTopUpConfirmationUiModel
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
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class TopAdsChooseCreditBottomSheet :
    BottomSheetUnify(),
    TopAdsCreditListAdapter.NominalClickListener {

    private var autoTopUpCreditHistoryTriple: Triple<String, String, Pair<String, Int>>? = null
    private var selectedNominal: TopUpCreditItemData? = null
    private var selectedNominalIndex: Int = INVALID_NOMINAL_INDEX
    private var creditResponse: CreditResponse? = null
    private var manualNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpAvailableNominalList: MutableList<AutoTopUpItem> = mutableListOf()
    private var autoTopUpMaxCreditLimit: Long = Long.ZERO
    private var autoTopUpFrequencySelected: Int = DEFAULT_TOP_UP_FREQUENCY
    private var statusBonus = 1.0

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

    private var binding by autoClearedNullable<TopadsDashChooseCreditSheetBinding>()

    companion object {

        private const val TOP_ADS_TAX_VALUE = 1.11
        private const val PPN_PERCENT = 11
        private const val PPN_PERCENT_FORMULA = 0.11
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
        initChildLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initChildLayout(inflater: LayoutInflater, container: ViewGroup?) {
        binding = TopadsDashChooseCreditSheetBinding.inflate(inflater, container, false)
        setDefaultConfigs()
        setChild(binding?.root)
    }

    private fun setDefaultConfigs() {
        clearContentPadding = true
        isDragable = true
        showCloseIcon = true
        setTitle(getString(R.string.title_top_ads_add_credit))
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels
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
            dismiss()
        }
    }

    private fun checkRadioDefaultTopUpType() {
        if (isAutoTopUpSelected) {
            binding?.autoRadioButton?.isChecked =
                true
        } else {
            binding?.manualRadioButton?.isChecked = true
        }
    }

    private fun setAutoRadioButtonLabel() {
        binding?.autoTopUpActiveLabel?.visibleWithCondition(isAutoTopUpActive)
        binding?.autoRadioButtonTextDescription?.visibleWithCondition(!isAutoTopUpActive)
    }

    private fun setDefaultState() {
        addNominalList(manualNominalList)
        changeButtonState(true)
        if (binding?.manualRadioButton?.isChecked == true) {
            changeToManualState()
        } else if (binding?.autoRadioButton?.isChecked == true) {
            changeToAutoState()
        }
    }

    private fun setUpRecyclerView() {
        adapter.setNominalClickListener(this)
        binding?.creditItemRecyclerView?.adapter = adapter
        binding?.creditItemRecyclerView?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun setUpClickListener() {
        binding?.run {
            saveButton.setOnClickListener {
                if (it.tag == IS_EDIT_TOP_UP) {
                    sendClickUbahPengaturanKreditOtomatisDiModalTambahKreditEvent()
                    enableEditAutoTopUpState()
                } else if (binding?.manualRadioButton?.isChecked == true) {
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

            cancelButton.setOnClickListener {
                sendClickBatalPerubahanPengaturanKreditOtomatisEvent()
                resetToCreditHistoryState()
            }

            applyButton.setOnClickListener {
                sendClickAktifkanTambahKreditOtomatisEvent()
                saveAutoTopUp()
            }

            toolTipAuto.setOnClickListener {
                sendClickInformasiKreditOtomatisEvent()
                TopAdsToolTipBottomSheet.newInstance().also {
                    it.setTitle(context?.getString(R.string.label_topads_automatic_topup) ?: "")
                    it.setDescription(
                        context?.getString(R.string.top_ads_tool_tip_auto_description) ?: ""
                    )
                }.show(childFragmentManager)
            }

            toolTipFrequency.setOnClickListener {
                TopAdsToolTipBottomSheet.newInstance().also {
                    it.setTitle(context?.getString(R.string.top_ads_frekuensi_tambah_kredit) ?: "")
                    it.setDescription(
                        context?.getString(R.string.top_ads_tool_tip_frequency_description) ?: ""
                    )
                }.show(childFragmentManager)
            }

            toolTipMaxCreditLimit.setOnClickListener {
                sendClickInfoMinimalTresholdKreditOtomatisEvent()
                TopAdsToolTipBottomSheet.newInstance().also {
                    it.setTitle(context?.getString(R.string.toapds_dash_tooltip_title) ?: "")
                    it.setDescription(
                        context?.getString(R.string.top_ads_tool_tip_max_credit_limit_description)
                            ?: ""
                    )
                }.show(childFragmentManager)
            }

            toolTipFrequencyHistory.setOnClickListener {
                sendClickInfoFrekuensiTambahKreditOtomatisEvent()
                TopAdsToolTipBottomSheet.newInstance().also {
                    it.setTitle(context?.getString(R.string.top_ads_frekuensi_tambah_kredit) ?: "")
                    it.setDescription(
                        context?.getString(R.string.top_ads_tool_tip_frequency_description) ?: ""
                    )
                }.show(childFragmentManager)
            }
        }
    }

    private fun resetToCreditHistoryState() {
        binding?.run {
            textPilihNominal.hide()
            creditItemRecyclerView.hide()
            groupFrequencyAutoTopUp.hide()
            changeButtonState(true)
            tncAutoCreditCheckBox.hide()
            autoTopUpCreditHistory.show()
            saveButton.isEnabled = true
            saveButton.tag = IS_EDIT_TOP_UP
        }
    }

    private fun enableEditAutoTopUpState() {
        binding?.run {
            textPilihNominal.show()
            creditItemRecyclerView.show()
            groupFrequencyAutoTopUp.show()
            autoTopUpCreditHistory.hide()
            when (autoTopUpCreditHistoryTriple?.third?.second) {
                TOP_UP_FREQUENCY_FOUR -> jarangRadioButton.isChecked = true
                DEFAULT_TOP_UP_FREQUENCY -> normalRadioButton.isChecked = true
                TOP_UP_FREQUENCY_EIGHT -> seringRadioButton.isChecked = true
            }
            tncAutoCreditCheckBox.isChecked = true
            tncAutoCreditCheckBox.isEnabled = false
            tncAutoCreditCheckBox.show()
            topCreditSheetScrollView.post {
                topCreditSheetScrollView.let { it1 ->
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
            applyButton.isEnabled = false
            setCheckBoxText()
            resetSaveButtonTag()
            expandBottomSheet()
        }
    }

    private fun resetSaveButtonTag() {
        binding?.saveButton?.tag = ""
    }

    private fun changeButtonState(isDefault: Boolean) {
        binding?.run {
            if (isDefault) {
                saveButton.visible()
                cancelButton.invisible()
                applyButton.invisible()
            } else {
                saveButton.invisible()
                cancelButton.visible()
                applyButton.visible()
            }
        }
    }

    private fun setUpCheckChangeListener() {
        binding?.run {
            topUpSwitchRadioButtonGroup.setOnCheckedChangeListener { _, id ->
                if (id == manualRadioButton.id) {
                    changeToManualState()
                } else {
                    changeToAutoState()
                }
            }

            radioGroupAutoTopUpFrequency.setOnCheckedChangeListener { _, id ->
                when (id) {
                    jarangRadioButton.id -> {
                        autoTopUpFrequencySelected = TOP_UP_FREQUENCY_FOUR
                        sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                            isFromEditAutoTopUp,
                            context?.getString(R.string.topads_frequency_four_text) ?: ""
                        )
                    }

                    normalRadioButton.id -> {
                        autoTopUpFrequencySelected = DEFAULT_TOP_UP_FREQUENCY
                        sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                            isFromEditAutoTopUp,
                            context?.getString(R.string.topads_frequency_six_text) ?: ""
                        )
                    }

                    seringRadioButton.id -> {
                        autoTopUpFrequencySelected = TOP_UP_FREQUENCY_EIGHT
                        sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
                            isFromEditAutoTopUp,
                            context?.getString(R.string.topads_frequency_eight_text) ?: ""
                        )
                    }
                }

                if (autoRadioButton.isChecked) {
                    setAutoStateFields()
                    tncAutoCreditCheckBox.isChecked = false
                    applyButton.isEnabled = tncAutoCreditCheckBox.isChecked ?: false
                }
            }

            tncAutoCreditCheckBox.setOnCheckedChangeListener { compoundButton, _ ->
                sendClickChecklistTncKreditOtomatisDiModalTambahKreditEvent(isFromEditAutoTopUp)
                saveButton.isEnabled =
                    compoundButton.isChecked && !selectedNominal?.productPrice.isNullOrEmpty()
                applyButton.isEnabled =
                    compoundButton.isChecked && !selectedNominal?.productPrice.isNullOrEmpty() && compoundButton?.isEnabled == true
            }
        }
    }

    private fun saveAutoTopUp() {
        if (autoTopUpAvailableNominalList.isNotEmpty() && selectedNominalIndex != INVALID_NOMINAL_INDEX) {

            //new approach
            val autoTopUpNominal = autoTopUpAvailableNominalList.getOrNull(selectedNominalIndex)
            val nominalConfirmation = if (autoTopUpNominal?.additionalFee?.isNotEmpty() == true) {
                //after April, 1 2024
                getNominalConfirmationUpAfterApril(autoTopUpNominal)
            } else {
                //before April, 1 2024
                getNominalConfirmationBeforeApril(autoTopUpNominal)
            }

            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }

            cacheManager?.put(
                AutoTopUpConfirmationBottomSheet.AUTO_TOP_UP_CONFIRMATION_KEY,
                nominalConfirmation
            )

            val bottomSheet = AutoTopUpConfirmationBottomSheet.newInstance(cacheManager?.id.orEmpty())
            bottomSheet.onSaved = onSaved
            bottomSheet.show(childFragmentManager)

            //old approach
            viewModel?.saveSelection(
                true,
                autoTopUpAvailableNominalList[selectedNominalIndex],
                autoTopUpFrequencySelected.toString()
            )
        }
    }

    private fun getNominalConfirmationBeforeApril(autoTopUpNominal: AutoTopUpItem?): AutoTopUpConfirmationUiModel {
        val topAdsCredit = autoTopUpNominal?.priceFmt.orEmpty()
        val topAdsValue = Utils.convertMoneyToValue(topAdsCredit)

        val topAdsBonus = Utils.convertToCurrencyString((topAdsValue * statusBonus / 100).toLong())

        val frequencyText = getString(R.string.topads_auto_top_up_confirmation_frequency_every_day, autoTopUpFrequencySelected.toString())


        val subTotalActual = (topAdsValue / TOP_ADS_TAX_VALUE).roundToLong()
        val subTotalActualFmt = Utils.convertToCurrencyString(subTotalActual)

        val ppnAmount = (topAdsValue * PPN_PERCENT_FORMULA).roundToLong()
        val ppnAmountFmt = Utils.convertToCurrencyString(ppnAmount)

        val totalAmount = Utils.convertToCurrencyString(subTotalActual + ppnAmount)

        return AutoTopUpConfirmationUiModel(
            topAdsCredit = topAdsCredit,
            topAdsBonus = topAdsBonus,
            frequencyText = frequencyText,
            autoTopUpFrequencySelected = autoTopUpFrequencySelected,
            subTotalStrikethrough = topAdsCredit,
            subTotalActual = subTotalActualFmt,
            ppnPercent = "$PPN_PERCENT%",
            ppnAmount = ppnAmountFmt,
            totalAmount = totalAmount,
            selectedItemId = autoTopUpNominal?.id.orZero().toString()
        )
    }

    private fun getNominalConfirmationUpAfterApril(autoTopUpNominal: AutoTopUpItem?): AutoTopUpConfirmationUiModel {
        val topAdsCredit = autoTopUpNominal?.priceFmt.orEmpty()
        val topAdsValue = Utils.convertMoneyToValue(topAdsCredit)

        val tax = autoTopUpNominal?.additionalFee?.find { it.type == "tax" }

        val topAdsBonus = Utils.convertToCurrencyString((topAdsValue * statusBonus / 100).toLong())

        val frequencyText = getString(R.string.topads_auto_top_up_confirmation_frequency_every_day, autoTopUpFrequencySelected.toString())

        val ppnAmount = tax?.amount.orZero()
        val ppnAmountFmt = Utils.convertToCurrencyString(ppnAmount)

        val totalAmount = Utils.convertToCurrencyString(autoTopUpNominal?.totalAmount.orZero())

        return AutoTopUpConfirmationUiModel(
            topAdsCredit = topAdsCredit,
            topAdsBonus = topAdsBonus,
            frequencyText = frequencyText,
            subTotalStrikethrough = "",
            autoTopUpFrequencySelected = autoTopUpFrequencySelected,
            subTotalActual = topAdsCredit,
            ppnPercent = "${tax?.percent}%",
            ppnAmount = ppnAmountFmt,
            totalAmount = totalAmount,
            selectedItemId = autoTopUpNominal?.id.orZero().toString()
        )
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
        binding?.groupFrequencyAutoTopUp?.hide()
        binding?.autoTopUpCreditTips?.hide()
        binding?.tncAutoCreditCheckBox?.isChecked = false
        binding?.tncAutoCreditCheckBox?.hide()
        autoTopUpNominalList.reset()
        binding?.saveButton?.text = context?.getString(R.string.label_add_credit)
        binding?.saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
        changeButtonState(true)
        binding?.autoTopUpCreditHistory?.hide()
        binding?.saveButton?.buttonVariant = UnifyButton.Variant.FILLED
        resetSaveButtonTag()
        collapseBottomSheet()
    }

    private fun changeToAutoState() {
        sendClickPilihTambahKreditOtomatisEvent()
        binding?.normalRadioButton?.isChecked = true
        binding?.groupFrequencyAutoTopUp?.showWithCondition(!isAutoTopUpActive)
        manualNominalList.reset()
        autoTopUpMaxCreditLimit = 0
        binding?.autoTopUpCreditTips?.hide()
        binding?.saveButton?.text = context?.getString(R.string.top_ads_auto_top_up_save_btn)
        binding?.saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
        manageAutoTopUpActiveState()
        addNominalList(autoTopUpNominalList)
        if (isAutoTopUpActive) collapseBottomSheet() else expandBottomSheet()
    }

    private fun manageAutoTopUpActiveState() {
        if (isAutoTopUpActive) {
            if (binding?.manualRadioButton?.isChecked == true) {
                resetManualState()
            } else {
                setCreditHistoryData()
            }
        } else {
            binding?.autoTopUpCreditHistory?.hide()
        }
    }

    private fun setCreditHistoryData() {
        binding?.run {
            textPilihNominal.hide()
            creditItemRecyclerView.hide()
            selectedNominal = TopUpCreditItemData(
                autoTopUpCreditHistoryTriple?.first ?: "",
                autoTopUpCreditHistoryTriple?.second ?: ""
            )
            selectedPrice.text = selectedNominal?.productPrice
            selectedBonus.text = HtmlCompat.fromHtml(
                String.format(
                    getString(R.string.topads_dash_auto_topup_bonus),
                    selectedNominal?.bonus
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            maxCreditLimit.text = autoTopUpCreditHistoryTriple?.third?.first
            val autoTopUpFrequencySelectedText =
                getTextFromFrequency(context, autoTopUpCreditHistoryTriple?.third?.second)
            selectedFrequncyTambahCredit.text = HtmlCompat.fromHtml(
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
            autoTopUpCreditTips.description =
                context?.let { Utils.getSpannableForTips(it, autoTopUpMaxCreditLimit) } ?: ""
            saveButton.text = context?.getString(R.string.topads_edit_auto_top_up)
            saveButton.buttonVariant = UnifyButton.Variant.GHOST
            saveButton.isEnabled = isCreditHistoryReceived
            creditHistoryShimmer.showWithCondition(!isCreditHistoryReceived)
            saveButton.tag = IS_EDIT_TOP_UP
            autoTopUpCreditHistory.showWithCondition(isCreditHistoryReceived)
            autoTopUpCreditTips.updateLayoutParams<ConstraintLayout.LayoutParams> {
                if (selectedFrequncyTambahCredit.id != null) {
                    topToBottom =
                        selectedFrequncyTambahCredit.id!!
                }
            }
            autoTopUpCreditTips.showWithCondition(isCreditHistoryReceived)
        }
    }

    private fun resetManualState() {
        binding?.run {
            textPilihNominal.show()
            creditItemRecyclerView.show()
            saveButton.text = context?.getString(R.string.label_add_credit)
        }
    }

    private fun setAutoStateFields() {
        if (selectedNominal?.productPrice?.isNotEmpty() == true) {
            binding?.tncAutoCreditCheckBox?.isEnabled = true
        }
        autoTopUpMaxCreditLimit = viewModel?.getAutoTopUpMaxCreditLimit(
            autoTopUpFrequencySelected,
            selectedNominal?.productPrice
        ) ?: 0L
        binding?.autoTopUpCreditTips?.description =
            context?.let { Utils.getSpannableForTips(it, autoTopUpMaxCreditLimit) } ?: ""
        binding?.autoTopUpCreditTips?.show()
        if (selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            binding?.topCreditSheetScrollView?.post {
                binding?.topCreditSheetScrollView?.bottom?.let {
                    binding?.topCreditSheetScrollView?.smoothScrollTo(
                        Int.ZERO,
                        it
                    )
                }
            }
            setCheckBoxText()
            binding?.tncAutoCreditCheckBox?.show()
        }
    }

    private fun setCheckBoxText() {
        binding?.tncAutoCreditCheckBox?.text = context?.let {
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
                    binding?.saveButton?.isLoading = false
                    onSaved?.invoke(true)
                    isAutoTopUpActive = true
                    handleResponseSaving()
                    dismiss()
                }
                is Loading -> {
                    binding?.saveButton?.isLoading = true
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
        statusBonus = data.statusBonus
        autoTopUpAvailableNominalList.clear()
        autoTopUpAvailableNominalList.addAll(data.availableNominals)
        autoTopUpNominalList.clear()
        list?.let { autoTopUpNominalList.addAll(it) }
        if (binding?.autoRadioButton?.isChecked == true) addNominalList(autoTopUpNominalList)
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
        if (binding?.manualRadioButton?.isChecked == true) addNominalList(manualNominalList)
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

        binding?.run {
            if (autoRadioButton.isChecked) {
                if (!isFromEditAutoTopUp) {
                    sendClickAmountOfAutoTopupEvent(selectedNominal?.productPrice ?: "")
                } else {
                    sendClickAmountOfAutoTopupDiModalTambahKreditEvent()
                }
                setAutoStateFields()
                tncAutoCreditCheckBox.isEnabled = true
                tncAutoCreditCheckBox.isChecked = false
                applyButton.isEnabled = tncAutoCreditCheckBox.isChecked ?: false
            } else {
                sendClickAmountOfManualTopupDiModalTambahKreditEvent(selectedNominal?.productPrice
                    ?: "")
                saveButton.isEnabled = true
            }
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
