package com.tokopedia.updateinactivephone.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat

import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IS_DUPLICATE_REQUEST
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.design.view.UpdateInactivePhoneInfoBottomSheet
import com.tokopedia.updateinactivephone.di.DaggerUpdateInactivePhoneComponent
import com.tokopedia.updateinactivephone.fragment.SelectImageNewPhoneFragment
import com.tokopedia.updateinactivephone.fragment.UpdateNewPhoneEmailFragment
import com.tokopedia.updateinactivephone.presenter.ChangeInactiveFormRequestPresenter
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest

import javax.inject.Inject

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE_FORM]
 * Please pass USER_ID and OLD_PHONE
 */
class ChangeInactiveFormRequestActivity : BaseSimpleActivity(), HasComponent<AppComponent>, ChangeInactiveFormRequest.View, SelectImageNewPhoneFragment.SelectImageInterface, UpdateNewPhoneEmailFragment.UpdateNewPhoneEmailInteractor {

    @Inject
    lateinit var presenter: ChangeInactiveFormRequestPresenter

    private var userId: String? = null
    private var tkpdProgressDialog: TkpdProgressDialog? = null
    private var updateNewPhoneEmailFragment: UpdateNewPhoneEmailFragment? = null
    private var newEmail: String? = null
    private var newPhoneNumber: String? = null
    private var updateInactivePhoneInfoBottomSheet: UpdateInactivePhoneInfoBottomSheet? = null

    override fun getNewFragment(): Fragment? {
        return SelectImageNewPhoneFragment.instance
    }

    override fun setupLayout(savedInstanceState: Bundle) {
        super.setupLayout(savedInstanceState)
        setupToolbar()
        initView()
    }

    override fun getLayoutRes(): Int {
        return R.layout.change_inactive_form_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        presenter.attachView(this)
        updateInactivePhoneInfoBottomSheet = UpdateInactivePhoneInfoBottomSheet(this)
    }

    private fun initInjector() {
        val daggerUpdateInactivePhoneComponent = DaggerUpdateInactivePhoneComponent.builder()
                .appComponent(component)
                .build() as DaggerUpdateInactivePhoneComponent

        daggerUpdateInactivePhoneComponent.inject(this)
    }

    private fun initView() {

        if (intent != null && intent.extras != null) {
            userId = intent.extras?.getString(USER_ID)
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(SelectImageNewPhoneFragment::class.java.simpleName)
        if (fragment == null) {
            fragment = SelectImageNewPhoneFragment.instance
        }
        fragmentTransaction.replace(R.id.parent_view, fragment, fragment.javaClass.simpleName)
        fragmentTransaction.addToBackStack(null).commit()

    }


    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val infoIcon = toolbar.findViewById<ImageView>(R.id.info_icon)
        infoIcon.setOnClickListener { view ->
            updateInactivePhoneInfoBottomSheet?.setTitle("Info")
            updateInactivePhoneInfoBottomSheet?.show()
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun getComponent(): AppComponent {
        return (application as MainApplication).appComponent
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onContinueButtonClick() {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        updateNewPhoneEmailFragment = supportFragmentManager.findFragmentByTag(UpdateNewPhoneEmailFragment::class.java.simpleName) as UpdateNewPhoneEmailFragment?

        if (updateNewPhoneEmailFragment == null) {
            updateNewPhoneEmailFragment = UpdateNewPhoneEmailFragment.getInstance(bundle)
        }

        val manager = supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        updateNewPhoneEmailFragment?.run {
            fragmentTransaction.add(R.id.parent_view, this).addToBackStack(null).commit()
        }
    }

    override val isValidPhotoIdPath: Boolean = presenter.isValidPhotoIdPath == true

    override fun setAccountPhotoImagePath(imagePath: String?) {
        imagePath?.let { presenter.setAccountPhotoImagePath(it) }
    }

    override fun setPhotoIdImagePath(imagePath: String?) {
        imagePath?.let { presenter.setPhotoIdImagePath(it) }
    }

    override fun dismissLoading() {
        if (tkpdProgressDialog != null)
            tkpdProgressDialog?.dismiss()
    }

    override fun showLoading() {
        if (tkpdProgressDialog == null)
            tkpdProgressDialog = TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS)

        tkpdProgressDialog?.showDialog()
    }

    override fun showErrorValidateData(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(this, errorMessage)
    }

    override fun onUserDataValidated(userId: String) {
        newEmail?.let { email -> newPhoneNumber?.let { number -> presenter.uploadPhotoIdImage(email, number, userId) } }
    }

    override fun onPhoneTooShort() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.phone_number_invalid_min_8)
        }
    }

    override fun onPhoneTooLong() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.phone_number_invalid_max_15)
        }
    }

    override fun onPhoneBlackListed() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.phone_blacklisted)
        }
    }

    override fun onWrongUserIDInput() {
        NetworkErrorHelper.showSnackbar(this, getString(R.string.wrong_user_id))
    }

    override fun onPhoneDuplicateRequest() {
        val bundle = Bundle()
        bundle.putBoolean(IS_DUPLICATE_REQUEST, true)
        val intent = ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(this, bundle)
        startActivity(intent)
        finish()
    }

    override fun onPhoneServerError() {
        NetworkErrorHelper.showSnackbar(this)
    }

    override fun onSameMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.old_new_phone_same)
        }
    }

    override fun onAlreadyRegisteredMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.already_registered_phone)
        }
    }

    override fun onEmptyMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.phone_empty)
        }
    }

    override fun onInvalidPhone() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.phone_invalid)
        }
    }

    override fun onMaxReachedPhone() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(R.string.max_limit_reached_for_phone)
        }
    }

    override fun showErrorPhoneNumber(resId: Int) {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorPhone(resId)
        }
    }

    override fun showErrorEmail(error_invalid_email: Int) {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorEmail(error_invalid_email)
        }
    }

    override fun onEmailError() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment?.showErrorEmail(R.string.error_invalid_email)
        }
    }

    override fun onUserNotRegistered() {
        NetworkErrorHelper.showSnackbar(this, getString(R.string.user_not_registered))
    }

    override fun onInvalidFileUploaded() {
        NetworkErrorHelper.showSnackbar(this)
    }

    override fun onUpdateDataRequestFailed() {
        NetworkErrorHelper.showSnackbar(this)
    }

    override fun onUpdateDataRequestSuccess() {
        val bundle = Bundle()
        bundle.putBoolean(IS_DUPLICATE_REQUEST, false)
        bundle.putString(USER_EMAIL, newEmail)
        bundle.putString(USER_PHONE, newPhoneNumber)
        val intent = ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(this, bundle)
        startActivity(intent)
        finish()
    }

    override fun onForbidden() {}

    override fun onSubmissionButtonClicked(email: String, phone: String, userId: String?) {
        newEmail = email
        newPhoneNumber = phone
        userId?.let { presenter.validateUserData(email, phone, it) }
    }

    companion object {

        fun createIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, ChangeInactiveFormRequestActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }

        fun createIntentWithUserId(context: Context, userId: String, oldPhoneNumber: String): Intent {
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)
            bundle.putString(OLD_PHONE, oldPhoneNumber)
            return createIntent(context, bundle)
        }
    }
}
