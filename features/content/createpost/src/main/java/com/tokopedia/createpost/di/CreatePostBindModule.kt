package com.tokopedia.createpost.di

import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.usecase.UseCase
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
@Module(includes = [CreatePostCommonModule::class])
abstract class CreatePostBindModule {

    @Binds
    @CreatePostScope
    abstract fun bindGetContentFormUseCase(usecase: GetContentFormUseCase): UseCase<GetContentFormDomain>
}