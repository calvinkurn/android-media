package com.tokopedia.talk.addtalk.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.producttalk.view.activity.TalkProductActivity
import com.tokopedia.talk.producttalk.view.fragment.ProductTalkFragment

class AddTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {
    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context) = Intent(context, AddTalkActivity::class.java)

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return ProductTalkFragment.newInstance(intent.extras)
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

}