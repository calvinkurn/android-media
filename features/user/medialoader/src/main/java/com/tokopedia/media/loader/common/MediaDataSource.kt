package com.tokopedia.media.loader.common

import com.bumptech.glide.load.DataSource

enum class MediaDataSource {
    LOCAL,
    REMOTE,
    DATA_DISK_CACHE,
    RESOURCE_DISK_CACHE,
    MEMORY_CACHE;

    companion object {
        fun mapToDataSource(dataSource: MediaDataSource): DataSource {
            return when (dataSource) {
                LOCAL -> DataSource.LOCAL
                REMOTE -> DataSource.REMOTE
                DATA_DISK_CACHE -> DataSource.DATA_DISK_CACHE
                RESOURCE_DISK_CACHE -> DataSource.RESOURCE_DISK_CACHE
                MEMORY_CACHE -> DataSource.MEMORY_CACHE
            }
        }
    }
}