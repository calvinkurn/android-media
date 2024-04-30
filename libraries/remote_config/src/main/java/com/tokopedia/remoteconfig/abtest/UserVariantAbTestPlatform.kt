package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.data.AbTestUserVariantInputParamModel
import com.tokopedia.remoteconfig.abtest.data.AbTestUserVariantPojo
import com.tokopedia.remoteconfig.abtest.data.RolloutUserVariants
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.schedulers.Schedulers
import timber.log.Timber

class UserVariantAbTestPlatform(
    context: Context
) : RemoteConfig {
    private var irisSession: IrisSession = IrisSession(context)
    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCE_USER_VARIANT_AB_TEST_PLATFORM,
        Context.MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private var requestParams = mapOf<String, Any>()

    override fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue.toString())
            ?: defaultValue.toString()
        if (cacheValue.equals(
                defaultValue.toString(),
                ignoreCase = true
            ) && cacheValue.isNotEmpty()
        ) {
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
        if (!cacheValue.equals(defaultValue, ignoreCase = true)) {
            return cacheValue
        }
        return defaultValue
    }

    override fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun setRequestParamsData(listExperimentName: List<String>, shopId: String) {
        this.requestParams = mapOf(
            KEY_INPUT to AbTestUserVariantInputParamModel(
                listExperimentName,
                shopId,
                ANDROID_CLIENT_ID.toString()
            )
        )
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        (requestParams[KEY_INPUT] as? AbTestUserVariantInputParamModel)?.irisSessionId =
            irisSession.getSessionId()
        val graphqlRequest =
            GraphqlRequest(QUERY, AbTestUserVariantPojo::class.java, requestParams, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.createObservable(RequestParams.EMPTY)
            .map { graphqlResponse ->
                gqlResponseHandler(graphqlResponse)
                listener?.onComplete(this@UserVariantAbTestPlatform)
            }
            .doOnError { error ->
                Timber.tag("doOnError").d(error.toString())
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

    private fun gqlResponseHandler(graphqlResponse: GraphqlResponse): RolloutUserVariants {
        val responseData: AbTestUserVariantPojo =
            graphqlResponse.getData(AbTestUserVariantPojo::class.java)
        val featureVariants = responseData.dataRollout.featureVariants
        if (featureVariants != null) {
            editor.clear()
            for (a in featureVariants) {
                editor.putString(a.feature, a.variant)
            }
        }
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
        private const val SHARED_PREFERENCE_USER_VARIANT_AB_TEST_PLATFORM =
            "tkpd-user-variant-ab-test-platform"
    }
}
