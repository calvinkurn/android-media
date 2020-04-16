package com.tokopedia.settingnotif.usersetting.view.fragment.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.network.constant.ErrorNetMessage.MESSAGE_ERROR_SERVER
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.di.DaggerUserSettingComponent
import com.tokopedia.settingnotif.usersetting.di.UserSettingModule
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.view.activity.ParentActivity
import com.tokopedia.settingnotif.usersetting.view.adapter.ItemAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactoryImpl
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingBigDividerDecoration
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingDividerDecoration
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

abstract class SettingFieldFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        SettingFieldAdapter.SettingFieldAdapterListener,
        SettingFieldContract.View,
        SectionItemListener {

    @Inject lateinit var presenter: SettingFieldContract.Presenter
    @Inject lateinit var userSession: UserSessionInterface

    /*
    * a flag for preventing request network if needed
    * @example: SMS section item
    * */
    protected var isRequestData = true

    /*
    * notificationType for graph query consume
    * it will be use it into set user setting
    *  */
    abstract fun getNotificationType(): String

    @RawRes abstract fun getGqlRawQuery(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
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
        getRecyclerView(view).also {
            if (it is VerticalRecyclerView) {
                it.clearItemDecoration()
                it.addItemDecoration(NotifSettingDividerDecoration(context))
                it.addItemDecoration(NotifSettingBigDividerDecoration(context))
                it.setHasFixedSize(true)
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        val adapter = ItemAdapter(
                getNotificationType(),
                this,
                adapterTypeFactory as SettingFieldTypeFactory,
                null
        )
        return adapter as BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>
    }

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        if (!isRequestData) return
        presenter.requestUpdateUserSetting(notificationType, updatedSettingIds)
        presenter.requestUpdateMoengageUserSetting(updatedSettingIds)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return SettingFieldTypeFactoryImpl(this, userSession)
    }

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        presenter.loadUserSettings()
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
        activity?.let {
            renderList(emptyList())
        }
    }

    private fun showMessage(@StringRes messageRes: Int) {
        activity?.let {
            val message = it.getString(messageRes)
            showMessage(message)
        }
    }

    private fun showMessage(message: String) {
        view?.let {
            Toaster.showNormal(it, message, Snackbar.LENGTH_SHORT)
        }
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val application = (activity as Activity).application as BaseMainApplication
            val baseAppComponent = application.baseAppComponent
            val userSettingComponent = DaggerUserSettingComponent
                    .builder()
                    .baseAppComponent(baseAppComponent)
                    .userSettingModule(UserSettingModule(context, getGqlRawQuery()))
                    .build()
            userSettingComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onItemClicked() {
        activity?.let {
            (it as ParentActivity).openSellerFiled()
        }
    }

    protected fun isNotificationEnabled(): Boolean {
        return context?.let {
            NotificationManagerCompat
                    .from(it)
                    .areNotificationsEnabled()
        }?: true
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun onItemClicked(item: Visitable<*>?) = Unit

    companion object {
        // these type will consume for graphql params
        const val TYPE_SELLER_NOTIF = "sellernotif"
        const val TYPE_PUSH_NOTIF = "pushnotif"
        const val TYPE_EMAIL = "email"
        const val TYPE_SMS = "sms"
    }

}