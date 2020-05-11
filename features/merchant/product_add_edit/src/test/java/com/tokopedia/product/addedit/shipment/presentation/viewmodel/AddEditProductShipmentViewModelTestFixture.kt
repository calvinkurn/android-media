package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.coroutine.TestCoroutineDispatchers
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule

/**
 * Created by faisalramd on 2020-05-08.
 */

abstract class AddEditProductShipmentViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var shipmentViewModel: AddEditProductShipmentViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shipmentViewModel = AddEditProductShipmentViewModel(TestCoroutineDispatchers.main)
    }
}