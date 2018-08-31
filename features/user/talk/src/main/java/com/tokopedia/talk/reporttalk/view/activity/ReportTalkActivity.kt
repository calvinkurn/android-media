package com.tokopedia.talk.reporttalk.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.reporttalk.view.fragment.ReportTalkFragment

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ReportTalkFragment.newInstance()
    }

    companion object {
        open fun createIntent(context: Context): Intent {
            return Intent(context, ReportTalkActivity::class.java)
        }
    }

    override fun getComponent(): TalkComponent {
       return  DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}