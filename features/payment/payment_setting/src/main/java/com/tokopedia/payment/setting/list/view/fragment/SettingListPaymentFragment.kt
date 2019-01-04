package com.tokopedia.payment.setting.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
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
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.payment.setting.add.view.activity.AddCreditCardActivity
import com.tokopedia.payment.setting.authenticate.view.activity.AuthenticateCreditCardActivity
import com.tokopedia.payment.setting.detail.view.activity.DetailCreditCardActivity
import com.tokopedia.payment.setting.list.di.SettingListPaymentModule
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentContract
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentPresenter
import com.tokopedia.payment.setting.list.view.adapter.SettingListEmptyViewHolder
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory
import kotlinx.android.synthetic.main.fragment_setting_list_payment.*
import kotlinx.android.synthetic.main.fragment_setting_list_payment.view.*
import com.tokopedia.design.component.Dialog


class SettingListPaymentFragment : BaseListFragment<SettingListPaymentModel, SettingListPaymentAdapterTypeFactory>(),
        SettingListPaymentContract.View, SettingListEmptyViewHolder.ListenerEmptyViewHolder {

    @Inject
    lateinit var settingListPaymentPresenter : SettingListPaymentPresenter
    var paymentSettingRouter: PaymentSettingRouter? = null
    val progressDialog : ProgressDialog by lazy { ProgressDialog(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.run {
            GraphqlClient.init(this)
        }
        super.onCreate(savedInstanceState)
        paymentSettingRouter = activity?.application as PaymentSettingRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_list_payment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.setMessage(getString(R.string.title_loading))
        context?.let {
            val dividerItemDecoration = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(it, R.drawable.divider_list_card)?.let { it1 -> dividerItemDecoration.setDrawable(it1) }
            getRecyclerView(view).addItemDecoration(dividerItemDecoration)
        }
        view.authenticateCreditCard.setOnClickListener{
            activity?.run {
                settingListPaymentPresenter.checkVerificationPhone()
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
                REQUEST_CODE_VERIF_PHONE -> onSuccessVerifPhone()
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
        hideAuthPaymentView()
        super.showGetListError(throwable)
    }

    override fun renderList(list: MutableList<SettingListPaymentModel>) {
        updateViewCounter(list.size)
        if(list.size > 0 && list.size < 4){
            list.add(SettingListAddCardModel())
        }
        super.renderList(list)
        showAuthPaymentView()
    }

    override fun initInjector() {
        DaggerSettingListPaymentComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .settingListPaymentModule(SettingListPaymentModule())
                .build()
                .inject(this)
        settingListPaymentPresenter.attachView(this)
    }

    override fun showLoadingDialog() {
        progressDialog.show()
    }

    override fun hideLoadingDialog() {
        progressDialog.hide()
    }

    override fun onSuccessVerifPhone() {
        activity?.run {
            this@SettingListPaymentFragment.startActivityForResult(AuthenticateCreditCardActivity.createIntent(this), REQUEST_CODE_AUTH_CREDIT_CARD)
        }
    }

    override fun onNeedVerifPhone() {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.payment_label_title_dialog_verif_phone))
        dialog.setDesc(getString(R.string.payment_label_desc_dialog_verif))
        dialog.setBtnOk(getString(R.string.payment_label_continue_dialog_verif))
        dialog.setBtnCancel(getString(R.string.payment_label_cancel_dialog_verif))
        dialog.setOnOkClickListener {
            activity?.run {
                this@SettingListPaymentFragment.startActivityForResult(paymentSettingRouter?.getProfileSettingIntent(this), REQUEST_CODE_VERIF_PHONE)
            }
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        settingListPaymentPresenter.detachView()
        super.onDestroy()
    }

    override fun loadData(page: Int) {
        settingListPaymentPresenter.getCreditCardList(resources)
        hideAuthPaymentView()
    }

    private fun hideAuthPaymentView() {
        counterCreditCard?.visibility = View.GONE
        dividerListPayment?.visibility = View.GONE
        authenticateCreditCard?.visibility = View.GONE
    }

    private fun showAuthPaymentView() {
        counterCreditCard?.visibility = View.VISIBLE
        dividerListPayment?.visibility = View.VISIBLE
        authenticateCreditCard?.visibility = View.VISIBLE
    }

    companion object {
        val REQUEST_CODE_DETAIL_CREDIT_CARD = 4213
        val REQUEST_CODE_ADD_CREDIT_CARD = 4273
        val REQUEST_CODE_AUTH_CREDIT_CARD = 4275
        val REQUEST_CODE_VERIF_PHONE = 3734

        fun createInstance() : SettingListPaymentFragment {
            return SettingListPaymentFragment()
        }
    }

}
