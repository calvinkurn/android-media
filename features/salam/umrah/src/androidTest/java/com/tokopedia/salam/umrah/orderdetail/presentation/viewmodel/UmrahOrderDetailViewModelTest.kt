package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before
import org.junit.Rule

/**
 * @author by furqan on 11/10/2019
 */
class UmrahOrderDetailViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val graphqlRepository: GraphqlRepository = mock()
    private val dispatcher: CoroutineDispatcher = mock()

    private val viewStateObserver: Observer<UmrahOrderDetailsEntity> = mock()
    private val viewModel: UmrahOrderDetailViewModel = UmrahOrderDetailViewModel(graphqlRepository, dispatcher)

    @Before
    fun setUp() {

    }
}