package com.tokopedia.payment.setting.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.di.DaggerSettingListPaymentComponent
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.PaymentSettingRouter
import javax.inject.Inject
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.payment.setting.add.AddCreditCardActivity
import com.tokopedia.payment.setting.authenticate.AuthenticateCreditCardActivity
import com.tokopedia.payment.setting.detail.DetailCreditCardActivity
import com.tokopedia.payment.setting.list.di.SettingListPaymentModule
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentContract
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentPresenter
import com.tokopedia.payment.setting.list.view.adapter.SettingListEmptyViewHolder
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory
import kotlinx.android.synthetic.main.fragment_setting_list_payment.view.*


class SettingListPaymentFragment : BaseListFragment<SettingListPaymentModel, SettingListPaymentAdapterTypeFactory>(),
        SettingListPaymentContract.View, SettingListEmptyViewHolder.ListenerEmptyViewHolder {

    @Inject
    lateinit var settingListPaymentPresenter : SettingListPaymentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.run {
            GraphqlClient.init(this)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_list_payment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 -> dividerItemDecoration.setDrawable(it1) }
            getRecyclerView(view).addItemDecoration(dividerItemDecoration)
        }
        view.authenticateCreditCard.setOnClickListener{
            activity?.run {
                this@SettingListPaymentFragment.startActivityForResult(AuthenticateCreditCardActivity.createIntent(this), REQUEST_CODE_AUTH_CREDIT_CARD)
            }
        }
        updateViewCounter(adapter.dataSize)
    }

    private fun updateViewCounter(size : Int) {
        view?.counterCreditCard?.setText(getString(R.string.payment_label_saved_card, size))
    }

    override fun getAdapterTypeFactory(): SettingListPaymentAdapterTypeFactory {
        return SettingListPaymentAdapterTypeFactory(context?.applicationContext as PaymentSettingRouter, this)
    }

    override fun onItemClicked(t: SettingListPaymentModel?) {
        activity?.run {
            this@SettingListPaymentFragment.startActivityForResult(DetailCreditCardActivity.createIntent(this, t), REQUEST_CODE_DETAIL_CREDIT_CARD)
        }
    }

    override fun onClickAddCard() {
        activity?.run {
            this@SettingListPaymentFragment.startActivityForResult(AddCreditCardActivity.createIntent(this), REQUEST_CODE_ADD_CREDIT_CARD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            when (requestCode){
                REQUEST_CODE_DETAIL_CREDIT_CARD, REQUEST_CODE_ADD_CREDIT_CARD, REQUEST_CODE_AUTH_CREDIT_CARD -> loadInitialData()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun showGetListError(throwable: Throwable?) {
        super.showGetListError(throwable)
    }

    override fun renderList(list: MutableList<SettingListPaymentModel>) {
        updateViewCounter(list.size)
        if(list.size > 0 && list.size < 4){
            list.add(SettingListAddCardModel())
        }
        super.renderList(list)
    }

    override fun initInjector() {
        DaggerSettingListPaymentComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .settingListPaymentModule(SettingListPaymentModule())
                .build()
                .inject(this)
        settingListPaymentPresenter.attachView(this)
    }

    override fun onDestroy() {
        settingListPaymentPresenter.detachView()
        super.onDestroy()
    }

    override fun loadData(page: Int) {
        settingListPaymentPresenter.getCreditCardList(resources)
    }

    companion object {
        val REQUEST_CODE_DETAIL_CREDIT_CARD = 4213
        val REQUEST_CODE_ADD_CREDIT_CARD = 4273
        val REQUEST_CODE_AUTH_CREDIT_CARD = 4275

        fun createInstance() : SettingListPaymentFragment {
            return SettingListPaymentFragment()
        }
    }

}
