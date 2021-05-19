package com.tokopedia.remoteconfig.abtest

import android.content.Context
import android.util.Log
import com.tokopedia.config.GlobalConfig
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

class AbTestPlatform @JvmOverloads constructor (val context: Context): RemoteConfig {

    private lateinit var userSession: UserSession
    private lateinit var irisSession: IrisSession
    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

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
        //override customer app ab config features
        if (GlobalConfig.PACKAGE_APPLICATION == CONSUMER_PRO_APPLICATION_PACKAGE) {
            when (key) {
                NAVIGATION_EXP_TOP_NAV -> return NAVIGATION_VARIANT_REVAMP
                EXPERIMENT_NAME_TOKOPOINT -> return EXPERIMENT_NAME_TOKOPOINT
            }
        }
        val cacheValue: String = this.sharedPreferences.getString(key, defaultValue)?: defaultValue
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

    fun getRevisionValue() = sharedPreferences.getInt(REVISION, 0)

    fun getFilteredKeyByKeyName(keyName: String): MutableSet<String> {
        return mutableSetOf<String>().apply {
            for ((key, value) in sharedPreferences.all){
                val valueClassType = value?.let { it::class.java }
                if ((key.equals(keyName, true) || keyName.isEmpty()) && valueClassType == String::class.java)
                    add(key)
            }
        }
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        // Get existing revision for next Gql request
        val revision = sharedPreferences.getInt(REVISION, 0)

        // Gql request
        val payloads = HashMap<String, Any>()
        payloads[REVISION] = revision
        payloads[CLIENTID] = ANDROID_CLIENTID
        if (userSession.isLoggedIn) {
            payloads[ID] = userSession.userId
        } else {
            payloads[ID] = userSession.deviceId
        }
        payloads[IRIS_SESSION_ID] = irisSession.getSessionId()

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

    private fun sendTracking(featureVariants: RolloutFeatureVariants) {
        featureVariants.featureVariants?.let { featureVariants ->
            val userSession : UserSessionInterface = UserSession(context)

            val dataLayerAbTest = mapOf(
                "event" to "abtesting",
                "eventCategory" to "abtesting",
                "user_id" to if (userSession.isLoggedIn) userSession.userId else null,
                "feature" to featureVariants.map {
                    FeatureVariantAnalytics(it.feature, it.variant)
                }
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(dataLayerAbTest)
        }
    }

    companion object {
        val REVISION = "rev"
        val CLIENTID = "client_id"
        val ID = "id"
        val IRIS_SESSION_ID = "iris_session_id"
        val ANDROID_CLIENTID = 1
        val KEY_SP_TIMESTAMP_AB_TEST = "key_sp_timestamp_ab_test"
        val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"

        private const val CONSUMER_PRO_APPLICATION = 3;
        private const val CONSUMER_PRO_APPLICATION_PACKAGE = "com.tokopedia.intl"

        const val NAVIGATION_EXP_TOP_NAV = "new_glmenu"
        const val NAVIGATION_VARIANT_OLD = "Existing Navigation"
        const val NAVIGATION_VARIANT_REVAMP = "new_glmenu"

        const val POWER_MERCHANT_PRO_POP_UP = "pm_pro"
        const val POWER_MERCHANT_PRO_POP_UP_SHOW = "pm_pro_show"
        const val POWER_MERCHANT_PRO_POP_UP_NOT_SHOW = "pm_pro_not_show"

        //home component rollence section

        const val HOME_COMPONENT_LEGO4BANNER_EXP= "lego4_test"
        const val HOME_COMPONENT_LEGO4BANNER_OLD = "lego_round"
        const val HOME_COMPONENT_LEGO4BANNER_VARIANT = "lego_bleeding"
        const val HOME_COMPONENT_CATEGORYWIDGET_EXP= "catwidget_test"
        const val HOME_COMPONENT_CATEGORYWIDGET_OLD = "current_design"
        const val HOME_COMPONENT_CATEGORYWIDGET_VARIANT = "new_design"

        // end of home component rollence section

        //TBD
        const val BALANCE_EXP = "Balance Widget"
        const val BALANCE_VARIANT_OLD = "Existing Balance Widget"
        const val BALANCE_VARIANT_NEW = "New Balance Widget"

        const val HOME_EXP = "Home Revamp 2021"
        const val HOME_VARIANT_OLD = "Existing Home"
        const val HOME_VARIANT_REVAMP = "home revamp"

        const val KEY_AB_INBOX_REVAMP = "ReviewTab_NewInbox"
        const val VARIANT_OLD_INBOX = "ReviewTab_OldInbox"
        const val VARIANT_NEW_INBOX = "ReviewTab_NewInbox"


        const val EXPERIMENT_NAME_TOKOPOINT = "tokopoints_glmenu"
    }

}
