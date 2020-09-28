package com.tkpd.remoteresourcerequest.utils

import com.tkpd.remoteresourcerequest.type.ResourceTypeMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class CSVArrayListHelperTest {

    @Test
    fun getResourceTypeObjectTest() {
        val nameSlot = slot<String>()
        val typeSlot = slot<String>()
        mockkObject(ResourceTypeMapper)

        every {
            ResourceTypeMapper.getResourceType(
                    capture(nameSlot),
                    capture(typeSlot)
            )
        } returns mockk(relaxed = true)
        val obj = CSVArrayListHelper.getResourceTypeObject("abc.png     ,singleDpi, , n")

        assertEquals(nameSlot.captured, "singleDpi")
        assertEquals(typeSlot.captured, "abc.png")
        assertEquals(obj.resourceVersion, "")
        assertFalse(obj.isUsedAnywhere)


    }
}
