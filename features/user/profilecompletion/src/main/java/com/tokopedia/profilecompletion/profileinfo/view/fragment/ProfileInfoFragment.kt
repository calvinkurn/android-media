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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.R.string.*
import com.tokopedia.profilecompletion.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity
import com.tokopedia.profilecompletion.databinding.FragmentProfileInfoBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoConstants
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoData
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoError
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker.Companion.LABEL_ENTRY_POINT_USER_ID
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.phonenumber.PhoneNumberUtil
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.android.synthetic.main.fragment_profile_info.*
import java.io.File
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ProfileInfoFragment : BaseDaggerFragment(),
    ProfileInfoItemViewHolder.ProfileInfoItemInterface,
    ProfileInfoTitleViewHolder.ProfileInfoTitleInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: ProfileInfoTracker

    private val binding: FragmentProfileInfoBinding? by viewBinding()

    val adapter by lazy {
	ProfileInfoAdapter(ProfileInfoListTypeFactory(this, this))
    }

    private val editPhotoListener = object : View.OnClickListener {
	override fun onClick(v: View?) {
	    val ctx = v?.context ?: return
	    tracker.trackOnChangeProfilePictureClick(ProfileInfoTracker.LABEL_CLICK)
	    val builder = ImagePickerBuilder.getSquareImageBuilder(ctx)
		.apply { maxFileSizeInKB = MAX_FILE_SIZE }
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

    override fun onFragmentBackPressed(): Boolean {
	tracker.trackOnClickBackBtn()
	return super.onFragmentBackPressed()
    }

    override fun onCreateView(
	inflater: LayoutInflater,
	container: ViewGroup?,
	savedInstanceState: Bundle?
    ): View? =
	inflater.inflate(R.layout.fragment_profile_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	super.onViewCreated(view, savedInstanceState)
	initViews()
	setupObserver()
	getProfileInfo()
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
	    when (it) {
		is ProfileInfoError.ErrorSavePhoto -> {
		    tracker.trackOnChangeProfilePictureClick("${ProfileInfoTracker.LABEL_FAILED} - ${it.errorMsg}")
		    showToasterError(it.errorMsg ?: "")
		}
		is ProfileInfoError.GeneralError -> {
		    when (it.error) {
			is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
			    view?.let {
				showGlobalError(GlobalError.NO_CONNECTION)
			    }
			}
			is RuntimeException -> {
			    when (it.error.localizedMessage.toIntOrNull()) {
				ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
				    GlobalError.NO_CONNECTION
				)
				ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
				ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
				else -> {
				    showGlobalError(GlobalError.SERVER_ERROR)
				}
			    }
			}
			else -> {
			    showToasterError(it.error.message ?: "")
			}
		    }
		}
	    }
	}

	viewModel.saveImageProfileResponse.observe(viewLifecycleOwner) {
	    binding?.profileInfoImageUnify?.setImageUrl(it)
	    tracker.trackOnChangeProfilePictureClick(ProfileInfoTracker.LABEL_SUCCESS)
	    showNormalToaster(getString(success_change_profile_picture))
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
			showNormalToaster(getString(change_name_success_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_BOD -> {
			showNormalToaster(getString(success_add_bod_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_EDIT_BOD -> {
			showNormalToaster(getString(success_change_bod_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_EDIT_PHONE -> {
			showNormalToaster(getString(success_change_phone_number_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_EMAIL -> {
			showNormalToaster(getString(success_add_email_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_PHONE -> {
			showNormalToaster(getString(success_add_phone_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_ADD_GENDER -> {
			showNormalToaster(getString(success_add_gender_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_CHANGE_EMAIL -> {
			showNormalToaster(getString(change_email_change_success_v2))
		    }
		    SettingProfileFragment.REQUEST_CODE_CHANGE_USERNAME_BIO -> {
			showNormalToaster(
			    data?.getStringExtra(
				ChangeBioUsernameFragment.RESULT_KEY_MESSAGE_SUCCESS_USERNAME_BIO
			    ) ?: ""
			)
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

    private fun getProfileInfo() {
	viewModel.getProfileInfo()
	binding?.shimmerProfileInfo?.root?.visible()
	binding?.globalError?.gone()
    }

    private fun showGlobalError(type: Int) {
	binding?.shimmerProfileInfo?.root?.gone()
	binding?.containerProfileInfo?.gone()
	binding?.globalError?.setType(type)
	binding?.globalError?.setActionClickListener {
	    getProfileInfo()
	}
	binding?.globalError?.show()
    }

    private fun setProfileData(data: ProfileInfoUiModel) {
	binding?.shimmerProfileInfo?.root?.visible()
	if (binding?.containerProfileInfo?.visibility == View.GONE) binding?.containerProfileInfo?.visible()

	val listItem = listOf(
	    ProfileInfoTitleUiModel(
		ProfileInfoConstants.PROFILE_INFO_SECTION,
		getString(R.string.profile_info_title)
	    ),
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.NAME,
		title = getString(R.string.title_item_name),
		itemValue = data.profileInfoData.fullName
	    ) {
		onNameClicked(data)
	    },
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.USERNAME,
		title = getString(R.string.title_username),
		itemValue = data.profileFeedData.profile.username,
		placeholder = getString(R.string.placeholder_username),
		isEnable = data.profileFeedData.profile.canChangeUsername,
		rightIcon = entryPointIconUsername(data)
	    ) {
		goToEditProfileInfo(ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME)
	    },
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.BIO,
		title = getString(R.string.title_bio),
		itemValue = data.profileFeedData.profile.biography,
		placeholder = getString(R.string.placeholder_bio)
	    ) {
		goToEditProfileInfo(ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO)
	    },
	    DividerProfileUiModel("line"),
	    ProfileInfoTitleUiModel(
		ProfileInfoConstants.PROFILE_PERSONAL_INFO_SECTION, getString(
		    title_personal_info
		)
	    ),
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.USER_ID,
		title = getString(R.string.title_user_id),
		itemValue = userSession.userId,
		rightIcon = IconUnify.COPY
	    ),
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.EMAIL,
		title = getString(R.string.title_email),
		itemValue = getEmailValue(data),
		showVerifiedTag = !data.profileInfoData.isEmailDone,
		placeholder = getString(R.string.placeholder_email)
	    ) {
		onEmailClicked(data)
	    },
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.PHONE,
		title = getString(title_phone),
		itemValue = data.profileInfoData.msisdn,
		showVerifiedTag = !data.profileInfoData.isMsisdnVerified,
		placeholder = getString(R.string.placeholder_phone)
	    ) {
		onPhoneClicked(data)
	    },
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.GENDER, title = getString(R.string.title_gender),
		itemValue = getGenderText(data.profileInfoData.gender),
		isEnable = data.profileRoleData.isAllowedChangeGender,
		rightIcon = entryPointIconGender(data),
		placeholder = getString(R.string.placeholder_gender)
	    ) {
		onGenderClicked(data)
	    },
	    ProfileInfoItemUiModel(
		ProfileInfoConstants.BIRTH_DATE, title = "Tanggal Lahir",
		itemValue = DateFormatUtils.formatDate(
		    DateFormatUtils.FORMAT_YYYY_MM_DD,
		    DateFormatUtils.FORMAT_DD_MMMM_YYYY,
		    data.profileInfoData.birthDay
		),
		placeholder = getString(R.string.placeholder_dob)
	    ) {
		onDobClicked(data)
	    },
	)
	adapter.setProfileInfoItem(listItem)
	renderWarningTickerName(data.profileInfoData)
    }

    private fun onNameClicked(data: ProfileInfoUiModel) {
	if (data.profileRoleData.isAllowedChangeName) {
	    tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRYPOINT_NAME)
	    val intent = RouteManager.getIntent(
		context,
		ApplinkConstInternalGlobal.CHANGE_NAME,
		data.profileInfoData.fullName,
		data.profileRoleData.chancesChangeName
	    )
	    startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_CHANGE_NAME)
	} else {
	    tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_BOTTOMSHEET)
	    openBottomSheetWarning(
		ProfileInfoConstants.NAME,
		data.profileRoleData.changeNameMessageInfoTitle,
		data.profileRoleData.changeNameMessageInfo
	    )
	}
    }

    private fun onEmailClicked(data: ProfileInfoUiModel) {
	if (data.profileInfoData.email.isEmpty() || !data.profileInfoData.isEmailDone) {
	    tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_EMAIL)
	    val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_EMAIL)
	    startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_EMAIL)
	} else {
	    if (data.profileInfoData.msisdn.isNotEmpty() && data.profileInfoData.isMsisdnVerified) {
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_EMAIL)
		goToChangeEmail()
	    } else if (data.profileInfoData.msisdn.isNotEmpty() && !data.profileInfoData.isMsisdnVerified) {
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_POPUP)
		showVerifyEmailDialog()
	    } else {
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_POPUP)
		showChangeEmailDialog()
	    }
	}
    }

    private fun onPhoneClicked(data: ProfileInfoUiModel) {
	tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_PHONE)
	if (data.profileInfoData.msisdn.isEmpty()) {
	    goToAddPhone()
	} else {
	    if (data.profileInfoData.isMsisdnVerified) {
		goToChangePhone(data.profileInfoData.msisdn, data.profileInfoData.email)
	    } else {
		goToAddPhoneBy(PhoneNumberUtil.replace62with0(data.profileInfoData.msisdn))
	    }
	}
    }

    private fun onGenderClicked(data: ProfileInfoUiModel) {
	if (data.profileRoleData.isAllowedChangeGender) {
	    tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_GENDER)
	    val intent =
		RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_GENDER)
	    startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_GENDER)
	}
    }

    private fun onDobClicked(data: ProfileInfoUiModel) {
	if (data.profileInfoData.birthDay.isEmpty()) {
	    tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_DOB)
	    goToAddDob()
	} else {
	    if (data.profileRoleData.isAllowedChangeDob) {
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_DOB)
		goToChangeDob(data.profileInfoData.birthDay)
	    } else {
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_BOTTOMSHEET)
		openBottomSheetWarning(
		    ProfileInfoConstants.BIRTH_DATE,
		    data.profileRoleData.changeDobMessageInfoTitle,
		    data.profileRoleData.changeDobMessageInfo
		)
	    }
	}
    }

    private fun openBottomSheetWarning(entryPoint: String, title: String, msg: String) {
	when (entryPoint) {
	    ProfileInfoConstants.BIRTH_DATE -> {
		tracker.trackOnCloseBottomSheetChangeBirthday()
	    }
	    ProfileInfoConstants.NAME -> {
		tracker.trackOnCloseBottomSheetChangeName()
	    }
	}
	val bottomSheetUnify = BottomSheetUnify()
	bottomSheetUnify.setTitle(title)
	bottomSheetUnify.setCloseClickListener {
	    bottomSheetUnify.dismiss()
	    tracker.trackOnCloseBottomSheetChangeName()
	}
	val view = View.inflate(context, R.layout.layout_bottomsheet_change_name, null).apply {
	    this.findViewById<Typography>(R.id.tv_body).apply {
		this.text = msg
	    }
	    this.findViewById<UnifyButton>(R.id.btn_ok)?.setOnClickListener {
		bottomSheetUnify.dismiss()
	    }
	}
	bottomSheetUnify.setChild(view)
	activity?.supportFragmentManager?.let {
	    bottomSheetUnify.show(it, "")
	}
    }

    private fun entryPointIconGender(data: ProfileInfoUiModel): Int {
	if (data.profileRoleData.isAllowedChangeGender) return IconUnify.CHEVRON_RIGHT
	else return ENTRY_POINT_DISABLED
    }

    private fun entryPointIconUsername(data: ProfileInfoUiModel): Int {
	return if (data.profileFeedData.profile.canChangeUsername) IconUnify.CHEVRON_RIGHT
	else ENTRY_POINT_DISABLED
    }

    private fun getEmailValue(data: ProfileInfoUiModel): String {
	return if (data.profileInfoData.isEmailDone) data.profileInfoData.email
	else ""

    }

    private fun getGenderText(gender: Int): String {
	return if (gender == GENDER_MALE) "Pria" else if (gender == GENDER_FEMALE) "Wanita" else ""
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
		    onErrorGetProfilePhoto(MessageErrorException(getString(failed_to_get_picture)))
		} else {
		    viewModel.uploadPicture(image)
		}
	    } else {
		onErrorGetProfilePhoto(MessageErrorException(getString(failed_to_get_picture)))
	    }
	} else {
	    onErrorGetProfilePhoto(MessageErrorException(getString(failed_to_get_picture)))
	}
    }

    private fun copyUserId() {
	tracker.trackOnEntryPointListClick(LABEL_ENTRY_POINT_USER_ID)
	val myClipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
	val myClip: ClipData = ClipData.newPlainText("user_id", userSession.userId)
	myClipboard.setPrimaryClip(myClip)
	showNormalToaster(getString(R.string.success_copy_userid))
    }

    override fun onSectionIconClicked(id: String?) {
	when (id) {
	    ProfileInfoConstants.PROFILE_INFO_SECTION -> {
		tracker.trackOnInfoProfileClick(ProfileInfoTracker.LABEL_PROFILE_INFO)
		openBottomSheetProfileInfo()
	    }
	    ProfileInfoConstants.PROFILE_PERSONAL_INFO_SECTION -> {
		tracker.trackOnInfoProfileClick(ProfileInfoTracker.LABEL_PERSONAL_INFO)
		openBottomSheetPersonalInfo()
	    }
	}
    }

    override fun onRightIconClicked(item: ProfileInfoItemUiModel?) {
	when (item?.id) {
	    ProfileInfoConstants.USER_ID -> {
		copyUserId()
	    }
	}
    }

    private fun openBottomSheetProfileInfo() {
	val bottomSheetUnify = BottomSheetUnify()
	val data = viewModel.profileInfoUiData.value?.profileFeedData?.profile
	bottomSheetUnify.isDragable = true
	bottomSheetUnify.showKnob = true
	bottomSheetUnify.isSkipCollapseState = true
	bottomSheetUnify.isHideable = true
	bottomSheetUnify.bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
	bottomSheetUnify.showCloseIcon = false
	val view = View.inflate(context, R.layout.layout_bottomsheet_profile_info, null).apply {
	    this.findViewById<ImageUnify>(R.id.iv_profile_info)
		.setImageUrl(data?.profilePreviewImageUrl ?: "")
	    this.findViewById<UnifyButton>(R.id.btn_profile_info)?.setOnClickListener {
		tracker.trackClickOnLihatHalaman()
		val shareLink = data?.shareLink
		RouteManager.route(activity, shareLink?.applink ?: "")
	    }
	}
	bottomSheetUnify.setChild(view)
	activity?.supportFragmentManager?.let {
	    bottomSheetUnify.show(it, "")
	}
    }

    private fun openBottomSheetPersonalInfo() {
	val bottomSheet = BottomSheetUnify()
	bottomSheet.showKnob = true
	bottomSheet.isSkipCollapseState = true
	bottomSheet.isHideable = true
	bottomSheet.bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
	bottomSheet.showCloseIcon = false
	val view = View.inflate(context, R.layout.layout_bottomsheet_personal_info, null).apply {

	}
	bottomSheet.clearContentPadding = true
	bottomSheet.setChild(view)
	activity?.supportFragmentManager?.let {
	    bottomSheet.show(it, "")
	}
    }

    private fun goToEditProfileInfo(page: String) {
	when (page) {
	    ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME ->
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRYPOINT_USERNAME)
	    ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO ->
		tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRYPOINT_BIO)
	}
	val intent =
	    RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.EDIT_PROFILE_INFO)
	intent.putExtra(ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM, page)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_CHANGE_USERNAME_BIO)
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
		setTitle(getString(add_and_verify_phone))
		setDescription(getString(add_and_verify_phone_detail))
		setPrimaryCTAText(getString(title_add_phone))
		setPrimaryCTAClickListener {
		    goToAddPhone()
		    this.dismiss()
		}
		setSecondaryCTAText(getString(label_cancel))
		setSecondaryCTAClickListener {
		    this.dismiss()
		}
	    }.show()
	}
    }

    private fun showVerifyEmailDialog() {
	context?.let {
	    DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
		setTitle(getString(add_and_verify_phone))
		setDescription(getString(add_and_verify_phone_detail))
		setPrimaryCTAText(getString(title_verify_phone))
		setPrimaryCTAClickListener {
		    goToAddPhone()
		    this.dismiss()
		}
		setSecondaryCTAText(getString(label_cancel))
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
	val intent =
	    RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE_WITH, phone)
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
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(title_add_bod))
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_ADD_BOD)
    }

    private fun goToChangeDob(bod: String) {
	val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_BOD)
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD_TITLE, getString(title_change_bod))
	intent.putExtra(ApplinkConstInternalGlobal.PARAM_BOD, bod)
	startActivityForResult(intent, SettingProfileFragment.REQUEST_CODE_EDIT_BOD)
    }

    companion object {
	const val MAX_FILE_SIZE = 2048
	const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200
	private const val DEFAULT_NAME = "Toppers-"
	private const val ENTRY_POINT_DISABLED = -1
	private const val GENDER_MALE = 1
	private const val GENDER_FEMALE = 2

	fun createInstance(): ProfileInfoFragment {
	    return ProfileInfoFragment()
	}
    }
}