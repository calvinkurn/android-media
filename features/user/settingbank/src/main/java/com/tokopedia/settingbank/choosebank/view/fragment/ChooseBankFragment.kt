package com.tokopedia.settingbank.choosebank.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.analytics.AddEditBankAnalytics.Companion.SCREEN_NAME_CHOOSE_BANK
import com.tokopedia.settingbank.addeditaccount.view.listener.ChooseBankContract
import com.tokopedia.settingbank.choosebank.di.ChooseBankDependencyInjector
import com.tokopedia.settingbank.choosebank.view.adapter.BankAdapter
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactoryImpl
import com.tokopedia.settingbank.choosebank.view.listener.BankListener
import com.tokopedia.settingbank.choosebank.view.presenter.ChooseBankPresenter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import kotlinx.android.synthetic.main.fragment_choose_bank.*

/**
 * @author by nisie on 6/22/18.
 */

class ChooseBankFragment : ChooseBankContract.View, BankListener, BaseDaggerFragment() {

    lateinit var adapter: BankAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var presenter: ChooseBankPresenter


    override fun getScreenName(): String {
        return SCREEN_NAME_CHOOSE_BANK
    }

    override fun initInjector() {
        presenter = ChooseBankDependencyInjector.Companion.inject(activity.applicationContext)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_choose_bank, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        presenter.getBankList("")
    }

    private fun setupView() {
        val adapterTypeFactory = BankTypeFactoryImpl(this)
        val listBank = ArrayList<Visitable<*>>()
        adapter = BankAdapter(adapterTypeFactory, listBank)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bank_list_rv.layoutManager = linearLayoutManager
        bank_list_rv.adapter = adapter

//        bank_list_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val index = linearLayoutManager.findLastVisibleItemPosition()
//                if (adapter.checkLoadMore(index)) {
//                    //TODO : LOAD MORE
//                }
//            }
//        })
    }

    override fun onBankSelected(adapterPosition: Int, element: BankViewModel?) {
        //TODO SET RESULT
        adapter.setSelected(adapterPosition)
        activity.finish()
    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onErrorGetBankList(errorMessage: String?) {

    }

    override fun onSuccessGetBankList(listBank: BankListViewModel) {
       adapter.addList(listBank)
    }
}