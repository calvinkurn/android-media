package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.entity.UploaderPolicy
import com.tokopedia.mediauploader.common.data.mapper.PolicyMapper
import org.junit.Test
import kotlin.test.assertEquals

class PolicyMapperTest {

    @Test fun `Should return policy mapper correctly`() {
        // Given
        val policy = UploaderPolicy()

        // When
        val mapper = PolicyMapper.map(policy)

        // Then
        assertEquals(SourcePolicy(), mapper)
    }

}