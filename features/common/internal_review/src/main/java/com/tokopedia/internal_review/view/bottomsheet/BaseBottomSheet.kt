package com.tokopedia.internal_review.view.bottomsheet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.internal_review.R
import com.tokopedia.internal_review.analytics.ReviewTracking
import com.tokopedia.internal_review.common.Const
import com.tokopedia.internal_review.factory.createReviewTracking
import com.tokopedia.internal_review.view.model.SendReviewParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.UserSession
import java.net.UnknownHostException

/**
 * Created By @ilhamsuaib on 22/01/21
 */

abstract class BaseBottomSheet : BottomSheetUnify() {

    private var onDestroyCallback: (() -> Unit)? = null
    protected var childView: View? = null
    protected var isSubmitted = false

    val userSession by lazy { UserSession(context?.applicationContext) }
    val tracker by lazy { createReviewTracking(userSession) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyCallback?.invoke()
    }

    fun setOnDestroyListener(callback: () -> Unit) {
        this.onDestroyCallback = callback
    }

    abstract fun show(fm: FragmentManager)

    protected abstract fun getResLayout(): Int

    protected abstract fun setupView(): Unit?

    protected open fun getParams(rating: Int, feedback: String): SendReviewParam {
        return SendReviewParam(
                userId = userSession.userId,
                rating = rating,
                feedback = feedback,
                appVersion = getAppVersion(),
                deviceModel = "${Build.BRAND}_${Build.MODEL}",
                osType = Const.OS_TYPE,
                osVersion = Build.VERSION.RELEASE
        )
    }

    private fun getAppVersion() : String {
        if (GlobalConfig.isSellerApp()) {
            return GlobalConfig.VERSION_NAME
        } else if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_PRO_APPLICATION) {
            return "PRO-" + GlobalConfig.VERSION_NAME
        } else {
            return "MA-" + GlobalConfig.VERSION_NAME
        }
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