package com.tokopedia.talk.feature.report.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.TalkConstants
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.report.presentation.fragment.TalkReportFragment

class TalkReportActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    private var talkId = 0
    private var commentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        getTalkOrCommentId()
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkReportFragment.createNewInstance(talkId, commentId)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = TalkConstants.NO_SHADOW_ELEVATION
    }

    private fun getTalkOrCommentId() {
        val uri = intent.data ?: return
        val talkIdString = uri.pathSegments[uri.pathSegments.size - 2] ?: return
        if (talkIdString.isNotEmpty()) {
            this.talkId = talkIdString.toIntOrZero()
        }
        val commentIdString = uri.lastPathSegment ?: return
        if (commentIdString.isNotEmpty()) {
            this.commentId = commentIdString.toIntOrZero()
        }
    }
}