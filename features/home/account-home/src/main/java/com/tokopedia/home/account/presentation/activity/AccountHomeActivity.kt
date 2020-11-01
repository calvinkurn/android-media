package com.tokopedia.home.account.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment

/**
 * Created by Yoris Prayogo on 01/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AccountHomeActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = AccountHomeFragment.newInstance(Bundle())

//    override fun getComponent(): BaseAppComponent {
//        return (application as BaseMainApplication).baseAppComponent
//    }
}