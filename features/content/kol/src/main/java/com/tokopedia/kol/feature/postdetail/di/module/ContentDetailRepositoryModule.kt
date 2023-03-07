package com.tokopedia.kol.feature.postdetail.di.module

import com.tokopedia.kol.feature.postdetail.data.repository.ContentDetailRepositoryImpl
import com.tokopedia.kol.feature.postdetail.di.ContentDetailScope
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import dagger.Binds
import dagger.Module

/**
 * Created by meyta.taliti on 02/08/22.
 */

@Module
abstract class ContentDetailRepositoryModule {

    @Binds
    @ContentDetailScope
    abstract fun bindRepository(repository: ContentDetailRepositoryImpl): ContentDetailRepository
}