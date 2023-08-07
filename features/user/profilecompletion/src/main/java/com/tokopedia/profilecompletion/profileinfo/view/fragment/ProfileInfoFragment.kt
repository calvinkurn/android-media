package com.tokopedia.profilecompletion.profileinfo.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity
import com.tokopedia.profilecompletion.databinding.FragmentProfileInfoBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.data.Detail
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoConstants
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoData
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoError
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.domain.UrlSettingProfileConst
import com.tokopedia.profilecompletion.profileinfo.tracker.CloseAccountTracker
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker.Companion.LABEL_ENTRY_POINT_USER_ID
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoAdapter
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory
import com.tokopedia.profilecompletion.profileinfo.view.bottomsheet.CloseAccountBottomSheet
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoTitleViewHolder
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.isUsingNightModeResources
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.toBlocking
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import com.tokopedia.utils.phonenumber.PhoneNumberUtil
import com.tokopedia.utils.view.binding.viewBinding
import dagger.Lazy
import kotlinx.coroutines.launch
import java.io.File
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

    @Inject
    lateinit var closeAccountTracker: CloseAccountTracker

    @Inject
    lateinit var userSessionDataStore: Lazy<UserSessionDataStore>

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

    private var loaderCloseAccount: LoaderDialog? = null

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
        setProfilePicture()
        initListener()

        binding?.itemProfileManagement?.imgTitle?.loadImageWithoutPlaceholder(
            if (isUsingNightModeResources()) {
                getString(R.string.img_profile_management_entry_point_night)
            } else {
                getString(R.string.img_profile_management_entry_point_light)
            }
        )

        val isProfileManagementM1Activated = isProfileManagementM1Activated()
        binding?.itemProfileManagement?.root?.showWithCondition(isProfileManagementM1Activated)
        binding?.fragmentInfoDivider2?.showWithCondition(isProfileManagementM1Activated)
    }

    private fun initListener() {
        binding?.textCloseAccount?.setOnClickListener {
            closeAccountTracker.trackClickCloseAccount(CloseAccountTracker.LABEL_KLIK)
            checkFinancialAssets()
        }

        binding?.itemProfileManagement?.root?.setOnClickListener {
            goToProfileManagement()
        }
    }

    private fun goToProfileManagement() {
        val intent = RouteManager.getIntent(requireActivity(), ApplinkConstInternalUserPlatform.PROFILE_MANAGEMENT)
        startActivity(intent)
    }

    private fun isProfileManagementM1Activated(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(KEY_ROLLENCE_PROFILE_MANAGEMENT_M1)
            .isNotEmpty()
    }

    private fun setProfilePicture() {
        lifecycleScope.launch {
            try {
                var profilePicture = userSessionDataStore.get().getProfilePicture().toBlocking()
                    .ifEmpty { userSession.profilePicture }
                if (profilePicture != userSession.profilePicture) {
                    profilePicture = userSession.profilePicture
                    logDataStoreError("profilePicture", DIFFERENT_EXCEPTION)
                }
                binding?.profileInfoImageUnify?.setImageUrl(profilePicture)
            } catch (e: Exception) {
                binding?.profileInfoImageUnify?.setImageUrl(userSession.phoneNumber)
                logDataStoreError("profilePicture", e)
            }
        }
    }

    private fun logDataStoreError(field: String, e: Throwable) {
        ServerLogger.log(
            Priority.P2, DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "error_access_field",
                "field_name" to field,
                "error" to Log.getStackTraceString(e).take(LIMIT_STACKTRACE),
            )
        )
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
            showNormalToaster(getString(R.string.profile_info_success_change_profile_picture))
        }

        viewModel.userFinancialAssets.observe(viewLifecycleOwner) {
            checkFinancialAssetsIsLoading(false)
            when (it) {
                is Success -> {
                    if (it.data.hasFinancialAssets)
                        showCloseAccount(it.data.detail)
                    else
                        goToCloseAccount()
                }
                is Fail -> {
                    closeAccountTracker.trackClickCloseAccount(CloseAccountTracker.LABEL_FAILED)
                    showToasterError(getString(R.string.close_account_failed))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
                        onSuccessGetProfilePhoto(data)
                    }
                    REQUEST_CODE_CHANGE_NAME -> {
                        showNormalToaster(getString(R.string.profile_info_success_change_name))
                    }
                    REQUEST_CODE_ADD_BOD -> {
                        showNormalToaster(getString(R.string.profile_info_success_add_bod))
                    }
                    REQUEST_CODE_EDIT_BOD -> {
                        showNormalToaster(getString(R.string.profile_info_success_change_bod))
                    }
                    REQUEST_CODE_EDIT_PHONE -> {
                        showNormalToaster(getString(R.string.profile_info_success_change_phone_number))
                    }
                    REQUEST_CODE_ADD_EMAIL -> {
                        showNormalToaster(getString(R.string.profile_info_success_add_email))
                    }
                    REQUEST_CODE_ADD_PHONE -> {
                        showNormalToaster(getString(R.string.profile_info_success_add_phone))
                    }
                    REQUEST_CODE_VERIFY_PHONE -> {
                        showNormalToaster(getString(R.string.profile_info_success_verify_phone))
                    }
                    REQUEST_CODE_ADD_GENDER -> {
                        showNormalToaster(getString(R.string.profile_info_success_add_gender))
                    }
                    REQUEST_CODE_CHANGE_EMAIL -> {
                        showNormalToaster(getString(R.string.profile_info_success_change_email_change))
                    }
                    REQUEST_CODE_CHANGE_USERNAME_BIO -> {
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
                    REQUEST_CODE_ADD_PHONE -> {
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
        binding?.shimmerProfileInfo?.root?.gone()
        if (binding?.containerProfileInfo?.visibility == View.GONE) binding?.containerProfileInfo?.visible()

        val listItem = listOf(
            ProfileInfoTitleUiModel(
                ProfileInfoConstants.PROFILE_INFO_SECTION,
                getString(R.string.profile_info_title)
            ),
            ProfileInfoItemUiModel(
                ProfileInfoConstants.NAME,
                title = getString(R.string.profile_info_title_item_name),
                itemValue = data.profileInfoData.fullName
            ) {
                onNameClicked(data)
            },
            ProfileInfoItemUiModel(
                ProfileInfoConstants.USERNAME,
                title = getString(R.string.profile_info_title_username),
                itemValue = data.profileFeedData.profile.username,
                placeholder = getString(R.string.profile_info_placeholder_username),
                isEnable = data.profileFeedData.profile.canChangeUsername,
                rightIcon = entryPointIconUsername(data)
            ) {
                goToEditProfileInfo(ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME)
            },
            ProfileInfoItemUiModel(
                ProfileInfoConstants.BIO,
                title = getString(R.string.profile_info_title_bio),
                itemValue = data.profileFeedData.profile.biography,
                placeholder = getString(R.string.profile_info_placeholder_bio)
            ) {
                goToEditProfileInfo(ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO)
            },
            DividerProfileUiModel("line"),
            ProfileInfoTitleUiModel(
                ProfileInfoConstants.PROFILE_PERSONAL_INFO_SECTION, getString(
                    R.string.profile_info_title_personal_info
                )
            ),
            ProfileInfoItemUiModel(
                ProfileInfoConstants.USER_ID,
                title = getString(R.string.profile_info_title_user_id),
                itemValue = userSession.userId,
                rightIcon = IconUnify.COPY
            ),
            ProfileInfoItemUiModel(
                ProfileInfoConstants.EMAIL,
                title = getString(R.string.title_email),
                itemValue = getEmailValue(data),
                showVerifiedTag = !data.profileInfoData.isEmailDone,
                placeholder = getString(R.string.profile_info_placeholder_email)
            ) {
                onEmailClicked(data)
            },
            ProfileInfoItemUiModel(
                ProfileInfoConstants.PHONE,
                title = getString(R.string.add_phone_title_phone),
                itemValue = data.profileInfoData.msisdn,
                showVerifiedTag = showVerifiedTag(data),
                placeholder = getString(R.string.profile_info_placeholder_phone)
            ) {
                onPhoneClicked(data)
            },
            ProfileInfoItemUiModel(
                ProfileInfoConstants.GENDER, title = getString(R.string.profile_info_title_gender),
                itemValue = getGenderText(data.profileInfoData.gender),
                isEnable = data.profileRoleData.isAllowedChangeGender,
                rightIcon = entryPointIconGender(data),
                placeholder = getString(R.string.profile_info_placeholder_gender)
            ) {
                onGenderClicked(data)
            },
            ProfileInfoItemUiModel(
                ProfileInfoConstants.BIRTH_DATE, title = getString(R.string.profile_info_title_dob),
                itemValue = DateFormatUtils.formatDate(
                    DateFormatUtils.FORMAT_YYYY_MM_DD,
                    DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                    data.profileInfoData.birthDay
                ),
                placeholder = getString(R.string.profile_info_placeholder_dob)
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
                ApplinkConstInternalUserPlatform.CHANGE_NAME,
                data.profileInfoData.fullName,
                data.profileRoleData.chancesChangeName
            )
            startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME)
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
            val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_EMAIL)
            startActivityForResult(intent, REQUEST_CODE_ADD_EMAIL)
        } else {
            if (data.profileInfoData.msisdn.isNotEmpty() && data.profileInfoData.isMsisdnVerified) {
                tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_ENTRY_POINT_EMAIL)
                goToChangeEmail()
            } else if (data.profileInfoData.msisdn.isNotEmpty() && !data.profileInfoData.isMsisdnVerified) {
                tracker.trackOnEntryPointListClick(ProfileInfoTracker.LABEL_POPUP)
                showVerifyEmailDialog(data.profileInfoData.msisdn)
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
                RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.CHANGE_GENDER)
            startActivityForResult(intent, REQUEST_CODE_ADD_GENDER)
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

    private fun showVerifiedTag(data: ProfileInfoUiModel): Boolean {
        return !data.profileInfoData.isMsisdnVerified && data.profileInfoData.msisdn.isNotBlank()
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
                    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.profile_info_failed_to_get_picture)))
                } else {
                    viewModel.uploadPicture(image)
                }
            } else {
                onErrorGetProfilePhoto(MessageErrorException(getString(R.string.profile_info_failed_to_get_picture)))
            }
        } else {
            onErrorGetProfilePhoto(MessageErrorException(getString(R.string.profile_info_failed_to_get_picture)))
        }
    }

    private fun copyUserId() {
        tracker.trackOnEntryPointListClick(LABEL_ENTRY_POINT_USER_ID)
        val myClipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("user_id", userSession.userId)
        myClipboard.setPrimaryClip(myClip)
        showNormalToaster(getString(R.string.profile_info_success_copy_userid))
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
        bottomSheetUnify.isSkipCollapseState = true
        bottomSheetUnify.isHideable = true
        bottomSheetUnify.setTitle(getString(R.string.profile_info_title))
        bottomSheetUnify.bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetUnify.showCloseIcon = true
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
        bottomSheet.showKnob = false
        bottomSheet.isSkipCollapseState = true
        bottomSheet.isHideable = true
        bottomSheet.bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
        bottomSheet.setTitle(getString(R.string.profile_info_title_personal_info))
        bottomSheet.showCloseIcon = true
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
        startActivityForResult(intent, REQUEST_CODE_CHANGE_USERNAME_BIO)
    }

    private fun goToChangeEmail() {
        val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
            appendPath(UrlSettingProfileConst.USER_PATH_URL)
            appendPath(UrlSettingProfileConst.PROFILE_PATH_URL)
            appendPath(UrlSettingProfileConst.EMAIL_PATH_URL)
        }.build().toString()

        val intent = ProfileSettingWebViewActivity.createIntent(requireContext(), url)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_EMAIL)
    }

    private fun showChangeEmailDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.profile_info_add_and_verify_phone))
                setDescription(getString(R.string.profile_info_add_and_verify_phone_detail))
                setPrimaryCTAText(getString(R.string.profile_info_title_add_phone))
                setPrimaryCTAClickListener {
                    goToAddPhone()
                    this.dismiss()
                }
                setSecondaryCTAText(getString(R.string.profile_info_label_cancel))
                setSecondaryCTAClickListener {
                    this.dismiss()
                }
            }.show()
        }
    }

    private fun showVerifyEmailDialog(phone: String) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.profile_info_change_email_and_unverified_phone))
                setDescription(getString(R.string.profile_info_description_verify_phone))
                setPrimaryCTAText(getString(R.string.profile_info_title_verify_phone))
                setPrimaryCTAClickListener {
                    goToAddPhoneBy(phone)
                    this.dismiss()
                }
                setSecondaryCTAText(getString(R.string.profile_info_label_cancel))
                setSecondaryCTAClickListener {
                    this.dismiss()
                }
            }.show()
        }
    }

    private fun goToAddPhone() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PHONE)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
    }

    private fun goToAddPhoneBy(phone: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PHONE_WITH, phone)
        startActivityForResult(intent, REQUEST_CODE_VERIFY_PHONE)
    }

    private fun goToChangePhone(phone: String, email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PHONE)
    }

    private fun goToAddDob() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_BOD)
        intent.putExtra(
            ApplinkConstInternalUserPlatform.PARAM_BOD_TITLE,
            getString(R.string.profile_info_title_add_bod)
        )
        startActivityForResult(intent, REQUEST_CODE_ADD_BOD)
    }

    private fun goToChangeDob(bod: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_BOD)
        intent.putExtra(
            ApplinkConstInternalUserPlatform.PARAM_BOD_TITLE,
            getString(R.string.profile_info_title_change_bod)
        )
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_BOD, bod)
        startActivityForResult(intent, REQUEST_CODE_EDIT_BOD)
    }

    private fun checkFinancialAssetsIsLoading(isLoading: Boolean) {
        if (isLoading) {
            loaderCloseAccount = LoaderDialog(requireActivity())
            loaderCloseAccount?.apply {
                setLoadingText(EMPTY_STRING)
                dialog.setOverlayClose(false)
                show()
            }
        } else {
            loaderCloseAccount?.dialog?.dismiss()
        }
    }

    private fun checkFinancialAssets() {
        checkFinancialAssetsIsLoading(true)
        viewModel.checkFinancialAssets()
    }

    private fun showCloseAccount(detail: Detail) {
        val bottomSheetCloseAccount = CloseAccountBottomSheet(detail)
        bottomSheetCloseAccount.show(childFragmentManager, TAG_BOTTOM_SHEET_CLOSE_ACCOUNT)
    }

    private fun goToCloseAccount() {
        RouteManager.route(
            context,
            "${ApplinkConst.WEBVIEW}?${WEBVIEW_PARAM_HIDE_TITLEBAR}&${WEBVIEW_PARAM_BACK_PRESSED_DISABLED}&url=" +
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CLOSE_ACCOUNT_PATH)
        )
    }

    companion object {
        const val REQUEST_CODE_EDIT_PHONE = 203
        const val REQUEST_CODE_EDIT_BOD = 204
        const val REQUEST_CODE_VERIFY_PHONE = 205

        const val REQUEST_CODE_CHANGE_NAME = 300
        const val REQUEST_CODE_ADD_BOD = 301
        const val REQUEST_CODE_ADD_EMAIL = 302
        const val REQUEST_CODE_ADD_PHONE = 303
        const val REQUEST_CODE_ADD_GENDER = 304
        const val REQUEST_CODE_CHANGE_EMAIL = 305
        const val REQUEST_CODE_CHANGE_USERNAME_BIO = 306

        const val MAX_FILE_SIZE = 2048
        const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200
        private const val DEFAULT_NAME = "Toppers-"
        private const val ENTRY_POINT_DISABLED = -1
        private const val GENDER_MALE = 1
        private const val GENDER_FEMALE = 2
        private const val TAG_BOTTOM_SHEET_CLOSE_ACCOUNT = "bottom sheet close account"
        private const val EMPTY_STRING = ""
        private const val WEBVIEW_PARAM_HIDE_TITLEBAR =
            "${com.tokopedia.webview.KEY_TITLEBAR}=false"
        private const val WEBVIEW_PARAM_BACK_PRESSED_DISABLED =
            "${com.tokopedia.webview.KEY_BACK_PRESSED_ENABLED}=false"
        private const val TOKOPEDIA_CLOSE_ACCOUNT_PATH = "user/close-account"
        private const val LIMIT_STACKTRACE = 1000
        private val DIFFERENT_EXCEPTION =
            Throwable(message = "Value is different from User Session")

        private const val KEY_ROLLENCE_PROFILE_MANAGEMENT_M1= "M1_Profile_Mgmt"

        fun createInstance(): ProfileInfoFragment {
            return ProfileInfoFragment()
        }
    }
}
