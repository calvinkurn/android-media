package com.tokopedia.kol.feature.video.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Window
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.kol.feature.video.view.fragment.MediaHolderFragment
import com.tokopedia.kol.feature.video.view.fragment.MediaPreviewFragment
import com.tokopedia.kotlin.extensions.view.hide

class MediaPreviewActivity : BaseSimpleActivity(), HasComponent<KolComponent>, MediaHolderFragment.OnControllerTouch {

    override fun getComponent(): KolComponent = DaggerKolComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment = MediaPreviewFragment
            .createInstance(intent.extras?.getString(MediaPreviewFragment.ARG_POST_ID) ?: "0",
                    intent.extras?.getInt(MediaPreviewFragment.ARG_MEDIA_INDEX, 0) ?: 0)

    companion object{

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