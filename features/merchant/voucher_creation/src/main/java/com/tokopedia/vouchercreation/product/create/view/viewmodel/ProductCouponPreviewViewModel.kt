package com.tokopedia.vouchercreation.product.create.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import rx.Scheduler
import javax.inject.Inject
import javax.inject.Named

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    @Named("io") private val ioScheduler: Scheduler,
    @Named("main") private val mainThreadScheduler: Scheduler,
) : BaseViewModel(dispatchers.main) {



}