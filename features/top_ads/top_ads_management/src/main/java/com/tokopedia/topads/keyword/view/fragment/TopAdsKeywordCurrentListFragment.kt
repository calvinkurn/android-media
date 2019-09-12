package com.tokopedia.topads.keyword.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topads.R
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption
import com.tokopedia.topads.dashboard.data.model.data.GroupAd
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordComponent
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordCurrentListActivity
import com.tokopedia.topads.keyword.view.adapter.TopAdsKeywordCurrentAdapterTypeFactory
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordListView
import com.tokopedia.topads.keyword.view.model.KeywordAd
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenter
import javax.inject.Inject

class TopAdsKeywordCurrentListFragment: BaseListFragment<KeywordAd, TopAdsKeywordCurrentAdapterTypeFactory>(),
        TopAdsKeywordListView {

    private var isPositive = true
    private var groupId = -1
    @Inject
    internal lateinit var presenter: TopAdsKeywordListPresenter

    companion object {
        fun createInstance(isPositive: Boolean, groupId: String): Fragment {
            return TopAdsKeywordCurrentListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(TopAdsKeywordCurrentListActivity.IS_POSITIVE, isPositive)
                    putString(TopAdsKeywordCurrentListActivity.GROUP_ID_PARAM, groupId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            isPositive = getBoolean(TopAdsKeywordCurrentListActivity.IS_POSITIVE, false)
            try {
                groupId = getString(TopAdsKeywordCurrentListActivity.GROUP_ID_PARAM, "-1").toInt()
            } catch (e: NumberFormatException){}
        }
    }

    override fun loadData(page: Int) {
        presenter.searchKeyword(null, null, null, KeywordStatusTypeDef.KEYWORD_STATUS_ALL,
                groupId, page, SortTopAdsOption.LATEST, isPositive)
    }

    override fun getAdapterTypeFactory() = TopAdsKeywordCurrentAdapterTypeFactory()


    override fun onItemClicked(t: KeywordAd?) {}

    override fun getEmptyDataViewModel(): Visitable<*> {
        return EmptyModel().apply {
            title = getString(if (isPositive) R.string.top_ads_keyword_your_keyword_empty else R.string.top_ads_keyword_your_keyword_negative_empty)
            content = getString(if (isPositive) R.string.top_ads_keyword_please_use else R.string.top_ads_keyword_please_use_negative)
        }
    }

    override fun getScreenName(): String?  = null

    override fun initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsComponent(getComponent(TopAdsComponent::class.java))
                .build()
                .inject(this)

        presenter.attachView(this)
    }

    override fun showListError(throwable: Throwable?) = super.showGetListError(throwable)


    override fun onSearchLoaded(data: MutableList<KeywordAd>, hasNextData: Boolean) = super.renderList(data, hasNextData)


    override fun onGetGroupAdListError() {}

    override fun onGetGroupAdList(groupAds: MutableList<GroupAd>?) {}

    override fun onBulkActionError(e: Throwable?) {}

    override fun onBulkActionSuccess(adBulkActions: PageDataResponse<DataBulkKeyword>?) {}
}