package com.tokopedia.home.account.presentation.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.PushNotifCheckerResponse
import com.tokopedia.home.account.presentation.PushNotifCheckerContract
import javax.inject.Inject


class PushNotifCheckerFragment : BaseDaggerFragment(), PushNotifCheckerContract.View {

    @Inject
    lateinit var presenter: PushNotifCheckerContract.Presenter

    companion object { }

    override fun getScreenName(): String = "Push Notification Troubleshooter"


    override fun initInjector() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_push_notif_checker, container, false).also {
            setupToolbar()
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        context?.let { GraphqlClient.init(it) }
//    }

    override fun onResume() {
        super.onResume()
        context?.let {
            GraphqlClient.init(it)
            getData()
        }
    }

    private fun getData() {
        context?.let {
            val pushNotifCheckerQuery = GraphqlHelper.loadRawString(it.resources, R.raw.query_push_notif_checker)
            presenter.getStatusPushNotifChecker(pushNotifCheckerQuery)
        }
    }

    override fun onSuccessGetStatusPushNotifChecker(data: PushNotifCheckerResponse) {
        // Change loading spinner to icon green checkmark if success, if failed, show x icon
        Log.d("data: ", data.toString())
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(e: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorNoConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
