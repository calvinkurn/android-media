package com.tokopedia.homenav.mainnav.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.analytics.TrackingBuSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingUserMenuSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {

    companion object {
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
        private const val REQUEST_LOGIN = 1234
        private const val REQUEST_REGISTER = 2345
    }

    @Inject
    lateinit var viewModel: MainNavViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: NpaLayoutManager
    lateinit var adapter: MainNavListAdapter

    private lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val baseNavComponent
                = DaggerBaseNavComponent.builder()
                .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
                .build() as DaggerBaseNavComponent

        DaggerMainNavComponent.builder()
                .baseNavComponent(baseNavComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        initAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_LOGIN, REQUEST_REGISTER -> viewModel.reloadMainNavAfterLogin()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeCategoryListData()
        viewModel.mainNavLiveData.observe(viewLifecycleOwner, Observer {
            populateAdapterData(it)
        })

        viewModel.accountLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.profileResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.membershipResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.ovoResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.shopResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })

    }

    override fun onRefresh() {
    }

    override fun onProfileLoginClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.REGISTER), REQUEST_REGISTER)
    }

    override fun onErrorProfileNameClicked(element: AccountHeaderViewModel) {
    }

    override fun onErrorProfileOVOClicked(element: AccountHeaderViewModel) {
        viewModel.reloadOvoData(element)
        viewModel.reloadSaldoData(element)
    }

    override fun onErrorProfileShopClicked(element: AccountHeaderViewModel) {
        viewModel.reloadShopData(getUserSession().shopId.toInt(), element)
    }

    override fun onMenuClick(homeNavMenuViewModel: HomeNavMenuViewModel) {
        view?.let {
            if (homeNavMenuViewModel.sectionId == MainNavConst.Section.BU_ICON) {
                TrackingBuSection.onClickBusinessUnitItem(homeNavMenuViewModel.itemTitle, userSession.userId)
                NavigationRouter.MainNavRouter.navigateTo(it, NavigationRouter.PAGE_CATEGORY,
                        bundleOf("title" to homeNavMenuViewModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuViewModel))
            } else {
                hitClickTrackingBasedOnId(homeNavMenuViewModel)
            }
        }
    }

    private fun hitClickTrackingBasedOnId(homeNavMenuViewModel: HomeNavMenuViewModel) {
        when(homeNavMenuViewModel.id) {
            ID_ALL_TRANSACTION -> TrackingTransactionSection.clickOnAllTransaction(userSession.userId)
            ID_TICKET -> TrackingTransactionSection.clickOnTicket(userSession.userId)
            ID_REVIEW -> TrackingTransactionSection.clickOnReview(userSession.userId)
            else -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuViewModel.itemTitle, userSession.userId)
        }
    }

    override fun onMenuImpression(homeNavMenuViewModel: HomeNavMenuViewModel) {

    }

    override fun getUserId(): String {
        return userSession.userId
    }

    private fun observeCategoryListData(){
        onRefresh()
        viewModel.businessListLiveData.observe(viewLifecycleOwner, Observer {
            if(it is Fail){

            }
        })
    }

    private fun initAdapter() {
        val mainNavFactory = MainNavTypeFactoryImpl(this, getUserSession())
        adapter = MainNavListAdapter(mainNavFactory)
        layoutManager = NpaLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun populateAccountHeader(data: AccountHeaderViewModel) {
        val dataList: List<Visitable<*>> = mutableListOf(data)
        adapter.submitList(dataList)
    }

    private fun populateAdapterData(data: MainNavigationDataModel) {
        adapter.submitList(data.dataList)
    }

    private fun getUserSession() : UserSessionInterface{
        if(!::userSession.isInitialized){
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }
}