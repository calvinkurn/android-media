package com.tokopedia.settingbank

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankPresenter(val userSession: UserSession) :
        SettingBankContract.Presenter,
        BaseDaggerPresenter<SettingBankContract.View>() {

}