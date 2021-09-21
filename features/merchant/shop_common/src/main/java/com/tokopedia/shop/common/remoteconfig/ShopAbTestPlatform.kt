package com.tokopedia.shop.common.remoteconfig

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.remoteconfig.data.ShopAbTestInputParamModel
import com.tokopedia.shop.common.remoteconfig.data.ShopAbTestVariantPojo
import com.tokopedia.shop.common.remoteconfig.data.ShopRolloutFeatureVariants
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class ShopAbTestPlatform (
        context: Context
) : RemoteConfig {
    private var irisSession: IrisSession = IrisSession(context)
    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_SHOP_AB_TEST_PLATFORM, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()
    var requestParams = mapOf<String, Any>()

    override fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue.toString())
                ?: defaultValue.toString()
        if (cacheValue.equals(defaultValue.toString(), ignoreCase = true) && cacheValue.isNotEmpty()) {
            return cacheValue.equals("true", ignoreCase = true)
        }
        return defaultValue
    }

    override fun getDouble(key: String?): Double {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getKeysByPrefix(prefix: String?): MutableSet<String> {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getDouble(key: String?, defaultValue: Double): Double {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getLong(key: String?): Long {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getLong(key: String?, defaultValue: Long): Long {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getString(key: String?): String {
        return getString(key, "")
    }

    override fun getString(key: String?, defaultValue: String): String {
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue) ?: defaultValue
        if (cacheValue.isNotEmpty() && !cacheValue.equals(defaultValue, ignoreCase = true)) {
            return cacheValue
        }
        return defaultValue
    }

    override fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        (requestParams[KEY_INPUT] as? ShopAbTestInputParamModel)?.irisSessionId = irisSession.getSessionId()
        val graphqlRequest = GraphqlRequest(QUERY, ShopAbTestVariantPojo::class.java, requestParams, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    gqlResponseHandler(graphqlResponse)
                    listener?.onComplete(this@ShopAbTestPlatform)
                }
                .doOnError { error ->
                    Log.d("doOnError", error.toString())
                    listener?.onError(Exception(error))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe {
                    object : Subscriber<GraphqlResponse>() {
                        override fun onNext(t: GraphqlResponse?) {
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable) {
                        }
                    }
                }
    }

    private fun gqlResponseHandler(graphqlResponse: GraphqlResponse): ShopRolloutFeatureVariants {
        val responseData: ShopAbTestVariantPojo = graphqlResponse.getData(ShopAbTestVariantPojo::class.java)
        val featureVariants = responseData.dataRollout.featureVariants
        val currentTimestamp = Date().time
        if (featureVariants != null) {
            editor.clear()
            for (a in featureVariants) {
                editor.putString(a.feature, a.variant)
            }
        }
        editor.putLong(KEY_SP_TIMESTAMP_AB_TEST, currentTimestamp)
        editor.commit()
        return responseData.dataRollout
    }

    companion object {
        private const val QUERY = """
            query RolloutUserVariant(${'$'}input: GetUserVariant!){
                 RolloutUserVariant(input:${'$'}input){
                     featureVariants {
                       feature
                       variant
                     }
                     message
                  }
             }
        """
        private const val KEY_INPUT = "input"
        private const val ANDROID_CLIENT_ID = 1
        private const val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test"
        private const val SHARED_PREFERENCE_SHOP_AB_TEST_PLATFORM = "tkpd-shop-ab-test-platform"

        fun createRequestParam(
                listExperimentName: List<String>,
                shopId: String
        ): Map<String, Any> {
            return mapOf(
                    KEY_INPUT to ShopAbTestInputParamModel(
                            listExperimentName,
                            shopId,
                            ANDROID_CLIENT_ID.toString()
                    )
            )
        }
    }
}