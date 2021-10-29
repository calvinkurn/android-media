package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.entity.UploaderPolicy
import org.junit.Test
import kotlin.test.assertEquals

class ImagePolicyMapperTest {

    @Test fun `Should return policy mapper correctly`() {
        // Given
        val policy = UploaderPolicy()

        // When
        val mapper = ImagePolicyMapper.mapToSourcePolicy(policy)

        // Then
        assertEquals(SourcePolicy(), mapper)
    }

}