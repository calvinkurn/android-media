package com.tokopedia.payment.setting.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.di.DaggerSettingListPaymentComponent
import com.tokopedia.payment.setting.list.di.SettingListPaymentModule
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.PaymentSettingRouter
import javax.inject.Inject
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_setting_list_payment.view.*


class SettingListPaymentFragment : BaseListFragment<SettingListPaymentModel, SettingListPaymentAdapterTypeFactory>(),
        SettingListPaymentContract.View, SettingListEmptyViewHolder.ListenerEmptyViewHolder {

    @Inject
    lateinit var settingListPaymentPresenter : SettingListPaymentPresenter

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickAddCard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
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
        fun createInstance() : SettingListPaymentFragment{
            return SettingListPaymentFragment()
        }
    }

}
