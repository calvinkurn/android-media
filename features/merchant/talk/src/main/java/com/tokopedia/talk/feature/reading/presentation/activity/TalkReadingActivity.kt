package com.tokopedia.talk.feature.reading.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent

class TalkReadingActivity : BaseSimpleActivity(), HasComponent<TalkReadingComponent> {

    override fun getNewFragment(): Fragment? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComponent(): TalkReadingComponent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}