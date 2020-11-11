package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.report.coroutine.TestCoroutineDispatchers
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

abstract class ProductReportViewModelTestFixture {

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    protected lateinit var viewModel: ProductReportViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}