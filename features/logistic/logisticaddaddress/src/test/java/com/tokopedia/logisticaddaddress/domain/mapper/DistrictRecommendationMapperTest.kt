package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import org.junit.Assert
import org.junit.Test

class DistrictRecommendationMapperTest {

    @Test
    fun `transform Response return success model`() {
        val mapperUT = DistrictRecommendationMapper()

        val t = DiscomDummyProvider.getSuccessResponse()
        val actual = mapperUT.transform(t)

        Assert.assertEquals(t.keroDistrictRecommendation.district.size, actual.addresses.size)
        Assert.assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
    }

    @Test
    fun `transform empty return empty model`() {
        val mapperUT = DistrictRecommendationMapper()

        val t = DiscomDummyProvider.getEmptyResponse()
        val actual = mapperUT.transform(t)

        Assert.assertTrue(actual.addresses.isEmpty())
        Assert.assertFalse(actual.isNextAvailable)
    }

    @Test
    fun `transform empty zip return empty model`() {
        val mapperUT = DistrictRecommendationMapper()

        val t = DiscomDummyProvider.getEmptyZipResponse()
        val actual = mapperUT.transform(t)

        Assert.assertTrue(actual.addresses[0].zipCodes.isEmpty())
        Assert.assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
    }
 }

