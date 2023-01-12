package com.tokopedia.topads.debit.autotopup.view.sheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.constraintlayout.widget.Group
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.createListOfSize
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TipsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

const val INVALID_NOMINAL_INDEX = -1

class TopAdsChooseCreditBottomSheet : BottomSheetUnify(),
    TopAdsCreditListAdapter.NominalClickListener {
    private var creditItemRecyclerView: RecyclerView? = null
    private val adapter by lazy { TopAdsCreditListAdapter() }
    private var topCreditSheetScrollView: NestedScrollView? = null
    private var manualRadioButton: RadioButtonUnify? = null
    private var autoRadioButton: RadioButtonUnify? = null
    private var radioGroupAutoTopUpFrequency: RadioGroup? = null
    private var jarangRadioButton: RadioButtonUnify? = null
    private var normalRadioButton: RadioButtonUnify? = null
    private var seringRadioButton: RadioButtonUnify? = null
    private var saveButton: UnifyButton? = null
    private var groupFrequencyAutoTopUp: Group? = null
    private var autoTopUpCreditTips: TipsUnify? = null
    private var tncAutoCreditCheckBox: CheckboxUnify? = null
    private var selectedNominal: TopUpCreditItemData? = null
    private var selectedNominalIndex: Int = INVALID_NOMINAL_INDEX
    private var creditResponse: CreditResponse? = null
    private var manualNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpNominalList: MutableList<TopUpCreditItemData> = mutableListOf()
    private var autoTopUpAvailableNominalList: MutableList<AutoTopUpItem> = mutableListOf()
    private var autoTopUpMaxCreditLimit: Long = 0L
    private var autoTopUpFrequencySelected: Int = 6
    var onSaved: ((productUrl: String) -> Unit)? = null

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
        setChild(contentView)
        initView(contentView)
    }

    private fun initView(contentView: View?) {
        topCreditSheetScrollView = contentView?.findViewById(R.id.topCreditSheetScrollView)
        manualRadioButton = contentView?.findViewById(R.id.manualRadioButton)
        autoRadioButton = contentView?.findViewById(R.id.autoRadioButton)
        creditItemRecyclerView = contentView?.findViewById(R.id.creditItemRecyclerView)
        saveButton = contentView?.findViewById(R.id.saveButton)
        groupFrequencyAutoTopUp = contentView?.findViewById(R.id.groupFrequencyAutoTopUp)
        autoTopUpCreditTips = contentView?.findViewById(R.id.autoTopUpCreditTips)
        jarangRadioButton = contentView?.findViewById(R.id.jarangRadioButton)
        normalRadioButton = contentView?.findViewById(R.id.normalRadioButton)
        seringRadioButton = contentView?.findViewById(R.id.seringRadioButton)
        radioGroupAutoTopUpFrequency = contentView?.findViewById(R.id.radioGroupAutoTopUpFrequency)
        tncAutoCreditCheckBox = contentView?.findViewById(R.id.tncAutoCreditCheckBox)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitialData()
        setObserver()
        setUpRecyclerView()
        addNominalList(manualNominalList)
        setUpClickListener()
        setUpCheckChangeListener()
    }


    private fun setUpRecyclerView() {
        adapter.setNominalClickListener(this)
        creditItemRecyclerView?.adapter = adapter
        creditItemRecyclerView?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun setUpClickListener() {
        manualRadioButton?.setOnClickListener {
            if (autoRadioButton?.isChecked == true) {
                autoRadioButton?.isChecked = false
                changeToManualState()
                addNominalList(manualNominalList)
            }
        }
        autoRadioButton?.setOnClickListener {
            if (manualRadioButton?.isChecked == true) {
                manualRadioButton?.isChecked = false
                changeToAutoState()
                addNominalList(autoTopUpNominalList)
            }
        }
        saveButton?.setOnClickListener {
            if (manualRadioButton?.isChecked == true) {
                openManualAdsPage()
            } else {
                saveAutoTopUp()
            }

        }
    }

    private fun setUpCheckChangeListener() {
        radioGroupAutoTopUpFrequency?.setOnCheckedChangeListener { _, id ->
            when (id) {
                jarangRadioButton?.id -> autoTopUpFrequencySelected = 4
                normalRadioButton?.id -> autoTopUpFrequencySelected = 6
                seringRadioButton?.id -> autoTopUpFrequencySelected = 8
            }

            if (autoRadioButton?.isChecked == true) {
                setAutoStateFields()
            }
        }

        tncAutoCreditCheckBox?.setOnCheckedChangeListener { compoundButton, b ->
            saveButton?.isEnabled =
                compoundButton.isChecked && !selectedNominal?.productPrice.isNullOrEmpty()
        }

    }

    private fun saveAutoTopUp() {
        if (autoTopUpAvailableNominalList.isNotEmpty()) {
            viewModel?.saveSelection(
                true,
                autoTopUpAvailableNominalList[selectedNominalIndex],
                autoTopUpFrequencySelected.toString()
            )
        }
    }

    private fun openManualAdsPage() {
        dismiss()
        if (selectedNominalIndex != INVALID_NOMINAL_INDEX) {
            creditResponse?.credit?.getOrNull(selectedNominalIndex)?.productUrl?.let { productUrl ->
                onSaved?.invoke(
                    productUrl
                )
            }
        }
    }

    private fun changeToManualState() {
        groupFrequencyAutoTopUp?.hide()
        autoTopUpCreditTips?.hide()
        tncAutoCreditCheckBox?.isChecked = false
        tncAutoCreditCheckBox?.hide()
        autoTopUpNominalList.reset()
        saveButton?.text = context?.getString(R.string.label_add_credit)
        saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
    }

    private fun changeToAutoState() {
        normalRadioButton?.isChecked = true
        groupFrequencyAutoTopUp?.show()
        manualNominalList.reset()
        autoTopUpMaxCreditLimit = 0
        autoTopUpCreditTips?.hide()
        saveButton?.text = context?.getString(R.string.top_ads_auto_top_up_save_btn)
        saveButton?.isEnabled = false
        selectedNominalIndex = INVALID_NOMINAL_INDEX
    }

    private fun setAutoStateFields() {
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
                is Success -> onSuccessGetAutoCreditListData(it.data)
            }
        }

        viewModel?.statusSaveSelection?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseSaving -> {
                    saveButton?.isLoading = false
                    onSaved?.invoke("")
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
        val list = viewModel?.getAutoTopUpCreditList(data)
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
        } else {
            saveButton?.isEnabled = true
        }
    }
}

private fun <E> MutableList<E>.reset() {
    this.forEach { (it as TopUpCreditItemData).clicked = false }
}
