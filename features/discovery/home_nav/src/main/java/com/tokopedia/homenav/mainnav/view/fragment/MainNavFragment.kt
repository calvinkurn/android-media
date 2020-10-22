package com.tokopedia.homenav.mainnav.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavAdapter
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.view.adapter.MainNavAdapter
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_main_nav.*
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {


    @Inject
    lateinit var viewModel: MainNavViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MainNavAdapter

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
        val view = inflater.inflate(R.layout.fragment_main_nav, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClickListener()
        viewModel.getMainNavData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.mainNavLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> populateAdapterData(it.data)
                is Fail -> {}
            }
        })

        viewModel.accountLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> populateAccountHeader(it.data)
            }
        })
    }
    private fun initAdapter() {
        val mainNavFactory = MainNavTypeFactoryImpl(this, getUserSession())
        adapter = MainNavAdapter(mainNavFactory)
        layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun populateAccountHeader(data: AccountHeaderViewModel) {
        val dataList: List<HomeNavVisitable> = mutableListOf(data)
        adapter.submitList(dataList)
    }

    private fun populateAdapterData(data: MainNavigationDataModel) {
        adapter.submitList(data.dataList)
    }

    private fun initClickListener() {
        testButton.setOnClickListener{
            NavigationRouter.MainNavRouter.navigateTo(it, NavigationRouter.PAGE_CATEGORY)
        }
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