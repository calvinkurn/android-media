package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder
import com.tokopedia.tokopedianow.common.decoration.ChipListDecoration
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

abstract class TokoNowCategoryBaseFragment: BaseDaggerFragment() {

    private companion object {
        const val TOOLBAR_PAGE_NAME = "TokoNow Category"
    }

    protected var binding by autoClearedNullable<FragmentTokopedianowCategoryBaseBinding>()
    protected var adapter by autoClearedNullable<CategoryAdapter>()

    protected val navToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
            return if (binding?.navToolbar?.height.isZero()) defaultHeight else binding?.navToolbar?.height ?: defaultHeight
        }

    override fun getScreenName(): String = String.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowCategoryBaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationToolbar()
        setupRecyclerView()
    }

    private fun setupNavigationToolbar() {
        binding?.navToolbar?.apply {
            bringToFront()
            setToolbarPageName(TOOLBAR_PAGE_NAME)
            setIcon(
                IconBuilder()
                    .addShare()
                    .addCart()
                    .addNavGlobal()
            )
            setupSearchbar()
            configureToolbarBackgroundInteraction()
        }
    }

    private fun NavToolbar.setupSearchbar() = setupSearchbar(
        hints = getNavToolbarHint(),
        searchbarClickCallback = {
            val autoCompleteApplink = getAutoCompleteApplink()
            val params = getModifiedAutoCompleteQueryParam(autoCompleteApplink)
            val finalApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" +
                UrlParamUtils.generateUrlParamString(params)

            RouteManager.route(context, finalApplink)
        },
        disableDefaultGtmTracker = true,
    )

    private fun IconBuilder.addShare() = addIcon(
        iconId = IconList.ID_SHARE,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) {

    }

    private fun IconBuilder.addNavGlobal(): IconBuilder = addIcon(
        iconId = IconList.ID_NAV_GLOBAL,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) {

    }

    private fun IconBuilder.addCart(): IconBuilder = addIcon(
        iconId = IconList.ID_CART,
        disableRouteManager = false,
        disableDefaultGtmTracker = true
    ) {

    }

    private fun getNavToolbarHint(): List<HintData> {
        val hint = getString(R.string.tokopedianow_search_bar_hint)
        return listOf(HintData(hint, hint))
    }

    protected open fun getAutoCompleteApplink(): String {
        val viewModelAutoCompleteApplink = ""

        return if (viewModelAutoCompleteApplink.isEmpty())
            getBaseAutoCompleteApplink()
        else
            viewModelAutoCompleteApplink
    }

    protected open fun getModifiedAutoCompleteQueryParam(
        autoCompleteApplink: String
    ): Map<String?, String> {
        val urlParser = URLParser(autoCompleteApplink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.BASE_SRP_APPLINK] = ApplinkConstInternalTokopediaNow.SEARCH
        params[SearchApiConst.PLACEHOLDER] = context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty()
        params[SearchApiConst.PREVIOUS_KEYWORD] = getKeyword()

        return params
    }

    protected open fun getBaseAutoCompleteApplink() =
        ApplinkConstInternalDiscovery.AUTOCOMPLETE

    protected open fun getKeyword() = ""

    protected open fun configureToolbarBackgroundInteraction() {
        val navToolbar = binding?.navToolbar ?: return

        activity?.let {
            navToolbar.setupToolbarWithStatusBar(activity = it)
        }
        viewLifecycleOwner.lifecycle.addObserver(navToolbar)

        binding?.rvCategory?.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
    }

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar,
    ): RecyclerView.OnScrollListener {
        val toolbarTransitionRangePixel = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()

        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = navToolbarHeight,
            toolbarTransitionRangePixel = toolbarTransitionRangePixel,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) {

                }

                override fun onSwitchToLightToolbar() {

                }

                override fun onSwitchToDarkToolbar() {
                    navToolbar.hideShadow()
                }

                override fun onYposChanged(yOffset: Int) {

                }
            },
            fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
        )
    }

    private fun setupRecyclerView() {
        binding?.rvCategory?.apply {
            adapter = this@TokoNowCategoryBaseFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }



}
