package com.tokopedia.troubleshooter.notification.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

class TroubleshootFragment : BaseDaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TroubleshootViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(TroubleshootViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_notif_troubleshooter,
                container,
                false
        ).apply { setupToolbar() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservable()
        showLoading()

        /*
        * adding interval to request
        * for user experience purpose
        * */
        Handler().postDelayed({
            viewModel.troubleshoot()
        }, REQ_DELAY)
    }

    private fun initObservable() {
        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.isSuccess == 1) {
                onIconStatus(true)
                viewModel.updateToken()
            } else {
                onIconStatus(false)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            hideLoading()
            onIconStatus(false)
        })
    }

    private fun onIconStatus(isSuccess: Boolean) {
        imgStatus?.show()

        activity?.let {
            imgStatus?.setImageDrawable(if (isSuccess) {
                drawable(it, R.drawable.ic_green_checked)
            } else {
                drawable(it, R.drawable.ic_red_error)
            })
        }
    }

    private fun showLoading() {
        pgLoader?.show()
    }

    private fun hideLoading() {
        pgLoader?.hide()
    }

    private fun setupToolbar() {
        activity?.let {
            it as? TroubleshootActivity?
        }.also {
            it?.supportActionBar?.title = screenName
        }
    }

    override fun initInjector() {
        val fcmComponent = DaggerFcmComponent.builder()
                .fcmModule(FcmModule(requireContext()))
                .build()

        DaggerTroubleshootComponent.builder()
                .fcmComponent(fcmComponent)
                .troubleshootModule(TroubleshootModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Push Notification Troubleshooter"
        private const val REQ_DELAY = 2000L
    }

}