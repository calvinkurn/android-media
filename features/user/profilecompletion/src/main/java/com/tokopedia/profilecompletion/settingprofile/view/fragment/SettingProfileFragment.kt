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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.PhoneNumberUtils
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_setting_profile.*
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class SettingProfileFragment: BaseDaggerFragment(){

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
        btnEditBod.setOnClickListener {  } //TODO add action on listener
        btnEditEmail.setOnClickListener {  } //TODO add action on listener
        btnEditPhone.setOnClickListener {  } //TODO add action on listener

        initSettingProfileData()
    }

    private fun initObserver(){
        profileInfoViewModel.userProfileInfo.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetUserProfileInfo(it.data)
                is Fail -> onErrorGetProfileInfo(it.throwable)
            }
        })
    }

    private fun initSettingProfileData(){
        showLoading()
        profileInfoViewModel.getUserProfileInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode){
            Activity.RESULT_OK -> {
                when(requestCode){
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_BOD -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_EMAIL -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_PHONE -> { } //TODO add action on result
                    else -> { } //TODO add action on result
                }
            }
            Activity.RESULT_CANCELED -> {
                when(requestCode){
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_BOD -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_EMAIL -> { } //TODO add action on result
                    REQUEST_CODE_EDIT_PHONE -> { } //TODO add action on result
                    else -> { } //TODO add action on result
                }
            }
            else -> { } //TODO add action on result
        }
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        getComponent(ProfileCompletionComponent::class.java).inject(this)
    }

    private fun onSuccessGetUserProfileInfo(profileCompletionData: ProfileCompletionData){
        dismissLoading()

        ImageHandler.loadImageCircle2(context, profilePhoto, profileCompletionData.profilePicture)
        name.text = profileCompletionData.fullName
        bod.text = DateFormatUtils.formatDate(
            DateFormatUtils.FORMAT_YYYY_MM_DD,
            DateFormatUtils.FORMAT_DD_MMMM_YYYY,
            profileCompletionData.birthDay)
        gender.text = if(profileCompletionData.gender == 1) getString(R.string.profile_completion_man)
            else getString(R.string.profile_completion_woman)
        email.text = profileCompletionData.email
        phone.text = PhoneNumberUtils.transform(profileCompletionData.phone)
        phoneVerified.visibility = if(profileCompletionData.isPhoneVerified) View.VISIBLE else View.GONE
    }

    private fun onErrorGetProfileInfo(throwable: Throwable){
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

    inner class EditUserProfilePhotoListener: View.OnClickListener{
        override fun onClick(v: View?) {
            //TODO add action on listener
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