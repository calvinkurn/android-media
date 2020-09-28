package com.tokopedia.gtmutil

import android.os.Bundle
import com.tokopedia.analyticparam.AnalyticParameter

object AnalyticRequirementChecker {
    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    fun checkRequired(required: Map<String, AnalyticParameter>, bundle: Bundle) {
        if (!bundle.keySet().containsAll(required.keys)) {
            throw Exception("Some required field is missing!")
        }
        for (key in required.keys) {
            if (bundle.get(key) is Bundle) {
                checkRequired(
                        (required[key] as AnalyticParameter).required!!,
                        (bundle.get(key) as Bundle?)!!)
            } else if (bundle.get(key) is List<*>) {
                (bundle.get(key) as List<Bundle>).forEach {
                    checkRequired(
                            (required[key] as AnalyticParameter).required!!,
                            it)
                }
            }
        }
    }
}
