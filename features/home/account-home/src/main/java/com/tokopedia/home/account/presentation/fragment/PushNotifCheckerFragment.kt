package com.tokopedia.home.account.presentation.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.NotifierSendTroubleshooter
import com.tokopedia.home.account.di.component.DaggerPushNotifCheckerComponent
import com.tokopedia.home.account.di.module.PushNotifCheckerModule
import com.tokopedia.home.account.presentation.PushNotifCheckerContract
import javax.inject.Inject


class PushNotifCheckerFragment : BaseDaggerFragment(), PushNotifCheckerContract.View {

    @Inject
    lateinit var presenter: PushNotifCheckerContract.Presenter
    private val progressBar: ProgressBar? by lazy { activity?.findViewById(R.id.progress_push_notif) as? ProgressBar }
    private val imgSuccessPushNotif: ImageView? by lazy { activity?.findViewById(R.id.img_success_push_notif_status) as? ImageView }
    private val imgErrorPushNotif: ImageView? by lazy { activity?.findViewById(R.id.img_error_push_notif_status) as? ImageView }

    companion object { }

    override fun getScreenName(): String = "Push Notification Troubleshooter"

    override fun initInjector() {
        activity?.let {
            DaggerPushNotifCheckerComponent.builder()
                    .pushNotifCheckerModule(PushNotifCheckerModule())
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)

            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_push_notif_checker, container, false).also {
            setupToolbar()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgSuccessPushNotif?.visibility = View.GONE
        showLoading()
        context?.let {
            GraphqlClient.init(it)
            presenter.getStatusPushNotifChecker()
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    override fun onSuccessGetStatusPushNotifChecker(data: NotifierSendTroubleshooter) {
        hideLoading()
        if (data.isSuccess == 1) {
            imgSuccessPushNotif?.visibility = View.VISIBLE
            imgErrorPushNotif?.visibility = View.GONE
        } else {
            imgSuccessPushNotif?.visibility = View.GONE
            imgErrorPushNotif?.visibility = View.VISIBLE
        }
    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun showError(message: String?) {

    }

    override fun showError(e: Throwable?) {

    }

    override fun showErrorNoConnection() {

    }

}
