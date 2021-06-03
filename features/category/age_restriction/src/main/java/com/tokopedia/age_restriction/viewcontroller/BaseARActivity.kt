package com.tokopedia.age_restriction.viewcontroller

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.age_restriction.R
import com.tokopedia.age_restriction.di.ARComponent
import com.tokopedia.age_restriction.di.DaggerARComponent
import com.tokopedia.age_restriction.viewmodel.BaseARViewModel
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.unifycomponents.Toaster

abstract class BaseARActivity<T : BaseARViewModel> : BaseViewModelActivity<T>() {

    private var isTncShowing = false
    private lateinit var arVM: T


    companion object {
        const val LOGIN_REQUEST = 514
        const val VERIFICATION_REQUEST = 515
        const val RESULT_IS_ADULT = 980
        const val RESULT_IS_NOT_ADULT = 180
        const val PARAM_EXTRA_DOB = "VERIFY DOB"
        private const val CATEGORYPAGE = "category page"
        private const val PDP = "product detail page"
        private const val SEARCHPAGE = "search result page"
        private const val FIND_PAGE = "find page"
        private const val CLICKCATEGORY = "clickCategory"
        private const val CLICKPDP = "clickPDP"
        private const val CLICKSEARCH = "clickSearchResult"
        private const val CLICK_FIND = "clickFindResult"
        private const val VIEWPDP = "viewPDP"
        private const val VIEWCATEGORY = "viewCategory"
        private const val VIEWSEARCH = "viewSearchResult"
        private const val VIEW_FIND = "viewFindResult"
        var event = CATEGORYPAGE
        var eventView = VIEWPDP
        var eventClick = CLICKPDP
        var origin = 1
        var destinationUrlGtm = ""


    }

    protected abstract fun getMenuRes(): Int

    protected abstract fun showProgressBar()

    protected abstract fun hideProgressBar()


    abstract override fun getViewModelType(): Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arVM = ViewModelProviders.of(this,getVMFactory()).get(getViewModelType())
        origin = intent.getIntExtra("ORIGIN", 1)
        destinationUrlGtm = intent.getStringExtra("DESTINATION_GTM")
        when (origin) {
            1 -> {
                event = CATEGORYPAGE
                eventView = VIEWCATEGORY
                eventClick = CLICKCATEGORY

            }
            2 -> {
                event = PDP
                eventView = VIEWPDP
                eventClick = CLICKPDP
            }
            3 -> {
                event = SEARCHPAGE
                eventView = VIEWSEARCH
                eventClick = CLICKSEARCH
            }
            4 -> {
                event = FIND_PAGE
                eventView = VIEW_FIND
                eventClick = CLICK_FIND
            }
        }




        supportActionBar?.setHomeAsUpIndicator(com.tokopedia.design.R.drawable.ic_icon_back_black)
        arVM.getProgBarVisibility().observe(this, Observer { visibility ->
            if (visibility != null) {
                if (visibility)
                    showProgressBar()
                else
                    hideProgressBar()
            }
        })

        arVM.getWarningMessage().observe(this,Observer { message ->
            hideProgressBar()
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.make(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        arVM.getErrorMessage().observe(this, Observer { message ->
            hideProgressBar()
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.make(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getButtonStringOnError(), View.OnClickListener { })
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    private fun getButtonStringOnError(): String {
        return getString(R.string.close)
    }

    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (getMenuRes() != -1) {
            menuInflater.inflate(getMenuRes(), menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isTncShowing) {
            supportActionBar.let {
                it?.setHomeAsUpIndicator(com.tokopedia.design.R.drawable.ic_icon_back_black)
                it?.title = title
            }
            isTncShowing = false
        }
        return super.onOptionsItemSelected(item)

    }

    protected fun showMessage(message: String) {
        Toaster.make(this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
    }

    protected fun navigateToActivityRequest(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    fun getComponent(): ARComponent =
            DaggerARComponent
                    .builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                    .build()


    override fun onBackPressed() {
        if (isTncShowing) {
            supportActionBar.let {
                it?.setHomeAsUpIndicator(com.tokopedia.design.R.drawable.ic_icon_back_black)
                it?.title = title
            }
            isTncShowing = false
        }
        super.onBackPressed()
    }
}