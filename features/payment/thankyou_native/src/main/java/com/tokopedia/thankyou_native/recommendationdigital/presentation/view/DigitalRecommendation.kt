package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.presentation.widget.DigitalRecommendationWidget
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendationdigital.analytics.DigitalRecommendationAnalytics
import com.tokopedia.thankyou_native.recommendationdigital.di.component.DaggerDigitalRecommendationComponent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.thank_pdp_digital_recommendation.view.*
import javax.inject.Inject

class DigitalRecommendation : FrameLayout, IDigitalRecommendationView {

    @Inject
    lateinit var analytics: dagger.Lazy<DigitalRecommendationAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: dagger.Lazy<UserSessionInterface>

    fun getLayout() = R.layout.thank_pdp_digital_recommendation

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        injectComponents()
        initUI()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        injectComponents()
        initUI()
    }

    constructor(context: Context) : super(context) {
        injectComponents()
        initUI()
    }

    private fun injectComponents() {
        DaggerDigitalRecommendationComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    private fun initUI() {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
    }

    override fun loadRecommendation(
        fragment: BaseDaggerFragment,
        pgCategoryIds: List<Int>,
        pageType: DigitalRecommendationPage
    ) {
        with(view_digital_recommendation){
            setViewModelFactory(viewModelFactory.get())
            setLifecycleOwner(fragment)
            setAdditionalData(DigitalRecommendationAdditionalTrackingData())
            setPage(pageType)
            listener = object : DigitalRecommendationWidget.Listener{
                override fun onFetchFailed(throwable: Throwable) {
                    hide()
                }

                override fun onEmptyResult() {
                    hide()
                }
            }
            build()
        }
    }
}