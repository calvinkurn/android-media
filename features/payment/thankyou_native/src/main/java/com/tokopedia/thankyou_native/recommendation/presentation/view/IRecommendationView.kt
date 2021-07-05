package com.tokopedia.thankyou_native.recommendation.presentation.view

import android.content.Intent
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.domain.model.ThanksPageData

interface IRecommendationView {
    fun loadRecommendation(thanksPageData: ThanksPageData, fragment: BaseDaggerFragment)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}