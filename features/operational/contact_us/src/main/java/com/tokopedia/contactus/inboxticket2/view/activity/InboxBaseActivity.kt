package com.tokopedia.contactus.inboxticket2.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.di.DaggerInboxComponent
import com.tokopedia.contactus.inboxticket2.di.InboxComponent
import com.tokopedia.contactus.inboxticket2.di.InboxModule
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject


abstract class InboxBaseActivity : BaseSimpleActivity(), InboxBaseView {
    abstract fun getPresenter(): InboxBasePresenter
    abstract fun initView()
    abstract fun getMenuRes(): Int
    abstract fun doNeedReattach(): Boolean
    @JvmField
    @Inject
    var mPresenter: InboxBasePresenter? = null
    @JvmField
    var mMenu: Menu? = null

    protected lateinit var component: InboxComponent
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun showMessage(message: String) {
        Toaster.make(getRootView(), message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL, "", View.OnClickListener { })
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun navigateToActivityRequest(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun showProgressBar() {
        getRootView().findViewById<View>(R.id.progress_bar_layout).visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        getRootView().findViewById<View>(R.id.progress_bar_layout).visibility = View.GONE
    }

    override fun getRootView(): View {
        return findViewById(R.id.root_view)
    }

    override fun getRequestCode(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executeInjector()
        NetworkClient.init(this)
        mPresenter = getPresenter()
        mPresenter?.attachView(this)
        initView()
    }

    override fun onRestart() {
        super.onRestart()
        if (doNeedReattach()) {
            mPresenter?.reAttachView()
        }
    }

    override fun onStop() {
        super.onStop()
        mPresenter?.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }

    private fun executeInjector() {
        component = DaggerInboxComponent.builder()
                .inboxModule(InboxModule(this))
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (getMenuRes() != -1) {
            menuInflater.inflate(getMenuRes(), menu)
            mMenu = menu
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mPresenter?.onOptionsItemSelected(item) == true || super.onOptionsItemSelected(item)
    }

    override fun setSnackBarErrorMessage(message: String, clickable: Boolean) {
        if (clickable) {
            Toaster.make(getRootView(), message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.ok), View.OnClickListener { })
        } else {
            Toaster.make(getRootView(), message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, "", View.OnClickListener {})
        }
    }
}