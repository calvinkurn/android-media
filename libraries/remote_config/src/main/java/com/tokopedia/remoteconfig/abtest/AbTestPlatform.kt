package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.R
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.data.AbTestVariantPojo
import com.tokopedia.remoteconfig.abtest.data.RolloutFeatureVariants
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.schedulers.Schedulers
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

class AbTestPlatform @JvmOverloads constructor (val context: Context): RemoteConfig {
    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_AB_TEST_PLATFORM, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()
    override fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue.toString())
        if (cacheValue.equals(defaultValue.toString(), ignoreCase = true) &&  !cacheValue.isEmpty()) {
            return cacheValue.equals("true", ignoreCase = true)
        }
        return defaultValue
    }

    override fun getByteArray(key: String?): ByteArray {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getByteArray(key: String?, defaultValue: ByteArray?): ByteArray {
        throw RuntimeException("Method is not implemented yet")
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
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue)
        if (!cacheValue.isEmpty() && !cacheValue.equals(defaultValue, ignoreCase = true)) {
            return cacheValue
        }
        return defaultValue
    }

    override fun setString(key: String?, value: String?) {
        if (editor != null) {
            editor.putString(key, value)
            editor.commit()
        }
    }

    fun fetchByType(listener: RemoteConfig.Listener?) {
        editor.clear().commit()
        fetch(listener)
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        // Get existing revision for next Gql request
        val revision = sharedPreferences.getInt(REVISION, 0)

        // Gql request
        val payloads = HashMap<String, Any>()
        payloads[REVISION] = revision
        payloads[CLIENTID] = ANDROID_CLIENTID

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_rollout_feature_variant), AbTestVariantPojo::class.java, payloads, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    val featureVariants = gqlResponseHandler(graphqlResponse)
                    return@map RolloutFeatureVariants(featureVariants.featureVariants)
                }
                .doOnError { error ->
                    Log.d("doOnError", error.toString())
                }
                .doOnNext {
                    sendTracking(it)
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe { object : Subscriber<GraphqlResponse>() {
                    override fun onNext(t: GraphqlResponse?) { }

                    override fun onCompleted() { }

                    override fun onError(e: Throwable?) { }

                }}
    }

    private fun gqlResponseHandler(graphqlResponse: GraphqlResponse): RolloutFeatureVariants {
        val responseData: AbTestVariantPojo = graphqlResponse.getData(AbTestVariantPojo::class.java)
        val featureVariants = responseData?.dataRollout?.featureVariants
        val globalRevision = responseData.dataRollout.globalRev
        val status = responseData.dataRollout.status

        val currentTimestamp = Date().time
        editor.clear().commit()
        editor.putLong(KEY_SP_TIMESTAMP_AB_TEST, currentTimestamp)
        editor.commit()

        if (featureVariants != null) {
            for (a in featureVariants) {
                setString(a.feature, a.variant)
            }
        }

        if (globalRevision != null) {
            editor.putInt(REVISION, globalRevision).commit()
        }

        return responseData.dataRollout
    }

    private fun sendTracking(featureVariants: RolloutFeatureVariants) {
        val userSession : UserSessionInterface = UserSession(context)

        val dataLayerAbTest = mapOf(
                "event" to "abtesting",
                "eventCategory" to "abtesting",
                "user_id" to if (userSession.isLoggedIn) userSession.userId else null,
                "feature" to featureVariants.featureVariants
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayerAbTest)
    }

    companion object {
        val REVISION = "rev"
        val CLIENTID = "client_id"
        val ANDROID_CLIENTID = 1
        val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test"
        val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"
    }

}
