package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.presentation.widget.DigitalRecommendationWidget
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.ThankPageType
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendationdigital.analytics.DigitalRecommendationAnalytics
import com.tokopedia.thankyou_native.recommendationdigital.di.component.DaggerDigitalRecommendationComponent
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationItem
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener.DigitalRecommendationViewListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.thank_pdp_digital_recommendation.view.*
import javax.inject.Inject

class DigitalRecommendation : FrameLayout, IDigitalRecommendationView {


    private lateinit var fragment: BaseDaggerFragment
    private var trackingQueue: TrackingQueue? = null
    private lateinit var paymentId: String
    private lateinit var thanksPageData: ThanksPageData

    @Inject
    lateinit var analytics: dagger.Lazy<DigitalRecommendationAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: dagger.Lazy<UserSessionInterface>

    var listener: DigitalRecommendationViewListener? = null

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

    override fun loadRecommendation(thanksPageData: ThanksPageData,
                                    fragment: BaseDaggerFragment,
                                    trackingQueue: TrackingQueue?,
                                    pgCategoryIds: List<Int>,
                                    pageType: ThankPageType
    ) {
        this.thanksPageData =  thanksPageData
        this.paymentId = thanksPageData.paymentID
        this.fragment = fragment
        this.trackingQueue = trackingQueue

        with(view_digital_recommendation){
            setViewModelFactory(viewModelFactory.get())
            setLifecycleOwner(fragment)
            setAdditionalData(DigitalRecommendationAdditionalTrackingData())
            setPage(DigitalRecommendationPage.DIGITAL_GOODS)
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

    //TODO : will be delete
    private fun onRecomProductClick(item: RecommendationItem, position: Int) {
        RouteManager.route(context, item.appLink)
        analytics.get().sendDigitalRecommendationItemClick(item, position, paymentId, thanksPageData.profileCode)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

}