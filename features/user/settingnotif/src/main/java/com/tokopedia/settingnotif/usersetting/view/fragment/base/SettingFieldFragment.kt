package com.tokopedia.settingnotif.usersetting.view.fragment.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER
import com.tokopedia.network.constant.ErrorNetMessage.MESSAGE_ERROR_SERVER
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.analytics.NotifSettingAnalytics.trackTroubleshooterClicked
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.di.DaggerUserSettingComponent
import com.tokopedia.settingnotif.usersetting.di.module.UserSettingModule
import com.tokopedia.settingnotif.usersetting.util.intent
import com.tokopedia.settingnotif.usersetting.view.activity.ParentActivity
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactoryImpl
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.listener.ActivationItemListener
import com.tokopedia.settingnotif.usersetting.view.listener.SectionItemListener
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState.GetSettingError
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState.SetSettingError
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingBigDividerDecoration
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingDividerDecoration
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

abstract class SettingFieldFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        SettingFieldAdapter.SettingFieldAdapterListener,
        SectionItemListener,
        ActivationItemListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var userSession: UserSessionInterface

    protected val settingViewModel: UserSettingViewModel by lazy {
        ViewModelProvider(
                this,
                viewModelFactory
        ).get(UserSettingViewModel::class.java)
    }

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

    // setting field adapter
    private val settingFieldAdapter by lazy(LazyThreadSafetyMode.NONE) {
        adapter as SettingFieldAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView(view)

        initObservable()
    }

    private fun initObservable() {
        settingViewModel.userSetting.observe(viewLifecycleOwner, Observer {
            onSuccessGetUserSetting(it)
        })
        settingViewModel.setUserSetting.observe(viewLifecycleOwner, Observer {
            onSuccessSetUserSetting()
        })
        settingViewModel.errorErrorState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is GetSettingError -> onErrorGetUserSetting()
                is SetSettingError -> onErrorSetUserSetting()
            }
        })
    }

    override fun requestUpdateUserSetting(
            notificationType: String,
            updatedSettingIds: List<Map<String, Any>>
    ) {
        if (!isRequestData) return

        settingViewModel.requestUpdateUserSetting(notificationType, updatedSettingIds)

        for (setting in updatedSettingIds) {
            val name = setting[SettingViewHolder.PARAM_SETTING_KEY]
            val value = setting[SettingViewHolder.PARAM_SETTING_VALUE]

            if (name !is String || value !is Boolean) return

            settingViewModel.requestUpdateMoengageUserSetting(name, value)
        }
    }

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        settingViewModel.loadUserSettings()
    }

    protected open fun onSuccessGetUserSetting(data: UserSettingDataView) {
        renderList(data.data)
    }

    private fun onSuccessSetUserSetting() {
        showMessage(R.string.state_success_set_user_setting)
    }

    private fun onErrorSetUserSetting() {
        showMessage(R.string.state_failed_set_user_setting)
    }

    private fun onErrorGetUserSetting() {
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

    override fun onItemClicked() {
        SellerMigrationTracking.trackClickNotificationSeller(userSession.userId.orEmpty())
        activity?.let {
            (it as ParentActivity).openPushNotificationFiled()
        }
    }

    protected fun permissionValidation(
            validation: Boolean,
            pinnedItem: NotificationActivation,
            lastStateItems: List<ParentSetting>,
            isRequiredPinnedActivation: Boolean = true
    ) {
        with(settingFieldAdapter) {
            if (validation) {
                if (isRequiredPinnedActivation) {
                    removePinnedActivation()
                }
                enableSwitchComponent(lastStateItems)
            } else {
                if (isRequiredPinnedActivation) {
                    addPinnedActivation(pinnedItem)
                }
                disableSwitchComponent()
            }
        }
    }

    protected fun permissionValidationNotification(
            notificationEnabled: Boolean,
            pinnedItem: NotificationActivation,
            lastStateItems: List<ParentSetting>
    ) {
        with(settingFieldAdapter) {
            addPinnedActivation(pinnedItem)
            if (notificationEnabled) {
                enableSwitchComponent(lastStateItems)
            } else {
                disableSwitchComponent()
            }
        }
    }

    protected fun isNotificationEnabled(): Boolean {
        return context?.let {
            NotificationManagerCompat
                    .from(it)
                    .areNotificationsEnabled()
        }?: true
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    private fun setupRecyclerView(view: View?) {
        getRecyclerView(view)?.also {
            if (it is VerticalRecyclerView) {
                it.clearItemDecoration()
                it.addItemDecoration(NotifSettingDividerDecoration(context))
                it.addItemDecoration(NotifSettingBigDividerDecoration(context))
                it.setHasFixedSize(true)
            }
        }
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return SettingFieldTypeFactoryImpl(
                this,
                this,
                userSession
        )
    }

    override fun onTroubleshooterClicked() {
        trackTroubleshooterClicked(userSession.userId, userSession.shopId)
        context?.let { it.startActivity(it.intent(PUSH_NOTIFICATION_TROUBLESHOOTER)) }
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

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val application = (activity as Activity).application as BaseMainApplication
            val baseAppComponent = application.baseAppComponent
            DaggerUserSettingComponent
                    .builder()
                    .baseAppComponent(baseAppComponent)
                    .userSettingModule(UserSettingModule(context, getGqlRawQuery()))
                    .build()
                    .inject(this)
        }
    }

    // disable pagination
    override fun isLoadMoreEnabledByDefault(): Boolean = false

    // the method comes from baseListAdapter
    override fun onItemClicked(item: Visitable<*>?) = Unit

    // for save temporary setting state on adapter
    override fun updateSettingState(setting: ParentSetting?) = Unit

    companion object {
        // types will consume for graphql params
        const val TYPE_SELLER_NOTIF = "sellernotif"
        const val TYPE_PUSH_NOTIF = "pushnotif"
        const val TYPE_EMAIL = "email"
        const val TYPE_SMS = "sms"

        // this will be used to filter buyer notification settings so that buyer setting will not be showed
        const val BUYING_TRANSACTION_SECTION_TITLE = "Transaksi Pembelian"
    }

}