package com.tokopedia.homenav.mainnav.data.factory

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.user.session.UserSessionInterface

class MainNavFactoryImpl (private val userSession: UserSessionInterface
): MainNavFactory {

    private lateinit var context: Context

    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    override fun buildVisitableList(context: Context): MainNavFactory {
        this.context = context
        return this
    }

    override fun addProfileSection(data: MainNavPojo?): MainNavFactory {
        this.visitableList.add(AccountHeaderViewModel(
                loginState = when {
                    userSession.isLoggedIn -> AccountHeaderViewModel.LOGIN_STATE_LOGIN
                    haveUserLogoutData() -> AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
                    else -> AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN
                },
                userName = userSession.name,
                userImage = userSession.profilePicture,
                badge = "",
                ovoSaldo = data?.wallet?.cashBalance ?: "",
                ovoPoint = data?.wallet?.pointBalance ?: "",
                saldo = "",
                shopName = userSession.shopName,
                shopId = userSession.shopId,
                shopNotifCount = ""
        ))
        return this
    }

    override fun build(): List<Visitable<*>> {
        return visitableList
    }


    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderViewModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }
}