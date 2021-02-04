package com.tokopedia.sellerreview.view.bottomsheet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerreview.analytics.SellerReviewTracking
import com.tokopedia.sellerreview.common.Const
import com.tokopedia.sellerreview.view.model.SendReviewParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 22/01/21
 */

abstract class BaseBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var tracker: SellerReviewTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected var childView: View? = null
    protected var isSubmitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.UnifyBottomSheetNotOverlapStyle)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        view?.post {
            if (isVisible) {
                super.dismiss()
            }
        }
    }

    abstract fun show(fm: FragmentManager)

    protected abstract fun getResLayout(): Int

    protected abstract fun setupView(): Unit?

    protected open fun initInjector() {}

    protected open fun getParams(rating: Int, feedback: String): SendReviewParam {
        return SendReviewParam(
                userId = userSession.userId,
                rating = rating,
                feedback = feedback,
                appVersion = GlobalConfig.VERSION_NAME,
                deviceModel = Build.BRAND,
                osType = Const.OS_TYPE,
                osVersion = Build.VERSION.SDK_INT.toString()
        )
    }

    protected open fun showErrorToaster(throwable: Throwable) = childView?.run {
        val errorMessage = if (throwable is UnknownHostException) {
            context.getString(R.string.sir_toaster_error_no_connection)
        } else {
            context.getString(R.string.sir_toaster_error)
        }

        view?.let {
            Toaster.toasterCustomBottomHeight = context.resources.getDimension(R.dimen.layout_lvl8).toInt()
            val toaster = Toaster.build(it.rootView, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, context.getString(R.string.sir_ok))
            toaster.show()
        }
    }

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child = inflater.inflate(getResLayout(), container, false)
        childView = child
        setChild(child)
        setupView()
    }
}