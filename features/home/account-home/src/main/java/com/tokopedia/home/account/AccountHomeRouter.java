package com.tokopedia.home.account;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;

public interface AccountHomeRouter {

    Intent getManageAddressIntent(Context context);

}
