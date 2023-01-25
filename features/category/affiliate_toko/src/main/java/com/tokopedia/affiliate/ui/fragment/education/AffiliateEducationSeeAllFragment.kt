package com.tokopedia.affiliate.ui.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_PROD_URL
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_STAGING_URL
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateEduCategoryChipClick
import com.tokopedia.affiliate.interfaces.AffiliateEducationSeeAllCardClickInterface
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSeeAllViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class AffiliateEducationSeeAllFragment :
    BaseViewModelFragment<AffiliateEducationSeeAllViewModel>(),
    AffiliateEducationSeeAllCardClickInterface,
    AffiliateEduCategoryChipClick {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var educationVM: AffiliateEducationSeeAllViewModel? = null
    private var pageType: String? = null
    private var categoryId: String? = null
    private var page: String? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private val seeAllAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(
                educationSeeAllCardClickInterface = this
            )
        )
    }
    private val categoryChipAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(
                affiliateEduCategoryChipClick = this
            )
        )
    }
    private var hasMoreData = true

    @JvmField
    @Inject
    var viewModelProviderFactory: ViewModelProviderFactory? = null

    companion object {
        private const val PARAM_PAGE_TYPE = "param_page_type"
        private const val PARAM_CATEGORY_ID = "param_category_id"
        fun newInstance(pageType: String?, categoryID: String?): Fragment {
            return AffiliateEducationSeeAllFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_PAGE_TYPE, pageType)
                    putString(PARAM_CATEGORY_ID, categoryID)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_education_see_all_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageType = arguments?.getString(PARAM_PAGE_TYPE)
        categoryId = arguments?.getString(PARAM_CATEGORY_ID)
        page = when (pageType) {
            PAGE_EDUCATION_EVENT -> getString(R.string.affiliate_event)
            PAGE_EDUCATION_ARTICLE -> getString(R.string.affiliate_artikel)
            PAGE_EDUCATION_TUTORIAL -> getString(R.string.affiliate_tutorial)
            else -> getString(R.string.affiliate_artikel)
        }
        educationVM?.fetchSeeAllData(pageType, categoryId)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)

        view.findViewById<RecyclerView>(R.id.rv_education_see_all)?.apply {
            loadMoreTriggerListener?.let { this.addOnScrollListener(it) }
            this.layoutManager = layoutManager
            this.adapter = seeAllAdapter
        }
        view.findViewById<RecyclerView>(R.id.rv_education_category_chip)?.apply {
            this.adapter = categoryChipAdapter
        }
        view.findViewById<NavToolbar>(R.id.education_see_all_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = page
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }
        setObservers()
    }

    private fun setObservers() {
        educationVM?.getEducationSeeAllData()?.observe(viewLifecycleOwner) {
            seeAllAdapter.addMoreData(it)
            loadMoreTriggerListener?.updateStateAfterGetData()
        }
        educationVM?.getEducationCategoryChip()?.observe(viewLifecycleOwner) {
            categoryChipAdapter.setVisitables(it)
            view?.findViewById<RecyclerView>(R.id.rv_education_category_chip)?.let { rv ->
                rv.post {
                    rv.smoothScrollToPosition(
                        categoryChipAdapter.list.indexOfFirst { visitable ->
                            (visitable as? AffiliateEduCategoryChipModel)?.chipType?.isSelected == true
                        }
                    )
                }
                sendEducationSeeAllImpressions(categoryChipAdapter.list)
            }
        }
        educationVM?.getTotalCount()?.observe(viewLifecycleOwner) {
            view?.findViewById<Typography>(R.id.tv_total_items)?.text = buildString {
                append(it)
                append(" ")
                append(page)
            }
        }
        educationVM?.hasMoreData()?.observe(viewLifecycleOwner) {
            hasMoreData = it
        }
    }

    private fun sendEducationSeeAllImpressions(list: MutableList<Visitable<Any>>) {
        when (pageType) {
            PAGE_EDUCATION_EVENT -> {
                list.forEach {
                    sendEducationImpressions(
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.title,
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.id.toString(),
                        AffiliateAnalytics.ActionKeys.IMPRESSION_EVENT_CATEGORY,
                        AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_EVENT
                    )
                }
            }
            PAGE_EDUCATION_ARTICLE -> {
                list.forEach {
                    sendEducationImpressions(
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.title,
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.id.toString(),
                        AffiliateAnalytics.ActionKeys.IMPRESSION_ARTICLE_CATEGORY,
                        AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_ARTICLE
                    )
                }
            }
            PAGE_EDUCATION_TUTORIAL -> {
                list.forEach {
                    sendEducationImpressions(
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.title,
                        (it as? AffiliateEduCategoryChipModel)?.chipType?.id.toString(),
                        AffiliateAnalytics.ActionKeys.IMPRESSION_TUTORIAL_CATEGORY,
                        AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_TUTORIAL
                    )
                }
            }
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (hasMoreData) {
                    educationVM?.fetchSeeAllData(pageType, categoryId)
                }
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProviderFactory
    }

    override fun getViewModelType(): Class<AffiliateEducationSeeAllViewModel> {
        return AffiliateEducationSeeAllViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        educationVM = viewModel as AffiliateEducationSeeAllViewModel
    }

    override fun initInject() {
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .injectEducationSeeMoreFragment(this)
    }

    override fun onCardClick(pageType: String, slug: String) {
        context?.let {
            RouteManager.route(
                it,
                getArticleEventUrl(
                    slug,
                    if (pageType == PAGE_EDUCATION_EVENT) {
                        getString(R.string.affiliate_event)
                    } else {
                        getString(R.string.affiliate_artikel)
                    }
                )
            )
        }
    }

    private fun getArticleEventUrl(slug: String, title: String): String {
        return String.format(
            Locale.getDefault(),
            "%s?title=%s&url=%s%s?navigation=hide",
            ApplinkConst.WEBVIEW,
            title.replace(" ", "+"),
            if (TokopediaUrl.getInstance().GQL.contains("staging")) {
                EDUCATION_ARTICLE_DETAIL_STAGING_URL
            } else {
                EDUCATION_ARTICLE_DETAIL_PROD_URL
            },
            slug
        )
    }

    override fun onChipClick(
        type: AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem?
    ) {
        val selectedIndex =
            categoryChipAdapter.list.indexOfFirst { (it as AffiliateEduCategoryChipModel).chipType == type }
        val previouslySelectedIndex =
            categoryChipAdapter.list.indexOfFirst { visitable ->
                (visitable as AffiliateEduCategoryChipModel).chipType?.isSelected == true
            }
        if (selectedIndex != previouslySelectedIndex && previouslySelectedIndex >= 0) {
            (categoryChipAdapter.list[selectedIndex] as AffiliateEduCategoryChipModel).chipType?.isSelected =
                true
            (categoryChipAdapter.list[previouslySelectedIndex] as AffiliateEduCategoryChipModel).chipType?.isSelected =
                false
            categoryChipAdapter.notifyItemChanged(selectedIndex)
            categoryChipAdapter.notifyItemChanged(previouslySelectedIndex)
        }
        seeAllAdapter.resetList()
        educationVM?.resetList(pageType, type?.id.toString())

        when (pageType) {
            PAGE_EDUCATION_EVENT -> {
                sendEducationClickEvent(
                    type?.title,
                    type?.id.toString(),
                    AffiliateAnalytics.ActionKeys.CLICK_EVENT_CATEGORY,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_EVENT
                )
            }
            PAGE_EDUCATION_ARTICLE -> {
                sendEducationClickEvent(
                    type?.title,
                    type?.id.toString(),
                    AffiliateAnalytics.ActionKeys.CLICK_ARTICLE_CATEGORY,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_ARTICLE
                )
            }
            PAGE_EDUCATION_TUTORIAL -> {
                sendEducationClickEvent(
                    type?.title,
                    type?.id.toString(),
                    AffiliateAnalytics.ActionKeys.CLICK_TUTORIAL_CATEGORY,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_TUTORIAL
                )
            }
        }
    }

    private fun sendEducationClickEvent(
        creativeName: String?,
        eventId: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            userSessionInterface.userId,
            creativeName
        )
    }

    private fun sendEducationImpressions(
        creativeName: String?,
        id: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.VIEW_ITEM,
            actionKeys,
            categoryKeys,
            id,
            position = 0,
            id,
            userSessionInterface.userId,
            creativeName
        )
    }
}
