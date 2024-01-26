package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.R
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.data.AbTestVariantPojo
import com.tokopedia.remoteconfig.abtest.data.FeatureVariantAnalytics
import com.tokopedia.remoteconfig.abtest.data.RolloutFeatureVariants
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap

class AbTestPlatform @JvmOverloads constructor(val context: Context) : RemoteConfig {

    private lateinit var userSession: UserSession
    private lateinit var irisSession: IrisSession
    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private var id: String = ""

    init {
        userSession = UserSession(context)
        irisSession = IrisSession(context)
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_AB_TEST_PLATFORM, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()
    override fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue.toString()) ?: defaultValue.toString()
        if (cacheValue.equals(defaultValue.toString(), ignoreCase = true) && !cacheValue.isEmpty()) {
            return cacheValue.equals("true", ignoreCase = true)
        }
        return defaultValue
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String?): Double {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getKeysByPrefix(prefix: String): MutableSet<String> {
        return mutableSetOf<String>().apply {
            for ((key, value) in sharedPreferences.all) {
                val valueClassType = value?.let { it::class.java }
                if (key.startsWith(prefix = prefix, ignoreCase = false) &&
                    valueClassType == String::class.java
                ) {
                    add(key)
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String?, defaultValue: Double): Double {
        throw RuntimeException("Method is not implemented yet")
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String?): Long {
        throw RuntimeException("Method is not implemented yet")
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String?, defaultValue: Long): Long {
        throw RuntimeException("Method is not implemented yet")
    }

    override fun getString(key: String?): String {
        return getString(key, "")
    }

    override fun getString(key: String?, defaultValue: String): String {
        // override customer app ab config features
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue) ?: defaultValue
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

    fun deleteKeyLocally(key: String) {
        editor?.let {
            it.remove(key)
            it.commit()
        }
    }

    fun fetchByType(listener: RemoteConfig.Listener?) {
        editor.clear().commit()
        fetch(listener)
    }

    fun getRevisionValue() = sharedPreferences.getInt(REVISION, 0)

    fun getFilteredKeyByKeyName(keyName: String): MutableSet<String> {
        return mutableSetOf<String>().apply {
            for ((key, value) in sharedPreferences.all) {
                val valueClassType = value?.let { it::class.java }
                if ((key.contains(keyName, true) || keyName.isEmpty()) && valueClassType == String::class.java) {
                    add(key)
                }
            }
        }
    }

    /**
     * Call this function if you want to set custom id value. Can be shopId, userId, etc
     */
    fun setId(id: String) {
        this.id = id
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        // Get existing revision for next Gql request
        val revision = sharedPreferences.getInt(REVISION, 0)

        // Gql request
        val payloads = HashMap<String, Any>()
        payloads[REVISION] = revision
        payloads[CLIENTID] = ANDROID_CLIENTID
        if (id.isNotEmpty()) {
            payloads[ID] = id
        } else {
            if (userSession.isLoggedIn) {
                payloads[ID] = userSession.userId
            } else {
                if (handleDeviceIdless()) { return }
                payloads[ID] = userSession.deviceId
            }
        }
        payloads[IRIS_SESSION_ID] = irisSession.getSessionId()

        val graphqlRequest = GraphqlRequest(
            GraphqlHelper.loadRawString(
                context.resources,
                R.raw.gql_rollout_feature_variant
            ),
            AbTestVariantPojo::class.java,
            payloads,
            false
        )

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
                listener?.onComplete(this)
            }
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribe {
                object : Subscriber<GraphqlResponse>() {
                    override fun onNext(t: GraphqlResponse?) { }

                    override fun onCompleted() { }

                    override fun onError(e: Throwable?) { }
                }
            }
    }

    private fun handleDeviceIdless(): Boolean {
        if (userSession.deviceId == null) {
            return true
        }
        return false
    }

    private fun gqlResponseHandler(graphqlResponse: GraphqlResponse): RolloutFeatureVariants {
        val responseData: AbTestVariantPojo = graphqlResponse.getData(AbTestVariantPojo::class.java)
        val featureVariants = responseData?.dataRollout?.featureVariants
        val globalRevision = responseData.dataRollout.globalRev

        val currentTimestamp = Date().time
        if (featureVariants != null) {
            editor.clear()
            for (a in featureVariants) {
                editor.putString(a.feature, a.variant)
            }
        }
        editor.putLong(KEY_SP_TIMESTAMP_AB_TEST, currentTimestamp)
        editor.putInt(REVISION, globalRevision)
        editor.commit()

        return responseData.dataRollout
    }

    companion object {
        val REVISION = "rev"
        val CLIENTID = "client_id"
        val ID = "id"
        val IRIS_SESSION_ID = "iris_session_id"
        val ANDROID_CLIENTID = 1
        val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test"
        val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"

        private const val CONSUMER_PRO_APPLICATION = 3
        private const val CONSUMER_PRO_APPLICATION_PACKAGE = "com.tokopedia.intl"
    }
}
