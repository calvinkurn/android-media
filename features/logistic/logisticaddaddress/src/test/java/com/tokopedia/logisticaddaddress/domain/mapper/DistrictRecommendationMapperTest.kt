package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class DistrictRecommendationMapperTest {

    private val mapperUT = DistrictRecommendationMapper()

    @Test
    fun transformResponse_returnSuccessModel() {
        val t = DiscomDummyProvider.getSuccessResponse()

        val actual = mapperUT.transform(t)

        assertEquals(t.keroDistrictRecommendation.district.size, actual.addresses.size)
        assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
    }

    @Test
    fun transformEmpty_returnEmptyModel() {
        val t = DiscomDummyProvider.getEmptyResponse()

        val actual = mapperUT.transform(t)

        assertTrue(actual.addresses.isEmpty())
        assertFalse(actual.isNextAvailable)
    }

    @Test
    fun transformEmptyZip_returnSuccessModel() {
        val t = DiscomDummyProvider.getEmptyZipResponse()

        val actual = mapperUT.transform(t)

        assertTrue(actual.addresses[0].zipCodes.isEmpty())
        assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
    }
}