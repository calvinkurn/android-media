package com.tokopedia.profilecompletion.profileinfo.view.fragment

import android.content.Intent
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
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.FragmentProfileInfoBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoAdapter
import com.tokopedia.profilecompletion.profileinfo.view.adapter.ProfileInfoListTypeFactory
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoTitleViewHolder
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
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
	    val builder = ImagePickerBuilder.getSquareImageBuilder(ctx).apply {
		maxFileSizeInKB = MAX_FILE_SIZE
	    }
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
	layoutManager.isAutoMeasureEnabled = true
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
        when(requestCode) {
	    REQUEST_CODE_EDIT_PROFILE_PHOTO -> {
	        onSuccessGetProfilePhoto(data)
	    }
        }
    }

    private fun setProfileData(data: ProfileInfoUiModel) {
        val listItem = listOf(
	    ProfileInfoTitleUiModel("info_profile", "Info Profil"),
	    ProfileInfoItemUiModel("0", title = "Nama", itemValue = data.profileInfoData.fullName),
	    ProfileInfoItemUiModel("1", title = "Username", itemValue = "username"),
	    ProfileInfoItemUiModel("2", title = "Bio", itemValue = "bio"),
	    DividerProfileUiModel("line"),
	    ProfileInfoTitleUiModel("info_personal", "Info Pribadi"),
	    ProfileInfoItemUiModel("4", title = "User ID", itemValue = userSession.userId),
	    ProfileInfoItemUiModel("5", title = "E-mail", itemValue = data.profileInfoData.email),
	    ProfileInfoItemUiModel("6", title = "Nomor HP", itemValue = data.profileInfoData.msisdn),
	    ProfileInfoItemUiModel("7", title = "Jenis Kelamin", itemValue = getGenderText(data.profileInfoData.gender)),
	    ProfileInfoItemUiModel("8", title = "Tanggal Lahir", itemValue = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY, data.profileInfoData.birthDay)),
	)
	adapter.setProfileInfoItem(listItem)
    }

    private fun getGenderText(gender: Int): String {
        return if(gender == 1) "Pria" else if (gender == 2) "Wanita" else "Pilih Jenis Kelamin"
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

    override fun onIconClicked(id: String?) {

    }

    override fun onRightIconClicked(itemId: String?) {

    }

    companion object {
	const val MAX_FILE_SIZE = 2048
	const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200

        fun createInstance(): ProfileInfoFragment {
            return ProfileInfoFragment()
        }
    }
}