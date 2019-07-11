package com.tokopedia.profilecompletion.settingprofile.view.fragment

//import com.tokopedia.unifycomponents.Toaster
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.PhoneNumberUtils
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.customview.UnifyDialog
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.view.widget.CustomFieldSettingProfile
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_setting_profile.*
import java.io.File
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class SettingProfileFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val profileInfoViewModel by lazy { viewModelProvider.get(ProfileInfoViewModel::class.java) }

    companion object {
        const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200
        const val REQUEST_CODE_EDIT_BOD = 201
        const val REQUEST_CODE_EDIT_EMAIL = 202
        const val REQUEST_CODE_EDIT_PHONE = 203

        fun createInstance(bundle: Bundle): SettingProfileFragment {
            val fragment = SettingProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            when(it){
                is Success -> onSuccessUploadProfilePicture(it.data)
                is Fail -> onErrorUploadProfilePicture(it.throwable)
            }
        })
    }

    private fun onErrorUploadProfilePicture(throwable: Throwable) {
        dismissLoadingUploadProfilePicture()
        Toast.makeText(context, "throw : " + throwable.message, Toast.LENGTH_LONG).show()
        //TODO
    }

    private fun onSuccessUploadProfilePicture(result: UploadProfileImageModel) {
        dismissLoadingUploadProfilePicture()
        if(result.data?.filePath != null && result.data.filePath.isNotBlank()) {
            ImageHandler.loadImageCircle2(context, profilePhoto, result.data.filePath)
        }else{
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
                    REQUEST_CODE_EDIT_EMAIL -> {
                    } //TODO add action on result
                    REQUEST_CODE_EDIT_PHONE -> {
                    } //TODO add action on result
                    else -> {
                    } //TODO add action on result
                }
            }
            Activity.RESULT_CANCELED -> {
                when (requestCode) {
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
                    } //TODO add action on result
                    REQUEST_CODE_EDIT_BOD -> {
                    } //TODO add action on result
                    REQUEST_CODE_EDIT_EMAIL -> {
                    } //TODO add action on result
                    REQUEST_CODE_EDIT_PHONE -> {
                    } //TODO add action on result
                    else -> {
                    } //TODO add action on result
                }
            }
            else -> {
            } //TODO add action on result
        }
    }

    private fun onSuccessGetProfilePhoto(data: Intent?) {
        if (data != null) {
            val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                val savedLocalImageUrl = imageUrlOrPathList[0]
                val file = File(savedLocalImageUrl)
                if(file.exists()) {
                    showLoadingUploadProfilePicture()
                    profileInfoViewModel.uploadProfilePicture(savedLocalImageUrl)
                }else{
                    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
                }
            }else{
                onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
            }
        }else{
            onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
        }
    }

    private fun showLoadingUploadProfilePicture() {
        //TODO SHOW LOADING
    }

    private fun dismissLoadingUploadProfilePicture() {
        //TODO
    }


    private fun onErrorGetProfilePhoto(errorException: Exception) {
        //TODO show message error
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        getComponent(ProfileCompletionComponent::class.java).inject(this)
    }

    private fun onSuccessGetUserProfileInfo(profileCompletionData: ProfileCompletionData) {
        dismissLoading()

        ImageHandler.loadImageCircle2(context, profilePhoto, profileCompletionData.profilePicture)

        name.showFilled(
            getString(R.string.subtitle_name_setting_profile),
            profileCompletionData.fullName,
            false,
            false
        )

        if(profileCompletionData.birthDay.isEmpty()) {
            bod.showEmpty(
                getString(R.string.subtitle_bod_setting_profile),
                getString(R.string.hint_bod_setting_profile),
                false,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }else{
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

        if(profileCompletionData.gender == 0){
            gender.showEmpty(
                getString(R.string.subtitle_gender_setting_profile),
                getString(R.string.hint_gender_setting_profile),
                true,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }else{
            gender.showFilled(
                getString(R.string.subtitle_gender_setting_profile),
                if(profileCompletionData.gender == 1)
                    getString(R.string.profile_completion_man)
                else getString(R.string.profile_completion_woman),
                false,
                false,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }

        if(profileCompletionData.email.isEmpty()){
            email.showEmpty(
                getString(R.string.subtitle_email_setting_profile),
                getString(R.string.hint_email_setting_profile),
                getString(R.string.message_email_setting_profile),
                false,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }else{
            email.showFilled(
                getString(R.string.subtitle_email_setting_profile),
                profileCompletionData.email,
                true,
                true,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }

        if(profileCompletionData.phone.isEmpty()){
            phone.showEmpty(
                getString(R.string.subtitle_phone_setting_profile),
                getString(R.string.hint_phone_setting_profile),
                getString(R.string.message_phone_setting_profile),
                false,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }else{
            phone.showFilled(
                getString(R.string.subtitle_phone_setting_profile),
                PhoneNumberUtils.transform(profileCompletionData.phone),
                profileCompletionData.isPhoneVerified,
                true,
                View.OnClickListener {
                    //TODO add action on listener
                }
            )
        }
    }

    private fun onErrorGetProfileInfo(throwable: Throwable) {
        dismissLoading()
        view?.run {
            //            Toaster.showError(
//                    this,
//                    ErrorHandlerSession.getErrorMessage(throwable, context, true),
//                    Snackbar.LENGTH_LONG)
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

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}