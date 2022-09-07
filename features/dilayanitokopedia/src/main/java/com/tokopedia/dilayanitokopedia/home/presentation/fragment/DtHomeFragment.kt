package com.tokopedia.dilayanitokopedia.home.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.FragmentDtHomeBinding
import com.tokopedia.dilayanitokopedia.home.domain.model.Data
import com.tokopedia.dilayanitokopedia.home.domain.model.SearchPlaceholder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.utils.lifecycle.autoClearedNullable


class DtHomeFragment : Fragment() {

    companion object {
        const val SOURCE = "dilayanitokopedia"

    }

    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var binding by autoClearedNullable<FragmentDtHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDtHomeBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiVariable()
        initNavToolbar()

        /**
         * Temporary
         * Remove later
         */
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)

    }

    private fun initUiVariable() {
        view?.apply {
//            ivHeaderBackground = binding?.viewBackgroundImage
            navToolbar = binding?.dtHomeNavToolbar
            statusBarBackground = binding?.dtHomeStatusBarBackground
//            rvHome = binding?.rvHome
//            swipeLayout = binding?.swipeRefreshLayout

        }
    }

    private fun initNavToolbar() {
        setupTopNavigation()
        setIconNewTopNavigation()
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton, disableDefaultGtmTracker = true)
            .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
            .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            initHint(SearchPlaceholder(Data(null, context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty(), "")))
            addNavBarScrollListener()
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
                toolbar.setToolbarTitle(getString(R.string.dt_home_title))
            }
        }
    }

    private fun initHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            navToolbar?.setupSearchbar(
                hints = listOf(
                    HintData(
                        data.placeholder.orEmpty(),
                        data.keyword.orEmpty()
                    )
                ),
                searchbarClickCallback = { onSearchBarClick() },
                searchbarImpressionCallback = {},
//                durationAutoTransition = durationAutoTransition,
//                shouldShowTransition = shouldShowTransition()
            )
        }
    }

    private fun onClickCartButton() {
    }

    private fun onClickShareButton() {

    }

    private fun addNavBarScrollListener() {
//        navBarScrollListener?.let {
//            rvHome?.addOnScrollListener(it)
//        }
    }

    private fun onSearchBarClick() {

    }

    private fun isFirstInstall(): Boolean {
//        context?.let {
//            if (!userSession.isLoggedIn && isShowFirstInstallSearch) {
//                val sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
//                var firstInstallCacheValue = sharedPrefs.getLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
//                if (firstInstallCacheValue == 0L) return false
//                firstInstallCacheValue += FIRST_INSTALL_CACHE_VALUE
//                val now = Date()
//                val firstInstallTime = Date(firstInstallCacheValue)
//                return if (now <= firstInstallTime) {
//                    true
//                } else {
//                    saveFirstInstallTime()
//                    false
//                }
//            } else {
//                return false
//            }
//        }
        return false
    }


    private fun getParamTokonowSRP() = "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}"


}