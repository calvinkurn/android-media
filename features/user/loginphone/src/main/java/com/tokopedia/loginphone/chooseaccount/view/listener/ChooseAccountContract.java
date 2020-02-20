package com.tokopedia.loginphone.chooseaccount.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo;
import com.tokopedia.loginphone.chooseaccount.data.UserDetail;
import com.tokopedia.sessioncommon.data.LoginTokenPojo;
import com.tokopedia.sessioncommon.data.profile.ProfilePojo;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseAccountContract {

    interface ViewAdapter {
        void onSelectedAccount(UserDetail account, String phone);
    }
}
