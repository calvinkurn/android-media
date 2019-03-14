package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.DataResponseCheckPromoCode
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.domain.model.ResponseCheckPromoCode
import com.tokopedia.promocheckout.common.domain.model.promostacking.OrdersItem
import com.tokopedia.promocheckout.common.domain.model.promostacking.ProductDetailsItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

class CheckPromoStackingCodeUseCase(val resources: Resources, val graphqlUseCase: GraphqlUseCase) : UseCase<DataVoucher>() {

    val PARAMS = "params"
    val PROMO_CODES = "codes"
    val SKIP_APPLY = "skipApply"
    val PARAM_PROMO_SUGGESTED = "suggested"
    val ONE_CLICK_SHIPMENT = "oneClickShipment"
    lateinit var variables: HashMap<String, Any?>

    private object PARAM {
        internal val PRODUCT_ID = "product_id"
        internal val QUANTITY = "quantity"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DataVoucher> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[PROMO_CODES] = requestParams?.getString(PROMO_CODES, "[\"\"]") ?: "[\"\"]"
        variables[SKIP_APPLY] = requestParams?.getBoolean(SKIP_APPLY, false) ?: false
        variables[PARAM_PROMO_SUGGESTED] = requestParams?.getBoolean(PARAM_PROMO_SUGGESTED, false)?:false
        variables[ONE_CLICK_SHIPMENT] = requestParams?.getBoolean(ONE_CLICK_SHIPMENT, false)?:false

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.check_promo_code), DataResponseCheckPromoCode::class.java, variables)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<DataResponseCheckPromoCode>(DataResponseCheckPromoCode::class.java)
                    if(checkPromoCode?.checkPromoCartV2?.status.equals("error", true)){
                        throw CheckPromoCodeException(checkPromoCode?.checkPromoCartV2?.errorMessage?.joinToString()?:"")
                    }
                    Observable.just(checkPromoCode?.checkPromoCartV2?.data?.dataVoucher)
                }
    }

    fun setParams() {
        variables = HashMap()

        val productId = 123
        val quantity = 1
        val productDetail = JsonObject()
        productDetail.addProperty(PARAM.PRODUCT_ID, productId)
        productDetail.addProperty(PARAM.QUANTITY, quantity)
        val listProductDetails = JsonArray()
        listProductDetails.add(productDetail)


        // belum selesai
        // variables.put("params", jsonObjectAtcRequest)
    }

    fun createRequestParams(): RequestParams {
        val requestParams = RequestParams.create()

        /*{
            "params": {
                "promo": {
                    "codes": [
                        "CASHBACK50"
                    ],
                    "skip_apply": 0,
                    "is_suggested": 1,
                    "orders": [
                         {
                            "shop_id": 123,
                            "unique_id": "",
                            "product_details": [
                                {
                                    "product_id": 123,
                                    "quantity": 1
                                }
                            ],
                            "codes": [
                                "VOUCHERTOKO10",
                                "JNE100"
                                ]
                          }
                     ]
                }
            }
        }*/

        /*val object = JsonObject()
        val arrayCodesOrders = JsonArray()
        // for (item in listMove) {
            // val first = item.first as ChatListViewModel
            // array.add(Integer.valueOf(first.getId()))
        // }
        arrayCodesOrders.add("VOUCHERTOKO10")
        arrayCodesOrders.add("JNE100")

        `object`.add("list_msg_id", array)
        requestParams.putObject("json", `object`)

        requestParams.putObject(PARAMS, )

        requestParams.putObject(PROMO_CODES, promoCodes)
        requestParams.putBoolean(SKIP_APPLY, skipApply)
        requestParams.putBoolean(PARAM_PROMO_SUGGESTED, suggestedPromo)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)*/
        return requestParams
    }

    /*fun writeToJson(): JsonObject {
        val `object` = JsonObject()
        try {
            if (complaints.size != 0) {
                `object`.add(PARAM_COMPLAINT, getProblemArray())
            }
            if (solution != 0) {
                val solutionObject = JsonObject()
                solutionObject.addProperty(PARAM_ID, solution)
                `object`.add(PARAM_SOLUTION, solutionObject)
            }
            if (refundAmount != 0) {
                `object`.addProperty(PARAM_REFUND, refundAmount)
            }
            if (attachmentCount != 0) {
                val attachmentObject = JsonObject()
                attachmentObject.addProperty(PARAM_COUNT, attachmentCount)
                `object`.add(PARAM_ATTACHMENT, attachmentObject)
            }
            if (message != null) {
                if (message.remark != "") {
                    `object`.add(PARAM_CONVERSATION, message.writeToJson())
                }
            }
            if (resolutionId != null) {
                `object`.addProperty(PARAM_RESOLUTION_ID, Integer.valueOf(resolutionId))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return `object`
    }*/

}