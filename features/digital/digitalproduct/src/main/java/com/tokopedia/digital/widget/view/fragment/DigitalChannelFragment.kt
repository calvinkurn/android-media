package com.tokopedia.digital.widget.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.core.database.manager.GlobalCacheManager
import com.tokopedia.digital.R
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService
import com.tokopedia.digital.common.data.source.CategoryListDataSource
import com.tokopedia.digital.common.data.source.StatusDataSource
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository
import com.tokopedia.digital.widget.data.source.RecommendationListDataSource
import com.tokopedia.digital.widget.domain.interactor.DigitalRecommendationUseCase
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetUseCase
import com.tokopedia.digital.widget.view.model.Recommendation
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper
import com.tokopedia.digital.widget.view.presenter.DigitalChannelContract
import com.tokopedia.digital.widget.view.presenter.DigitalChannelPresenter
import kotlinx.android.synthetic.main.fragment_digital_channel.*

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalChannelFragment: BaseDaggerFragment(), DigitalChannelContract.View {

    private lateinit var digitalChannelPresenter: DigitalChannelPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_digital_channel, container, false)

        digitalChannelPresenter.attachView(this)

        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        digitalChannelPresenter.getRecommendationList(5)
    }

    override fun initInjector() {
        val digitalEndpointService = DigitalEndpointService()
        val digitalGqlApiService = DigitalGqlApiService()
        val globalCacheManager = GlobalCacheManager()

        val statusDataSource = StatusDataSource(
                digitalEndpointService, globalCacheManager, StatusMapper()
        )

        val categoryListDataSource = CategoryListDataSource(
                digitalEndpointService, globalCacheManager, CategoryMapper()
        )

        val recommendationListDataSource = RecommendationListDataSource(
                digitalGqlApiService
        )

        val digitalWidgetRepository = DigitalWidgetRepository(
                statusDataSource, categoryListDataSource, recommendationListDataSource
        )

        val digitalRecommendationUseCase = DigitalRecommendationUseCase(digitalWidgetRepository)

        digitalChannelPresenter = DigitalChannelPresenter(
                digitalRecommendationUseCase
        )
    }

    override fun getScreenName(): String {
        return DigitalChannelFragment::class.java.simpleName
    }

    override fun renderRecommendationList(recommendations: List<Recommendation>) {
        pulsa_place_holders.visibility = View.GONE
        digital_widget_fragment.visibility = View.GONE
    }

    override fun fetchCategoryList() {
        pulsa_place_holders.visibility = View.GONE
        error_view.visibility = View.GONE
        digital_widget_fragment.visibility = View.VISIBLE
        val digitalWidgetFragment = DigitalWidgetFragment()
        fragmentManager?.beginTransaction()?.add(R.id.digital_widget_fragment, digitalWidgetFragment)
                ?.commit()
    }

    override fun showError(message: String?) {
        pulsa_place_holders.visibility = View.GONE
        digital_widget_fragment.visibility = View.GONE
        error_view.visibility = View.VISIBLE
        text_error_message.text = message
    }

}