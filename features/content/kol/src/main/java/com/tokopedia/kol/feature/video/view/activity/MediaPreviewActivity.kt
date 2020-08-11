package com.tokopedia.kol.feature.video.view.activity

import android.os.Bundle
import android.view.Window
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.kol.feature.video.view.fragment.MediaHolderFragment
import com.tokopedia.kol.feature.video.view.fragment.MediaPreviewFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero

const val DEFAULT_POST_ID = "0"

class MediaPreviewActivity : BaseSimpleActivity(), HasComponent<KolComponent>, MediaHolderFragment.OnControllerTouch {

    private var postId: String = "0"

    override fun getComponent(): KolComponent = DaggerKolComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getDataFromIntent()
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            postId = it.lastPathSegment ?: DEFAULT_POST_ID
        }
    }

    override fun getNewFragment(): Fragment {
        val data = intent.data
        val mediaIndex = data?.getQueryParameter(MediaPreviewFragment.ARG_MEDIA_INDEX)?.toIntOrZero() ?: 0
        return MediaPreviewFragment.createInstance(
                postId = postId,
                index = mediaIndex
        )
    }

    override fun onTouch(isVisible: Boolean) {
        if (fragment is MediaPreviewFragment)
            (fragment as MediaPreviewFragment).showDetail(isVisible)
    }
}