package com.tokopedia.homenav.mainnav.data.factory

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.data.mapper.toVisitable
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.user.session.UserSessionInterface

class MainNavDataFactoryImpl(
        private val context: Context,
        private val userSession: UserSessionInterface
): MainNavDataFactory {

    private var visitableList: MutableList<HomeNavVisitable> = mutableListOf()
    private lateinit var userPojo: UserPojo

    override fun buildVisitableList(userPojo: UserPojo): MainNavDataFactory {
        this.userPojo = userPojo
        return this
    }

    override fun addProfileSection(): MainNavDataFactory {
        when (val loginState = getLoginState()) {
            AccountHeaderViewModel.LOGIN_STATE_LOGIN -> {
                visitableList.add(AccountHeaderViewModel(
                        userName = userPojo.profile.name,
                        userImage = userPojo.profile.profilePicture,
                        loginState = loginState,
                        shopId = userSession.shopId))
            }
            AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS,
            AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN -> {
                visitableList.add(AccountHeaderViewModel(loginState = loginState))
            }
        }
        return this
    }

    override fun addSeparatorSection(): MainNavDataFactory {
        visitableList.add(SeparatorViewModel())
        return this
    }

    override fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory {
        categoryData?.toVisitable()?.let { visitableList.addAll(it) }
        return this
    }

    override fun build(): List<HomeNavVisitable> {
        return visitableList
    }

    private fun getLoginState(): Int {
        return when {
            userSession.isLoggedIn -> AccountHeaderViewModel.LOGIN_STATE_LOGIN
            haveUserLogoutData() -> AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
            else -> AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN
        }
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderViewModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

}