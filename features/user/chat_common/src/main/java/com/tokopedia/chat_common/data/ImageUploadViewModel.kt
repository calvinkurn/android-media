package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Created by stevenfredian on 11/28/17.
 */

class ImageUploadViewModel(
    builder: Builder
) : SendableViewModel(builder), Visitable<BaseChatTypeFactory> {

    var imageUrl: String? = builder.imageUrl
    var imageUrlThumbnail: String? = builder.imageUrlThumbnail
    var isRetry: Boolean = builder.isRetry

    init {
        this.isRetry = builder.isRetry
        this.imageUrl = builder.imageUrl
        this.imageUrlThumbnail = builder.imageUrlThumbnail
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder: SendableViewModel.Builder<Builder, ImageUploadViewModel>() {

        internal var imageUrl: String? = null
        internal var imageUrlThumbnail: String? = null
        internal var isRetry: Boolean = false

        fun withImageUrl(imageUrl: String): Builder {
            this.imageUrl = imageUrl
            return self()
        }

        fun withImageUrlThumbnail(imageUrlThumbnail: String): Builder {
            this.imageUrlThumbnail = imageUrlThumbnail
            return self()
        }

        fun withIsRetry(isRetry: Boolean): Builder {
            this.isRetry = isRetry
            return self()
        }

        override fun build(): ImageUploadViewModel {
            return ImageUploadViewModel(this)
        }
    }
}
