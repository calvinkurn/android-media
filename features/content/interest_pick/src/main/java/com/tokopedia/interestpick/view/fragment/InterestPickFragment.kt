package com.tokopedia.interestpick.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.di.DaggerInterestPickComponent
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.presenter.InterestPickPresenter
import javax.inject.Inject

/**
 * @author by milhamj on 03/09/18.
 */

class InterestPickFragment : BaseDaggerFragment(), InterestPickContract.View {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    @Inject
    lateinit var presenter: InterestPickPresenter

    override fun getScreenName() = null

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerInterestPickComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_interest_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
        presenter.attachView(this)
        presenter.fetchData()
    }

    private fun initView() {

    }

    private fun initVar() {

    }
}