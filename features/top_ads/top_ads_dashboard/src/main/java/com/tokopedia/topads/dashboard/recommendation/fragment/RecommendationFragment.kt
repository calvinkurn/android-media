package com.tokopedia.topads.dashboard.recommendation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.viewmodel.RecommendationViewModel
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import kotlin.math.abs

class RecommendationFragment : BaseDaggerFragment() {
    private var layoutAppBar: AppBarLayout? = null
    private var saranAdsTypeTab: TabsUnify? = null
    private var saranTopAdsViewPager: ViewPager? = null

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: TopAdsHeadlineBaseFragment.AppBarActionHeadline? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[RecommendationViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private lateinit var detailPagerAdapter: TopAdsDashboardBasePagerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopAdsHeadlineBaseFragment.AppBarActionHeadline)
            collapseStateCallBack = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_fragment_saran_top_ads_layout, container, false
        )
        layoutAppBar = view.findViewById(R.id.appBarLayout)
        saranAdsTypeTab = view.findViewById(R.id.saranAdsTypeTab)
        saranTopAdsViewPager = view.findViewById(R.id.saranTopAdsViewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutAppBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED;
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE;
                }
            }
        })
        setUpObserver()
    }

    override fun getScreenName(): String? = null

    private fun setUpObserver() {
        viewModel.shopInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    renderTabAndViewPager(it.data)
                }
            }
        }
    }

    private fun renderTabAndViewPager(data: TopAdsGetShopInfoUiModel) {
        saranTopAdsViewPager?.adapter = getViewPagerAdapter(data)
        saranTopAdsViewPager?.offscreenPageLimit = 1
        saranTopAdsViewPager?.currentItem = 0
        saranTopAdsViewPager?.let { saranAdsTypeTab?.setupWithViewPager(it) }
    }

    private fun getViewPagerAdapter(data: TopAdsGetShopInfoUiModel): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        saranAdsTypeTab?.getUnifyTabLayout()?.removeAllTabs()
        saranAdsTypeTab?.customTabMode = TabLayout.MODE_FIXED
        setTabs(list, data)
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    private fun setTabs(list: MutableList<FragmentTabItem>, data: TopAdsGetShopInfoUiModel) {
        if (data.isProduct && data.isHeadline) {
            saranAdsTypeTab?.addNewTab("Iklan Produk")
            saranAdsTypeTab?.addNewTab("Iklan Toko")
            saranAdsTypeTab?.show()
        } else {
            saranAdsTypeTab?.hide()
        }
        if (data.isProduct) {
            list.add(FragmentTabItem("Iklan Produk", SaranTabsFragment(TYPE_PRODUCT)))
        }
        if (data.isHeadline) {
            list.add(FragmentTabItem("Iklan Toko", SaranTabsFragment(TYPE_SHOP)))
        }

    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
    }

    companion object {
        const val TYPE_PRODUCT = 0
        const val TYPE_SHOP = 1
    }
}
