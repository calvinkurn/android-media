package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.usecase.RequestParams
import java.util.*

class AbTestPlatform @JvmOverloads constructor (val context: Context): RemoteConfig {

//    val SHARED_PREFERENCE_NAME = "tkpd_ab_test_platform"
//    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test";
    val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_AB_TEST_PLATFORM, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()
//    lateinit var editor: SharedPreferences.Editor

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
            editor.putString(key, value).apply()
        }
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
//        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
//                R.raw.gql_rollout_feature_variant), AbTestVariantPojo::class.java, createRequestParams(0, 1))
//        val graphqlUseCase = GraphqlUseCase()
//        graphqlUseCase.addRequest(graphqlRequest)
//        graphqlUseCase.execute(createRequestParams(0, 1), subscriber)

        // =================== ToDo =================== //
        // 1. Request gql
        // 2. Save the result to sharedPref
        // 3. 

        val currentTimestamp = Date().time
        editor.putLong(KEY_SP_TIMESTAMP_AB_TEST, currentTimestamp)
        editor.apply()
    }

    //    fun execute(requestParams: RequestParams, subscriber: Subscriber<GraphqlResponse>) {
//        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
//                R.raw.gql_rollout_feature_variant), AbTestVariantPojo::class.java, requestParams.parameters)
//        val graphqlUseCase = GraphqlUseCase()
//        graphqlUseCase.addRequest(graphqlRequest)
//        graphqlUseCase.execute(requestParams, subscriber)
//    }

    fun createRequestParams(rev: Int, client_id: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(revision, rev)
        requestParams.putInt(clientId, client_id)
        return requestParams
    }

    companion object {
        val revision = "rev"
        val clientId = "client_id"
    }

}
