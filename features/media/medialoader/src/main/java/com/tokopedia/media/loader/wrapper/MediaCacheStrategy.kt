package com.tokopedia.media.loader.wrapper

import com.bumptech.glide.load.engine.DiskCacheStrategy

enum class MediaCacheStrategy {
    ALL,
    NONE,
    DATA,
    RESOURCE,
    AUTOMATIC;

    companion object {
        fun mapTo(cacheStrategy: MediaCacheStrategy): DiskCacheStrategy {
            return when (cacheStrategy) {
                ALL -> DiskCacheStrategy.ALL
                NONE -> DiskCacheStrategy.NONE
                DATA -> DiskCacheStrategy.DATA
                RESOURCE -> DiskCacheStrategy.RESOURCE
                AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
            }
        }

        fun mapTo(cacheStrategy: DiskCacheStrategy): MediaCacheStrategy {
            return when (cacheStrategy) {
                DiskCacheStrategy.ALL -> ALL
                DiskCacheStrategy.NONE -> NONE
                DiskCacheStrategy.DATA -> DATA
                DiskCacheStrategy.RESOURCE -> RESOURCE
                else -> AUTOMATIC
            }
        }
    }

}
