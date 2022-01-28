package com.tokopedia.attachvoucher.stub.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.common.GraphqlRepositoryStub
import com.tokopedia.attachvoucher.di.AttachVoucherScope
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.stub.usecase.GetVoucherUseCaseStub
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import dagger.Module
import dagger.Provides

@Module
class AttachVoucherUseCaseModuleStub {

    @Provides
    @AttachVoucherScope
    fun provideGetVoucherUseCase(
        stub: GetVoucherUseCaseStub
    ): GetVoucherUseCase = stub

    @Provides
    @AttachVoucherScope
    fun provideGetVoucherUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers,
        mapper: VoucherMapper
    ): GetVoucherUseCaseStub {
        return GetVoucherUseCaseStub(
            repositoryStub, dispatchers, mapper
        )
    }
}