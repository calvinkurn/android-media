package com.tokopedia.profilecompletion.profilecompletion.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.profilecompletion.profilecompletion.data.ProfileCompletionDataView
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by stevenfredian on 6/22/17.
 */
interface ProfileCompletionContract : CustomerView {
    interface View : CustomerView {
	fun skipView(tag: String?)
	fun onGetUserInfo(profileCompletionDataView: ProfileCompletionDataView?)
	fun onErrorGetUserInfo(string: String?)
	fun onSuccessEditProfile(edit: Int)
	fun onFailedEditProfile(errorMessage: String?)
	fun getString(id: Int): String?
	fun disableView()
	val data: ProfileCompletionDataView?
	fun canProceed(canProceed: Boolean)
	fun getView(): android.view.View?
	val userSession: UserSessionInterface?
    }
}