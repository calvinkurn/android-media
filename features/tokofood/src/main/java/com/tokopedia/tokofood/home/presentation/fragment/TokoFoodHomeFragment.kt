package com.tokopedia.tokofood.home.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodFragment
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeListDiffer
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodLegoComponentCallback
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodHomeFragment: BaseTokofoodFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()

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

    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
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

    private fun createLegoBannerCallback(): TokoFoodLegoComponentCallback {
        return TokoFoodLegoComponentCallback()
    }

    private fun createBannerCallback(): TokoFoodBannerComponentCallback {
        return TokoFoodBannerComponentCallback()
    }

    private fun createCategoryWidgetCallback(): TokoFoodCategoryWidgetV2ComponentCallback {
        return TokoFoodCategoryWidgetV2ComponentCallback()
    }

    // endregion

}