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
import javax.inject.Inject

class SettingListPaymentFragment : BaseListFragment<SettingListPaymentModel, SettingListPaymentAdapterTypeFactory>(), SettingListPaymentContract.View {

    @Inject
    lateinit var settingListPaymentPresenter : SettingListPaymentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_list_payment, container, false)
        return view
    }

    override fun getAdapterTypeFactory(): SettingListPaymentAdapterTypeFactory {
        return SettingListPaymentAdapterTypeFactory()
    }

    override fun onItemClicked(t: SettingListPaymentModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        settingListPaymentPresenter.getCreditCardList()
    }

}
