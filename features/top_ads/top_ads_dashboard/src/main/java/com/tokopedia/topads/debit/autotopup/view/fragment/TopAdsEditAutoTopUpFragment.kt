package com.tokopedia.topads.debit.autotopup.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseNominalBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseTopUpAmountSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_dash_auto_topup_off_layout.*
import kotlinx.android.synthetic.main.topads_dash_fragment_edit_auto_topup.*
import javax.inject.Inject

/**
 * Created by Pika on 22/9/20.
 */

class TopAdsEditAutoTopUpFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var userSession: UserSession? = null
    private var selectedItem = AutoTopUpItem()
    private var bonus = 1.0
    private var autoTopupStatus: AutoTopUpStatus? = null
    private var autoTopupEnabled = true

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_fragment_edit_auto_topup, container, false)
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
        loadData()
        userSession = UserSession(context)
        viewModel.statusSaveSelection.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseSaving -> {
                    handleResponseSaving(it)
                }
            }
        })

        auto_topup_status?.setOnClickListener {
            if (auto_topup_status?.isChecked != false) {
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
        }

        creditDropMenu?.setOnClickListener {
            sheetNomianl?.setTitle(resources.getString(R.string.topads_dash_pick_nominal))
            sheetNomianl?.show(childFragmentManager, null, false, selectedItem.id)
            sheetNomianl?.onSavedAutoTopUp = { pos ->
                if (pos != -1)
                    saveSelection(pos, TYPE_NOMINAL)
            }
        }
        tooltip?.setOnClickListener {
            val view1 = View.inflate(context, R.layout.topads_dash_sheet_info, null)
            val bottomSheet = BottomSheetUnify()
            bottomSheet.setTitle(getString(R.string.toapds_dash_tooltip_title))
            bottomSheet.setChild(view1)
            bottomSheet.show(childFragmentManager, "")
        }
    }

    private fun loadData() {
        loader.visibility = View.VISIBLE
        viewModel.getAutoTopUpStatusFull()
    }

    private fun saveSelection(pos: Int, typeBottomsheet: Int) {
        setLayoutOnToggle(true)
        showToastSuccess(typeBottomsheet)
        autoTopupStatus?.availableNominals?.let {
            selectedItem = it[pos]
        }
        viewModel.saveSelection(auto_topup_status.isChecked, selectedItem)
        setupText()

    }

    private fun setupText() {
        bonusText.text = Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount),
                convertToCurrency(calculatePercentage(selectedItem.priceFmt.removeCommaRawString(), bonus).toLong())))
        topupAmount?.text = selectedItem.priceFmt
        dedAmount?.text = selectedItem.minCreditFmt
    }

    private fun onSuccessGetAutoTopUp(data: AutoTopUpStatus) {
        selectedItem = data.selectedPrice
        autoTopupStatus = data
        bonus = data.statusBonus
        setupText()
        val isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
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
            auto_topup_status?.isChecked = true
            activeText.setText(R.string.topads_active)
            activeText.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_select_color_checked))
            selectCreditCard?.visibility = View.VISIBLE
            offLayout?.visibility = View.GONE
        } else {
            auto_topup_status?.isChecked = false
            activeText.setText(R.string.topads_inactive)
            activeText.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_text_disabled))
            selectCreditCard?.visibility = View.GONE
            desc2.text = Html.fromHtml(String.format(resources.getString(R.string.topads_adash_auto_topup_off_desc2), "$bonus%"))
            offLayout?.visibility = View.VISIBLE
        }
    }

    private fun showConfirmationDialog() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setDescription(String.format(it.getString(R.string.topads_dash_auto_topup_off_dialog_desc), "$bonus%"))
            dialog.setTitle(it.getString(R.string.topads_dash_auto_topup_off_dialog_title))
            dialog.setPrimaryCTAText(it.getString(R.string.topads_dash_auto_topup_off_dialog_cancel_btn))
            dialog.setSecondaryCTAText(it.getString(R.string.topads_dash_auto_topup_off_dialog_ok_btn))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                setLayoutOnToggle(true)
            }
            dialog.setSecondaryCTAClickListener {
                autoTopupEnabled = false
                dialog.dismiss()
                viewModel.saveSelection(auto_topup_status.isChecked, selectedItem)
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