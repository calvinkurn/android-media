package com.tokopedia.managename.view.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.managename.di.DaggerManageNameComponent
import com.tokopedia.managename.di.ManageNameComponent
import com.tokopedia.managename.di.ManageNameModule
import com.tokopedia.managename.view.fragment.AddNameFragment


/**
 * Created by Yoris Prayogo on 2020-06-03.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameActivity : BaseSimpleActivity(), HasComponent<ManageNameComponent> {

    override fun getNewFragment(): Fragment? {
        return intent?.extras?.let {
            AddNameFragment.newInstance(it)
        }
    }

    companion object {
        fun newInstance(context: android.content.Context?): Intent {
            return Intent(context, AddNameActivity::class.java)
        }
    }

    override fun getComponent(): ManageNameComponent {
        return DaggerManageNameComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .manageNameModule(ManageNameModule(this))
                .build()
    }
}
