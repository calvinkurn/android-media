package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.metadata.TokoFoodCheckoutMetadata
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartInfoParam
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartParam
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodParam
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFood
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodData
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodMainData
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutGeneralTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<CheckoutTokoFoodResponse, CheckoutGeneralTokoFoodResponse>(dispatchers.io) {

    private val isDebug = true

    override fun graphqlQuery(): String = """
        mutation TokoFoodCheckoutGeneral($$PARAMS_KEY: CheckoutGeneralV2Params!) {
          checkout_general_v2(params: $$PARAMS_KEY) {
            header {
              process_time
              reason
              error_code
            }
            data {
              success
              error
              error_state
              message
              data{
                callback_url
                query_string
                redirect_url
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: CheckoutTokoFoodResponse): Flow<CheckoutGeneralTokoFoodResponse> =
        flow {
            if (isDebug) {
                kotlinx.coroutines.delay(1000)
                emit(getDummyResponse())
            } else {
                val param = generateParam(params)
                val response =
                    repository.request<Map<String, Any>, CheckoutGeneralTokoFoodResponse>(
                        graphqlQuery(),
                        param
                    )
                emit(response)
            }
        }

    private fun getDummyResponse(): CheckoutGeneralTokoFoodResponse {
        return CheckoutGeneralTokoFoodResponse(
            checkoutGeneralTokoFood = CheckoutGeneralTokoFood(
                data = CheckoutGeneralTokoFoodData(
                    success = 1,
                    data = CheckoutGeneralTokoFoodMainData(
                        redirectUrl = "https://pay.tokopedia.com/v2/payment",
                        callbackUrl = "https://www.tokopedia.com/cart/thanks",
                        queryString = "amount=150052000\\u0026currency=IDR\\u0026customer_email=rinahalim%40ymail.com\\u0026customer_msisdn=081366500633\\u0026customer_name=Rina+Halim\\u0026gateway_code=\\u0026items%5Bname%5D=Jetski+Seadoo+Spark+-+Personal+Watercraft+%2F+Sea-Do...\\u0026items%5Bname%5D=Biaya+Kirim+yang+dibebankan\\u0026items%5Bprice%5D=150000000\\u0026items%5Bprice%5D=52000\\u0026items%5Bquantity%5D=1\\u0026items%5Bquantity%5D=1\\u0026language=id-ID\\u0026merchant_code=tokopedia\\u0026nid=\\u0026payment_metadata=4gIuMcll52RPxX_FGehL0HMHxnrzMr0iTy_-vGHnZB0X45HBjZjmr7_IDgEQ3q-Yl2aR39zcVBZHkayZ1uJEyLLybz4yfVSFnfzOF4V4qqHMDwryFDM5IyIA1KJdvu-HBUlz0jz9ZLk3fMDcPC6dbtwVwuEEEfX_S-XPVsbQaGeRfFwEi-OnFNK6sdGae0xgQt-w9UJybed0q5Q4Nu7lrgpl3MGbPDtgLjybkGMF9yfvgzRMVbTyahgq6F1lEh9kGl3HJnlTwYvTRlrYvb65bUGZdQ0e8dcgrIGDAxwmT2WOXScEO0pC_PQEZ9YtozkaffB-nGzOBUzPCd06_e0qrth8Ud0r7XWzU14BbeRiBgksKHqih051bBH8Nj-tUopoVhE8be51R7wwhAXrPxXVm4Pfybvv6VAf4IUsvceltBsKoD9VmGcFdOashPaThWv4cEpKG-dUq5Y8qS6EO8t84RsQv2McN3lxsvFViIjEkDBeVUHohjoaF3V5ZBL8_ztip1x_gnTk_Ri1AekhbRE3qGqY0Tgiecb5BND-dCg2nR69otwRPi8L8W6yDyAU8WX6uLBzmhSRj-0NHVsL1H2PkAsQeaw7RLIRcMIpmBHTJcJm0BMp_a_45g5xSE_0_5LzbIdK8Xigt2iJsr-uAuJCAVsMvP09iUxh3iFVVRQkMxfA8S_0AXv_Pmtq1I-7eytcpwapYb0YSODCPrIdaJH8-ftIExc0VaiY-1VnIlD-4z9s9fqLfmIX0gADLaKdEhBxETA7Spf71ykmzAs1sFrcjNCOESzzz49IlBdWYJE4SGZuiZbm0VfS2TXK6l3B3DK57ZuWc-njzEFkMxSLsbqTy7bOd4NxoTXp9knjXwT2IDj8ZNWUFdXUf8K9J8HISF_rT1gLNGGy-q-V4ELfb96ODJ0uv8JNnZRXuWdwqh2fZxC-_kVb8qb0SFFCqfOYqR_VUGSf4E0pzeOlkzQ2JqEQ7Mg_FSSsQyzP2u1lVtd7Ey-Pi1lVhUeRxVL8ptCO8ncECoX0y-CWfxv8sSSZwR4-DpGwZATH9QWwYkd6-3z3rH32puAZKenx-F9yN0qCZvLN5mrlrgUEI8ZTDH7W_PjgArIuDV2QwUn9RMyoc8vNR0CGUucDAoqjR5ZWGGwowNFtG9BIs2y_SDLy-281uj3wSELBdbV3hfR0s6QQlycL_-8NXdBvFkPNIW6832S6T1jXSR07mhYu381HX9LVjoA780Od3Krf8tlcX5D9Njv76EyPNIlSpWJ0SOoUSIprIOqT1QPz7NxIWWvRTKyX31tBZgRQwZTJtkfwLJ0d4GEwdIlBBjLxd7zKWJQym1Yhy5MR3D8D0VmBi5ovte3jfi9nmMGY2qmfwNoeWqzULo8FTjT-w7zg3_T_MEv1PVo5FhyHlklmaixv9vmheIJ1EyRRv6GDipjZaD5aIK9qGQ2q66s-WM5j7sBgzkpLZveh38nBsHgP-WYF16wjK8wNfWwB53IBdUellpJW091vpEQsDbOfd80l00H7ZC7GQFP3lDZESnKBvZKr4AxJb5NmehgEnZfcjmaQ5F6cVda6EkO5NGLcZDa0pw-0SaNyQmlDecdzw8M4Y1WwwgGN81mgiiLUdz8YLe6WTd_9-jcsHaiE4Lruhwt6SlJbckXtxVQFwVs0f0ADmUAHGCdxTvf6Xv-FFf_MU0eKfnKQOeDCQtDAEkWIooqH7VQJT529aB-C1ueeUdxiCmUGfVCnt7WXe1MBhDqXTNG79GDm62-BiGB7aPr5R2HFJeTKLgexQJGp6YMtzVWHMgpQIBYSbe6V4yZeCfonXnHDdCwRpFO5CqpKT402F892ZBztcqO2KcdfFoTF3RoJKMjgDPafa3sI9sIDsKoDTOf9k70dKnDAk-KLxXH2iXjsCIDDOaZc0U8GEFN-CX75LN3nQlugynZctMPsHh9CVUU90sQ4K07VS5V0vT-pv9xlSleeIwoqSwQSv9RPBxmgCpmQa7-I1tlUDK36oWxOQ_zrilZxW_QrvD-QI_irI8ByDmIVibj9_F5ceLX3acRhOOIbuS0SZ5EPvb-vW6s8vbxurwhGJ6Mq4DaSpotvTbXYEi8MX99_rRDT7Q-dWRAe6cwXc8qdXo5t99IqOL7zk3EVMuNgsPsodKrD0dkPPG5-Y3mloWC6dK7yjzxoKhvRgW_5vNBVO0JiN1kzwWN2PnO-K_RAzhIjsRhnR1Bu2D4N2aA_MvKcRrMWgoqmY90DdYkZZ6XrNRzmq28-Xy_j57NnG5Kr-HUPuHFBBEDgGCzuBn0Yg5G3Eu7wREAYZnNvPLMFEkvi3q38GrcxBDbAdNO9_z0GnQ-H49hr2CgKKHuv6EyPX_uhqnqTGvB0M_dWQz-ZUE_VLBxiCCcef3d1avTJGF2qRN5YrOyNolBkcq5oS-ryPP7MQUmlGhXYAtv7V20msqDURmFZHBlJo-0R5k7JHvp37_DgLObpOWUYHSGiK6ry2xpW4ypWTP7tJ3bGN47CwmXSLBqSnCiPs1_ueZsvQ9pI4VhWebOgrASCoOM5_Hm7tcK6KUc6RFeI1vdnob61472mVsnvoKlLPsacr8f94vbMIb1qn1_tmS6S_6sbL74WHb5Qns_cvbx0Tj4J4L-5QlxOH2eu72hnEcFJyftY9Ty_OtYPJiUgqRHAR97gYKFUU9BJomcOXGfnapabsYFNEEwkLOGSXf1MX3qHi9twPh80DtnB2Tj4z-lAeP0V4kCQu40NG06V_-aNjnau92f1UGVLzBySeR8uVcuwmta359vjIhiqSj96r1rFDZRYrnXH8UJXV7ODzvWghoZcyMHO7TdAVfNBIF2OnPVVBuDrWaBtOhJ5D3dnWOniXhxvo-uUzW26-tciNasweeTZ13qML7IcmBv39JqfRcef9CdYLL9bK8P7E2xUrxmZClkY2TqHIxp6HHhSTFpNdawvymVmFJll45-D8LqUthbTnWJ0qrlN-nw2kXUM0Rcymha-X5ZdMUpKpZTiV-STOTagWZtCJK_0xPPSGerThCUdeNa_UoFCLf1ia01977qKrSDT-j3pHniLdOTXp4pOHEp1ZFOZNs4LjpjLMp8rctHp5vHgNw91ZNyfPGQuZqVacAxteDzYJKl9TbLzn7703u_0ikIMx6E-4AB86QLyTvrh1kNXrdJdKrA0kJ4GXQvDCmIdD73c-CCCWoOQBckE6y60trTBH-4Wf8ZJSQWbnMNvdShx6zYrQ6ocPsWEjzYhjcLnlAtuB3pUy-nlxeLxuLK5HTldl33wBbsCGOEOV4a0pHL22yR9tfOw5B2qdeI9kIT0dPfSsKzDs4twRWNuSFJNGLC4jjLq7uRbmt_J0kRFvOWxcJwh-CvP7Zv0p2tPibjIJ7sJ0FrjiDtX8ukRD-aaYQFhmwb6jIOZ5jCJIScxJp4S9P2PspQg9F2Wo4nfAO91JZ7CNYeNgOUROk7mi4spC2IwZqDTnz_G3SDSnbYITicCnMosXhYj4jl4PBMBiSn6JCxxETK8RQFkU9ODtKPB7hu_F2vbA-y0fZV1n3scIFrUexB7g3lRgn8ZV7ZMtxi6EUBDlcdUoqbEFHA7_lXJJ2nosPhv4nHLMPd9_uYNLg1prJelqdPIKE8gwaKt0J8zrblNTxjfRMz1pJTJq6fVD0mgPSJE0N5DmzPH4BL1MkFP070soorxv8jFweC8j_FVexs-IR1ZMqp7DYZywn4WuglNEDEy_f7IYS5tH6nlrMdIaXBUMVTHkvz9o8AsYhnnnmX4HVezgUMookDcxrTGdFA56il-tFriJm3Q1QXrBvcSZG71zgOqkiZEFX9uQTDoDiBI0gqqxpDaSY7sgR2qLRezu48MgGyFEDkR_m7VZ_BPmcdUStDxlSAR9PoMl26kNHYq9ewCxTAKsSC6wJ8jVcKUu-JZ1GC3xxP7RKZiYythxw8uloZ6_ccqMUlF2fHcxxFU9kC1QVTGKtSAA21LT8T5HuAjtpNIePkLH30pkCAzxCuXKpjTTRBwEaFjzAUvHdi6y5Hc7IxaEOc_dHAxq44sSA0-VtEPMZgyJtLJmLRLS19s8Jc9XFmbsd2VQsBWPU5aD08rIOwj2wnWY90G232cV8mQmp1I8bBi9VeKjzH7SBHYNNmxpiUPk7swtnKfcNTmewj9MFokjQAp-htFGoIJRVWZKZiVF2XKNu9h2B1k3rEGHqEvXnV25r5q3U9fqDAYy91xYiYp-YSSLIcigvv7f2Ru5U-PY9R35_RbIT7j4DRFGib_ItfgYT8AJkKxkYwXr1eeUMAAuNqnBu1ouxvSm6vzLrtN6f33CCtCK5GxR-Mjts-ooAsgnAlWGo5dFrJip25M1-CCZxs0K7Zp9vR_fIt7OOEE2ly663XAAF7_sJoSWrXO3R0VwNNzCRr_rifjkYqiG0HhKHF7UbZsGpHc9swnXJtsp6ZDLAA9wv97ur1-gGV5PndPifxd7-Kg_6xAknAmxFIkSSNd-eMiA-U1DWwDhEi5nNGxFUpoiH0K6oa6SvMF-gAxTvqFRmw26_P7A5s9UFa3fJB50hb9HPJfnfI32YwS569URIl6WJzT2D2AVzFYcXOZkdGSjrtJ2c_5NQATBvBDpzOjEjDAqeXfBLFNOfUa3CGIVw_T7fL_e3iEZntApel6LA_x0KGu5VsYHa46tmMoTZg0t9vWoIvKxOe7UxwlRkhhpjLvguyiiIubFBei33WQxK_zat3lZ3IHmd828bE6owD2t-acnroBDKPi-hueURkV19BzwYIK8Kiu_VLbH2OUqp7mTzHnS-E-ONsDg8SsEMQsSmhXCINVS90wrk7NLK7SmZ04sTa-JGoq9AaJwADKQtBzh9ngTQdXWNi2_4hLSCRAIHAvdSV_LBuiYwT15vJVEt3lAEYj3RZunLBsVVltuVQbfKPfq2QBoLGbeCN1qGnAo1RN3cHYbbjSmKC4ugL93DodtebWJR9VlaRuhIGsTdUSnk_trPCkJr9gtoem1VHy\\u0026pid=\\u0026profile_code=AUTO_FIFTH\\u0026signature=0225e261c706ca038e75c678897afde48f2d3901\\u0026transaction_date=2019-07-09T02%3A36%3A52Z\\u0026transaction_id=437115058\\u0026user_defined_value=%7B%22device%22%3A1%2C%22user_id%22%3A25361202%2C%22payment_description%22%3A%22%22%2C%22msisdn%22%3A%22081366500633%22%2C%22user_address%22%3A%7B%22address%22%3A%22Tiong+Residence+Jalan+tiong+no.+35+A+%28pagar+Hijau%29%22%2C%22city%22%3A%22Jakarta+Selatan%22%2C%22country%22%3A%22Indonesia%22%2C%22phone%22%3A%22081366500633%22%2C%22postal_code%22%3A%2212940%22%2C%22state%22%3A%22DKI+Jakarta%22%7D%2C%22va_code%22%3A%22081366500633%22%2C%22access_token%22%3A%22%22%2C%22refresh_token%22%3A%22lTJ3wlhvS3mcSjHdMuMfmg%22%2C%22expires_in%22%3A0%2C%22client_id%22%3A%22%22%2C%22gmv_official_stores%22%3A150052000%2C%22txn_official_stores%22%3A1%2C%22subsidized_amount%22%3A150052000%2C%22fingerprint_data%22%3A%7B%22fingerprint_publickey%22%3A%22%22%2C%22fingerprint_support%22%3Afalse%7D%2C%22callback_url%22%3A%22https%3A%2F%2Fwww.tokopedia.com%2Fcart%2Fthanks%22%2C%22new_notify_merchant_url%22%3A%22%22%2C%22new_local_notify_merchant_url%22%3A%22%22%2C%22new_notify_gateway_codes%22%3Anull%2C%22is_stacking%22%3Afalse%7D"
                    )
                )
            )
        )
    }

    private fun getDummyErrorResponse(): CheckoutGeneralTokoFoodResponse {
        return CheckoutGeneralTokoFoodResponse(
            checkoutGeneralTokoFood = CheckoutGeneralTokoFood(
                data = CheckoutGeneralTokoFoodData(
                    success = 0,
                    errorMetadata = "{\"popup_message\":{},\"popup_error_message\":{\"text\":\"X item sedang tidak bisa diproses.\",\"action_text\":\"Oke\",\"action\":1,\"link\":\"\"},\"bottomsheet\":{}}"
                )
            )
        )
    }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParam(tokoFoodResponse: CheckoutTokoFoodResponse): Map<String, Any> {
            val checkoutMetadata = TokoFoodCheckoutMetadata(
                shop = tokoFoodResponse.data.shop,
                userAddress = tokoFoodResponse.data.userAddress,
                availableSection = tokoFoodResponse.data.availableSection,
                unavailableSection = tokoFoodResponse.data.unavailableSection,
                shipping = tokoFoodResponse.data.shipping,
                shoppingSummary = tokoFoodResponse.data.shoppingSummary
            )
            val metadataString = checkoutMetadata.generateString()
            val param =
                CheckoutGeneralTokoFoodParam(
                    carts = CheckoutGeneralTokoFoodCartParam(
                        cartInfo = listOf(
                            CheckoutGeneralTokoFoodCartInfoParam(
                                metadata = metadataString
                            )
                        )
                    )
                )
            return mapOf(PARAMS_KEY to param)
        }

    }
}