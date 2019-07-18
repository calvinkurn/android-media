package com.tokopedia.profilecompletion.settingprofile.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.PhoneNumberUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.customview.UnifyDialog
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.data.UploadProfilePictureResult
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_setting_profile.*
import java.io.File
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class SettingProfileFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val profileInfoViewModel by lazy { viewModelProvider.get(ProfileInfoViewModel::class.java) }

    lateinit var overlayView: View
    lateinit var tickerPhoneVerification: Ticker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_profile, container, false)
        overlayView = view.findViewById(R.id.overlay_view)
        tickerPhoneVerification = view.findViewById(R.id.ticker_phone_verification)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overlayView.setOnClickListener { }
        initObserver()

        profilePhoto.setOnClickListener(EditUserProfilePhotoListener())
        btnEditProfilePhoto.setOnClickListener(EditUserProfilePhotoListener())

        initSettingProfileData()
    }

    private fun showChangeEmailDialog() {
        val dialog = UnifyDialog(activity as Activity, UnifyDialog.SINGLE_ACTION, UnifyDialog.NO_HEADER)
        dialog.setTitle(getString(R.string.title_change_email_dialog))
        dialog.setDescription(getString(R.string.cannot_change_email))
        dialog.setOk(getString(R.string.title_ok))
        dialog.setOkOnClickListner(View.OnClickListener { dialog.dismiss() })
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
        profileInfoViewModel.getUserProfileInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
                        onSuccessGetProfilePhoto(data)
                    }
                    REQUEST_CODE_EDIT_BOD -> {
                    } //TODO add action on result
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

    private fun onSuccessEditPhone(data: Intent?) {
        refreshProfile()
        view?.run {
            Toaster.showNormal(this, getString(R.string.success_change_phone_number), Snackbar.LENGTH_LONG)
        }
    }

    private fun refreshProfile() {
        showLoading(true)
        profileInfoViewModel.getUserProfileInfo()
    }

    private fun onSuccessAddGender(data: Intent?) {
        data?.extras?.run {
            val genderResult = getInt(ChangeGenderFragment.EXTRA_SELECTED_GENDER, 1)

            //TODO ADE GENDER IS STILL SHOWING EMPTY

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
                phone.showFilled(
                        getString(R.string.subtitle_phone_setting_profile),
                        PhoneNumberUtils.transform(phoneString),
                        true,
                        true,
                        View.OnClickListener {
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
                            startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
                        }
                )
            }
        }
    }

    private fun onSuccessAddEmail(data: Intent?) {
        data?.extras?.run {
            val emailString = getString(AddEmailFragment.EXTRA_EMAIL, "")
            if (emailString.isNotBlank()) {
                view?.run {
                    Toaster.showNormal(this, getString(R.string.success_add_email), Snackbar.LENGTH_LONG)
                }
                email.showFilled(
                        getString(R.string.subtitle_email_setting_profile),
                        emailString,
                        true,
                        true,
                        View.OnClickListener {
                            showChangeEmailDialog()
                        }
                )
            }
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
                } else if (!imageIsValid(file)) {
                    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.error_oversize_avatar_image)))
                } else {
                    showLoading(true)
                    profileInfoViewModel.uploadProfilePicture(savedLocalImageUrl)
                }

            } else {
                onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
            }
        } else {
            onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
        }
    }

    private fun imageIsValid(file: File): Boolean {
        val MAX_FILE_SIZE: Long = 2048
        val DEFAULT_ONE_MEGABYTE: Long = 1024

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = Integer.parseInt((file.length() / DEFAULT_ONE_MEGABYTE).toString())

        return if (fileSize >= MAX_FILE_SIZE) {
            false
        } else {
            true
        }
    }

    private fun onErrorGetProfilePhoto(errorException: Exception) {
        //TODO show message error
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun onSuccessGetUserProfileInfo(profileCompletionData: ProfileCompletionData) {
        userSession.phoneNumber = profileCompletionData.phone
        userSession.email = profileCompletionData.email

        dismissLoading()

        ImageHandler.loadImageCircle2(context, profilePhoto, profileCompletionData.profilePicture)

        name.showFilled(
                getString(R.string.subtitle_name_setting_profile),
                profileCompletionData.fullName,
                false,
                false
        )

        if (profileCompletionData.birthDay.isEmpty()) {
            bod.showEmpty(
                    getString(R.string.subtitle_bod_setting_profile),
                    getString(R.string.hint_bod_setting_profile),
                    false,
                    View.OnClickListener {
                        //TODO add action on listener
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
                        //TODO add action on listener
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

        if (profileCompletionData.email.isEmpty()) {
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
                        showChangeEmailDialog()
                    }
            )
        }

        if (profileCompletionData.phone.isEmpty()) {
            phone.showEmpty(
                    getString(R.string.subtitle_phone_setting_profile),
                    getString(R.string.hint_phone_setting_profile),
                    getString(R.string.message_phone_setting_profile),
                    false,
                    View.OnClickListener {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
                        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
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
                    override fun onDescriptionViewClick(link: CharSequence?) {
                        goToVerifyPhone()
                    }

                    override fun onDismiss() {
                    }

                })

            }
        }
    }

    private fun goToVerifyPhone() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.SETTING_PROFILE_PHONE_VERIFICATION)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PHONE)
    }

    private fun goToChangePhone(phone: String, email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PHONE)
    }

    private fun onErrorGetProfileInfo(throwable: Throwable) {
        dismissLoading()
        view?.run {
            Toaster.showError(
                    this,
                    ErrorHandlerSession.getErrorMessage(throwable, context, true),
                    Snackbar.LENGTH_LONG)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        profileInfoViewModel.userProfileInfo.removeObservers(this)
        profileInfoViewModel.clear()
    }

    inner class EditUserProfilePhotoListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val builder = ImagePickerBuilder.getDefaultBuilder(context)
            val multipleSelectionBuilder = ImagePickerMultipleSelectionBuilder.getDefaultBuilder()
            multipleSelectionBuilder.maximumNoPick = 1
            builder.imagePickerMultipleSelectionBuilder = multipleSelectionBuilder
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
        const val REQUEST_CODE_EDIT_BOD = 201
        const val REQUEST_CODE_EDIT_EMAIL = 202 //No Implementation yet
        const val REQUEST_CODE_EDIT_PHONE = 203

        const val REQUEST_CODE_ADD_BOD = 301
        const val REQUEST_CODE_ADD_EMAIL = 302
        const val REQUEST_CODE_ADD_PHONE = 303
        const val REQUEST_CODE_ADD_GENDER = 304


        fun createInstance(bundle: Bundle): SettingProfileFragment {
            val fragment = SettingProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}