package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.recommendation.data.ProductRecommendationModel
import javax.inject.Inject

/**
 * Created by Pika on 20/7/20.
 */

class TopAdsInsightBaseProductFragment : BaseDaggerFragment() {


    private lateinit var adapter:TopadsRecommendationAdapter
    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    lateinit var rvRecomProduct:RecyclerView

    override fun getScreenName(): String {
        return TopAdsInsightBaseProductFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.topads_dash_recon_daily_budget_list, container, false)
        rvRecomProduct = view.findViewById(R.id.rvProductRecom)
        setAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TopadsRecommendationAdapter()
        topAdsDashboardPresenter.getProductRecommendation (::onSuccessProductRecommendation)
    }


    private fun setAdapter(){
        rvRecomProduct.adapter = adapter
        rvRecomProduct.layoutManager = LinearLayoutManager(context)

    }

    private fun onSuccessProductRecommendation(productRecommendationModel: ProductRecommendationModel) {
        productRecommendationModel.topadsGetProductRecommendation.data.products.forEach {
            adapter.items.add(it)
        }
        adapter.notifyDataSetChanged()
    }
}