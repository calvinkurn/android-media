package com.tokopedia.recharge_slice.ui.provider

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat.createWithBitmap
import androidx.core.graphics.drawable.IconCompat.createWithResource
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.*
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.recharge_slice.R
import com.tokopedia.recharge_slice.data.Data
import com.tokopedia.recharge_slice.data.Recommendation
import com.tokopedia.recharge_slice.ui.activity.MainActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.recharge_slice.data.Product
import com.tokopedia.recharge_slice.data.TrackingData
import com.tokopedia.recharge_slice.di.DaggerRechargeSliceComponent
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import timber.log.Timber

/**
 * @author by M on 6/12/2019
 */

class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context

    @Inject
    lateinit var repository: GraphqlRepository

    var recommendationModel: List<Recommendation>? = null

    var loadString : String ? = "Loading..."
    var alreadyGetData = false

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return createGetInvoiceV3Slice(sliceUri)
    }

    private fun createPendingIntent(id: Int?, applink: String?): PendingIntent? {
        return id?.let {
            PendingIntent.getActivity(
                    contextNonNull,
                    it,
                    RouteManager.getIntent(contextNonNull, ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createPendingIntent(id: Int?, applink: String?, trackingClick:String): PendingIntent? {
        return id?.let {
            PendingIntent.getActivity(
                    contextNonNull,
                    it,
                    RouteManager.getIntent(contextNonNull, applink)
                            .putExtra(RECHARGE_PRODUCT_EXTRA, trackingClick),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createGetInvoiceV3Slice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
                contextNonNull,
                0,
                RouteManager.getIntent(contextNonNull,ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME),
                0
        )
            if (recommendationModel == null) {
                    if(!alreadyGetData)
                    getData(sliceUri)

                    return list(contextNonNull, sliceUri, INFINITY) {
                        setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                        header {
                            title = loadString
                        }
                    }
            } else {
                return list(contextNonNull, sliceUri, INFINITY) {
                    setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                    header {
                        title = contextNonNull.resources.getString(R.string.slice_daftar_rekomendasi)
                        subtitle = (contextNonNull.resources.getString(R.string.slice_pembelian_terakhir)+recommendationModel?.get(0)?.productName).capitalizeWords()
                        primaryAction = SliceAction.create(
                                mainPendingIntent,
                                createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                                ICON_IMAGE,
                                contextNonNull.resources.getString(R.string.slice_search_title)
                        )
                    }
                    addAction(
                            SliceAction.create(
                                    mainPendingIntent,
                                    createWithBitmap(recommendationModel?.get(0)?.iconUrl?.getBitmap()),
                                    SMALL_IMAGE,
                                    ""
                            )
                    )
                    recommendationModel?.indices?.let { recomRange ->
                      var listProduct : MutableList<Product> = mutableListOf()
                        for (i in recomRange) {
                            var product = Product()
                            row {
                                setTitleItem(createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()), SMALL_IMAGE)
                                recommendationModel.let {
                                    it?.let {
                                        product = Product(it.get(i).categoryId.toString(), it.get(i).productName, it.get(i).title )
                                        listProduct.add(i, product)
                                    }
                                    it?.get(i)?.categoryName?.capitalizeWords()?.let { it1 -> setTitle(it1) }
                                    it?.get(i)?.title?.capitalizeWords()?.let { it1 -> setSubtitle(it1) }
                                }
                                val trackingClick = TrackingData(listOf(product))
                                primaryAction = createPendingIntent(recommendationModel?.get(i)?.position, recommendationModel?.get(i)?.appLink, trackingClick.toString())?.let {
                                    SliceAction.create(
                                            it,
                                            createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()),
                                            SMALL_IMAGE,
                                            ""
                                    )
                                }
                            }
                        }
                    val trackingImpression = TrackingData(listProduct)
                    Timber.d(contextNonNull.resources.getString(R.string.slice_track_timber_impression)+trackingImpression)
                    }
                }
        }
    }

    private fun createGetInvoiceV2Slice(sliceUri: Uri): Slice? {
        if (sliceUri.getQueryParameter("serviceName") != null) {
            return null
        } else {
            if (recommendationModel == null) {
                getData(sliceUri)
                return list(contextNonNull, sliceUri, INFINITY) {
                    setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                    header {
                        title = contextNonNull.resources.getString(R.string.slice_loading)
                    }
                }
            } else {
                return list(contextNonNull, sliceUri, INFINITY) {
                    setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                    header {
                        title =  contextNonNull.resources.getString(R.string.slice_daftar_rekomendasi)
                        subtitle = (contextNonNull.resources.getString(R.string.slice_pembelian_terakhir)+recommendationModel?.get(0)?.productName).capitalizeWords()
                        primaryAction = createPendingIntent(recommendationModel?.get(0)?.position, recommendationModel?.get(0)?.productName)?.let {
                            SliceAction.create(
                                    it,
                                    createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                                    ICON_IMAGE,
                                    contextNonNull.resources.getString(R.string.slice_search_title)
                            )
                        }
                    }
                    createPendingIntent(recommendationModel?.get(0)?.position, recommendationModel?.get(0)?.productName)?.let {
                        SliceAction.create(
                                it,
                                createWithBitmap(recommendationModel?.get(0)?.iconUrl?.getBitmap()),
                                SMALL_IMAGE,
                                ""
                        )
                    }?.let {
                        addAction(
                                it
                        )
                    }
                    gridRow {
                        recommendationModel?.indices?.let { recomRange ->
                            for (i in recomRange) {
                                cell {
                                    createPendingIntent(recommendationModel?.get(i)?.position, recommendationModel?.get(i)?.productName)?.let { tapSliceAction(it, createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()), SMALL_IMAGE, recommendationModel?.get(i)?.title.toString()) }
                                    addImage(createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()), SMALL_IMAGE)
                                    recommendationModel.let {
                                        it?.get(i)?.categoryName?.capitalizeWords()?.let { it1 -> addTitleText(it1) }
                                        it?.get(i)?.title?.capitalizeWords()?.let { it1 -> addText(it1) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun createGetInvoiceSlice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
                contextNonNull,
                sliceUri.hashCode(),
                Intent(contextNonNull, MainActivity::class.java),
                0
        )
        if (sliceUri.getQueryParameter("serviceName") != null) {
            return null
        } else {
            if (recommendationModel == null) {
                getData(sliceUri)
                return list(contextNonNull, sliceUri, INFINITY) {
                    setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                    header {
                        title = contextNonNull.resources.getString(R.string.slice_loading)
                    }
                }
            } else {
                return list(contextNonNull, sliceUri, INFINITY) {
                    setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
                    header {
                        title =  contextNonNull.resources.getString(R.string.slice_daftar_rekomendasi)
                        subtitle = (contextNonNull.resources.getString(R.string.slice_pembelian_terakhir)+recommendationModel?.get(0)?.productName).capitalizeWords()
                        primaryAction = createPendingIntent(recommendationModel?.get(0)?.position, recommendationModel?.get(0)?.productName)?.let {
                            SliceAction.create(
                                    it,
                                    createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                                    ICON_IMAGE,
                                    contextNonNull.resources.getString(R.string.slice_search_title)
                            )
                        }
                    }
                    createPendingIntent(recommendationModel?.get(0)?.position, recommendationModel?.get(0)?.productName)?.let {
                        SliceAction.create(
                                it,
                                createWithBitmap(recommendationModel?.get(0)?.iconUrl?.getBitmap()),
                                SMALL_IMAGE,
                                ""
                        )
                    }?.let {
                        addAction(
                                it
                        )
                    }
                    recommendationModel?.indices?.let { recomRange ->
                        for (i in recomRange) {
                            row {
                                recommendationModel.let {
                                    title = it?.get(i)?.categoryName?.capitalizeWords()
                                    subtitle = it?.get(i)?.title?.capitalizeWords()
                                }
                                addEndItem(SliceAction.create(
                                        mainPendingIntent,
                                        createWithBitmap(recommendationModel?.get(i)?.iconUrl?.getBitmap()),
                                        SMALL_IMAGE,
                                        ""
                                ))
                                primaryAction = createPendingIntent(recommendationModel?.get(i)?.position, recommendationModel?.get(i)?.productName)?.let {
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
    }

    private fun getData(sliceUri: Uri) {
        val gqlQuery = GraphqlHelper.loadRawString(contextNonNull.resources, R.raw.recharge_slice_gql)
        val deviceId = 0
        val params = mapOf(RECHARGE_SLICE_DEVICE_ID to deviceId)
        val graphqlRequest = GraphqlRequest(gqlQuery, Data::class.java, params)
        alreadyGetData = true
        GraphqlClient.init(contextNonNull)
        DaggerRechargeSliceComponent.builder().build().inject(this)
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val data = repository.getReseponse(listOf(graphqlRequest)).getSuccessData<Data>()
                recommendationModel = data.rechargeFavoriteRecommendationList.recommendations
                updateSlice(sliceUri)
            } catch (e: Exception) {
                if(e.message==NOT_LOGIN)
                loadString = contextNonNull.resources.getString(R.string.slice_not_login)
                Timber.d(contextNonNull.resources.getString(R.string.slice_track_timber_impression)+contextNonNull.resources.getString(R.string.slice_not_login))
                updateSlice(sliceUri)
            }
        }
    }

    private fun updateSlice(sliceUri: Uri){
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
        contextNonNull = context ?: return false
        return true
    }

    companion object {
        const val RECHARGE_SLICE_DEVICE_ID = "device_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val RECHARGE_USER_EXTRA = "RECHARGE_USER_EXTRA"
        const val NOT_LOGIN = "401 - UNAUTHORIZED"
    }


}