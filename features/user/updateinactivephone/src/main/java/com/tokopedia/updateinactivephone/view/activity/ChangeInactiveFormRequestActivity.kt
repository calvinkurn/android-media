package com.tokopedia.updateinactivephone.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IS_DUPLICATE_REQUEST
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.USER_ID
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneAnalytics
import com.tokopedia.updateinactivephone.di.component.DaggerUpdateInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import com.tokopedia.updateinactivephone.view.fragment.SelectImageNewPhoneFragment
import com.tokopedia.updateinactivephone.view.fragment.UpdateNewPhoneEmailFragment
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactiveFormRequestViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE_FORM]
 * Please pass USER_ID and OLD_PHONE
 */
class ChangeInactiveFormRequestActivity : BaseSimpleActivity(),
        HasComponent<BaseAppComponent>,
        ChangeInactiveFormRequest.View,
        SelectImageNewPhoneFragment.SelectImageInterface,
        UpdateNewPhoneEmailFragment.UpdateNewPhoneEmailInteractor {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var analytics: UpdateInactivePhoneAnalytics

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChangeInactiveFormRequestViewModel::class.java) }

    private var userId: String? = null
    private var updateNewPhoneEmailFragment: UpdateNewPhoneEmailFragment? = null
    private var newEmail: String? = null
    private var newPhoneNumber: String? = null
    private val updateInactivePhoneInfoBottomSheet by lazy { BottomSheetUnify() }
    private var loader: LoaderUnify? = null
    private var parentView: FrameLayout? = null

    override fun getNewFragment(): Fragment? {
        return SelectImageNewPhoneFragment.instance
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        initInjector()
        super.setupLayout(savedInstanceState)
        setupToolbar()
        initView()

        viewModel.validateUserDataResponse.observe(this, Observer {
            when(it){
                is Success -> {
                    when(it.data.isSuccess) {
                        true -> onUserDataValidated(it.data.userId.toString())
                        false -> {
                            dismissLoading()
                            resolveError(it.data.error)
                        }
                    }
                }
                is Fail -> {
                    dismissLoading()
                    it.throwable.message?.let { message -> analytics.eventFailedClickButtonSubmission(message) }
                    onPhoneServerError()
                }
            }
        })

        viewModel.submitImageLiveData.observe(this, Observer {
            dismissLoading()
            hideKeyboard()
            when(it){
                is Success -> {
                    when(it.data.changeInactivePhoneQuery.isSuccess) {
                        1 -> { onUpdateDataRequestSuccess() }
                        0 -> { it.data.changeInactivePhoneQuery.error.let { error -> resolveError(error) } }
                    }
                }
                is Fail -> {
                    it.throwable.message?.let { message -> analytics.eventFailedClickButtonSubmission(message) }
                    onPhoneServerError()
                }
            }
        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.change_inactive_form_layout
    }

    private fun initInjector() {
        DaggerUpdateInactivePhoneComponent.builder()
                .baseAppComponent(component)
                .updateInactivePhoneModule(UpdateInactivePhoneModule(this))
                .build()
                .inject(this)
    }

    private fun initView() {
        if (intent != null && intent.extras != null) {
            userId = intent.extras?.getString(USER_ID)
        }
        loader = findViewById(R.id.progress_bar)
        parentView = findViewById(R.id.parent_view)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(SelectImageNewPhoneFragment::class.java.name)
        if (fragment == null) {
            fragment = SelectImageNewPhoneFragment.instance
        }
        fragmentTransaction.replace(R.id.parent_view, fragment, fragment.javaClass.name)
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
            updateInactivePhoneInfoBottomSheet.setTitle("Info")
            val infoView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_info_layout, null)
            updateInactivePhoneInfoBottomSheet.setChild(infoView)
            updateInactivePhoneInfoBottomSheet.setCloseClickListener { updateInactivePhoneInfoBottomSheet.dismiss() }
            updateInactivePhoneInfoBottomSheet.show(supportFragmentManager, SelectImageNewPhoneFragment::class.java.name)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
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

    override fun setAccountPhotoImagePath(imagePath: String?) {
        imagePath?.let { viewModel.setAccountPhotoImagePath(it) }
    }

    override fun setPhotoIdImagePath(imagePath: String?) {
        imagePath?.let { viewModel.setPhotoIdImagePath(it) }
    }

    override fun dismissLoading() {
        loader?.hide()
        parentView?.show()
    }

    override fun showLoading() {
        loader?.show()
        parentView?.hide()
    }

    override fun showErrorValidateData(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(this, errorMessage)
    }

    override fun onUserDataValidated(userId: String) {
        newEmail?.let { email ->
            newPhoneNumber?.let { number ->
                viewModel.requestChangePhoneNumber(email, number, userId)
            }
        }
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

    private fun onChangePhoneNumberError(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(this, errorMessage)
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
        analytics.eventSuccessClickButtonSubmission()
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
        val phoneStatus = viewModel.isValidPhoneNumber(newPhoneNumber?: "")
        val emailStatus = viewModel.isValidEmail(newEmail?: "")
        if(phoneStatus == 0 && emailStatus == 0) {
            userId?.let {
                showLoading()
                viewModel.validateUserData(email, phone, it)
            }
        } else {
            when {
                (phoneStatus != 0) -> {showErrorPhoneNumber(phoneStatus)}
                (emailStatus != 0) -> {showErrorEmail(emailStatus)}
            }
        }
    }

    private fun resolveError(error: String) {
        analytics.eventFailedClickButtonSubmission(error)
        when {
            UpdateInactivePhoneConstants.ResponseConstants.SAME_MSISDN.equals(error, ignoreCase = true) -> onSameMsisdn()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_TOO_SHORT.equals(error, ignoreCase = true) -> onPhoneTooShort()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_TOO_LONG.equals(error, ignoreCase = true) -> onPhoneTooLong()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_BLACKLISTED.equals(error, ignoreCase = true) -> onPhoneBlackListed()
            UpdateInactivePhoneConstants.ResponseConstants.WRONG_USER_ID.equals(error, ignoreCase = true) -> onWrongUserIDInput()
            UpdateInactivePhoneConstants.ResponseConstants.PHONE_WITH_PENDING_REQUEST.equals(error, ignoreCase = true) -> onPhoneDuplicateRequest()
            UpdateInactivePhoneConstants.ResponseConstants.SERVER_ERROR.equals(error, ignoreCase = true) -> onPhoneServerError()
            UpdateInactivePhoneConstants.ResponseConstants.REGISTERED_MSISDN.equals(error, ignoreCase = true) -> onAlreadyRegisteredMsisdn()
            UpdateInactivePhoneConstants.ResponseConstants.EMPTY_MSISDN.equals(error, ignoreCase = true) -> onEmptyMsisdn()
            UpdateInactivePhoneConstants.ResponseConstants.INVALID_PHONE.equals(error, ignoreCase = true) -> onInvalidPhone()
            UpdateInactivePhoneConstants.ResponseConstants.INVALID_EMAIL.equals(error, ignoreCase = true) -> onEmailError()
            UpdateInactivePhoneConstants.ResponseConstants.SERVER_ERROR.equals(error, ignoreCase = true) -> onPhoneServerError()
            UpdateInactivePhoneConstants.ResponseConstants.MAX_REACHED_MSISDN.equals(error, ignoreCase = true) -> onMaxReachedPhone()
            else -> onChangePhoneNumberError(getString(R.string.error_general))
        }
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
