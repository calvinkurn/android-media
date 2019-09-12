package com.tokopedia.settingnotif.usersetting.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.constant.ErrorNetMessage.MESSAGE_ERROR_SERVER
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.di.DaggerUserSettingComponent
import com.tokopedia.settingnotif.usersetting.di.UserSettingModule
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.view.activity.PushNotificationCheckerActivity
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactoryImpl
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingBigDividerDecoration
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingDividerDecoration
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

abstract class SettingFieldFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        SettingFieldAdapter.SettingFieldAdapterListener,
        SettingFieldContract.View {

    @Inject
    lateinit var presenter: SettingFieldContract.Presenter

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val baseAppComponent = ((activity as Activity).application as BaseMainApplication).baseAppComponent
            val userSettingComponent = DaggerUserSettingComponent.builder()
                    .baseAppComponent(baseAppComponent)
                    .userSettingModule(UserSettingModule(context, getGqlRawQuery()))
                    .build()

            userSettingComponent.inject(this)
            presenter.attachView(this)
        }
    }

    @RawRes
    abstract fun getGqlRawQuery(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            setupToolbar()
            setupRecyclerView(it)
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    private fun setupRecyclerView(view: View?) {
        val recyclerView = getRecyclerView(view)
        if (recyclerView is VerticalRecyclerView) {
            recyclerView.clearItemDecoration()
            recyclerView.addItemDecoration(NotifSettingDividerDecoration(context))
            recyclerView.addItemDecoration(NotifSettingBigDividerDecoration(context))
            recyclerView.setHasFixedSize(true)
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        val adapter = SettingFieldAdapter<Visitable<SettingFieldTypeFactory>>(
                getNotificationType(),
                this,
                adapterTypeFactory as SettingFieldTypeFactory,
                null
        )
        return adapter as BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>
    }

    abstract fun getNotificationType(): String

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        presenter.requestUpdateUserSetting(notificationType, updatedSettingIds)
        presenter.requestUpdateMoengageUserSetting(updatedSettingIds)
    }

    override fun goToPushNotificationCheckerPage() {
        RouteManager.route(context, ApplinkConst.PUSHNOTIFCHECKER)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return SettingFieldTypeFactoryImpl()
    }

    override fun onItemClicked(item: Visitable<*>?) {

    }

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        presenter.loadUserSettings()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        renderList(data.data)
    }

    override fun onSuccessSetUserSetting(data: SetUserSettingResponse) {
        showMessage(R.string.state_success_set_user_setting)
    }

    override fun onErrorSetUserSetting() {
        showMessage(R.string.state_failed_set_user_setting)
    }

    override fun onErrorGetUserSetting() {
        showMessage(MESSAGE_ERROR_SERVER)
        renderList(emptyList())
    }

    protected fun showMessage(@StringRes messageRes: Int) {
        val message = getString(messageRes)
        view?.let {
            Toaster.showNormal(it, message, Snackbar.LENGTH_SHORT)
        }
    }

    protected fun showMessage(message: String) {
        view?.let {
            Toaster.showNormal(it, message, Snackbar.LENGTH_SHORT)
        }
    }
}