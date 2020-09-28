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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE_STATUS
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.extensions.selectedPrice
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsAutoTopUpChipsAdapter
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseTopUpAmountSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_dash_choose_topup_layout.*
import kotlinx.android.synthetic.main.topads_dash_fragment_edit_auto_topup.*
import javax.inject.Inject

/**
 * Created by Pika on 22/9/20.
 */

class TopAdsEditAutoTopUpFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var userSession: UserSession? = null
    private lateinit var dataItem: List<DataCredit>
    private lateinit var creditResponse: CreditResponse
    private var selectedItem = AutoTopUpItem()
    private var bonus = 1

    private val adapter: TopAdsAutoTopUpChipsAdapter? by lazy {
        context?.run { TopAdsAutoTopUpChipsAdapter() }
    }
    private val sheet: TopAdsChooseTopUpAmountSheet? by lazy {
        TopAdsChooseTopUpAmountSheet.newInstance()
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
        userSession = UserSession(context)
        creditOptionsRV?.adapter = adapter
        creditOptionsRV?.layoutManager = GridLayoutManager(context, 2)
        viewModel.getAutoTopUpStatusFull(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_package_auto_topup))
        viewModel.populateCreditList(userSession?.shopId ?: "", ::onSuccessCreditInfo)
        adapter?.setListener(object : TopAdsAutoTopUpChipsAdapter.OnCreditOptionItemClicked {
            override fun onItemClicked(position: Int) {
                saveChoice?.visibility = View.VISIBLE
                bonusTxt.text = Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount),
                        convertToCurrency(calculateBonus(dataItem[position].productPrice).toLong())))
                if (position == 0 && dataItem[position].productPrice == CREDIT_AMOUNT) {
                    setinitialState()
                } else {
                    dedAmount?.text = dataItem[position].minCredit
                    deductionDesc?.visibility = View.VISIBLE
                }
            }
        })

        viewModel.statusSaveSelection.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseSaving -> {
                    handleResponseSaving(it)
                }
            }
        })

        auto_topup_status?.setOnClickListener {
            if (auto_topup_status?.isChecked != false) {
                sheet?.show(childFragmentManager, creditResponse, bonus)
                sheet?.onCancel = {
                    setLayoutOnToggle(false)
                }
                sheet?.onSaved = { pos ->
                    saveSelection(pos)
                }
            } else {
                showConfirmationDialog()
            }
        }
        saveChoice?.setOnClickListener {
            viewModel.saveSelection(GraphqlHelper
                    .loadRawString(resources, R.raw.gql_topads_save_auto_topup_selection),
                    auto_topup_status.isChecked, selectedItem)
            showToastSuccess(TYPE_BOTTOMSHEET - 1)
        }
    }

    private fun saveSelection(pos: Int) {
        adapter?.setSelected(pos)
        setLayoutOnToggle(true)
        showToastSuccess(TYPE_BOTTOMSHEET)
        saveChoice?.visibility = View.VISIBLE
        viewModel.saveSelection(GraphqlHelper
                .loadRawString(resources, R.raw.gql_topads_save_auto_topup_selection),
                auto_topup_status.isChecked, selectedItem)

        if (pos == 0 && dataItem[pos].productPrice == CREDIT_AMOUNT) {
            setinitialState()
        } else {
            dedAmount?.text = dataItem[pos].minCredit
        }
    }

    private fun setinitialState() {
        saveChoice?.visibility = View.INVISIBLE
        dedAmount?.text = MIN_CREDIT_25
        deductionDesc?.visibility = View.GONE
    }

    private fun onSuccessGetAutoTopUp(data: AutoTopUpStatus) {
        selectedItem = data.selectedPrice
        bonus = data.statusBonus
        val isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        setLayoutOnToggle(isAutoTopUpActive)

    }

    private fun calculateBonus(productPrice: String): Int {
        val price = productPrice.removeCommaRawString()
        var result = 0
        if (price.isNotEmpty())
            result = (price.toInt() * bonus) / 100
        return result
    }


    private fun handleResponseSaving(r: ResponseSaving?) {
        saveChoice?.visibility = View.INVISIBLE
        sendResultIntentOk()

    }

    private fun showToastSuccess(type: Int) {
        view?.let {
            val toast = if (type == TYPE_BOTTOMSHEET) {
                getString(R.string.topads_dash_auto_topup_activated_toast)
            } else {
                getString(R.string.topads_dash_auto_topup_setting_is_saved_toast)
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
            selectCreditCard?.visibility = View.VISIBLE
            offLayout?.visibility = View.GONE
        } else {
            auto_topup_status?.isChecked = false
            selectCreditCard?.visibility = View.GONE
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
                auto_topup_status?.isChecked = true
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
                viewModel.saveSelection(GraphqlHelper
                        .loadRawString(resources, R.raw.gql_topads_save_auto_topup_selection),
                        auto_topup_status.isChecked, selectedItem)
                setLayoutOnToggle(false)
            }
            dialog.show()
        }
    }

    private fun onSuccessCreditInfo(data: CreditResponse) {
        creditResponse = data
        dataItem = data.credit
        adapter?.setChipData(data)
        bonusTxt.text = Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount),
                convertToCurrency(calculateBonus(data.credit.firstOrNull()?.productPrice
                        ?: ACTIVE_STATUS).toLong())))
        data.credit.firstOrNull().let {
            if (it?.productPrice == CREDIT_AMOUNT) {
                setinitialState()
            }
        }
    }

    companion object {
        private const val TYPE_BOTTOMSHEET = 1
        const val MIN_CREDIT_25 = "Rp 15.000"
        const val CREDIT_AMOUNT = "25.000"

        @JvmStatic
        fun createInstance(): Fragment = TopAdsEditAutoTopUpFragment()
    }

}