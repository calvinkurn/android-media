package com.tokopedia.play.data.storage

import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2023
 */
@PlayScope
class PlayPageSourceStorage @Inject constructor() {

    var pageSource: String = ""
}
