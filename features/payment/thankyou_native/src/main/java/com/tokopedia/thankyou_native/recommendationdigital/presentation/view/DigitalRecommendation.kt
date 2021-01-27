package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.decorator.ProductCardDefaultDecorator
import com.tokopedia.thankyou_native.recommendationdigital.analytics.DigitalRecommendationAnalytics
import com.tokopedia.thankyou_native.recommendationdigital.di.component.DaggerDigitalRecommendationComponent
import com.tokopedia.thankyou_native.recommendationdigital.model.DigitalRecommendationList
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.DigitalRecommendationAdapter
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener.DigitalRecommendationViewListener
import com.tokopedia.thankyou_native.recommendationdigital.presentation.viewmodel.DigitalRecommendationViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_pdp_recommendation.view.*
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

    var isObserverAttached = false

    private val viewModel: DigitalRecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(fragment, viewModelFactory.get())
        viewModelProvider[DigitalRecommendationViewModel::class.java]
    }

    private lateinit var adapter: DigitalRecommendationAdapter

    var listener: DigitalRecommendationViewListener? = null

    fun getLayout() = R.layout.thank_pdp_recommendation

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
                                    fragment: BaseDaggerFragment, trackingQueue: TrackingQueue?) {
        this.thanksPageData =  thanksPageData
        this.paymentId = thanksPageData.paymentID.toString()
        this.fragment = fragment
        this.trackingQueue = trackingQueue
        startViewModelObserver()
        viewModel.getDigitalRecommendationData(5, "")
    }

    private fun startViewModelObserver() {
        if (!isObserverAttached)
            viewModel.digitalRecommendationLiveData.observe(fragment,
                    Observer {
                        when (it) {
                            is Success -> addResultToUI(it.data)
                            is Fail -> hide()
                        }
                    }
            )
        isObserverAttached = true
    }

    private fun addResultToUI(result: DigitalRecommendationList) {
        if(result.recommendations.isNullOrEmpty()){
            hide()
        }else {
            visible()
            tvTitle.text = result.title
            tvTitle.visible()
            setupRecyclerView(result.recommendations as List<RecommendationsItem>, result.title)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView(recommendationItemList: List<RecommendationsItem>,
                                  title: String?) {
        listener = getRecommendationListener()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,
                false)
        adapter = DigitalRecommendationAdapter(recommendationItemList, listener!!)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(ProductCardDefaultDecorator())
    }


    private fun getRecommendationListener(): DigitalRecommendationViewListener {
        return object : DigitalRecommendationViewListener {
            override fun onDigitalProductClick(item: RecommendationsItem, position: Int) {
                onRecomProductClick(item, position)
            }

            override fun onDigitalProductImpression(item: RecommendationsItem, position: Int) {
                analytics.get().sendDigitalRecommendationItemDisplayed(trackingQueue, item,
                        position, paymentId, thanksPageData.profileCode)
            }
        }

    }


    private fun onRecomProductClick(item: RecommendationsItem, position: Int) {
        RouteManager.route(context, item.appLink)
        analytics.get().sendDigitalRecommendationItemClick(item, position, paymentId, thanksPageData.profileCode)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

}