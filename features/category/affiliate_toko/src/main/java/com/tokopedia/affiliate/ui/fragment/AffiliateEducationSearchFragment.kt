package com.tokopedia.affiliate.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.adapter.AffiliateEducationSearchAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSearchViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateEducationSearchFragment :
    BaseViewModelFragment<AffiliateEducationSearchViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val tabFragments = arrayListOf<Fragment>()
    private var searchBar: SearchBarUnify? = null
    private var viewPager: ViewPager2? = null
    private var affiliateEducationSearchViewModel: AffiliateEducationSearchViewModel? = null

    companion object {
        const val SEARCH_KEYWORD = "search_keyword"
        fun getFragmentInstance(searchKeyword: String?): Fragment {
            return AffiliateEducationSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_KEYWORD, searchKeyword)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.affiliate_education_search_fragment_layout,
            container,
            false
        )
        view?.findViewById<NavToolbar>(R.id.education_search_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            searchBar =
                getCustomViewContentView()?.findViewById<SearchBarUnify>(R.id.edukasi_searchbar)
                    ?.apply {
                        this.searchBarTextField.setText(arguments?.getString(SEARCH_KEYWORD))
                    }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setSearchListener(searchBar)
    }

    private fun setSearchListener(searchBar: SearchBarUnify?) {
        val searchTextField = searchBar?.searchBarTextField
        val searchClearButton = searchBar?.searchBarIcon

        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !searchTextField.text.toString()
                        .isNullOrEmpty()
                ) {
                    affiliateEducationSearchViewModel?.searchKeyword?.value =
                        searchTextField.text.toString()
                    return true
                }
                return false
            }
        })

        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
        }
    }

    private fun setupViewPager() {
        val tabLayout = view?.findViewById<TabsUnify>(R.id.tab_layout_education_search)
        viewPager = view?.findViewById(R.id.view_pager_education_search)
        activity?.let {
            tabFragments.add(
                AffiliateEducationSearchArticleFragment.getFragmentInstance(
                    AffiliateEducationSearchArticleFragment.ARTICLE,
                    arguments?.getString(SEARCH_KEYWORD)
                )
            )
            tabFragments.add(
                AffiliateEducationSearchArticleFragment.getFragmentInstance(
                    AffiliateEducationSearchArticleFragment.EVENT,
                    arguments?.getString(SEARCH_KEYWORD)
                )
            )
            tabFragments.add(
                AffiliateEducationSearchArticleFragment.getFragmentInstance(
                    AffiliateEducationSearchArticleFragment.TUTORIAL,
                    arguments?.getString(SEARCH_KEYWORD)
                )
            )
            val adapter =
                AffiliateEducationSearchAdapter(
                    childFragmentManager,
                    lifecycle,
                    context,
                    tabFragments
                )
            viewPager?.adapter = adapter
            if (tabLayout != null) {
                viewPager?.let {
                    TabLayoutMediator(tabLayout.getUnifyTabLayout(), it) { _, _ -> }.attach()
                }
            }

            tabLayout?.getUnifyTabLayout()
                ?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) = Unit

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        tab?.position?.let { adapter.setUnSelectView(tab) }
                    }

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.position?.let {
                            adapter.setOnSelectView(tab)
                        }
                    }

                })
        }
        setCustomTabText(requireContext(), tabLayout?.getUnifyTabLayout())
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()


    private fun setCustomTabText(context: Context?, tabLayout: TabLayout?) {
        if (context != null && tabLayout != null) {
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.affiliate_artikel)
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.affiliate_event)
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )
            }
            val tabThree = Typography(context)
            tabThree.apply {
                text = context.getString(R.string.affiliate_tutorial)
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )

            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
            tabLayout.getTabAt(2)?.customView = tabThree
        }
    }

    override fun getViewModelType(): Class<AffiliateEducationSearchViewModel> {
        return AffiliateEducationSearchViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateEducationSearchViewModel =
            viewModel as AffiliateEducationSearchViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectEducationSearchFragment(this)
    }
}
