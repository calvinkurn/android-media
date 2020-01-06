package com.tokopedia.profilecompletion.settingprofile.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.PhoneNumberUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.changename.data.analytics.ChangeNameTracker
import com.tokopedia.profilecompletion.customview.UnifyDialog
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.data.ProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.data.UploadProfilePictureResult
import com.tokopedia.profilecompletion.settingprofile.domain.UrlSettingProfileConst
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileRoleViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.url.Url
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_setting_profile.*
import java.io.File
import java.net.URLEncoder
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class SettingProfileFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val profileInfoViewModel by lazy { viewModelProvider.get(ProfileInfoViewModel::class.java) }
    private val profileRoleViewModel by lazy { viewModelProvider.get(ProfileRoleViewModel::class.java) }

    lateinit var overlayView: View
    lateinit var tickerPhoneVerification: Ticker

    private var chancesChangeName = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_profile, container, false)
        overlayView = view.findViewById(R.id.overlay_view)
        tickerPhoneVerification = view.findViewById(R.id.ticker_phone_verification)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImageHandler.LoadImage(headerPhoto, HEADER_PICT_URL)

        overlayView.setOnClickListener { }
        initObserver()

        profilePhoto.setOnClickListener(EditUserProfilePhotoListener())
        btnEditProfilePhoto.setOnClickListener(EditUserProfilePhotoListener())

        initSettingProfileData()
    }

    private fun showChangeEmailDialog() {
        val dialog = UnifyDialog(activity as Activity, UnifyDialog.VERTICAL_ACTION, UnifyDialog.NO_HEADER)
        dialog.setTitle(getString(R.string.add_and_verify_phone))
        dialog.setDescription(getString(R.string.add_and_verify_phone_detail))
        dialog.setOk(getString(R.string.title_add_phone))
        dialog.setOkOnClickListner(View.OnClickListener { goToAddPhone() })
        dialog.setSecondary(getString(com.tokopedia.abstraction.R.string.label_cancel))
        dialog.setSecondaryOnClickListner(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun showVerifyEmailDialog() {
        val dialog = UnifyDialog(activity as Activity, UnifyDialog.VERTICAL_ACTION, UnifyDialog.NO_HEADER)
        dialog.setTitle(getString(R.string.add_and_verify_phone))
        dialog.setDescription(getString(R.string.add_and_verify_phone_detail))
        dialog.setOk(getString(R.string.title_verify_phone))
        dialog.setOkOnClickListner(View.OnClickListener { goToVerifyPhone() })
        dialog.setSecondary(getString(com.tokopedia.abstraction.R.string.label_cancel))
        dialog.setSecondaryOnClickListner(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun initObserver() {
        profileInfoViewModel.userProfileInfo.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetUserProfileInfo(it.data)
                is Fail -> onErrorGetProfileInfo(it.throwable)
            }
        })

        profileInfoViewModel.uploadProfilePictureResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessUploadProfilePicture(it.data)
                is Fail -> onErrorUploadProfilePicture(it.throwable)
            }
        })

        profileRoleViewModel.userProfileRole.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetProfileRole(it.data)
                is Fail -> onErrorGetProfileRole(it.throwable)
            }
        })
    }

    private fun onErrorUploadProfilePicture(throwable: Throwable) {
        dismissLoading()
        view?.run {
            Toaster.showError(
                    this,
                    ErrorHandlerSession.getErrorMessage(throwable, context, false),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessUploadProfilePicture(result: UploadProfilePictureResult) {
        dismissLoading()

        if (result.uploadProfileImageModel.data.filePath.isNotBlank()) {
            userSession.profilePicture = result.uploadProfileImageModel.data.filePath

            view?.run {
                Toaster.showNormal(this, getString(R.string.success_change_profile_picture), Snackbar.LENGTH_LONG)
            }
            ImageHandler.loadImageCircle2(context, profilePhoto,
                    result.uploadProfileImageModel.data.filePath)
        } else {
            onErrorUploadProfilePicture(MessageErrorException(getString(R.string.failed_to_upload_picture)))
        }

    }

    private fun initSettingProfileData() {
        showLoading()
        profileInfoViewModel.getUserProfileInfo(context!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
                        onSuccessGetProfilePhoto(data)
                    }
                    REQUEST_CODE_CHANGE_NAME -> {
                        onSuccessChangeName(data)
                    }
                    REQUEST_CODE_ADD_BOD -> {
                        onSuccessAddBOD(data)
                    }
                    REQUEST_CODE_EDIT_BOD -> {
                        onSuccessChangeBOD(data)
                    }
                    REQUEST_CODE_EDIT_PHONE -> {
                        onSuccessEditPhone(data)
                    }
                    REQUEST_CODE_ADD_EMAIL -> {
                        onSuccessAddEmail(data)
                    }
                    REQUEST_CODE_ADD_PHONE -> {
                        onSuccessAddPhone(data)
                    }
                    REQUEST_CODE_ADD_GENDER -> {
                        onSuccessAddGender(data)
                    }
                }
            }
        }
    }

    private fun onSuccessChangeName(data: Intent?) {
        refreshProfile()
        view?.run {
            Toaster.make(this, getString(R.string.change_name_change_success), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessAddBOD(data: Intent?) {
        refreshProfile()
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_add_bod), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessChangeBOD(data: Intent?) {
        refreshProfile()
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_change_bod), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessEditPhone(data: Intent?) {
        refreshProfile()
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_change_phone_number), Snackbar.LENGTH_LONG)
        }
    }

    private fun refreshProfile() {
        showLoading(true)
        profileInfoViewModel.getUserProfileInfo(context!!)
    }

    private fun onSuccessAddGender(data: Intent?) {
        data?.extras?.run {
            val genderResult = getInt(ChangeGenderFragment.EXTRA_SELECTED_GENDER, 1)

            view?.run {
                Toaster.showNormal(this, getString(R.string.success_add_gender), Snackbar.LENGTH_LONG)
            }
            gender.showFilled(
                    getString(R.string.subtitle_gender_setting_profile),
                    if (genderResult == 1)
                        getString(R.string.profile_completion_man)
                    else getString(R.string.profile_completion_woman),
                    false,
                    false
            )
        }
    }

    private fun onSuccessAddPhone(data: Intent?) {
        data?.extras?.run {
            val phoneString = getString(AddPhoneFragment.EXTRA_PHONE, "")
            if (phoneString.isNotBlank()) {
                view?.run {
                    Toaster.showNormal(this, getString(R.string.success_add_phone), Snackbar.LENGTH_LONG)
                }
            }
            refreshProfile()
        }
    }

    private fun onSuccessAddEmail(data: Intent?) {
        data?.extras?.run {
            val emailString = getString(AddEmailFragment.EXTRA_EMAIL, "")
            if (emailString.isNotBlank()) {
                view?.run {
                    Toaster.showNormal(this, getString(R.string.success_add_email), Snackbar.LENGTH_LONG)
                }
            }
            refreshProfile()
        }
    }

    private fun onSuccessGetProfilePhoto(data: Intent?) {
        if (data != null) {
            val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                val savedLocalImageUrl = imageUrlOrPathList[0]
                val file = File(savedLocalImageUrl)

                if (!file.exists()) {
                    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
                } else {
                    showLoading(true)
                    profileInfoViewModel.uploadProfilePicture(context!!, savedLocalImageUrl)
                }

            } else {
                onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
            }
        } else {
            onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
        }
    }

    private fun onErrorGetProfilePhoto(errorException: Exception) {
        view?.run {
            Toaster.showError(
                    this,
                    ErrorHandlerSession.getErrorMessage(errorException, context, false),
                    Snackbar.LENGTH_LONG)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun onSuccessGetUserProfileInfo(profileCompletionData: ProfileCompletionData) {
        userSession.phoneNumber = profileCompletionData.phone
        userSession.email = profileCompletionData.email

        ImageHandler.loadImageCircle2(context, profilePhoto, profileCompletionData.profilePicture)

        name?.showFilled(
                getString(R.string.subtitle_name_setting_profile),
                profileCompletionData.fullName,
                showVerified = false,
                showButton = true,
                fieldClickListener = View.OnClickListener {
                    ChangeNameTracker().clickOnChangeName()
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_NAME, profileCompletionData.fullName, chancesChangeName)
                    startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME)
                }
        )

        if (profileCompletionData.birthDay.isEmpty()) {
            bod.showEmpty(
                    getString(R.string.subtitle_bod_setting_profile),
                    getString(R.string.hint_bod_setting_profile),
                    true,
                    View.OnClickListener {
                        goToAddBod()
                    }
            )
        } else {
            bod.showFilled(
                    getString(R.string.subtitle_bod_setting_profile),
                    DateFormatUtils.formatDate(
                            DateFormatUtils.FORMAT_YYYY_MM_DD,
                            DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                            profileCompletionData.birthDay),
                    false,
                    true,
                    View.OnClickListener {
                        goToChangeBod(profileCompletionData.birthDay)
                    }
            )
        }

        if (profileCompletionData.gender != 1 && profileCompletionData.gender != 2) {
            gender.showEmpty(
                    getString(R.string.subtitle_gender_setting_profile),
                    getString(R.string.hint_gender_setting_profile),
                    true,
                    View.OnClickListener {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_GENDER)
                        startActivityForResult(intent, REQUEST_CODE_ADD_GENDER)
                    }
            )
        } else {
            gender.showFilled(
                    getString(R.string.subtitle_gender_setting_profile),
                    if (profileCompletionData.gender == 1)
                        getString(R.string.profile_completion_man)
                    else getString(R.string.profile_completion_woman),
                    false,
                    false
            )
        }
        val isEmailDone = profileCompletionData.isEmailDone
        if (profileCompletionData.email.isEmpty() || !isEmailDone) {
            email.showEmpty(
                    getString(R.string.subtitle_email_setting_profile),
                    getString(R.string.hint_email_setting_profile),
                    getString(R.string.message_email_setting_profile),
                    false,
                    View.OnClickListener {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_EMAIL)
                        startActivityForResult(intent, REQUEST_CODE_ADD_EMAIL)
                    }
            )
        } else {
            email.showFilled(
                    getString(R.string.subtitle_email_setting_profile),
                    profileCompletionData.email,
                    true,
                    true,
                    View.OnClickListener {
                        if(profileCompletionData.phone.isNotEmpty() && profileCompletionData.isPhoneVerified){
                            goToChangeEmail(profileCompletionData.email)
                        } else if(profileCompletionData.phone.isNotEmpty() && !profileCompletionData.isPhoneVerified) {
                            showVerifyEmailDialog()
                        }else{
                            showChangeEmailDialog()
                        }
                    }
            )
        }

        if (profileCompletionData.phone.isEmpty()) {
            phone.showEmpty(
                    getString(R.string.subtitle_phone_setting_profile),
                    getString(R.string.hint_phone_setting_profile),
                    getString(R.string.message_phone_setting_profile),
                    true,
                    View.OnClickListener {
                        goToAddPhone()
                    }
            )
            tickerPhoneVerification.visibility = View.GONE
        } else {
            phone.showFilled(
                    getString(R.string.subtitle_phone_setting_profile),
                    PhoneNumberUtils.transform(profileCompletionData.phone),
                    profileCompletionData.isPhoneVerified,
                    true,
                    View.OnClickListener {
                        if (profileCompletionData.isPhoneVerified) {
                            goToChangePhone(profileCompletionData.phone, profileCompletionData.email)
                        } else {
                            goToVerifyPhone()
                        }
                    }
            )

            if (profileCompletionData.isPhoneVerified) {
                tickerPhoneVerification.visibility = View.GONE
            } else {
                tickerPhoneVerification.visibility = View.VISIBLE
                tickerPhoneVerification.setHtmlDescription(
                        getString(R.string.ticker_phone_verification)
                )
                tickerPhoneVerification.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        goToVerifyPhone()
                    }

                    override fun onDismiss() {
                    }

                })

            }
        }

        profileRoleViewModel.getUserProfileRole()
    }

    private fun onErrorGetProfileInfo(throwable: Throwable) {
        dismissLoading()
        view?.run {
            val error = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            NetworkErrorHelper.showEmptyState(context, this, error) {
                initSettingProfileData()
            }
        }
    }

    private fun onSuccessGetProfileRole(profileRoleData: ProfileRoleData) {
        dismissLoading()
        bod.isEnabled = profileRoleData.isAllowedChangeDob
        name?.isEnabled = profileRoleData.isAllowedChangeName && remoteConfig.getBoolean(REMOTE_KEY_CHANGE_NAME, false)
        chancesChangeName = profileRoleData.chancesChangeName
    }

    private fun onErrorGetProfileRole(throwable: Throwable) {
        dismissLoading()
        view?.run {
            val error = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            NetworkErrorHelper.showEmptyState(context, this, error) {
                initSettingProfileData()
            }
        }
    }

    private fun goToAddPhone() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
    }

    private fun goToVerifyPhone() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.SETTING_PROFILE_PHONE_VERIFICATION)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PHONE)
    }

    private fun goToChangePhone(phone: String, email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PHONE)
    }

    private fun goToAddBod() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_BOD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(R.string.title_add_bod))
        startActivityForResult(intent, REQUEST_CODE_ADD_BOD)
    }

    private fun goToChangeBod(bod: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_BOD)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(R.string.title_change_bod))
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD, bod)
        startActivityForResult(intent, REQUEST_CODE_EDIT_BOD)
    }

    private fun goToChangeEmail(email: String){
        val encodedUrlB = URLEncoder.encode(
                Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
                    appendPath(UrlSettingProfileConst.USER_PATH_URL)
                    appendPath(UrlSettingProfileConst.PROFILE_PATH_URL)
                    appendPath(UrlSettingProfileConst.EDIT_PATH_URL)
                    appendQueryParameter(UrlSettingProfileConst.PARAM_IS_BACK, true.toString())
                }.build().toString(),
                "UTF-8"
        )

        val encodedUrlLd = URLEncoder.encode(
                Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
                    appendPath(UrlSettingProfileConst.USER_PATH_URL)
                    appendPath(UrlSettingProfileConst.PROFILE_PATH_URL)
                    appendPath(UrlSettingProfileConst.EMAIL_PATH_URL)
                    appendQueryParameter(UrlSettingProfileConst.PARAM_V_OEMAIL, email)
                    appendQueryParameter(UrlSettingProfileConst.PARAM_TYPE, "change")
                }.build().toString(),
                "UTF-8"
        )

        val encodedEmail = URLEncoder.encode(email, "UTF-8")

        val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
            appendPath(UrlSettingProfileConst.OTP_PATH_URL)
            appendPath(UrlSettingProfileConst.CHECK_PATH_URL)
            appendPath(UrlSettingProfileConst.PAGE_PATH_URL)
            appendQueryParameter(UrlSettingProfileConst.PARAM_B, encodedUrlB)
            appendQueryParameter(UrlSettingProfileConst.PARAM_EMAIL, encodedEmail)
            appendQueryParameter(UrlSettingProfileConst.PARAM_LD, encodedUrlLd)
            appendQueryParameter(UrlSettingProfileConst.PARAM_OTP_TYPE, 14.toString())
        }.build().toString()

        RouteManager.route(
                context,
                ApplinkConstInternalGlobal.WEBVIEW.replace("{url}", URLEncoder.encode(url, "UTF-8"))
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        profileInfoViewModel.userProfileInfo.removeObservers(this)
        profileInfoViewModel.clear()
    }

    inner class EditUserProfilePhotoListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val MAX_SIZE = 2048
            val builder = ImagePickerBuilder.getDefaultBuilder(context)
            builder.maxFileSizeInKB = 2048
            builder.imagePickerMultipleSelectionBuilder = null
            val intent = ImagePickerActivity.getIntent(context, builder)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE_PHOTO)
        }
    }

    private fun showLoading(isOverlay: Boolean = false) {
        if (isOverlay) {
            overlayView.visibility = View.VISIBLE
        } else {
            mainView.visibility = View.GONE
        }

        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        overlayView.visibility = View.GONE
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200
        const val REQUEST_CODE_EDIT_EMAIL = 202 //No Implementation yet
        const val REQUEST_CODE_EDIT_PHONE = 203
        const val REQUEST_CODE_EDIT_BOD = 204

        const val REQUEST_CODE_CHANGE_NAME = 300
        const val REQUEST_CODE_ADD_BOD = 301
        const val REQUEST_CODE_ADD_EMAIL = 302
        const val REQUEST_CODE_ADD_PHONE = 303
        const val REQUEST_CODE_ADD_GENDER = 304

        const val REMOTE_KEY_CHANGE_NAME = "android_customer_change_public_name"

        const val HEADER_PICT_URL = "https://ecs7.tokopedia.net/img/android/others/bg_setting_profile_header.png"

        fun createInstance(bundle: Bundle): SettingProfileFragment {
            val fragment = SettingProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}