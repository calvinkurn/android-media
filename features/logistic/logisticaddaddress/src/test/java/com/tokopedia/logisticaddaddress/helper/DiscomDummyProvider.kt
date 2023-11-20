package com.tokopedia.logisticaddaddress.helper

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import java.lang.reflect.Type

object DiscomDummyProvider {

    private val mapper = DistrictRecommendationMapper()

    fun getSuccessModel(): AddressResponse {
        val response = Gson().fromJson(jakDiscomResponse, DistrictRecommendationResponse::class.java)
        return mapper.transform(response)
    }

    fun getEmptyModel(): AddressResponse {
        val response = Gson().fromJson(qwrDiscomResponse, DistrictRecommendationResponse::class.java)
        return mapper.transform(response)
    }

    fun getSuccessResponse(): DistrictRecommendationResponse =
        Gson().fromJson(jakDiscomResponse, DistrictRecommendationResponse::class.java)

    fun getSuccessGqlResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        result[DistrictRecommendationResponse::class.java] = getSuccessResponse()
        return GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
    }

    fun getEmptyResponse(): DistrictRecommendationResponse =
        Gson().fromJson(qwrDiscomResponse, DistrictRecommendationResponse::class.java)

    fun getEmptyZipResponse(): DistrictRecommendationResponse =
        Gson().fromJson(wolowaDiscomResponse, DistrictRecommendationResponse::class.java)
}

val qwrDiscomResponse = """
{
    "kero_district_recommendation": {
      "district": [],
      "next_available": false
    }
}
""".trimIndent()

val wolowaDiscomResponse = """
{
    "kero_district_recommendation": {
      "district": [
        {
          "district_id": 8144,
          "district_name": "Wolowa",
          "city_id": 382,
          "city_name": "Kab. Buton",
          "province_id": 26,
          "province_name": "Sulawesi Tenggara",
          "zip_code": []
        }
      ],
      "next_available": false
    }
}
""".trimIndent()

val jakDiscomResponse = """
{
    "kero_district_recommendation": {
      "district": [
        {
          "district_id": 2253,
          "district_name": "Cengkareng",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11710",
            "11720",
            "11730",
            "11740",
            "11750"
          ]
        },
        {
          "district_id": 2254,
          "district_name": "Grogol",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11440",
            "11450",
            "11460",
            "11470",
            "22545"
          ]
        },
        {
          "district_id": 2255,
          "district_name": "Kalideres",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11810",
            "11820",
            "11830",
            "11840",
            "11850"
          ]
        },
        {
          "district_id": 2256,
          "district_name": "Kebon Jeruk",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11510",
            "11520",
            "11530",
            "11540",
            "11550",
            "11560"
          ]
        },
        {
          "district_id": 2257,
          "district_name": "Kembangan",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11610",
            "11620",
            "11630",
            "11640",
            "11650"
          ]
        },
        {
          "district_id": 2258,
          "district_name": "Palmerah",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11410",
            "11420",
            "11430",
            "11480"
          ]
        },
        {
          "district_id": 2259,
          "district_name": "Taman Sari",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11110",
            "11120",
            "11130",
            "11140",
            "11150",
            "11160",
            "11170",
            "11180"
          ]
        },
        {
          "district_id": 2260,
          "district_name": "Tambora",
          "city_id": 174,
          "city_name": "Kota Administrasi Jakarta Barat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "11210",
            "11220",
            "11230",
            "11240",
            "11250",
            "11260",
            "11270",
            "11310",
            "11320",
            "11330"
          ]
        },
        {
          "district_id": 2273,
          "district_name": "Cempaka Putih",
          "city_id": 176,
          "city_name": "Kota Administrasi Jakarta Pusat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "10510",
            "10520",
            "10570"
          ]
        },
        {
          "district_id": 2274,
          "district_name": "Gambir",
          "city_id": 176,
          "city_name": "Kota Administrasi Jakarta Pusat",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "zip_code": [
            "10110",
            "10120",
            "10130",
            "10140",
            "10150",
            "10160"
          ]
        }
      ],
      "next_available": true
    }
}
""".trimIndent()
