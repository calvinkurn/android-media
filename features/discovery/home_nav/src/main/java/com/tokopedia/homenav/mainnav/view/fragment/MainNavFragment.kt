package com.tokopedia.homenav.mainnav.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.homenav.R
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_main_nav.*
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {


    @Inject
    lateinit var viewModel: MainNavViewModel

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
        initAdapter()
        initClickListener()
        viewModel.getMainNavData()
    }

    private fun initAdapter() {
        val mainNavFactory = MainNavTypeFactoryImpl(this, getUserSession())
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