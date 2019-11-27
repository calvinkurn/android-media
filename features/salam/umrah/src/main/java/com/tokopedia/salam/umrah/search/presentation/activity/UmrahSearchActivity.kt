package com.tokopedia.salam.umrah.search.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.salam.umrah.search.di.DaggerUmrahSearchComponent
import com.tokopedia.salam.umrah.search.di.UmrahSearchComponent
import com.tokopedia.salam.umrah.search.presentation.fragment.UmrahSearchFragment

class UmrahSearchActivity : BaseSimpleActivity(), HasComponent<UmrahSearchComponent> {
    lateinit var onBackListener: UmrahPdpActivity.OnBackListener
    private var categorySlugName = ""
    private var categoryTitle = ""
    private var departureCityId = ""
    private var departurePeriod = ""
    private var priceMin = 0
    private var priceMax = 0
    private var durationDaysMin = 0
    private var durationDaysMax = 0
    private var sort = ""

    override fun getNewFragment(): Fragment? =
            UmrahSearchFragment.getInstance(categorySlugName, departureCityId, departurePeriod,
                    priceMin, priceMax, durationDaysMin, durationDaysMax, sort)

    override fun getComponent(): UmrahSearchComponent =
            DaggerUmrahSearchComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
        if (categoryTitle != "") updateTitle(categoryTitle)
        else updateTitle(getString(R.string.umrah_search_title))
    }

    private fun getIntentData() {
        val uri = intent.data
        if (uri != null) {
            when (uri.lastPathSegment) {
                getString(R.string.umrah_last_path_segment_search) -> getSearchIntent(uri)
                else -> getCategoryIntent(uri)
            }
        } else {
            categoryTitle = intent.getStringExtra(EXTRA_CATEGORY_TITLE)
            categorySlugName = intent.getStringExtra(EXTRA_CATEGORY_SLUG_NAME)
            departureCityId = intent.getStringExtra(EXTRA_DEPARTURE_CITY_ID)
            departurePeriod = intent.getStringExtra(EXTRA_DEPARTURE_PERIOD)
            priceMin = intent.getIntExtra(EXTRA_PRICE_MIN, 0)
            priceMax = intent.getIntExtra(EXTRA_PRICE_MAX, 0)
            durationDaysMin = intent.getIntExtra(EXTRA_DURATION_DAYS_MIN, 0)
            durationDaysMax = intent.getIntExtra(EXTRA_DURATION_DAYS_MAX, 0)
            sort = intent.getStringExtra(EXTRA_SORT)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun getCategoryIntent(uri: Uri?) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        categorySlugName = uri!!.lastPathSegment
        val titleArray = categorySlugName.split("-")
        categoryTitle = ""
        for (title in titleArray) categoryTitle += title.capitalize() + " "
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getSearchIntent(uri: Uri) {
        if (!uri.getQueryParameter(PARAM_PRICE_MIN).isNullOrEmpty()) priceMin = uri.getQueryParameter(PARAM_PRICE_MIN).toInt()
        if (!uri.getQueryParameter(PARAM_PRICE_MAX).isNullOrEmpty()) priceMax = uri.getQueryParameter(PARAM_PRICE_MAX).toInt()
        if (!uri.getQueryParameter(PARAM_DEPARTURE_CITY_ID).isNullOrEmpty()) departureCityId = uri.getQueryParameter(PARAM_DEPARTURE_CITY_ID)
        if (!uri.getQueryParameter(PARAM_DEPARTURE_PERIOD).isNullOrEmpty()) departurePeriod = uri.getQueryParameter(PARAM_DEPARTURE_PERIOD)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBackListener.onBack()
    }

    interface OnBackListener {
        fun onBack()
    }

    companion object {
        const val REQUEST_FILTER = 0x10

        const val PARAM_DEPARTURE_CITY_ID = "dcity"
        const val PARAM_DEPARTURE_PERIOD = "dmonth"
        const val PARAM_PRICE_MIN = "pmin"
        const val PARAM_PRICE_MAX = "pmax"

        private const val EXTRA_CATEGORY_TITLE = "EXTRA_CATEGORY_TITLE"
        const val EXTRA_CATEGORY_SLUG_NAME = "EXTRA_CATEGORY_SLUG_NAME"
        const val EXTRA_DEPARTURE_CITY_ID = "EXTRA_DEPARTURE_CITY_ID"
        const val EXTRA_DEPARTURE_PERIOD = "EXTRA_DEPARTURE_PERIOD"
        const val EXTRA_PRICE_MIN = "EXTRA_PRICE_MIN"
        const val EXTRA_PRICE_MAX = "EXTRA_PRICE_MAX"
        const val EXTRA_SORT = "EXTRA_SORT"
        const val EXTRA_DURATION_DAYS_MIN = "EXTRA_DURATION_DAYS_MIN"
        const val EXTRA_DURATION_DAYS_MAX = "EXTRA_DURATION_DAYS_MAX"

        fun createIntent(context: Context, departureCityId: String, departurePeriod: String,
                         priceMin: Int, priceMax: Int, durationMin: Int, durationMax: Int,
                         defaultSort: String): Intent =
                Intent(context, UmrahSearchActivity::class.java)
                        .putExtra(EXTRA_CATEGORY_TITLE, "")
                        .putExtra(EXTRA_CATEGORY_SLUG_NAME, "")
                        .putExtra(EXTRA_DEPARTURE_CITY_ID, departureCityId)
                        .putExtra(EXTRA_DEPARTURE_PERIOD, departurePeriod)
                        .putExtra(EXTRA_PRICE_MIN, priceMin)
                        .putExtra(EXTRA_PRICE_MAX, priceMax)
                        .putExtra(EXTRA_DURATION_DAYS_MIN, durationMin)
                        .putExtra(EXTRA_DURATION_DAYS_MAX, durationMax)
                        .putExtra(EXTRA_SORT, defaultSort)

        fun createIntent(context: Context, categoryTitle: String, categorySlugName: String): Intent =
                Intent(context, UmrahSearchActivity::class.java)
                        .putExtra(EXTRA_CATEGORY_TITLE, categoryTitle)
                        .putExtra(EXTRA_CATEGORY_SLUG_NAME, categorySlugName)
                        .putExtra(EXTRA_DEPARTURE_CITY_ID, "")
                        .putExtra(EXTRA_DEPARTURE_PERIOD, "")
                        .putExtra(EXTRA_PRICE_MIN, 0)
                        .putExtra(EXTRA_PRICE_MAX, 0)
                        .putExtra(EXTRA_DURATION_DAYS_MIN, 0)
                        .putExtra(EXTRA_DURATION_DAYS_MAX, 0)
                        .putExtra(EXTRA_SORT, "")
    }

}
