package com.tokopedia.play.broadcaster.view.contract

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.unifycomponents.Toaster

interface PlayEtalaseSetupCoordinator {

    fun openEtalaseDetail(
            etalaseId: String,
            etalaseName: String,
            sharedElements: List<View>
    )

    fun getParent(): Fragment

    fun openSearchPage(keyword: String)

    fun openProductSearchPage(keyword: String)

    fun postponeEnterTransition()

    fun startPostponedEnterTransition()

    fun goBack(clazz: Class<out Fragment>)

    fun showBottomAction(shouldShow: Boolean)

    fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            duration: Int = Toaster.LENGTH_SHORT,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener {  }
    )

    fun showGlobalError(errorType: Int, errorAction: () -> Unit)

    fun hideGlobalError()
}