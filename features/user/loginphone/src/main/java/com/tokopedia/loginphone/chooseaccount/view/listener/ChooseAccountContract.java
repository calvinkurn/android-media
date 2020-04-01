package com.tokopedia.loginphone.chooseaccount.view.listener;

import com.tokopedia.loginphone.chooseaccount.data.UserDetail;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseAccountContract {

    interface ViewAdapter {
        void onSelectedAccount(UserDetail account, String phone);
    }
}
