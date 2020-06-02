package com.tokopedia.thankyou_native.recommendation.presentation.view

import android.content.Intent
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

interface IRecommendationView {
    fun loadRecommendation(fragment: BaseDaggerFragment)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : Boolean
}