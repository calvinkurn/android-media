package com.tokopedia.play.broadcaster.view.fragment

import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import javax.inject.Inject

/**
 * @author by furqan on 03/06/2020
 */
class PlayCoverCropperFragmen @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    override fun getTitle(): String = "Tambah Cover & Judul"

    override fun isRootFragment(): Boolean = false

    override fun getScreenName(): String = "Play Cover Title Setup"

}