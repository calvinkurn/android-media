package com.tokopedia.tokofood.home.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodFragment
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoFoodHomeFragment: BaseTokofoodFragment() {

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()

    private var navToolbar: NavToolbar? = null

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
    }

    private fun setupUi() {
        view?.apply {
            navToolbar = binding?.navToolbar
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

}