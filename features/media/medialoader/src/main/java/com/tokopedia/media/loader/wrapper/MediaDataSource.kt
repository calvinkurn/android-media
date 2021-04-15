package com.tokopedia.media.loader.wrapper

import com.bumptech.glide.load.DataSource

enum class MediaDataSource {
    LOCAL,
    REMOTE,
    DATA_DISK_CACHE,
    RESOURCE_DISK_CACHE,
    MEMORY_CACHE;

    companion object {
        fun mapTo(dataSource: MediaDataSource): DataSource {
            return when (dataSource) {
                LOCAL -> DataSource.LOCAL
                REMOTE -> DataSource.REMOTE
                DATA_DISK_CACHE -> DataSource.DATA_DISK_CACHE
                RESOURCE_DISK_CACHE -> DataSource.RESOURCE_DISK_CACHE
                MEMORY_CACHE -> DataSource.MEMORY_CACHE
            }
        }

        fun mapTo(dataSource: DataSource?): MediaDataSource {
            if (dataSource == null) return REMOTE
            return when (dataSource) {
                DataSource.LOCAL -> LOCAL
                DataSource.REMOTE -> REMOTE
                DataSource.DATA_DISK_CACHE -> DATA_DISK_CACHE
                DataSource.RESOURCE_DISK_CACHE -> RESOURCE_DISK_CACHE
                DataSource.MEMORY_CACHE -> MEMORY_CACHE
            }
        }
    }
}