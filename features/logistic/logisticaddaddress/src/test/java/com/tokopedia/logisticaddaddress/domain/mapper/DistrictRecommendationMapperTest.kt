package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object DistrictRecommendationMapperTest: Spek({

    Feature("transformResponse") {
        val mapperUT = DistrictRecommendationMapper()


        Scenario("transformResponse_returnSuccessModel"){
           val t = DiscomDummyProvider.getSuccessResponse()
           val actual = mapperUT.transform(t)

           Then("return success model "){
               Assert.assertEquals(t.keroDistrictRecommendation.district.size, actual.addresses.size)
               Assert.assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
           }

       }

        Scenario("transformEmpty_returnEmptyModel") {
            val t = DiscomDummyProvider.getEmptyResponse()
            val actual = mapperUT.transform(t)

            Then("return empty model") {
                Assert.assertTrue(actual.addresses.isEmpty())
                Assert.assertFalse(actual.isNextAvailable)
            }
        }

        Scenario("transformEmptyZip_returnSuccessModel") {
            val t = DiscomDummyProvider.getEmptyZipResponse()
            val actual = mapperUT.transform(t)

            Then("return success model") {
                Assert.assertTrue(actual.addresses[0].zipCodes.isEmpty())
                Assert.assertEquals(t.keroDistrictRecommendation.nextAvailable, actual.isNextAvailable)
            }
        }

    }

})