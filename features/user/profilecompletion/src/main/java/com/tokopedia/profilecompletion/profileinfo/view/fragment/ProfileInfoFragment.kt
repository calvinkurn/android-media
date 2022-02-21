package com.tokopedia.profilecompletion.profileinfo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

class ProfileInfoFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val binding: FragmentProfileInfoBinding? by viewBinding()

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
	    } else {
		onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
	    }
	} else {
	    onErrorGetProfilePhoto(MessageErrorException(getString(R.string.failed_to_get_picture)))
	}
    }

    companion object {
	const val MAX_FILE_SIZE = 2048
	const val REQUEST_CODE_EDIT_PROFILE_PHOTO = 200

        fun createInstance(): ProfileInfoFragment {
            return ProfileInfoFragment()
        }
    }
}