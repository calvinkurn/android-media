package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.entity.UploaderPolicy
import com.tokopedia.mediauploader.common.data.mapper.ImagePolicyMapper
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