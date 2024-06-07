package com.tokopedia.accountprofile.settingprofile.profileinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.CheckUserFinancialAssets
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileInfoError
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.accountprofile.settingprofile.profileinfo.usecase.ProfileFeedInfoUseCase
import com.tokopedia.accountprofile.settingprofile.profileinfo.usecase.ProfileInfoUseCase
import com.tokopedia.accountprofile.settingprofile.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.accountprofile.settingprofile.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.accountprofile.settingprofile.profileinfo.usecase.UserFinancialAssetsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileInfoUseCase: ProfileInfoUseCase,
    private val profileRoleUseCase: ProfileRoleUseCase,
    private val profileFeedInfoUseCase: ProfileFeedInfoUseCase,
    private val uploader: UploaderUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val userFinancialAssetsUseCase: UserFinancialAssetsUseCase,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableProfileInfoUiData = MutableLiveData<ProfileInfoUiModel>()
    val profileInfoUiData: LiveData<ProfileInfoUiModel>
        get() = mutableProfileInfoUiData

    private val mutableErrorMessage = SingleLiveEvent<ProfileInfoError>()
    val errorMessage: LiveData<ProfileInfoError> = mutableErrorMessage

    private val _saveImageProfileResponse = MutableLiveData<String>()
    val saveImageProfileResponse: LiveData<String> = _saveImageProfileResponse

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, error ->
        mutableErrorMessage.value = ProfileInfoError.GeneralError(error)
    }

    private val _userFinancialAssets = SingleLiveEvent<Result<CheckUserFinancialAssets>>()
    val userFinancialAssets: LiveData<Result<CheckUserFinancialAssets>> get() = _userFinancialAssets

    fun getProfileInfo() {
        launch(coroutineErrorHandler) {
            try {
                val profileInfo = async { profileInfoUseCase(Unit) }
                val profileRole = async { profileRoleUseCase(Unit) }
                val profileFeed = async { profileFeedInfoUseCase(Unit) }

                mutableProfileInfoUiData.value = ProfileInfoUiModel(
                    profileInfo.await().profileInfoData,
                    profileRole.await().profileRole,
                    profileFeed.await().profileFeedData
                )
            } catch (e: Exception) {
                mutableErrorMessage.value = ProfileInfoError.GeneralError(e)
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
                    else -> mutableErrorMessage.value =
                        ProfileInfoError.ErrorSavePhoto((result as UploadResult.Error).message)
                }
            } catch (e: Exception) {
                mutableErrorMessage.value = ProfileInfoError.ErrorSavePhoto(e.message)
            }
        }
    }

    /* To send uploadID from media uploader to accounts BE */
    private fun saveProfilePicture(uploadId: String) {
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
                    mutableErrorMessage.value =
                        ProfileInfoError.ErrorSavePhoto(res.data.errorMessage.first())
                }
            } catch (e: Exception) {
                mutableErrorMessage.value = ProfileInfoError.ErrorSavePhoto(e.message)
            }
        }
    }

    fun checkFinancialAssets() {
        launchCatchError(coroutineContext, {
            val response = userFinancialAssetsUseCase(Unit)
            _userFinancialAssets.value = Success(response.checkUserFinancialAssets)
        }) {
            _userFinancialAssets.value = Fail(it)
        }
    }

    companion object {
        private const val SOURCE_ID = "tPxBYm"
    }
}
