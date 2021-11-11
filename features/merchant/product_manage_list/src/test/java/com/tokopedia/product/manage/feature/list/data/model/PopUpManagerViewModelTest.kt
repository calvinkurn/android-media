package com.tokopedia.product.manage.feature.list.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PopUpManagerViewModelTest {

    @Test
    fun `when getShopManagerPopups should return shopManagerPopups value`() {
        val shopManagerPopups = GetShopManagerPopups()
        val popupManager = PopupManagerResponse(shopManagerPopups)

        assertEquals(shopManagerPopups, popupManager.getShopManagerPopups)
    }
}