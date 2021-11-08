package com.tokopedia.attachproduct.fake.di

import com.tokopedia.attachproduct.di.AttachProductContext
import com.tokopedia.attachproduct.di.AttachProductScope
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.fake.usecase.FakeAttachProductUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by Hendri on 19/02/18.
 */
@Module
class FakeAttachProductModule(private val repositoryStub: GraphqlRepositoryStub) {

    @Provides
    @AttachProductScope
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @Provides
    @AttachProductScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    @Provides
    @AttachProductScope
    fun provideUseCase(dispatcher: CoroutineDispatcher): AttachProductUseCase {
        return AttachProductUseCase(repositoryStub, "",  dispatcher)
    }
}
