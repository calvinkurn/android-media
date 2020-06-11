package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Intent
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

interface IDigitalRecommendationView {
    fun loadRecommendation(fragment: BaseDaggerFragment)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : Boolean
}