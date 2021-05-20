package com.tokopedia.recharge_credit_card.bottomsheet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.di.RechargeCCInstance
import com.tokopedia.recharge_credit_card.adapter.CreditCardBankAdapter
import com.tokopedia.recharge_credit_card.analytics.CreditCardAnalytics
import com.tokopedia.recharge_credit_card.util.RechargeCCGqlQuery
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CCBankListBottomSheet(val categoryId: String) : BottomSheetUnify() {

    private lateinit var adapter: CreditCardBankAdapter
    private lateinit var childView: View
    private lateinit var rechargeCCViewModel: RechargeCCViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var descBankList: TextView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var creditCardAnalytics: CreditCardAnalytics
    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        initViewModel()
        initBottomSheet()
        initAdapter()
        initView()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setTitle(getString(R.string.cc_bank_list_title))
        setCloseClickListener {
            dismiss()
        }

        childView = View.inflate(requireContext(), R.layout.bottom_sheet_cc_bank_list, null)
        descBankList = childView.findViewById(R.id.desc_bank_list)
        recyclerView = childView.findViewById(R.id.recycler_view_bank_list)
        setChild(childView)
    }

    private fun initInjector() {
        activity?.let {
            val creditCardComponent = RechargeCCInstance.getComponent(it.application)
            creditCardComponent.inject(this)
        }
    }

    private fun initViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            rechargeCCViewModel = viewModelProvider.get(RechargeCCViewModel::class.java)
        }
    }

    private fun initAdapter() {
        adapter = CreditCardBankAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun initView() {
        descBankList.text = getString(R.string.cc_desc_bank_list)
        rechargeCCViewModel.getListBank(RechargeCCGqlQuery.creditCardBankList, 26)
        rechargeCCViewModel.rechargeCCBankList.observe(this, Observer {
            adapter = CreditCardBankAdapter(it.bankList)
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
        })
        rechargeCCViewModel.errorCCBankList.observe(this, Observer {
            Toast.makeText(context, ErrorHandler.getErrorMessage(requireContext(), it), Toast.LENGTH_SHORT).show()
        })

        creditCardAnalytics.impressionBankList(categoryId,"none", userSession.userId)
    }
}