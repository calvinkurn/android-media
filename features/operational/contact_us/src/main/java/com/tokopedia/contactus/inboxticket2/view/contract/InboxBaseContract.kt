package com.tokopedia.contactus.inboxticket2.view.contract

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface InboxBaseContract {
    interface InboxBaseView : CustomerView {
        fun showMessage(message: String)
        fun getActivity(): Activity
        fun navigateToActivityRequest(intent: Intent, requestCode: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun getRootView(): View?
        fun getRequestCode(): Int
        fun updateDataSet()
        fun setSnackBarErrorMessage(message: String, clickable: Boolean)
        fun clearSearch()
        fun isSearchMode(): Boolean
        fun toggleSearch(visibility: Int)
        fun startActivityForResult(intent: Intent, requestCode: Int)

        companion object {
            const val REQUEST_SUBMIT_FEEDBACK = 0X1
            const val RESULT_FINISH = 909
            const val REQUEST_DETAILS = 204
            const val REQUEST_IMAGE_PICKER = 145
        }
    }

    interface InboxBasePresenter : CustomerPresenter<InboxBaseView> {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun onDestroy()
        fun onOptionsItemSelected(item: MenuItem): Boolean
        fun reAttachView()
        fun clickCloseSearch()
        fun refreshLayout()
    }
}