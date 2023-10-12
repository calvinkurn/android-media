package com.tokopedia.shop.flashsale.common.util

import com.tokopedia.campaign.data.response.RolloutFeatureVariants

class FlashSaleTokoUtil {

    fun getKeysByPrefix(prefix: String, dataRollout: RolloutFeatureVariants): MutableMap<String, Any> {
        dataRollout.featureVariants?.let {
            if (it.isNotEmpty()) {
                return mutableMapOf<String, Any>().apply {
                    for ((key, value) in it) {
                        val valueClassType = value.let { it::class.java }
                        if (key.startsWith(prefix = prefix, ignoreCase = false) && valueClassType == String::class.java) {
                            put(key, value)
                        }
                    }
                }
            } else {
                return mutableMapOf()
            }
        }
        return mutableMapOf()
    }
}
