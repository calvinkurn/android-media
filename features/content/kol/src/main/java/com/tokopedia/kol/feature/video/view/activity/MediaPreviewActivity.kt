package com.tokopedia.kol.feature.video.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Window
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.kol.feature.video.view.fragment.MediaHolderFragment
import com.tokopedia.kol.feature.video.view.fragment.MediaPreviewFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class MediaPreviewActivity : BaseSimpleActivity(), HasComponent<KolComponent>, MediaHolderFragment.OnControllerTouch {

    override fun getComponent(): KolComponent = DaggerKolComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment {
        val data = intent.data
        val postId: String = data?.lastPathSegment ?: 0.toString()
        val mediaIndex = data?.getQueryParameter(MediaPreviewFragment.ARG_MEDIA_INDEX)?.toIntOrZero() ?: 0

        return MediaPreviewFragment.createInstance(
                postId = postId,
                index = mediaIndex
        )
    }

    companion object{

        @Deprecated("Use ApplinkConstInternalContent")
        @JvmStatic
        fun createIntent(context: Context, postId: String, index: Int = 0): Intent =
                Intent(context, MediaPreviewActivity::class.java)
                        .putExtra(MediaPreviewFragment.ARG_POST_ID, postId)
                        .putExtra(MediaPreviewFragment.ARG_MEDIA_INDEX, index)
    }

    override fun onTouch(isVisible: Boolean) {
        if (fragment is MediaPreviewFragment)
            (fragment as MediaPreviewFragment).showDetail(isVisible)
    }
}