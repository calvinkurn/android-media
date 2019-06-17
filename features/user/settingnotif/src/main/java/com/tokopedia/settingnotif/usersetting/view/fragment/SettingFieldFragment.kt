package com.tokopedia.settingnotif.usersetting.view.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.settingnotif.usersetting.di.DaggerUserSettingComponent
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactoryImpl
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import javax.inject.Inject

abstract class SettingFieldFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(), SettingFieldContract.View {

    @Inject
    lateinit var presenter: SettingFieldContract.Presenter

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val userSettingComponent = DaggerUserSettingComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            userSettingComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            setupToolbar()
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        val adapter = SettingFieldAdapter<Visitable<SettingFieldTypeFactory>>(adapterTypeFactory as SettingFieldTypeFactory, null)
        return adapter as BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return SettingFieldTypeFactoryImpl()
    }

    override fun onItemClicked(item: Visitable<*>?) {

    }

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        presenter.getDummyData()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        renderList(data.data)
    }
}