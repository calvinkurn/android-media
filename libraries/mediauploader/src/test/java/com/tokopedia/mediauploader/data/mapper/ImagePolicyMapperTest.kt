package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.entity.UploaderPolicy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ImagePolicyMapperTest: Spek({
    Feature("policy mapper") {
        Scenario("mapping from UploaderPolicy to SourcePolicy") {
            val uploaderPolicy = UploaderPolicy()
            Then("it should return correctly") {
                val mapper = ImagePolicyMapper.mapToSourcePolicy(uploaderPolicy)
                assert(mapper == SourcePolicy())
            }
        }
    }
})