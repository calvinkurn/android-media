package com.tokopedia.home_component.widget.common.carousel

import android.os.Bundle

interface HomeComponentCarouselDiffUtil {
    fun getId(): String
    fun equalsWith(visitable: Any?): Boolean
    fun getChangePayloadFrom(visitable: Any?): Bundle? = null
}
