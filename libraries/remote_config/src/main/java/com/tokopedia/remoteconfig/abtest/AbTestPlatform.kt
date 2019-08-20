package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.R
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.data.AbTestVariantPojo
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.schedulers.Schedulers
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByteArray(key: String?, defaultValue: ByteArray?): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDouble(key: String?): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDouble(key: String?, defaultValue: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(key: String?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(key: String?, defaultValue: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        // =================== ToDo =================== //
        // 1. Request gql
        // 2. Save the result to sharedPref and replace the existing
        // 3. Send analytics data from gql response

        // Get existing revision for next Gql request
        val revision = sharedPreferences.getInt(REVISION, 0)

        // Reset sharedPref
        editor.clear().commit()

        // Set timestamp when request gql
        val currentTimestamp = Date().time
        editor.putLong(KEY_SP_TIMESTAMP_AB_TEST, currentTimestamp)
        editor.commit()

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
                    val responseData: AbTestVariantPojo = graphqlResponse.getData(AbTestVariantPojo::class.java)
                    val featureVariants = responseData?.dataRollout?.featureVariants
                    val globalRevision = responseData.dataRollout.globalRev
                    val status = responseData.dataRollout.status

                    // Save response to sharedPref
                    if (featureVariants != null) {
                        for (a in featureVariants) {
                            setString(a.feature, a.variant)
                        }
                    }

                    if (globalRevision != null) {
                        editor.putInt(REVISION, globalRevision).commit()
                    }

                }
                .doOnError{ error -> {
                    Log.d("adasda", error.toString())
                }}
                .doOnNext({
                    val dataLayerAbTest = mapOf(
                            "event" to "abtesting",
                            "eventCategory" to "abtesting",
                            "user_id" to "..."
                    )
                    Log.d("Track: ", dataLayerAbTest.toString())
                    // TrackApp.getInstance().gtm.sendGeneralEvent(dataLayerAbTest)
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe { object : Subscriber<GraphqlResponse>() {
                    override fun onNext(t: GraphqlResponse?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onCompleted() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError(e: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                }}

//        graphqlUseCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
//            override fun onNext(response: GraphqlResponse) {
//                val responseData = response.getData<AbTestVariantPojo>(AbTestVariantPojo::class.java)
//                val featureVariants = responseData.dataRollout.featureVariants
//                val globalRevision = responseData.dataRollout.globalRev
//                val status = responseData.dataRollout.status
//
//                // Save response to sharedPref
//                if (featureVariants != null) {
//                    for (a in featureVariants) {
//                        setString(a.feature, a.variant)
//                    }
//                }
//
//                if (globalRevision != null) {
//                    editor.putInt(REVISION, globalRevision).commit()
//                }
//
////                val dataLayerAbTest = mapOf(
////                        "event" to "abtesting",
////                        "eventCategory" to "abtesting",
////                        "user_id" to "...",
////                        "feature" to featureVariants
////                )
////
////                TrackApp.getInstance().gtm.sendGeneralEvent(dataLayerAbTest)
//            }
//
//            override fun onCompleted() {
//                Log.d("onCompleted: ", "onCompleted")
//            }
//
//            override fun onError(e: Throwable?) {
//                Log.d("onError: ", "onError")
//            }
//
//        })
    }

//    private fun sendTracking() {
//        TrackApp.getInstance().gtm.sendGeneralEvent(
//                mapOf(
//                        "event" to "abtesting",
//                        "eventCategory" to "abtesting",
//                        "user_id" to "...",
//                        "feature" to
//                )
//        )
//    }

    companion object {
        val REVISION = "rev"
        val CLIENTID = "client_id"
        val ANDROID_CLIENTID = 1
        val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test"
        val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"
    }

}
