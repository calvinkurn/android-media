package com.tokopedia.seller_migration_common.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureFragmentAdapter
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_seller_migration.*

class SellerMigrationFragment : Fragment(), SellerFeatureCarousel.RecyclerViewListener {

    companion object {
        const val KEY_PARAM_FEATURE_NAME: String = "feature_name"
        const val SCREEN_NAME = "/migration-page"
        const val KEY_STATE_LAST_TAB_POSITION = "last_tab_position"

        const val PRODUCT_TAB = 0
        const val DISCUSSION_TAB = 1
        const val CHAT_TAB = 2
        const val REVIEW_TAB = 3
        const val ADS_TAB = 4
        const val STATISTIC_TAB = 5
        const val FEED_PLAY_TAB = 6
        const val FINANCIAL_SERVICES_TAB = 7

        fun createInstance(@SellerMigrationFeatureName featureName: String): SellerMigrationFragment {
            return SellerMigrationFragment().apply {
                arguments = Bundle().also {
                    it.putString(KEY_PARAM_FEATURE_NAME, featureName)
                }
            }
        }
    }

    private val tabList = ArrayList<SellerFeatureFragmentAdapter.SellerFeatureFragmentItem>()
    private var lastTabPosition = -1
    private val viewPagerOnTabSelectedListener by lazy {
        object : TabLayout.TabLayoutOnPageChangeListener(tabSellerMigration.getUnifyTabLayout()) {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val view = fragmentAdapter?.getItem(position)?.view
                view?.post {
                    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)

                    if (viewPagerSellerMigration.layoutParams.height != view.measuredHeight) {
                        viewPagerSellerMigration.layoutParams = (viewPagerSellerMigration.layoutParams as LinearLayout.LayoutParams)
                                .also { lp -> lp.height = view.measuredHeight }
                    }
                }
                if (position != lastTabPosition) {
                    lastTabPosition = position
                    SellerMigrationTracking.eventClickSellerFeatureTab(tabList[position].tabName, SCREEN_NAME, userSession.userId)
                }
            }
        }
    }

    private var featureName: String = ""
    private var fragmentAdapter: SellerFeatureFragmentAdapter? = null
    private val userSession by lazy { UserSession(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            lastTabPosition = it.getInt(KEY_STATE_LAST_TAB_POSITION, -1)
        }
        featureName = arguments?.getString(KEY_PARAM_FEATURE_NAME).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_migration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initBody()
        initFooter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_STATE_LAST_TAB_POSITION, lastTabPosition)
        super.onSaveInstanceState(outState)
    }

    private fun initHeader() {
        initImage()
    }

    private fun initBody() {
        initViewPager()
        initTabs()
        selectTab()
    }

    private fun initFooter() {
        sellerMigrationButton.setOnClickListener { goToPlayStore() }
        sellerMigrationLink?.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_fragment_footer)).spannedString }
        sellerMigrationLink?.setOnTouchListener(SellerMigrationTouchListener {
            goToInformationWebview(it)
        })
    }

    override fun onRecyclerViewBindFinished() {
        viewPagerOnTabSelectedListener.onPageSelected(viewPagerSellerMigration.currentItem)
    }

    private fun initViewPager() {
        val tabLayout = tabSellerMigration.getUnifyTabLayout()
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPagerSellerMigration))
        viewPagerSellerMigration.addOnPageChangeListener(viewPagerOnTabSelectedListener)
    }

    private fun initTabs() {
        addTabFragments()

        fragmentAdapter = SellerFeatureFragmentAdapter(childFragmentManager)
        fragmentAdapter?.setItemList(tabList)
        viewPagerSellerMigration.adapter = fragmentAdapter
    }

    private fun selectTab() {
        val position = when (featureName) {
            SellerMigrationFeatureName.FEATURE_SET_VARIANT, SellerMigrationFeatureName.FEATURE_MULTI_EDIT,
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT, SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT,
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK -> {
                PRODUCT_TAB
            }
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT, SellerMigrationFeatureName.FEATURE_SELLER_CHAT,
            SellerMigrationFeatureName.FEATURE_CHAT_SETTING -> {
                CHAT_TAB
            }
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS -> {
                REVIEW_TAB
            }
            SellerMigrationFeatureName.FEATURE_SHOP_CASHBACK_VOUCHER, SellerMigrationFeatureName.FEATURE_TOPADS,
            SellerMigrationFeatureName.FEATURE_ADS, SellerMigrationFeatureName.FEATURE_ADS_DETAIL,
            SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT, SellerMigrationFeatureName.FEATURE_CENTRALIZED_PROMO -> {
                ADS_TAB
            }
            SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT, SellerMigrationFeatureName.FEATURE_MARKET_INSIGHT -> {
                STATISTIC_TAB
            }
            SellerMigrationFeatureName.FEATURE_PLAY_FEED , SellerMigrationFeatureName.FEATURE_POST_FEED-> {
                FEED_PLAY_TAB
            }
            SellerMigrationFeatureName.FEATURE_FINANCIAL_SERVICES -> {
                FINANCIAL_SERVICES_TAB
            }
            SellerMigrationFeatureName.FEATURE_DISCUSSION -> {
                DISCUSSION_TAB
            }
            else -> {
                PRODUCT_TAB
            }
        }
        viewPagerOnTabSelectedListener.onPageSelected(position)
    }

    private fun initImage() {
        ivSellerMigration.urlSrc = SellerMigrationConstants.SELLER_MIGRATION_FRAGMENT_BANNER_LINK
    }

    private fun addTabFragments() {
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureProductTabFragment>(), getString(R.string.seller_migration_fragment_tab_product)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureDiscussionTabFragment>(), getString(R.string.seller_migration_fragment_tab_discussion)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureChatTabFragment>(), getString(R.string.seller_migration_fragment_tab_chat)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureReviewTabFragment>(), getString(R.string.seller_migration_fragment_tab_review)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureAdsPromoTabFragment>(), getString(R.string.seller_migration_fragment_tab_promo_and_ads)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureStatisticTabFragment>(), getString(R.string.seller_migration_fragment_tab_statistic)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureFeedPlayTabFragment>(), getString(R.string.seller_migration_fragment_tab_feed_play)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(getSellerFeatureTabFragment<SellerFeatureFinancialServicesTabFragment>(), getString(R.string.seller_migration_fragment_tab_financial_services)))
    }

    private inline fun <reified T : BaseSellerFeatureTabFragment> getSellerFeatureTabFragment(): Fragment {
        return childFragmentManager.fragments.findOrCreate<T>().apply {
            recyclerViewListener = this@SellerMigrationFragment
        }
    }

    private inline fun <reified T> Iterable<*>.findOrCreate(): T {
        return find { it is T } as? T ?: T::class.java.newInstance()
    }

    private fun goToPlayStore() {
        with(SellerMigrationConstants) {
            try {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
            } catch (anfe: ActivityNotFoundException) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
            } finally {
                SellerMigrationTracking.eventClickGoToPlayStore(SCREEN_NAME, userSession.userId)
            }
        }
    }

    private fun goToInformationWebview(link: String): Boolean {
        return if (RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")) {
            SellerMigrationTracking.eventClickLearnMore(SCREEN_NAME, userSession.userId)
            true
        } else {
            false
        }
    }
}