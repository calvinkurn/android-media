package com.tokopedia.shop.product.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.isNumeric
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.OldShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SCREEN_SHOP_PAGE
import com.tokopedia.shop.common.constant.ShopCommonExtraConstant
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.databinding.ActivityNewShopProductListResultBinding
import com.tokopedia.shop.product.view.fragment.ShopPageProductListResultFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListResultFragment.Companion.createInstance
import com.tokopedia.shop.product.view.fragment.ShopPageProductListResultFragment.ShopPageProductListResultFragmentListener
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by nathan on 2/15/18.
 */
class ShopProductListResultActivity : BaseSimpleActivity(), HasComponent<ShopComponent?>, OnShopProductListFragmentListener, ShopPageProductListResultFragmentListener {
    private var component: ShopComponent? = null
    private var shopId: String? = null
    private var shopDomain: String = ""
    private var shopRef: String? = ""
    private var sourceRedirection = ""

    // this field only used first time for new fragment
    private var keyword: String? = ""
    private var etalaseId: String? = null
    private var sort: String? = null
    private var attribution: String? = null
    private var isNeedToReloadData = false
    private var shopInfo: ShopInfo? = null
    private var shopPageTracking: OldShopPageTrackingBuyer? = null
    private var editTextSearch: EditText? = null
    private var actionUpBtn: AppCompatImageView? = null
    private val viewBinding: ActivityNewShopProductListResultBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        shopId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_ID)
        shopRef = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_REF)
        etalaseId = intent.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
        sort = if (intent.getStringExtra(ShopParamConstant.EXTRA_SORT_ID) == null) "" else intent.getStringExtra(ShopParamConstant.EXTRA_SORT_ID)
        attribution = intent.getStringExtra(ShopParamConstant.EXTRA_ATTRIBUTION)
        isNeedToReloadData = intent.getBooleanExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
        sourceRedirection = intent.getStringExtra(ShopParamConstant.EXTRA_SOURCE_REDIRECTION).orEmpty()
        keyword = getSearchKeywordData(savedInstanceState)
        var data = intent.data
        if (null != data) {
            data = RouteManager.getIntent(this, intent.data.toString()).data
            val pathSegments = data?.pathSegments.orEmpty()
            getShopIdOrDomainFromUri(data, pathSegments)
            getEtalaseIdFromUri(data, pathSegments)
            shopRef = if (data?.getQueryParameter(QUERY_SHOP_REF) == null) "" else data.getQueryParameter(QUERY_SHOP_REF)
            sort = if (data?.getQueryParameter(QUERY_SORT) == null) "" else data.getQueryParameter(QUERY_SORT)
            attribution = if (data?.getQueryParameter(QUERY_ATTRIBUTION) == null) "" else data.getQueryParameter(QUERY_ATTRIBUTION)
        }
        if (shopRef == null) {
            shopRef = ""
        }
        shopPageTracking = OldShopPageTrackingBuyer(TrackingQueue(this))
        super.onCreate(savedInstanceState)
        initSearchInputView()
        viewBinding?.mainLayout?.requestFocus()
    }

    private fun getSearchKeywordData(savedInstanceState: Bundle?): String {
        return if (savedInstanceState == null) {
            when {
                intent.hasExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD) -> intent.getStringExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD).orEmpty()
                intent.hasExtra(KEY_QUERY_PARAM_EXTRA) -> getSearchKeywordDataFromQueryParam(intent.getBundleExtra(KEY_QUERY_PARAM_EXTRA))
                intent.data?.getQueryParameter(QUERY_SEARCH) != null -> intent.data?.getQueryParameter(QUERY_SEARCH).orEmpty()
                intent.data?.getQueryParameter(SearchApiConst.Q) != null -> intent.data?.getQueryParameter(SearchApiConst.Q).orEmpty()
                else -> ""
            }
        } else {
            savedInstanceState.getString(SAVED_KEYWORD, "")
        }
    }

    private fun getSearchKeywordDataFromQueryParam(bundleExtra: Bundle?): String {
        return bundleExtra?.let {
            when {
                it.containsKey(QUERY_SEARCH) -> it.getString(QUERY_SEARCH, "")
                it.containsKey(SearchApiConst.Q) -> it.getString(SearchApiConst.Q, "")
                else -> ""
            }
        }.orEmpty()
    }

    private fun getShopIdOrDomainFromUri(data: Uri?, pathSegments: List<String>) {
        if (pathSegments.size >= 2) {
            val segmentData = data?.pathSegments?.getOrNull(SHOP_ID_OR_DOMAIN_PATH_SEGMENT).orEmpty()
            if (segmentData.isNumeric()) {
                // take segment data as shop id if it's a numeric data
                shopId = segmentData
            } else {
                // take segment data as shop domain if it's non numeric data
                shopDomain = segmentData
                shopId = "0"
            }
        } else {
            shopId = "0"
        }
    }

    private fun getEtalaseIdFromUri(data: Uri?, pathSegments: List<String>) {
        etalaseId = if (pathSegments.size >= SHOWCASE_APP_LINK_MINIMUM_PATH_SEGMENTS) {
            data?.pathSegments?.getOrNull(SHOWCASE_ID_POSITION_ON_APP_LINK).orEmpty()
        } else {
            "0"
        }
    }

    private fun initSearchInputView() {
        editTextSearch = viewBinding?.editTextSearchProduct
        actionUpBtn = viewBinding?.actionUpBtn
        editTextSearch?.setText(keyword)
        editTextSearch?.setKeyListener(null)
        editTextSearch?.setMovementMethod(null)
        editTextSearch?.setOnClickListener(
            View.OnClickListener { view: View? ->
                if (null != shopPageTracking) shopPageTracking?.clickSearchBox(SCREEN_SHOP_PAGE)
                if (null != shopInfo) {
                    if (fragment is ShopPageProductListResultFragment) (fragment as ShopPageProductListResultFragment?)?.onSearchBarClicked()
                }
            }
        )
        actionUpBtn?.setOnClickListener(View.OnClickListener { view: View? -> finish() })
    }

    override fun getNewFragment(): Fragment? {
        return createInstance(shopId.orEmpty(), shopDomain, shopRef, keyword, etalaseId, sort, attribution, isNeedToReloadData, sourceRedirection)
    }

    override fun getComponent(): ShopComponent? {
        if (component == null) {
            component = ShopComponentHelper().getComponent(application, this)
        }
        return component
    }

    override fun updateUIByShopName(shopName: String) {
        if (null != editTextSearch) {
            editTextSearch?.hint = getString(
                R.string.shop_product_search_hint_2,
                MethodChecker.fromHtml(shopName)
            )
        }
    }

    override fun updateUIByEtalaseName(etalaseName: String?) {
        if (null != editTextSearch) {
            editTextSearch?.hint = getString(
                R.string.shop_product_search_hint_3,
                MethodChecker.fromHtml(etalaseName)
            )
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_shop_product_list_result
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = RouteManager.getIntentNoFallback(this, ApplinkConst.HOME)
            if (intent != null) {
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_KEYWORD, keyword)
    }

    override fun updateShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
    }

    override fun onSortValueUpdated(sortValue: String) {
        sort = sortValue
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    companion object {
        const val SAVED_KEYWORD = "svd_keyword"
        private const val QUERY_SHOP_REF = "shop_ref"
        private const val QUERY_SORT = "sort"
        private const val QUERY_ATTRIBUTION = "tracker_attribution"
        private const val QUERY_SEARCH = "search"
        private const val SHOWCASE_APP_LINK_MINIMUM_PATH_SEGMENTS = 4
        private const val SHOP_ID_OR_DOMAIN_PATH_SEGMENT = 1
        private const val SHOWCASE_ID_POSITION_ON_APP_LINK = 3
        private const val KEY_QUERY_PARAM_EXTRA = "QUERY_PARAM"
        fun createIntent(
            context: Context?,
            shopId: String?,
            keyword: String?,
            etalaseId: String?,
            attribution: String?,
            sortId: String?,
            shopRef: String?
        ): Intent {
            val intent = createIntent(context, shopId, keyword, etalaseId, attribution, shopRef)
            intent.putExtra(ShopParamConstant.EXTRA_SORT_ID, sortId)
            return intent
        }

        fun createIntent(
            context: Context?,
            shopId: String?,
            keyword: String?,
            etalaseId: String?,
            attribution: String?,
            shopRef: String?
        ): Intent {
            val intent = Intent(context, ShopProductListResultActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword)
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId)
            intent.putExtra(ShopParamConstant.EXTRA_ATTRIBUTION, attribution)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_REF, shopRef)
            return intent
        }

        fun createIntentWithSourceRedirection(
            context: Context?,
            shopId: String?,
            keyword: String?,
            etalaseId: String?,
            attribution: String?,
            shopRef: String?,
            sourceRedirection: String?
        ): Intent {
            val intent = createIntent(context, shopId, keyword, etalaseId, attribution, shopRef)
            intent.putExtra(ShopParamConstant.EXTRA_SOURCE_REDIRECTION, sourceRedirection)
            return intent
        }

        fun createIntent(context: Context?, shopId: String?, shopRef: String?): Intent {
            val intent = Intent(context, ShopProductListResultActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_REF, shopRef)
            return intent
        }
    }
}
