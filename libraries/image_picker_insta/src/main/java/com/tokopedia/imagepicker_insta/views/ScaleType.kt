package com.tokopedia.imagepicker_insta.views

import androidx.annotation.IntDef
import com.tokopedia.imagepicker_insta.views.MediaScaleType.Companion.MEDIA_CENTER_CROP
import com.tokopedia.imagepicker_insta.views.MediaScaleType.Companion.MEDIA_CENTER_INSIDE

@Retention(AnnotationRetention.SOURCE)
@IntDef(MEDIA_CENTER_INSIDE, MEDIA_CENTER_CROP)
annotation class MediaScaleType{
    companion object{
        const val MEDIA_CENTER_INSIDE = 1
        const val MEDIA_CENTER_CROP = 2
    }
}

