package com.tokopedia.similarsearch

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class SimilarSearchTestRunner: AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, SimilarSearchTestApplication::class.java.name, context)
    }
}