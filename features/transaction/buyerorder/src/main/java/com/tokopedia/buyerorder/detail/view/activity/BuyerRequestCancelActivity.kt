package com.tokopedia.buyerorder.detail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.fragment.BuyerRequestCancelFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import java.io.Serializable
import java.lang.reflect.Type

/**
 * Created by fwidjaja on 08/06/20.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BuyerRequestCancelActivity : BaseSimpleActivity(), HasComponent<OrderDetailsComponent> {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        val cacheId = intent.getStringExtra(BuyerConsts.PARAM_CACHE_ID).orEmpty()
        val cacheManagerId = intent.getStringExtra(BuyerConsts.PARAM_CACHE_MANAGER_ID).orEmpty()
        if (cacheId.isNotEmpty() && cacheManagerId.isNotEmpty()) {
            val cacheManager = SaveInstanceCacheManager(this, cacheManagerId)
            val mapType: Type = object : TypeToken<Map<String, Any>>() {}.type
            cacheManager.get(cacheId, mapType, mapOf<String, Any>())?.let {
                bundle.putAllPayload(it)
            }
        } else if (intent.extras != null) {
            bundle.putAll(intent.extras ?: Bundle())
        } else {
            bundle.putString(BuyerConsts.PARAM_SHOP_NAME, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE, "")
            bundle.putSerializable(BuyerConsts.PARAM_SERIALIZABLE_LIST_PRODUCT, null)
            bundle.putSerializable(BuyerConsts.PARAM_JSON_LIST_PRODUCT, null)
            bundle.putString(BuyerConsts.PARAM_ORDER_ID, "")
            bundle.putString(BuyerConsts.PARAM_URI, "")
            bundle.putBoolean(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, false)
            bundle.putString(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, "")
            bundle.putString(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, "")
            bundle.putInt(BuyerConsts.PARAM_SHOP_ID, -1)
            bundle.putString(BuyerConsts.PARAM_BOUGHT_DATE, "")
            bundle.putString(BuyerConsts.PARAM_INVOICE_URL, "")
            bundle.putString(BuyerConsts.PARAM_STATUS_ID, "")
            bundle.putString(BuyerConsts.PARAM_STATUS_INFO, "")
            bundle.putBoolean(BuyerConsts.PARAM_IS_WAIT_TO_CANCEL, false)
            bundle.putString(BuyerConsts.PARAM_WAIT_MSG, "")
        }
        return BuyerRequestCancelFragment.newInstance(bundle)
    }

    override fun getComponent(): OrderDetailsComponent {
        return DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun Bundle.putAllPayload(data: Map<String, Any>) {
        data.forEach {
            when (val value = it.value) {
                is String -> putString(it.key, value)
                is Boolean -> putBoolean(it.key, value)
                is Int -> putInt(it.key, value)
            }
        }
    }
}
