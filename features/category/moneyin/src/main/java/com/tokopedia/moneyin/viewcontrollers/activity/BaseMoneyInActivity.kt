package com.tokopedia.moneyin.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.moneyin.MoneyInGTMConstants
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.di.DaggerMoneyInComponent
import com.tokopedia.moneyin.di.MoneyInComponent
import com.tokopedia.moneyin.viewcontrollers.ContextInterface
import com.tokopedia.moneyin.viewmodel.BaseMoneyInViewModel
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.showError
import com.tokopedia.unifycomponents.Toaster.showErrorWithAction
import javax.inject.Inject

abstract class BaseMoneyInActivity<T : BaseMoneyInViewModel> : BaseViewModelActivity<T>(), ContextInterface, HasComponent<MoneyInComponent?> {
    protected var moneyInVM: BaseMoneyInViewModel? = null
    protected var clickEvent = MoneyInGTMConstants.ACTION_CLICK_MONEYIN
    protected var viewEvent = MoneyInGTMConstants.ACTION_VIEW_MONEYIN
    protected abstract val menuRes: Int

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuRes != -1) {
            menuInflater.inflate(menuRes, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_show_tnc) {
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    MoneyInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE,
                    MoneyInGTMConstants.ACTION_CLICK_ICON_TNC,
                    "")
            showTnC()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun sendGeneralEvent(event: String?, category: String?, action: String?, label: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event,
                category,
                action,
                label)
    }

    abstract override fun getViewModelType(): Class<T>
    abstract override fun setViewModel(viewModel: BaseViewModel)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moneyInVM = ViewModelProviders.of(this, getVMFactory())[getViewModelType()]
        moneyInVM?.setContextInterface(this)
        toolbar.setTitle(R.string.money_in)
        if (supportActionBar != null) {
            supportActionBar?.setHomeAsUpIndicator(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)
        }

        setObservers()
    }

    private fun setObservers() {
        moneyInVM?.let { moneyInVM ->
            moneyInVM.getProgBarVisibility().observe(this, { visibility: Boolean? ->
                if (visibility != null) {
                    if (visibility) showProgressBar() else hideProgressBar()
                }
            })
            moneyInVM.getWarningMessage().observe(this, { message: String? ->
                hideProgressBar()
                if (!message.isNullOrEmpty()) {
                    try {
                        Toaster.build(findViewById(android.R.id.content),
                                message,
                                Snackbar.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
            moneyInVM.getErrorMessage().observe(this, { message: String? ->
                hideProgressBar()
                if (!message.isNullOrEmpty()) {
                    try {
                        Toaster.build(findViewById(android.R.id.content),
                                message,
                                Snackbar.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    protected fun showTnC() {
        RouteManager.route(this, MONEYIN_TNC_URL)
    }

    open val rootViewId: Int
        get() = R.id.root_view

    private val rootView: View
        get() = findViewById(R.id.root_view)

    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    protected fun showMessageWithAction(message: String?, actionText: String?, listener: View.OnClickListener) {
        Toaster.build(findViewById(android.R.id.content),
                message ?: "",
                Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, actionText ?: "", listener).show()
    }

    protected fun showMessage(message: String?) {
        Toaster.build(findViewById(android.R.id.content),
                message ?: "",
                Snackbar.LENGTH_LONG).show()
    }

    protected fun navigateToActivityRequest(intent: Intent?, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    protected fun showProgressBar() {
        rootView.findViewById<View>(R.id.progress_bar_layout).show()
    }

    protected fun hideProgressBar() {
        rootView.findViewById<View>(R.id.progress_bar_layout).gone()
    }

    override val contextFromActivity: Context
        get() = this

    override fun getComponent(): MoneyInComponent {
        return DaggerMoneyInComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    public override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelFactory!!
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123
        const val LOGIN_REQUEST = 514
        const val APP_SETTINGS = 9988
        const val MONEYIN_TNC_URL = "https://www.tokopedia.com/help/article/st-2135-syarat-dan-ketentuan-langsung-laku"
    }
}