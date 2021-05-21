package com.tokopedia.tokomart.home.presentation.fragment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.HomeMainToolbar
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.di.component.DaggerTokoMartHomeComponent
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapter
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapterTypeFactory
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.viewholder.HomeChooseAddressWidgetViewHolder
import com.tokopedia.tokomart.home.presentation.viewmodel.TokoMartHomeViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_tokomart_home.*
import javax.inject.Inject

class TokoMartHomeFragment: Fragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: TokoMartHomeViewModel

    private var oldToolbar: HomeMainToolbar? = null
    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var localCacheModel: LocalCacheModel? = null
    private var ivHeaderBackground: ImageView? = null
    private var searchBarTransitionRange = 0
    private var startToTransitionOffset = 0


    private val adapter by lazy { TokoMartHomeAdapter(TokoMartHomeAdapterTypeFactory(this), TokoMartHomeListDiffer()) }

    private val homeMainToolbarHeight: Int
        get() {
            var height = resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
            context?.let {
//                if (isNavOld()) {
//                    if (oldToolbar != null) {
//                        height = oldToolbar?.height
//                                ?: resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
//                        oldToolbar?.let {
//                            if (!it.isShadowApplied()) {
//                                height += resources.getDimensionPixelSize(R.dimen.dp_8)
//                            }
//                        }
//                    }
//                } else if (isNavRevamp()) {
//
//                }
                navToolbar?.let {
                        height = navToolbar?.height ?: resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                        height += resources.getDimensionPixelSize(R.dimen.dp_8)
                }
            }
            return height
        }

//    private fun isNavRevamp(): Boolean {
//        try {
//            val rollanceNavType = RemoteConfigInstance.getInstance().abTestPlatform.getString(MainParentActivity.ROLLANCE_EXP_NAME, MainParentActivity.ROLLANCE_VARIANT_OLD)
//            return rollanceNavType.equals(MainParentActivity.ROLLANCE_VARIANT_REVAMP, ignoreCase = true)
//        } catch (e: Exception) {
//            return  false
//        }
//    }
//
//    private fun isNavOld(): Boolean {
//        return try {
//            getAbTestPlatform().getString(EXP_TOP_NAV, VARIANT_OLD) == VARIANT_OLD
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    companion object {
        fun newInstance() = TokoMartHomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.home_revamp_searchbar_transition_range)
        startToTransitionOffset = resources.getDimensionPixelOffset(R.dimen.dp_1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokomart_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        getLayoutData()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
    }

    private fun initInjector() {
        DaggerTokoMartHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setup() {
        setupNavToolbar()
        setupStatusBar()
        setupRecyclerView()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
    }

    private fun setupNavToolbar() {
        ivHeaderBackground = view?.findViewById(R.id.view_background_image)
        navToolbar = view?.findViewById(R.id.navToolbar)
        navToolbar?.bringToFront()
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            rvHome?.addOnScrollListener(NavRecyclerViewScrollListener(
                    navToolbar = toolbar,
                    startTransitionPixel = homeMainToolbarHeight,
                    toolbarTransitionRangePixel = searchBarTransitionRange,
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) {

                        }

                        override fun onSwitchToDarkToolbar() {
//                            navAbTestCondition(
//                                    ifNavRevamp = {
//                                        navToolbar?.hideShadow()
//                                    }
//                            )
                        }

                        override fun onSwitchToLightToolbar() {

                        }

                        override fun onYposChanged(yOffset: Int) {
                            ivHeaderBackground?.y = -(yOffset.toFloat())
                        }
                    },
                    fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            ))
            // set icon top nav
            val icons = IconBuilder(
                        IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME)
            ).addIcon(IconList.ID_SHARE) {}
                    .addIcon(IconList.ID_CART) {}
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            toolbar.setIcon(icons)
        }
        activity?.let {
            navToolbar?.setupToolbarWithStatusBar(it)
        }
    }

    private fun setupStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        statusBarBackground = view?.findViewById(R.id.status_bar_bg)
        activity?.let {
            statusBarBackground?.apply {
                background = ColorDrawable(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha(0f)
        }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = statusBarBackground?.background
        drawable?.alpha = alpha.toInt()
        statusBarBackground?.background = drawable
    }

    private fun setupRecyclerView() {
        with(rvHome) {
            adapter = this@TokoMartHomeFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLiveData() {
        observe(viewModel.homeLayout) {
            if (it is Success) adapter.submitList(it.data)
            if (!isChooseAddressWidgetShowed(false)) {
                adapter.removeHomeChooseAddressWidget()
            }
        }
    }

    private fun getLayoutData() {
        viewModel.getHomeLayout()
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        this,
                        it
                )
                if (isUpdated) {
                    adapter.updateHomeChooseAddressWidget(true)
                }
            }
        }
    }

    private fun isChooseAddressWidgetShowed(isMyShop: Boolean): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val isRollOutUser = ChooseAddressUtils.isRollOutUser(context)
        val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                HomeChooseAddressWidgetViewHolder.ENABLE_CHOOSE_ADDRESS_WIDGET,
                true
        )
//        return isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled && !isMyShop
        return true
    }

    private fun updateCurrentPageLocalCacheModelData() {
        localCacheModel = getWidgetUserAddressLocalData(context)
    }

    private fun getWidgetUserAddressLocalData(context: Context?): LocalCacheModel? {
        return context?.let{
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }
}