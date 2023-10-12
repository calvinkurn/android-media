package com.tokopedia.play.broadcaster.shorts.view.activity

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsModule

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
class PlayShortsActivity : BasePlayShortsActivity() {

    override fun inject() {
        DaggerPlayShortsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .creationUploaderComponent(CreationUploaderComponentProvider.get(this))
            .playShortsModule(PlayShortsModule(this))
            .build()
            .inject(this)
    }
}
