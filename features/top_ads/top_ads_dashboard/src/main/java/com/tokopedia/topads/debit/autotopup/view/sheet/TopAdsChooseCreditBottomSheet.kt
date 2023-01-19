package com.tokopedia.topads.debit.autotopup.view.sheet

import android.app.Activity
import android.content.Intent
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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.createListOfSize
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

const val INVALID_NOMINAL_INDEX = -1

class TopAdsChooseCreditBottomSheet : BottomSheetUnify(),
    TopAdsCreditListAdapter.NominalClickListener {
    private var autoTopUpCreditHistoryTriple: Triple<String, String, Pair<String, Int>>? = null
    private var creditItemRecyclerView: RecyclerView? = null
    private var textPilihNominal: com.tokopedia.unifyprinciples.Typography? = null
    private val adapter by lazy { TopAdsCreditListAdapter() }
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
    private var selectedNominal: TopUpCreditItemData? = null
    private var selectedNominalIndex: Int = INVALID_NOMINAL_INDEX
    private var creditResponse: CreditResponse? = null
    private var manualNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpAvailableNominalList: MutableList<AutoTopUpItem> = mutableListOf()
    private var autoTopUpMaxCreditLimit: Long = 0L
    private var autoTopUpFrequencySelected: Int = 6
    var isAutoTopUpActive: Boolean = false
    var isAutoTopUpSelected: Boolean = false
    private var isCreditHistoryReceived: Boolean = false
    var onSaved: ((productUrl: String, isAutoAdsSaved: Boolean) -> Unit)? = null
    var onCancel: (() -> Unit)? = null

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
        savedInstanceState: Bundle?,
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
        showCloseIcon = false
        clearContentPadding = true
        isDragable = true
        setTitle(getString(R.string.title_top_ads_add_credit))
        showCloseIcon = true
        customPeekHeight = 600
        setChild(contentView)
        initView(contentView)
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
        }
    }

    private fun checkRadioDefaultTopUpType() {
        if (isAutoTopUpSelected) autoRadioButton?.isChecked =
            true else manualRadioButton?.isChecked = true
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
            if (it.tag == "editAutoTopUp") {
                enableEditAutoTopUpState()
            } else if (manualRadioButton?.isChecked == true) {
                openManualAdsCreditWebView()
            } else {
                saveAutoTopUp()
            }
        }

        cancelButton?.setOnClickListener {
            resetToCreditHistoryState()
        }

        applyButton?.setOnClickListener {
            saveAutoTopUp()
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
        saveButton?.tag = "editAutoTopUp"
    }

    private fun enableEditAutoTopUpState() {
        textPilihNominal?.show()
        creditItemRecyclerView?.show()
        groupFrequencyAutoTopUp?.show()
        groupAutoTopUpCreditHistory?.hide()
        when (autoTopUpCreditHistoryTriple?.third?.second) {
            4 -> jarangRadioButton?.isChecked = true
            6 -> normalRadioButton?.isChecked = true
            8 -> seringRadioButton?.isChecked = true
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
                triple.first, autoTopUpNominalList
            )
        }

        defaultEditList?.first?.let { list -> adapter.submitList(list) }
        selectedNominalIndex = defaultEditList?.second ?: INVALID_NOMINAL_INDEX
        applyButton?.isEnabled = false
        resetSaveButtonTag()
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
                jarangRadioButton?.id -> autoTopUpFrequencySelected = 4
                normalRadioButton?.id -> autoTopUpFrequencySelected = 6
                seringRadioButton?.id -> autoTopUpFrequencySelected = 8
            }

            if (autoRadioButton?.isChecked == true) {
                setAutoStateFields()
                applyButton?.isEnabled = true
            }
        }

        tncAutoCreditCheckBox?.setOnCheckedChangeListener { compoundButton, _ ->
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
        dismiss()
        if (selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            creditResponse?.credit?.getOrNull(selectedNominalIndex)?.productUrl?.let { productUrl ->
                onSaved?.invoke(
                    productUrl,
                    false
                )
            }
        }
    }

    private fun changeToManualState() {
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
    }

    private fun changeToAutoState() {
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
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        maxCreditLimit?.text = autoTopUpCreditHistoryTriple?.third?.first
        val autoTopUpFrequencySelectedText = getTextFromFrequency(autoTopUpCreditHistoryTriple?.third?.second)
        selectedFrequncyTambahCredit?.text = HtmlCompat.fromHtml(
            String.format(
                getString(R.string.topads_dash_auto_topup_frequency),
                autoTopUpFrequencySelectedText,
                autoTopUpCreditHistoryTriple?.third?.second
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        autoTopUpMaxCreditLimit = viewModel?.getAutoTopUpMaxCreditLimit(
            autoTopUpFrequencySelected,
            autoTopUpCreditHistoryTriple?.first
        ) ?: 0L
        autoTopUpCreditTips?.description =
            context?.let { Utils.getSpannableForTips(it, autoTopUpMaxCreditLimit) } ?: ""
        saveButton?.text = "Ubah Pengaturan"
        saveButton?.buttonVariant = UnifyButton.Variant.GHOST
        saveButton?.isEnabled = isCreditHistoryReceived
        creditHistoryShimmer?.showWithCondition(!isCreditHistoryReceived)
        saveButton?.tag = "editAutoTopUp"
        groupAutoTopUpCreditHistory?.showWithCondition(isCreditHistoryReceived)
        autoTopUpCreditTips?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (selectedFrequncyTambahCredit?.id != null) topToBottom =
                selectedFrequncyTambahCredit?.id!!
        }
        autoTopUpCreditTips?.showWithCondition(isCreditHistoryReceived)
    }

    private fun getTextFromFrequency(autoTopUpFrequencySelected: Int?): String {
        return when (autoTopUpFrequencySelected) {
            4 -> "Jarang"
            6 -> "Normal"
            8 -> "Sering"
            else -> "Normal"
        }
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
            tncAutoCreditCheckBox?.text = context?.let {
                Utils.getTncSpannable(
                    it,
                    autoTopUpAvailableNominalList.getOrNull(selectedNominalIndex)?.minCreditFmt
                        ?: ""
                )
            }
            tncAutoCreditCheckBox?.show()
        }

    }

    private fun getInitialData() {
        viewModel?.populateCreditList(::onSuccessCreditInfo)
        viewModel?.getAutoTopUpStatusFull()
    }

    private fun setObserver() {
        viewModel?.topAdsTopUpCreditData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetCreditListData(it.data)
            }
        }
        viewModel?.getAutoTopUpStatus?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetAutoCreditListData(it.data)
                    autoTopUpCreditHistoryTriple = viewModel?.getAutoTopUpCreditHistoryData(it.data)
                    isCreditHistoryReceived = true
                    manageAutoTopUpActiveState()

                }
            }
        }

        viewModel?.statusSaveSelection?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseSaving -> {
                    saveButton?.isLoading = false
                    onSaved?.invoke("", true)
                    isAutoTopUpActive = true
                    handleResponseSaving(it)
                    dismiss()

                }
                is Loading -> {
                    saveButton?.isLoading = true
                }
            }
        }
    }

    private fun handleResponseSaving(it: ResponseSaving) {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent())
        }
    }

    private fun onSuccessCreditInfo(data: CreditResponse) {
        viewModel?.getManualTopAdsCreditList()
        creditResponse = data
    }

    private fun onSuccessGetAutoCreditListData(data: AutoTopUpStatus) {
        val list = viewModel?.getAutoTopUpCreditList(data, isAutoTopUpActive)
        autoTopUpAvailableNominalList.clear()
        autoTopUpAvailableNominalList.addAll(data.availableNominals)
        autoTopUpNominalList.clear()
        list?.let { autoTopUpNominalList.addAll(it) }
        if (autoRadioButton?.isChecked == true) addNominalList(autoTopUpNominalList)
    }

    private fun onSuccessGetCreditListData(data: TopAdsShopTierShopGradeData.ShopInfoByID.Result) {
        val list = viewModel?.getCreditItemData2(creditResponse?.credit, data)
        manualNominalList.clear()
        list?.let { manualNominalList.addAll(it) }
        if (manualRadioButton?.isChecked == true) addNominalList(manualNominalList)
    }

    private fun addNominalList(list: MutableList<TopUpCreditItemData>?) {
        if (list.isNullOrEmpty()) {
            adapter.submitList(
                mutableListOf<TopUpCreditItemData>().createListOfSize(
                    TopUpCreditItemData(),
                    8
                )
            )
        } else {
            adapter.submitList(list)
        }
    }

    fun show(
        fragmentManager: FragmentManager
    ) {
        show(fragmentManager, "top_up_sheet")
    }

    override fun onNominalClicked(nominalList: ArrayList<TopUpCreditItemData>, position: Int) {
        selectedNominal = nominalList.getOrNull(position)
        selectedNominalIndex = position
        nominalList.forEach { it.clicked = false }
        selectedNominal?.clicked = true
        adapter.submitList(nominalList.toMutableList())
        if (autoRadioButton?.isChecked == true) {
            setAutoStateFields()
            tncAutoCreditCheckBox?.isEnabled = true
            applyButton?.isEnabled = true
        } else {
            saveButton?.isEnabled = true
        }
    }
}

private fun <E> MutableList<E>.reset() {
    this.forEach { (it as TopUpCreditItemData).clicked = false }
}
