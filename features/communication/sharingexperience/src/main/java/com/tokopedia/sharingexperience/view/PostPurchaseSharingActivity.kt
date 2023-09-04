package com.tokopedia.sharingexperience.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent

class PostPurchaseSharingActivity: BaseSimpleActivity(), HasComponent<Unit> {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): Unit {

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

}
