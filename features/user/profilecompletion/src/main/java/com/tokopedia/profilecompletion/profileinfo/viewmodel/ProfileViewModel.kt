package com.tokopedia.profilecompletion.profileinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileInfoUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileInfoUseCase: ProfileInfoUseCase,
    private val profileRoleUseCase: ProfileRoleUseCase,
    private val uploader: UploaderUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val mutableProfileInfoUiData = MutableLiveData<ProfileInfoUiModel>()
    val profileInfoUiData: LiveData<ProfileInfoUiModel>
        get() = mutableProfileInfoUiData

    private val mutableErrorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val _saveImageProfileResponse = MutableLiveData<String>()
    val saveImageProfileResponse: LiveData<String> = _saveImageProfileResponse

    fun getProfileInfo() {
        launch {
            try {
                val profileInfo = async { profileInfoUseCase(Unit) }
                val profileRole = async { profileRoleUseCase(Unit) }
                mutableProfileInfoUiData.value = ProfileInfoUiModel(
                    profileInfo.await().profileInfoData,
                    profileRole.await().profileRole
                )
            } catch (e: Exception) {
                mutableErrorMessage.value = e.message
            }
        }
    }

    /* Use media uploader */
    fun uploadPicture(image: File) {
        launch {
            try {
                val param = uploader.createParams(filePath = image, sourceId = SOURCE_ID)
                when (val result = uploader(param)) {
                    is UploadResult.Success -> saveProfilePicture(result.uploadId)
                    is UploadResult.Error -> mutableErrorMessage.value = result.message
                }
            } catch (e: Exception) {
                mutableErrorMessage.value = e.message
            }
        }
    }

    /* To send uploadID from media uploader to accounts BE */
    fun saveProfilePicture(uploadId: String) {
        launch {
            try {
                val res = saveProfilePictureUseCase(
                    mapOf(SaveProfilePictureUseCase.PARAM_UPLOAD_ID to uploadId)
                )
                if (res.data.errorMessage.isEmpty() &&
                    res.data.innerData.isSuccess == 1 &&
                    res.data.innerData.imageUrl.isNotEmpty()
                ) {
                    val imgUrl = res.data.innerData.imageUrl
                    userSession.profilePicture = imgUrl
                    _saveImageProfileResponse.value = imgUrl
                } else {
                    mutableErrorMessage.value = res.data.errorMessage.first()
                }
            } catch (e: Exception) {
                mutableErrorMessage.value = e.message
            }
        }
    }

    companion object {
        private val SOURCE_ID = "tPxBYm"
    }
}