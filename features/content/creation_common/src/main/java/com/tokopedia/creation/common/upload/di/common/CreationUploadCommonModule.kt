package com.tokopedia.creation.common.upload.di.common

import com.tokopedia.creation.common.upload.util.logger.CreationUploadLogger
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLoggerImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on November 10, 2023
 */
@Module
abstract class CreationUploadCommonModule {

    @Binds
    abstract fun bindCreationUploadLogger(logger: CreationUploadLoggerImpl): CreationUploadLogger
}
