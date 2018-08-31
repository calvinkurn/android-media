package com.tokopedia.notifcenter.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.DaggerNotifCenterComponent
import com.tokopedia.notifcenter.view.NotifCenterContract
import com.tokopedia.notifcenter.view.adapter.NotifCenterAdapter
import com.tokopedia.notifcenter.view.presenter.NotifCenterPresenter
import kotlinx.android.synthetic.main.fragment_notif_center.*
import javax.inject.Inject

/**
 * @author by alvinatin on 21/08/18.
 */

class NotifCenterFragment : BaseDaggerFragment(), NotifCenterContract.View {

    @Inject
    lateinit var adapter : NotifCenterAdapter
    @Inject
    lateinit var presenter: NotifCenterPresenter

    companion object {
        fun createInstance() = NotifCenterFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notif_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        activity?.let {
            DaggerNotifCenterComponent.builder()
                    .baseAppComponent(it.applicationContext as BaseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    private fun initView() {
        notifRv
    }
}