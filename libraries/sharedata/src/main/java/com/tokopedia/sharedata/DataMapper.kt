package com.tokopedia.sharedata

import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.UserData
import com.tokopedia.user.session.UserSession

object DataMapper {

    fun getLinkerShareData(linkerData: LinkerData): LinkerShareData {
        val userSession = UserSession(LinkerManager.getInstance().context)

        val userData = UserData()
        userData.name = userSession.name
        userData.phoneNumber = userSession.phoneNumber
        userData.userId = userSession.userId
        userData.email = userSession.email
        userData.isFirstTimeUser = userSession.isFirstTimeUser
        userData.isLoggedin = userSession.isLoggedIn

        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        linkerShareData.userData = userData

        return linkerShareData
    }

}
