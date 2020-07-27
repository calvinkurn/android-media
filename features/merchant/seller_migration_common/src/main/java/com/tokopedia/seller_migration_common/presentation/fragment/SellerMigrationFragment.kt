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
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureFragmentAdapter
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.fragment_seller_migration.*

class SellerMigrationFragment : Fragment(), BaseSellerFeatureFragment.RecyclerViewListener {

    companion object {
        const val KEY_PARAM_FEATURE_NAME: String = "feature_name"

        fun createInstance(featureName: String): SellerMigrationFragment {
            return SellerMigrationFragment().apply {
                arguments = Bundle().also {
                    it.putString(KEY_PARAM_FEATURE_NAME, featureName)
                }
            }
        }
    }

    private val tabList = ArrayList<SellerFeatureFragmentAdapter.SellerFeatureFragmentItem>()
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
            }
        }
    }

    private var featureName: String = ""
    private var fragmentAdapter: SellerFeatureFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        featureName = arguments?.getString(KEY_PARAM_FEATURE_NAME).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_migration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBody()
        initHeader()
        initFooter()
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
        sellerMigrationButton.setOnClickListener { goToSellerApp() }
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
                0
            }
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT, SellerMigrationFeatureName.FEATURE_ATTACH_VOUCHER -> {
                1
            }
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE, SellerMigrationFeatureName.FEATURE_REVIEW_STATISTIC -> {
                2
            }
            SellerMigrationFeatureName.FEATURE_SHOP_CASHBACK_VOUCHER, SellerMigrationFeatureName.FEATURE_TOPADS -> {
                3
            }
            SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT, SellerMigrationFeatureName.FEATURE_MARKET_INSIGHT -> {
                4
            }
            else -> {
                0
            }
        }
        viewPagerOnTabSelectedListener.onPageSelected(position)
    }

    private fun initImage() {
        ivSellerMigration.urlSrc = SellerMigrationConstants.SELLER_MIGRATION_ACCOUNT_IMAGE_LINK
    }

    private fun addTabFragments() {
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(SellerFeatureProductTabFragment(this)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(SellerFeatureChatTabFragment(this)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(SellerFeatureReviewTabFragment(this)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(SellerFeatureAdsPromoTabFragment(this)))
        tabList.add(SellerFeatureFragmentAdapter.SellerFeatureFragmentItem(SellerFeatureStatisticTabFragment(this)))
    }

    private fun goToSellerApp() {
        with(SellerMigrationConstants) {
            try {
                val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
                if (intent != null) {
                    intent.putExtra(SELLER_MIGRATION_KEY_AUTO_LOGIN, true)
                    activity?.startActivity(intent)
                } else {
                    activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
                }
            } catch (anfe: ActivityNotFoundException) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
            }
        }
    }

    private fun goToInformationWebview(link: String): Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }
}