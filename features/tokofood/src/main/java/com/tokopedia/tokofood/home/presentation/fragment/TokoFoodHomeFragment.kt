package com.tokopedia.tokofood.home.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeListDiffer
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodHomeFragment: BaseDaggerFragment(), IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodHomeViewModel::class.java)
    }

    private val adapter by lazy {
        TokoFoodHomeAdapter(
            typeFactory = TokoFoodHomeAdapterTypeFactory(
                createLegoBannerCallback(),
                createBannerCallback(),
                createCategoryWidgetCallback()
            ),
            differ = TokoFoodHomeListDiffer()
        )
    }

    companion object {
        fun createInstance(): TokoFoodHomeFragment {
            return TokoFoodHomeFragment()
        }
    }

    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupNavToolbar()
        setupRecycleView()
    }

    private fun setupUi() {
        view?.apply {
            navToolbar = binding?.navToolbar
            rvHome = binding?.rvHome
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvHome?.apply {
                adapter = this@TokoFoodHomeFragment.adapter
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
           activity?.let {
                toolbar.showShadow(true)
                toolbar.setupToolbarWithStatusBar(it, applyPadding = false, applyPaddingNegative = true)
                toolbar.setToolbarTitle(getString(R.string.tokofood_title))
           }
        }
    }


    private fun setIconNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
            .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
            .addIcon(IconList.ID_NAV_GLOBAL, onClick = ::onClickNavGlobalButton)
        navToolbar?.setIcon(icons)
    }


    private fun onClickShareButton() {
        //TODO SHARE FUNC
    }

    private fun onClickNavGlobalButton() {
        //TODO CLICK NAVGLOBAL
    }

    private fun onClickListTransactionButton() {
        //TODO CLICK LIST TRANSACTION
    }


    // region TokoFood Home Component Callback

    private fun createLegoBannerCallback(): TokoFoodHomeLegoComponentCallback {
        return TokoFoodHomeLegoComponentCallback()
    }

    private fun createBannerCallback(): TokoFoodHomeBannerComponentCallback {
        return TokoFoodHomeBannerComponentCallback()
    }

    private fun createCategoryWidgetCallback(): TokoFoodHomeCategoryWidgetV2ComponentCallback {
        return TokoFoodHomeCategoryWidgetV2ComponentCallback()
    }

    // endregion

}