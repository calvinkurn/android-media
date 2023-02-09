package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_PROD_URL
import com.tokopedia.affiliate.EDUCATION_ARTICLE_DETAIL_STAGING_URL
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateEduCategoryChipClick
import com.tokopedia.affiliate.interfaces.AffiliateEducationSeeAllCardClickInterface
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSearchArticleViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSearchViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class AffiliateEducationSearchArticleFragment :
    BaseViewModelFragment<AffiliateEducationSearchArticleViewModel>(),
    AffiliateEduCategoryChipClick,
    AffiliateEducationSeeAllCardClickInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(
            requireParentFragment(),
            viewModelProvider
        )
    }

    private val categoryChipAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(
                affiliateEduCategoryChipClick = this
            )
        )
    }
    private val adapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(
                educationSeeAllCardClickInterface = this
            )
        )
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    private var affiliateEducationSearchSharedViewModel: AffiliateEducationSearchViewModel? = null
    private var affiliateEducationSearchArticleViewModel: AffiliateEducationSearchArticleViewModel? =
        null

    private var identifier = ARTICLE
    private var searchKeyword: String? = null

    companion object {
        const val ARTICLE = "article"
        const val EVENT = "event"
        const val TUTORIAL = "tutorial"
        fun getFragmentInstance(
            pageType: String?,
            keyword: String?
        ): AffiliateEducationSearchArticleFragment {
            return AffiliateEducationSearchArticleFragment().apply {
                identifier = pageType ?: ARTICLE
                searchKeyword = keyword
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.affiliate_education_search_article_fragment_layout,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        affiliateEducationSearchSharedViewModel =
            viewModelFragmentProvider[AffiliateEducationSearchViewModel::class.java]
        view.findViewById<RecyclerView>(R.id.rv_education_search_article_category_chip)?.apply {
            this.adapter = categoryChipAdapter
        }
        setObservers()
        setUpRecyclerView()
        affiliateEducationSearchArticleViewModel?.fetchSearchData(identifier, searchKeyword)
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        view?.findViewById<RecyclerView>(R.id.rv_education_search_article_recycler_view)
            ?.let { recyclerView ->
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
            }
    }

    private fun setObservers() {
        affiliateEducationSearchSharedViewModel?.getSearchKeyword()?.observe(viewLifecycleOwner) {
            adapter.resetList()
            affiliateEducationSearchArticleViewModel?.resetList(
                identifier,
                it.also { searchKeyword = it },
                null
            )
        }
        affiliateEducationSearchArticleViewModel?.getEducationSearchData()
            ?.observe(viewLifecycleOwner) {
                adapter.addMoreData(it)
            }

        affiliateEducationSearchArticleViewModel?.getEducationCategoryChip()
            ?.observe(viewLifecycleOwner) {
                categoryChipAdapter.setVisitables(it)
                view?.findViewById<RecyclerView>(R.id.rv_education_search_article_category_chip)
                    ?.let { rv ->
                        rv.post {
                            rv.smoothScrollToPosition(
                                categoryChipAdapter.list.indexOfFirst { visitable ->
                                    (visitable as? AffiliateEduCategoryChipModel)?.chipType?.isSelected == true
                                }
                            )
                        }
                    }
            }
        affiliateEducationSearchArticleViewModel?.getTotalCount()?.observe(viewLifecycleOwner) {
            view?.findViewById<Typography>(R.id.tv_total_items_education_search)?.text =
                buildString {
                    append(it)
                    append(" ")
                    append(identifier)
                }
            if (it.isZero()) {
                view?.findViewById<EmptyStateUnify>(R.id.eta_empty_state)?.visible()
                view?.findViewById<Typography>(R.id.tv_latest_article_education_search).apply {
                    this?.visible()
                    this?.text = when (identifier) {
                        ARTICLE -> getString(R.string.affiliate_article_empty_state_heading)
                        EVENT -> getString(R.string.affiliate_event_empty_state_heading)
                        TUTORIAL -> getString(R.string.affiliate_tutorial_empty_state_heading)
                        else -> ""
                    }
                }
            } else {
                view?.findViewById<EmptyStateUnify>(R.id.eta_empty_state)?.gone()
                view?.findViewById<Typography>(R.id.tv_latest_article_education_search)?.gone()
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectEducationSearchArticleFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getViewModelType(): Class<AffiliateEducationSearchArticleViewModel> {
        return AffiliateEducationSearchArticleViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateEducationSearchArticleViewModel =
            viewModel as AffiliateEducationSearchArticleViewModel
    }

    override fun onCardClick(pageType: String, slug: String) {
        context?.let {
            RouteManager.route(
                it,
                getArticleEventUrl(
                    slug,
                    when (pageType) {
                        EVENT -> getString(R.string.affiliate_event)
                        ARTICLE -> getString(R.string.affiliate_artikel)
                        TUTORIAL -> getString(R.string.affiliate_tutorial)
                        else -> getString(R.string.affiliate_event)
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

    override fun onChipClick(type: AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem?) {
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
        affiliateEducationSearchArticleViewModel?.currentCategoryId = type?.id.toString()
        adapter.resetList()
        affiliateEducationSearchArticleViewModel?.resetList(
            identifier,
            searchKeyword,
            type?.id.toString()
        )
    }
}
