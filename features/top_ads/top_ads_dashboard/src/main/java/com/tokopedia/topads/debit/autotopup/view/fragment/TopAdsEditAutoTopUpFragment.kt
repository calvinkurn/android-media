package com.tokopedia.topads.debit.autotopup.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.Utils.calculatePercentage
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.extensions.selectedPrice
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsCreditNonAktifanAdapter
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseNominalBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseTopUpAmountSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var userSession: UserSession? = null
    private var selectedItem = AutoTopUpItem()
    private var bonus = 1.0
    private var autoTopupStatus: AutoTopUpStatus? = null

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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAutoTopUpStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAutoTopUp(it.data)
            }
        })
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
                enableAutoAdssheet?.show(childFragmentManager, autoTopupStatus, bonus)
                enableAutoAdssheet?.onCancel = {
                    setLayoutOnToggle(false)
                }
                enableAutoAdssheet?.onSaved = { pos ->
                    if (pos != -1)
                        saveSelection(pos, TYPE_BOTTOMSHEET)
                }
            } else {
                showConfirmationDialog()
            }
            TopadsTopupTracker.clickToggleOnOff(switchAutoTopupStatus?.isChecked == true)
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
        val isAutoTopUpActive =
            (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        setLayoutOnToggle(isAutoTopUpActive)
        loader?.visibility = View.GONE
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
                else -> {
                    getString(R.string.topads_dash_auto_topup_setting_is_saved_toast)
                }
            }
            Toaster.make(it, toast, Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL)
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
            selectCreditCard?.visibility = View.VISIBLE
            offLayout?.visibility = View.GONE
        } else {
            switchAutoTopupStatus?.isChecked = false
            selectCreditCard?.visibility = View.GONE
            offLayout?.visibility = View.VISIBLE
        }
    }

    private fun showConfirmationDialog() {
        var autoTopupEnabled = true
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setDescription(String.format(it.getString(R.string.topads_dash_auto_topup_off_dialog_desc),
                "$bonus%"))
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

        @JvmStatic
        fun createInstance(): Fragment = TopAdsEditAutoTopUpFragment()
    }

}
