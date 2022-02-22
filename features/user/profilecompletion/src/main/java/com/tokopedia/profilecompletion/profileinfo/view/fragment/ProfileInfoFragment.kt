package com.tokopedia.profilecompletion.profileinfo.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity
import com.tokopedia.profilecompletion.databinding.FragmentProfileInfoBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoConstants
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoData
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoAdapter
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoTitleViewHolder
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.profilecompletion.settingprofile.domain.UrlSettingProfileConst
import com.tokopedia.profilecompletion.settingprofile.view.fragment.SettingProfileFragment
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.phonenumber.PhoneNumberUtil
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

class ProfileInfoFragment: BaseDaggerFragment(), ProfileInfoItemViewHolder.ProfileInfoItemInterface, ProfileInfoTitleViewHolder.ProfileInfoTitleInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val binding: FragmentProfileInfoBinding? by viewBinding()

    val adapter by lazy {
	ProfileInfoAdapter(ProfileInfoListTypeFactory(this, this))
    }

    private val editPhotoListener = object: View.OnClickListener {
	override fun onClick(v: View?) {
	    val ctx = v?.context ?: return
	    val builder = ImagePickerBuilder.getSquareImageBuilder(ctx).apply { maxFileSizeInKB = MAX_FILE_SIZE }
	    val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
	    intent.putImagePickerBuilder(builder)
	    startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE_PHOTO)
	}
    }

    private val viewModel by lazy {
	ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun initInjector() {
	getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = "New Profile Info"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	super.onViewCreated(view, savedInstanceState)
	initViews()
	setupObserver()
	viewModel.getProfileInfo()
    }

    private fun initViews() {
	val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
	binding?.fragmentProfileInfoRv?.layoutManager = layoutManager
	binding?.fragmentProfileInfoRv?.adapter = adapter

        binding?.profileInfoImageSubtitle?.setOnClickListener(editPhotoListener)
	binding?.profileInfoImageUnify?.setImageUrl(userSession.profilePicture)
    }

    private fun setupObserver() {
	viewModel.profileInfoUiData.observe(viewLifecycleOwner) { setProfileData(it) }

	viewModel.errorMessage.observe(viewLifecycleOwner) {
	    showToasterError(it)
	}

	viewModel.saveImageProfileResponse.observe(viewLifecycleOwner) {
	    binding?.profileInfoImageUnify?.setImageUrl(it)
	    showNormalToaster(getString(R.string.success_change_profile_picture))
	}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	when (resultCode) {
	    Activity.RESULT_OK -> {
		when (requestCode) {
		    SettingProfileFragment.REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
			onSuccessGetProfilePhoto(data)
		    }
		    SettingProfileFragment.REQUEST_CODE_CHANGE_NAME -> {
			showNormalToaster(getString(R.string.change_name_change_success))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_BOD -> {
			showNormalToaster(getString(R.string.success_add_bod))
		    }
		    SettingProfileFragment.REQUEST_CODE_EDIT_BOD -> {
			showNormalToaster(getString(R.string.success_change_bod))
		    }
		    SettingProfileFragment.REQUEST_CODE_EDIT_PHONE -> {
			showNormalToaster(getString(R.string.success_change_phone_number))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_EMAIL -> {
			showNormalToaster(getString(R.string.success_add_email))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_PHONE -> {
			showNormalToaster(getString(R.string.success_add_phone))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_GENDER -> {
			showNormalToaster(getString(R.string.success_add_gender))
		    }
		    SettingProfileFragment.REQUEST_CODE_CHANGE_EMAIL -> {
			showNormalToaster(getString(R.string.change_email_change_success))
		    }
		}
		viewModel.getProfileInfo()
	    }
	    else -> {
		when (requestCode) {
		    SettingProfileFragment.REQUEST_CODE_ADD_PHONE -> {
			AddPhoneNumberTracker().viewPersonalDataPage(false)
		    }
		}
	    }
	}
    }

    private fun setProfileData(data: ProfileInfoUiModel) {
        val listItem = listOf(
	    ProfileInfoTitleUiModel(ProfileInfoConstants.PROFILE_INFO_SECTION, "Info Profil"),
	    ProfileInfoItemUiModel(ProfileInfoConstants.NAME, title = "Nama", itemValue = data.profileInfoData.fullName),
	    ProfileInfoItemUiModel(ProfileInfoConstants.USERNAME, title = "Username", itemValue = "username"),
	    ProfileInfoItemUiModel(ProfileInfoConstants.BIO, title = "Bio", itemValue = "bio"),
	    DividerProfileUiModel("line"),
	    ProfileInfoTitleUiModel(ProfileInfoConstants.PROFILE_PERSONAL_INFO_SECTION, "Info Pribadi"),
	    ProfileInfoItemUiModel(ProfileInfoConstants.USER_ID, title = "User ID", itemValue = userSession.userId, rightIcon = IconUnify.COPY),
	    ProfileInfoItemUiModel(ProfileInfoConstants.EMAIL, title = "E-mail", itemValue = data.profileInfoData.email) {
		if (data.profileInfoData.email.isEmpty() || !data.profileInfoData.isEmailDone) {
		    val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_EMAIL)
		    startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_EMAIL)
		} else {
		    if (data.profileInfoData.msisdn.isNotEmpty() && data.profileInfoData.isMsisdnVerified) {
			goToChangeEmail()
		    } else if (data.profileInfoData.msisdn.isNotEmpty() && !data.profileInfoData.isMsisdnVerified) {
			showVerifyEmailDialog()
		    } else {
			showChangeEmailDialog()
		    }
		}
	    } ,
	    ProfileInfoItemUiModel(ProfileInfoConstants.PHONE, title = "Nomor HP", itemValue = data.profileInfoData.msisdn) {
		if (data.profileInfoData.msisdn.isEmpty()) {
		    goToAddPhone()
		} else {
		    if (data.profileInfoData.isMsisdnVerified) {
			goToChangePhone(data.profileInfoData.msisdn, data.profileInfoData.email)
		    } else {
			goToAddPhoneBy(PhoneNumberUtil.replace62with0(data.profileInfoData.msisdn))
		    }
		}
	    },
	    ProfileInfoItemUiModel(ProfileInfoConstants.GENDER, title = "Jenis Kelamin", itemValue = getGenderText(data.profileInfoData.gender), isEnable = data.profileRoleData.isAllowedChangeGender){
		val genderType = data.profileInfoData.gender
		if (genderType != GENDER_MALE && genderType != GENDER_FEMALE) {
		    val intent =
			RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_GENDER)
		    startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_GENDER)
		}
	    },
	    ProfileInfoItemUiModel(ProfileInfoConstants.BIRTH_DATE, title = "Tanggal Lahir", itemValue = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY, data.profileInfoData.birthDay), isEnable = data.profileRoleData.isAllowedChangeDob) {
		if(data.profileInfoData.birthDay.isEmpty()) {
		    goToAddDob()
		} else {
		    goToChangeDob(data.profileInfoData.birthDay)
		}
	    },
	)
	adapter.setProfileInfoItem(listItem)
	renderWarningTickerName(data.profileInfoData)
    }

    private fun getGenderText(gender: Int): String {
        return if(gender == GENDER_MALE) "Pria" else if (gender == GENDER_FEMALE) "Wanita" else "Pilih Jenis Kelamin"
    }

    private fun showToasterError(errorMsg: String) {
	Toaster.build(requireView(), errorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun showNormalToaster(msg: String) {
	Toaster.build(requireView(), msg, Toaster.LENGTH_LONG).show()
    }

    private fun onErrorGetProfilePhoto(errorException: Exception) {
        showToasterError(ErrorHandlerSession.getErrorMessage(errorException, context, false))
    }

    private fun renderWarningTickerName(profileInfoData: ProfileInfoData) {
	if (profileInfoData.fullName.contains(DEFAULT_NAME)) {
	    binding?.fragmentProfileInfoTicker?.show()
	} else {
	    binding?.fragmentProfileInfoTicker?.hide()
	}
    }

    private fun onSuccessGetProfilePhoto(data: Intent?) {
	if (data != null) {
	    val imageUrlOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
	    if (imageUrlOrPathList.isNotEmpty()) {
		val image = File(imageUrlOrPathList[0])
		if (!image.exists()) {
		    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
		} else {
//		    showLoading(true)
		    viewModel.uploadPicture(image)
		}
	    } else { onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture))) }
	} else {
	    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
	}
    }

    private fun copyUserId() {
	val myClipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
	val myClip: ClipData = ClipData.newPlainText("user_id", userSession.userId)
	myClipboard.setPrimaryClip(myClip)
	showNormalToaster("User ID Copied")
    }

    override fun onSectionIconClicked(id: String?) {
	when(id) {
	    ProfileInfoConstants.PROFILE_INFO_SECTION -> {
	        // Show Bottom Sheet here
	    }
	    ProfileInfoConstants.PROFILE_PERSONAL_INFO_SECTION -> {
		// Show Bottom Sheet here
	    }
	}
    }

    override fun onItemClicked(item: ProfileInfoItemUiModel?) {
	when(item?.id) {
	    ProfileInfoConstants.USER_ID -> { copyUserId() }
	}
    }

    private fun goToChangeEmail() {
	val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
	    appendPath(UrlSettingProfileConst.USER_PATH_URL)
	    appendPath(UrlSettingProfileConst.PROFILE_PATH_URL)
	    appendPath(UrlSettingProfileConst.EMAIL_PATH_URL)
	}.build().toString()

	val intent = ProfileSettingWebViewActivity.createIntent(requireContext(), url)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_CHANGE_EMAIL)
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

    private fun goToAddPhone() {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_PHONE)
    }

    private fun goToAddPhoneBy(phone: String) {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE_WITH, phone)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_PHONE)
    }

    private fun goToChangePhone(phone: String, email: String) {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER)
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_EDIT_PHONE)
    }

    private fun goToAddDob() {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_BOD)
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(R.string.title_add_bod))
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_BOD)
    }

    private fun goToChangeDob(bod: String) {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_BOD)
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(R.string.title_change_bod))
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD, bod)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_EDIT_BOD)
    }

    companion object {
	const val MAX_FILE_SIZE = 2048
	const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200
	private const val DEFAULT_NAME = "Toppers-"

	private const val GENDER_MALE = 1
	private const val GENDER_FEMALE = 2

	fun createInstance(): ProfileInfoFragment {
            return ProfileInfoFragment()
        }
    }
}