package com.tokopedia.product.detail.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class ProductReportViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 private val userSessionInterface: UserSessionInterface,
                                                 @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun getProductReportType() {

    }

    fun reportProduct() {

    }

    companion object {

    }

}