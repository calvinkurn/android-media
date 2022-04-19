package com.tokopedia.buyerorderdetail.stub.detail.di.component

import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailScope
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailViewModelModule
import com.tokopedia.buyerorderdetail.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.buyerorderdetail.stub.detail.di.module.BuyerOrderDetailModuleStub
import com.tokopedia.buyerorderdetail.stub.detail.di.module.BuyerOrderDetailUseCaseModuleStub
import dagger.Component

@BuyerOrderDetailScope
@Component(
    modules = [
        BuyerOrderDetailModuleStub::class,
        BuyerOrderDetailViewModelModule::class,
        BuyerOrderDetailUseCaseModuleStub::class
    ],
    dependencies = [BaseAppComponentStub::class]
)
interface BuyerOrderDetailComponentStub : BuyerOrderDetailComponent