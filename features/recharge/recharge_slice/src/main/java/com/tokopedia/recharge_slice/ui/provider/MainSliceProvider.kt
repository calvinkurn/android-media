package com.tokopedia.recharge_slice.ui.provider

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat.createWithBitmap
import androidx.core.graphics.drawable.IconCompat.createWithResource
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.*
import com.bumptech.glide.Glide
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.recharge_slice.R
import com.tokopedia.recharge_slice.data.Data
import com.tokopedia.recharge_slice.data.Recommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.recharge_slice.data.Product
import com.tokopedia.recharge_slice.di.DaggerRechargeSliceComponent
import com.tokopedia.recharge_slice.util.RechargeSliceGqlQuery
import com.tokopedia.recharge_slice.util.SliceTracking
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by M on 6/12/2019
 */

class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context
    private lateinit var userSession: UserSessionInterface
    private lateinit var sliceTracking: SliceTracking
    private lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @Inject
    lateinit var repository: GraphqlRepository

    var recommendationModel: List<Recommendation>? = null

    var alreadyLoadData: Boolean = false
    var isError: Boolean = false

    override fun onBindSlice(sliceUri: Uri): Slice? {
        allowReads {
            userSession = UserSession(contextNonNull)
            sliceTracking = SliceTracking(userSession)
        }
        return createGetInvoiceSlice(sliceUri)
    }

    private fun createPendingIntent(id: Int?, applink: String?, trackingClick: String): PendingIntent? {
        return id?.let {
            PendingIntent.getActivity(
                    contextNonNull,
                    it,
                    allowReads {
                        RouteManager.getIntent(contextNonNull, applink)
                                .putExtra(RECHARGE_PRODUCT_EXTRA, trackingClick)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createPendingIntentLogin() = PendingIntent.getActivity(
            contextNonNull,
            0,
            allowReads {
                RouteManager.getIntent(contextNonNull, ApplinkConsInternalDigital.APPLINK_RECHARGE_SLICE)
            },
            0
    )

    private fun createPendingIntentNoAccess() = PendingIntent.getActivity(
            contextNonNull,
            0,
            allowReads {
                RouteManager.getIntent(contextNonNull, RECHARGE_NEW_HOME_PAGE)
                        .putExtra(RECHARGE_HOME_PAGE_EXTRA, true)
            },
            0
    )

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createGetInvoiceSlice(sliceUri: Uri): Slice? {
        if (getRemoteConfigRechargeSliceEnabler(contextNonNull)) {
            try {
                if (allowReads {userSession.isLoggedIn}) {
                    if (!alreadyLoadData)
                        getData(sliceUri)
                    if(alreadyLoadData  && isError){
                        return sliceNoAccess(sliceUri)
                    }
                    return list(contextNonNull, sliceUri, INFINITY) {
                        setAccentColor(ContextCompat.getColor(contextNonNull, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                        header {
                            title = contextNonNull.resources.getString(R.string.slice_daftar_rekomendasi)
                            if (recommendationModel.isNullOrEmpty() && !alreadyLoadData){
                                subtitle = contextNonNull.resources.getString(R.string.slice_loading)
                                 allowReads {
                                     sliceTracking.onLoadingState()
                                 }
                            }
                            else if (recommendationModel.isNullOrEmpty() && alreadyLoadData) {
                                allowReads {
                                    sliceTracking.onEmptyState()
                                }
                                title = contextNonNull.resources.getString(R.string.slice_empty_data)
                            }
                            else {
                                subtitle = (contextNonNull.resources.getString(R.string.slice_rekomendasi))
                            }

                            primaryAction = createPendingIntentNoAccess()?.let {
                                SliceAction.create(
                                        it,
                                        createWithResource(contextNonNull, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                                        SMALL_IMAGE,
                                        ""
                                )
                            }
                        }
                        recommendationModel?.indices?.let { recomRange ->
                            if (!recommendationModel.isNullOrEmpty()) {
                                var listProduct: MutableList<Product> = mutableListOf()
                                for (i in recomRange) {
                                    if (!recommendationModel?.get(i)?.title.isNullOrEmpty() && !recommendationModel?.get(i)?.appLink.isNullOrEmpty()) {
                                        allowReads {
                                            sliceTracking.onImpressionSliceRecharge(recommendationModel?.get(i), getDate())
                                        }
                                        var product = Product()
                                        row {
                                            setTitleItem(createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()), SMALL_IMAGE)
                                            recommendationModel.let {
                                                it?.let {
                                                    product = Product(it.get(i).productId.toString(), it.get(i).title, rupiahFormatter(it.get(i).productPrice))
                                                    listProduct.add(i, product)
                                                }
                                                if(!it?.get(i)?.productName.isNullOrEmpty()) {
                                                    it?.get(i)?.productName?.capitalizeWords()?.let { it1 -> setTitle(it1) }
                                                } else {
                                                    it?.get(i)?.categoryName?.capitalizeWords()?.let { it1 -> setTitle(it1) }
                                                }
                                                it?.get(i)?.productPrice?.let { it1 -> setSubtitle(rupiahFormatter(it1)) }
                                            }
                                            primaryAction = createPendingIntent(recommendationModel?.get(i)?.position, recommendationModel?.get(i)?.appLink, getClickProduct(recommendationModel?.get(i)))?.let {
                                                SliceAction.create(
                                                        it,
                                                        createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()),
                                                        SMALL_IMAGE,
                                                        ""
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    allowReads {
                        sliceTracking.onNonLoginState()
                    }
                    return sliceNotLogin(sliceUri)
                }
            } catch (e: Exception) {
                allowReads {
                    sliceTracking.onErrorState()
                }
                return sliceNoAccess(sliceUri)
            }
        } else {
            allowReads {
                sliceTracking.onErrorState()
            }
            return sliceNoAccess(sliceUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun sliceNotLogin(sliceUri: Uri): Slice {
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            header {
                title = contextNonNull.resources.getString(R.string.slice_not_login)
                primaryAction = createPendingIntentLogin()?.let {
                    SliceAction.create(
                            it,
                            createWithResource(contextNonNull, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                            SMALL_IMAGE,
                            ""
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun sliceNoAccess(sliceUri: Uri): Slice {
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull,com.tokopedia.unifyprinciples.R.color.Unify_G500))
            header {
                title = contextNonNull.resources.getString(R.string.slice_not_access)
                primaryAction = createPendingIntentNoAccess()?.let {
                    SliceAction.create(
                            it,
                            createWithResource(contextNonNull, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                            SMALL_IMAGE,
                            ""
                    )
                }
            }
        }
    }


    private fun getData(sliceUri: Uri) {
        val gqlQuery = RechargeSliceGqlQuery.rechargeFavoriteRecommendationList
        val deviceId = 0
        val params = mapOf(RECHARGE_SLICE_DEVICE_ID to deviceId)
        val graphqlRequest = GraphqlRequest(gqlQuery, Data::class.java, params)
        GraphqlClient.init(contextNonNull)
        DaggerRechargeSliceComponent.builder().build().inject(this)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getReseponse(listOf(graphqlRequest)).getSuccessData<Data>()
                recommendationModel = getOnlyThreeData(data.rechargeFavoriteRecommendationList.recommendations)
                alreadyLoadData = true
                updateSlice(sliceUri)
            } catch (e: Exception) {
                isError = true
                updateSlice(sliceUri)
            }
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        contextNonNull.contentResolver.notifyChange(sliceUri, null)
    }

    private fun String.getBitmap(): Bitmap? {
        val futureTarget = Glide.with(contextNonNull)
                .asBitmap()
                .load(this)
                .submit()
        return futureTarget.get()
    }

    @SuppressLint("DefaultLocale")
    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

    override fun onCreateSliceProvider(): Boolean {
        contextNonNull = context?.applicationContext ?: return false
        remoteConfig = FirebaseRemoteConfigImpl(contextNonNull)
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        try {
            SplitCompat.install(contextNonNull)
        } catch (e: Exception){
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        return true
    }

    private fun getOnlyThreeData(recommendations: List<Recommendation>): List<Recommendation> {
        return recommendations.subList(0, 3)
    }

    private fun getClickProduct(recommendation: Recommendation?): String {
        return "${recommendation?.categoryName} - ${recommendation?.operatorName} - ${recommendation?.productName}"
    }

    private fun rupiahFormatter(nonRupiah: Int): String {
        val localeID = Locale("in", "ID")
        val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        rupiahFormat.maximumFractionDigits = 0
        return if (nonRupiah != 0) rupiahFormat.format(nonRupiah) else ""
    }

    fun getRemoteConfigRechargeSliceEnabler(context: Context): Boolean {
        remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLICE_ACTION_RECHARGE, true))
    }

    fun <T> allowReads(block: () -> T): T {
        val oldPolicy = StrictMode.allowThreadDiskReads()
        try {
            return block()
        } finally {
            StrictMode.setThreadPolicy(oldPolicy)
        }
    }

    fun getDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        return formatedDate
    }

    companion object {
        const val RECHARGE_SLICE_DEVICE_ID = "device_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"
        const val RECHARGE_NEW_HOME_PAGE = "tokopedia://recharge/home?platform_id=31"
        private val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"
    }
}