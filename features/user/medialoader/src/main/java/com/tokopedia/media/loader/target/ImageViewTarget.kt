package com.tokopedia.media.loader.target

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource
import com.tokopedia.media.loader.utils.MediaException

class ImageViewTarget(
        override val view: ImageView,
        private val listener: LoaderStateListener?
) : ViewTarget<ImageView> {

    override fun successLoad(resource: Drawable?, dataSource: MediaDataSource?) {
        listener?.successLoad(resource, dataSource)
        view.setImageDrawable(resource)
    }

    override fun failedLoad(error: MediaException?) {
        listener?.failedLoad(error)
        // view.setImageDrawable(resource)
    }

}