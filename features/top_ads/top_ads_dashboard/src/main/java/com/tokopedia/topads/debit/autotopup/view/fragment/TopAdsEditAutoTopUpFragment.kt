package com.tokopedia.topads.debit.autotopup.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.calculatePercentage
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.extensions.selectedPrice
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsCreditNonAktifanAdapter
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseCreditBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseNominalBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseTopUpAmountSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by Pika on 22/9/20.
 */

class TopAdsEditAutoTopUpFragment : BaseDaggerFragment() {

    private var loader: LoaderUnify? = null
    private var switchAutoTopupStatus: SwitchUnify? = null
    private var selectCreditCard: CardUnify? = null
    private var creditDropMenu: LinearLayout? = null
    private var topupAmount: Typography? = null
    private var bonusText: Typography? = null
    private var tooltip: ImageUnify? = null
    private var dedAmount: Typography? = null
    private var offLayout: CardUnify? = null
    private var autoTopUpCreditHistoryLayout: ConstraintLayout? = null
    private var autoTopUpCreditTipsUnify: TipsUnify? = null
    private var editAutoTopUpButton: UnifyButton? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var userSession: UserSession? = null
    private var selectedItem = AutoTopUpItem()
    private var bonus = 1.0
    private var autoTopupStatus: AutoTopUpStatus? = null

    private var autoTopUpCreditHistoryTriple: Triple<String, String, Pair<String, Int>>? = null
    private var showOldFlow = true
    private var isAutoTopUpActive = false

    private val enableAutoAdssheet: TopAdsChooseTopUpAmountSheet? by lazy {
        TopAdsChooseTopUpAmountSheet.newInstance()
    }

    private val sheetNomianl: TopAdsChooseNominalBottomSheet? by lazy {
        TopAdsChooseNominalBottomSheet.newInstance()
    }

    override fun getScreenName(): String {
        return TopAdsEditAutoTopUpFragment::class.java.name
    }

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(TopAdsAutoTopUpViewModel::class.java)
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_dash_fragment_edit_auto_topup, container, false)
        loader = view.findViewById(R.id.loader)
        switchAutoTopupStatus = view.findViewById(R.id.auto_topup_status)
        selectCreditCard = view.findViewById(R.id.selectCreditCard)
        creditDropMenu = view.findViewById(R.id.creditDropMenu)
        topupAmount = view.findViewById(R.id.topupAmount)
        bonusText = view.findViewById(R.id.bonusText)
        tooltip = view.findViewById(R.id.tooltip)
        dedAmount = view.findViewById(R.id.dedAmount)
        offLayout = view.findViewById(R.id.offLayout)
        autoTopUpCreditHistoryLayout = view.findViewById(R.id.autoTopUpCreditHistoryLayout)
        autoTopUpCreditTipsUnify = view.findViewById(R.id.autoTopUpCreditTipsUnify)
        editAutoTopUpButton = view.findViewById(R.id.editAutoTopUpButton)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAutoTopUpStatus.observe(viewLifecycleOwner){
            if (it is Success) {
                onSuccessGetAutoTopUp(it.data)
            }
        }
        viewModel.isUserWhitelisted.observe(viewLifecycleOwner){
            if (it is Success){
                showOldFlow = !it.data
            } else if (it is Fail){
                showOldFlow = true
            }

            setLayoutOnToggle(isAutoTopUpActive)
            loader?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
        userSession = UserSession(context)
        viewModel.statusSaveSelection.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseSaving -> {
                    handleResponseSaving(it)
                }
            }
        })

        switchAutoTopupStatus?.setOnClickListener {
            if (switchAutoTopupStatus?.isChecked != false) {
                if (showOldFlow) showOldTopUpBottomSheet()
                else showNewAutoTopUpFlow()
            } else {
                showConfirmationDialog()
            }
            TopadsTopupTracker.clickToggleOnOff(switchAutoTopupStatus?.isChecked == true)
        }

        editAutoTopUpButton?.setOnClickListener {
            showNewAutoTopUpFlow(isAutoTopUpActive = true, isShowEditHistory = true)
        }

        creditDropMenu?.setOnClickListener {
            sheetNomianl?.setTitle(context?.resources?.getString(R.string.topads_dash_pick_nominal) ?: "")
            sheetNomianl?.show(childFragmentManager, null, false, selectedItem.id)
            sheetNomianl?.onSavedAutoTopUp = { pos ->
                if (pos != -1)
                    saveSelection(pos, TYPE_NOMINAL)
            }
            TopadsTopupTracker.clickSaldoDropdownList()
        }
        tooltip?.setOnClickListener {
            val view1 = View.inflate(context, R.layout.topads_dash_sheet_info, null)
            val bottomSheet = BottomSheetUnify()
            bottomSheet.setTitle(getString(R.string.toapds_dash_tooltip_title))
            bottomSheet.setChild(view1)
            bottomSheet.show(childFragmentManager, "")
        }
    }

    private fun showNewAutoTopUpFlow(isAutoTopUpActive:Boolean = false, isShowEditHistory: Boolean = false) {
        val sheet = TopAdsChooseCreditBottomSheet.newInstance().also {
            it.isAutoTopUpSelected = true
            it.isAutoTopUpActive = isAutoTopUpActive
            it.isShowEditHistory = isShowEditHistory
            it.isFullpage = true
        }
        sheet.show(childFragmentManager)
        sheet.onCancel = {
            setLayoutOnToggle(false)
        }
        sheet.onSaved = { _, isAutoAdsSaved ->
            if (isAutoAdsSaved) {
                showToastSuccess(TYPE_AUTO_TOP_CREDIT_ENABLED)
                loadData()
            }
        }
    }

    private fun showOldTopUpBottomSheet() {
        enableAutoAdssheet?.show(childFragmentManager, autoTopupStatus, bonus)
        enableAutoAdssheet?.onCancel = {
            setLayoutOnToggle(false)
        }
        enableAutoAdssheet?.onSaved = { pos ->
            if (pos != -1)
                saveSelection(pos, TYPE_BOTTOMSHEET)
        }
    }

    private fun initView() {
        view?.findViewById<RecyclerView>(R.id.layoutOtomatis)?.apply {
            adapter = TopAdsCreditNonAktifanAdapter()
        }
    }

    private fun loadData() {
        loader?.visibility = View.VISIBLE
        viewModel.getAutoTopUpStatusFull()
    }

    private fun saveSelection(pos: Int, typeBottomsheet: Int) {
        setLayoutOnToggle(true)
        showToastSuccess(typeBottomsheet)
        autoTopupStatus?.availableNominals?.let {
            selectedItem = if (it.size < pos) it[0] else it[pos]
        }
        viewModel.saveSelection(switchAutoTopupStatus?.isChecked == true, selectedItem)
        setupText()

    }

    private fun setupText() {
        bonusText?.text =
            Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount),
                convertToCurrency(calculatePercentage(selectedItem.priceFmt.removeCommaRawString(),
                    bonus).toLong())))
        topupAmount?.text = selectedItem.priceFmt
        dedAmount?.text = selectedItem.minCreditFmt
    }

    private fun onSuccessGetAutoTopUp(data: AutoTopUpStatus) {
        selectedItem = data.selectedPrice
        autoTopupStatus = data
        bonus = data.statusBonus
        setupText()
        isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        autoTopUpCreditHistoryTriple = viewModel.getAutoTopUpCreditHistoryData(data)
        viewModel.getWhiteListedUser()
    }

    private fun handleResponseSaving(r: ResponseSaving?) {
        sendResultIntentOk()
    }

    private fun showToastSuccess(type: Int) {
        view?.let {
            val toast = when (type) {
                TYPE_BOTTOMSHEET -> {
                    getString(R.string.topads_dash_auto_topup_activated_toast)
                }
                TYPE_AUTOTOPUP_DISABLED -> {
                    getString(R.string.topads_dash_auto_topup_disabled_toast)
                }
                TYPE_AUTO_TOP_CREDIT_ENABLED -> {
                    getString(R.string.topads_dash_auto_topup_activated_toast)
                }
                else -> {
                    getString(R.string.topads_dash_auto_topup_setting_is_saved_toast)
                }
            }
            if (type == TYPE_AUTO_TOP_CREDIT_ENABLED) {
                Toaster.build(
                    it,
                    toast,
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
                ).show()
            }else{
                Toaster.build(
                    it,
                    toast,
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                ).show()
            }
        }
    }

    private fun sendResultIntentOk() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent())
        }
    }

    private fun setLayoutOnToggle(toShow: Boolean) {
        if (toShow) {
            switchAutoTopupStatus?.isChecked = true
            selectCreditCard?.showWithCondition(showOldFlow)
            offLayout?.hide()
            if (!showOldFlow) setCreditHistoryData()
        } else {
            switchAutoTopupStatus?.isChecked = false
            selectCreditCard?.hide()
            offLayout?.show()
            autoTopUpCreditHistoryLayout?.hide()
        }
    }

    private fun setCreditHistoryData() {
        (autoTopUpCreditHistoryLayout?.findViewById<Typography>(R.id.selectedPrice))?.text =
            autoTopUpCreditHistoryTriple?.first
        (autoTopUpCreditHistoryLayout?.findViewById<Typography>(R.id.selectedBonus))?.text =
            HtmlCompat.fromHtml(
                String.format(
                    getString(R.string.topads_dash_auto_topup_bonus),
                    autoTopUpCreditHistoryTriple?.second
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        (autoTopUpCreditHistoryLayout?.findViewById<Typography>(R.id.maxCreditLimit))?.text =
            autoTopUpCreditHistoryTriple?.third?.first
        val autoTopUpFrequencySelectedText =
            getTextFromFrequency(autoTopUpCreditHistoryTriple?.third?.second)
        (autoTopUpCreditHistoryLayout?.findViewById<Typography>(R.id.selectedFrequncyTambahCredit))?.text =
            HtmlCompat.fromHtml(
                String.format(
                    getString(R.string.topads_dash_auto_topup_frequency),
                    autoTopUpFrequencySelectedText,
                    autoTopUpCreditHistoryTriple?.third?.second
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        autoTopUpCreditHistoryTriple?.third?.second?.let {
            val autoTopUpMaxCreditLimit = viewModel.getAutoTopUpMaxCreditLimit(
                it,
                autoTopUpCreditHistoryTriple?.first
            )
            autoTopUpCreditTipsUnify?.description =
                context?.let { context ->
                    Utils.getSpannableForTips(
                        context,
                        autoTopUpMaxCreditLimit
                    )
                } ?: ""
        }
        autoTopUpCreditHistoryLayout?.show()
    }

    private fun getTextFromFrequency(autoTopUpFrequencySelected: Int?): String {
        return when (autoTopUpFrequencySelected) {
            4 -> "Jarang"
            6 -> "Normal"
            8 -> "Sering"
            else -> "Normal"
        }
    }

    private fun showConfirmationDialog() {
        var autoTopupEnabled = true
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setDescription(
                String.format(
                    it.getString(R.string.topads_dash_auto_topup_off_dialog_desc),
                    "$bonus%"
                )
            )
            dialog.setTitle(it.getString(R.string.topads_dash_auto_topup_off_dialog_title))
            dialog.setPrimaryCTAText(it.getString(R.string.topads_dash_auto_topup_off_dialog_cancel_btn))
            dialog.setSecondaryCTAText(it.getString(R.string.topads_dash_auto_topup_off_dialog_ok_btn))
            dialog.setPrimaryCTAClickListener {
                TopadsTopupTracker.clickTetapGunakan()
                dialog.dismiss()
                setLayoutOnToggle(true)
            }
            dialog.setSecondaryCTAClickListener {
                TopadsTopupTracker.clickYaNonaktifkan()
                autoTopupEnabled = false
                dialog.dismiss()
                viewModel.saveSelection(switchAutoTopupStatus?.isChecked == true, selectedItem)
                setLayoutOnToggle(false)
                showToastSuccess(TYPE_AUTOTOPUP_DISABLED)
            }
            dialog.setOnDismissListener {
                if (autoTopupEnabled)
                    setLayoutOnToggle(true)
            }
            dialog.show()
        }
    }

    companion object {
        private const val TYPE_BOTTOMSHEET = 1
        private const val TYPE_AUTOTOPUP_DISABLED = 2
        private const val TYPE_NOMINAL = 0
        private const val TYPE_AUTO_TOP_CREDIT_ENABLED = 3

        @JvmStatic
        fun createInstance(): Fragment = TopAdsEditAutoTopUpFragment()
    }

}
