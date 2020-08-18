package com.tokopedia.product.manage.feature.etalase.data.model

import com.tokopedia.product.manage.feature.etalase.view.adapter.factory.EtalaseListTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class EtalaseViewModelTest {

    @Test
    fun `when get view model type should return view type resourceId`() {
        val viewType = 10000
        val typeFactory = mockk<EtalaseListTypeFactory>(relaxed = true)
        val viewModel = EtalaseViewModel("1", "Name", 1)

        every { typeFactory.type(any()) } returns viewType

        assertEquals(viewType, viewModel.type(typeFactory))
    }

    @Test
    fun `when get view model field should return field value`() {
        val id = "2"
        val name = "Barang Baru"
        val position = 2

        val viewModel = EtalaseViewModel(id, name, position)

        assertEquals(id, viewModel.id)
        assertEquals(name, viewModel.name)
        assertEquals(position, viewModel.position)
    }
}