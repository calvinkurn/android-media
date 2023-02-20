package com.tokopedia.product.addedit.common.util

import android.app.Activity
import android.content.Context
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel
import java.math.BigInteger

object SharedPreferencesUtil {

    private const val MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION = "FirstTimeSpecification"
    private const val MA_SA_ADDEDITPRODUCT_FIRST_TIME_CUSTOM_VARIANT_TYPE = "FirstTimeCVT"
    private const val MA_SA_ADDEDITPRODUCT_FIRST_TIME_WEIGHT_PER_VARIANT = "FirstTimeWPV"
    private const val MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED = "PriceWhenLoaded"
    private const val MA_SA_ADDEDITPRODUCT_PRODUCT_LIMITATION_MODEL = "ProductLimitationModel"
    private const val MA_SA_ADDEDITPRODUCT_SHIPMENT_CPL_ON_BOARDING = "CPLShipmentOnBoarding"

    fun getFirstTimeSpecification(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION, false)
    }

    fun setFirstTimeSpecification(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION, value)
            commit()
        }
    }

    fun shouldShowCPLWhitelabelOnBoarding(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_SHIPMENT_CPL_ON_BOARDING, true)
    }

    fun setCPLWhitelabelOnBoarding(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_SHIPMENT_CPL_ON_BOARDING, value)
            commit()
        }
    }

    fun getPriceWhenLoaded(activity: Activity): BigInteger {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val value = sharedPref.getString(MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED, "0")
        return value?.toBigIntegerOrNull().orZero()
    }

    fun setPriceWhenLoaded(activity: Activity, value: BigInteger) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED, value.toString())
            commit()
        }
    }

    fun getProductLimitationModel(activity: Activity): ProductLimitationModel? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref.getString(MA_SA_ADDEDITPRODUCT_PRODUCT_LIMITATION_MODEL, "").orEmpty()

        return try {
            mapJsonToObject(json, ProductLimitationModel::class.java)
        } catch (e: Exception) {
            AddEditProductErrorHandler.logExceptionToCrashlytics(e)
            AddEditProductErrorHandler.logMessage(json)
            null // catch error parsing exception
        }
    }

    fun setProductLimitationModel(activity: Activity, productLimitationModel: ProductLimitationModel) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val json = mapObjectToJson(productLimitationModel).orEmpty()
        with(sharedPref.edit()) {
            putString(MA_SA_ADDEDITPRODUCT_PRODUCT_LIMITATION_MODEL, json)
            commit()
        }
    }

    fun getFirstTimeCustomVariantType(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_CUSTOM_VARIANT_TYPE, false)
    }

    fun setFirstTimeCustomVariantType(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_CUSTOM_VARIANT_TYPE, value)
            commit()
        }
    }

    fun getFirstTimeWeightPerVariant(activity: Activity?): Boolean {
        if (activity == null) return false
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_WEIGHT_PER_VARIANT, true)
    }

    fun setFirstTimeWeightPerVariant(activity: Activity?, value: Boolean) {
        if (activity == null) return
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_WEIGHT_PER_VARIANT, value)
            commit()
        }
    }
}
