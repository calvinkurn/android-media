package com.tokopedia.profilecompletion.settingprofile.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.changename.data.analytics.ChangeNameTracker
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.phonenumber.PhoneNumberUtil
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
    private var tickerAddNameWarning: Ticker? = null

    private var chancesChangeName = "0"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_profile, container, false)
        overlayView = view.findViewById(R.id.overlay_view)
        tickerPhoneVerification = view.findViewById(R.id.ticker_phone_verification)
        tickerAddNameWarning = view.findViewById(R.id.ticker_default_name_warning)
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

    override fun onResume() {
        super.onResume()
        refreshProfile()
    }

    private fun showChangeEmailDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.add_and_verify_phone))
                setDescription(getString(R.string.add_and_verify_phone_detail))
                setPrimaryCTAText(getString(R.string.title_add_phone))
                setPrimaryCTAClickListener {
                    goToAddPhone()
                    this.dismiss()
                }
                setSecondaryCTAText(getString(R.string.label_cancel))
                setSecondaryCTAClickListener {
                    this.dismiss()
                }
            }.show()
        }
    }

    private fun showVerifyEmailDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.add_and_verify_phone))
                setDescription(getString(R.string.add_and_verify_phone_detail))
                setPrimaryCTAText(getString(R.string.title_verify_phone))
                setPrimaryCTAClickListener {
                    goToAddPhone()
                    this.dismiss()
                }
                setSecondaryCTAText(getString(R.string.label_cancel))
                setSecondaryCTAClickListener {
                    this.dismiss()
                }
            }.show()
        }
    }

    private fun initObserver() {
        profileInfoViewModel.userProfileInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetUserProfileInfo(it.data)
                is Fail -> onErrorGetProfileInfo(it.throwable)
            }
        })

        profileInfoViewModel.uploadProfilePictureResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessUploadProfilePicture(it.data)
                is Fail -> onErrorUploadProfilePicture(it.throwable)
            }
        })

        profileRoleViewModel.userProfileRole.observe(viewLifecycleOwner, Observer {
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
        profileInfoViewModel.getUserProfileInfo(requireContext())
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
            else -> {
                when (requestCode) {
                    REQUEST_CODE_ADD_PHONE -> {
                        AddPhoneNumberTracker().viewPersonalDataPage(false)
                    }
                }
            }
        }
    }

    private fun onSuccessChangeName(data: Intent?) {
        view?.run {
            Toaster.make(this, getString(R.string.change_name_change_success), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessAddBOD(data: Intent?) {
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_add_bod), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessChangeBOD(data: Intent?) {
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_change_bod), Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessEditPhone(data: Intent?) {
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_change_phone_number), Snackbar.LENGTH_LONG)
        }
    }

    private fun refreshProfile() {
        showLoading(true)
        profileInfoViewModel.getUserProfileInfo(requireContext())
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
            AddPhoneNumberTracker().viewPersonalDataPage(true)
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
        }
    }

    private fun onSuccessGetProfilePhoto(data: Intent?) {
        if (data != null) {
            val imageUrlOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
            if (imageUrlOrPathList.size > 0) {
                val savedLocalImageUrl = imageUrlOrPathList[0]
                val file = File(savedLocalImageUrl)

                if (!file.exists()) {
                    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
                } else {
                    showLoading(true)
                    profileInfoViewModel.uploadProfilePicture(requireContext(), savedLocalImageUrl)
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
        userSession.phoneNumber = profileCompletionData.msisdn
        userSession.email = profileCompletionData.email

        ImageHandler.loadImageCircle2(context, profilePhoto, profileCompletionData.profilePicture)

        renderWarningTickerName(profileCompletionData)
        renderNameField(profileCompletionData)

        if (profileCompletionData.birthDay.isEmpty()) {
            bod?.showEmpty(
                    getString(R.string.subtitle_bod_setting_profile),
                    getString(R.string.hint_bod_setting_profile),
                    true,
                    View.OnClickListener {
                        goToAddBod()
                    }
            )
        } else {
            bod?.showFilled(
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
            gender?.showEmpty(
                    getString(R.string.subtitle_gender_setting_profile),
                    getString(R.string.hint_gender_setting_profile),
                    true,
                    View.OnClickListener {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_GENDER)
                        startActivityForResult(intent, REQUEST_CODE_ADD_GENDER)
                    }
            )
        } else {
            gender?.showFilled(
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
            email?.showEmpty(
                    getString(R.string.subtitle_email_setting_profile),
                    getString(R.string.hint_email_setting_profile),
                    getString(R.string.message_email_setting_profile),
                    false,
                    View.OnClickListener {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_EMAIL)
                        startActivityForResult(intent, REQUEST_CODE_ADD_EMAIL)
                    })
        } else {
            email?.showFilled(
                    getString(R.string.subtitle_email_setting_profile),
                    profileCompletionData.email,
                    true,
                    true,
                    View.OnClickListener {
                        if (profileCompletionData.msisdn.isNotEmpty() && profileCompletionData.isMsisdnVerified) {
                            goToChangeEmail()
                        } else if (profileCompletionData.msisdn.isNotEmpty() && !profileCompletionData.isMsisdnVerified) {
                            showVerifyEmailDialog()
                        } else {
                            showChangeEmailDialog()
                        }
                    }
            )
        }

        if (profileCompletionData.msisdn.isEmpty()) {
            phone?.showEmpty(
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
            phone?.showFilled(
                    getString(R.string.subtitle_phone_setting_profile),
                    PhoneNumberUtil.transform(profileCompletionData.msisdn),
                    profileCompletionData.isMsisdnVerified,
                    true,
                    View.OnClickListener {
                        if (profileCompletionData.isMsisdnVerified) {
                            goToChangePhone(profileCompletionData.msisdn, profileCompletionData.email)
                        } else {
                            goToAddPhoneBy(PhoneNumberUtil.replace62with0(profileCompletionData.msisdn))
                        }
                    }
            )

            if (profileCompletionData.isMsisdnVerified) {
                tickerPhoneVerification.visibility = View.GONE
            } else {
                tickerPhoneVerification.visibility = View.VISIBLE
                tickerPhoneVerification.setHtmlDescription(
                        getString(R.string.ticker_phone_verification)
                )
                tickerPhoneVerification.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        goToAddPhoneBy(PhoneNumberUtil.replace62with0(profileCompletionData.msisdn))
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
        bod?.isEnabled = profileRoleData.isAllowedChangeDob
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

    private fun goToAddPhoneBy(phone: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE_WITH, phone)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
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

    private fun goToChangeEmail() {
        val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
            appendPath(UrlSettingProfileConst.USER_PATH_URL)
            appendPath(UrlSettingProfileConst.PROFILE_PATH_URL)
            appendPath(UrlSettingProfileConst.EMAIL_PATH_URL)
        }.build().toString()

        RouteManager.route(
                context,
                ApplinkConstInternalGlobal.WEBVIEW.replace("{url}", URLEncoder.encode(url, "UTF-8"))
        )
    }

    private fun renderNameField(profileCompletionData: ProfileCompletionData) {
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
    }

    private fun renderWarningTickerName(profileCompletionData: ProfileCompletionData) {
        if (profileCompletionData.fullName.contains(DEFAULT_NAME)) {
            tickerAddNameWarning?.show()
        } else {
            tickerAddNameWarning?.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        profileInfoViewModel.userProfileInfo.removeObservers(this)
        profileInfoViewModel.flush()
    }

    inner class EditUserProfilePhotoListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val ctx = context ?: return
            val builder = ImagePickerBuilder.getSquareImageBuilder(ctx).apply {
                maxFileSizeInKB = 2048
            }
            val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE_PHOTO)
        }
    }

    private fun showLoading(isOverlay: Boolean = false) {
        if (isOverlay) {
            overlayView.visibility = View.VISIBLE
        } else {
            mainView?.visibility = View.GONE
        }

        progressBar?.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        overlayView.visibility = View.GONE
        mainView?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
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

        private const val DEFAULT_NAME = "Toppers-"

        fun createInstance(bundle: Bundle): SettingProfileFragment {
            val fragment = SettingProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}