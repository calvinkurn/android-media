package com.tokopedia.troubleshooter.notification.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

class TroubleshootFragment : BaseDaggerFragment() {

    private lateinit var viewModel: TroubleshootViewModel

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservable()
        showLoading()

        viewModel.troubleshoot()
    }

    private fun initObservable() {
        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            hideLoading()
            imgStatus.show()
            onIconStatus(it.isSuccess == 1)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            hideLoading()
            onIconStatus(false)
        })
    }

    private fun onIconStatus(isSuccess: Boolean) {
        if (isSuccess) {
            imgStatus?.setImageDrawable(drawable(activity, R.drawable.ic_green_checked))
        } else {
            imgStatus?.setImageDrawable(drawable(activity, R.drawable.ic_red_error))
        }
        imgStatus?.show()
    }

    private fun showLoading() {
        pgLoader?.show()
    }

    private fun hideLoading() {
        pgLoader?.hide()
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Push Notification Troubleshooter"
    }

}