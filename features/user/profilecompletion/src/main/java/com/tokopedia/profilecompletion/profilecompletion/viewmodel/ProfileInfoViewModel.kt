package com.tokopedia.profilecompletion.profilecompletion.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase.Companion.PARAM_UPLOAD_ID
import com.tokopedia.profilecompletion.profilecompletion.data.ProfileCompletionData
import com.tokopedia.profilecompletion.profilecompletion.data.UploadProfilePictureResult
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileInfoData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class ProfileInfoViewModel @Inject constructor(
    private val userProfileInfoUseCase: GraphqlUseCase<UserProfileInfoData>,
    private val uploader: UploaderUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val userSession: UserSessionInterface,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val userProfileInfo = MutableLiveData<Result<ProfileCompletionData>>()
    val uploadProfilePictureResponse = MutableLiveData<Result<UploadProfilePictureResult>>()

    private val _saveImageProfileResponse = MutableLiveData<Result<String>>()
    val saveImageProfileResponse: LiveData<Result<String>> = _saveImageProfileResponse

    fun getUserProfileInfo(context: Context) {
	GraphqlHelper.loadRawString(context.resources, R.raw.query_user_profile_completion)
	    ?.let { query ->
		userProfileInfoUseCase.run {
		    setGraphqlQuery(query)
		    execute(
			{
			    it.profileCompletionData.apply {
				this.profilePicture = userSession.profilePicture
				userProfileInfo.value = Success(this)
			    }
			}, {
			    userProfileInfo.value = Fail(it)
			}
		    )
		}
	    }
    }

    /* Use media uploader */
    fun uploadPicture(image: File) {
	viewModelScope.launch(dispatcher.io) {
	    val param = uploader.createParams(
		filePath = image,
		sourceId = SOURCE_ID
	    )
	    val result = uploader(param)
	    withContext(dispatcher.main) {
		when (result) {
		    is UploadResult.Success -> saveProfilePicture(result.uploadId)
		    is UploadResult.Error -> _saveImageProfileResponse.value =
			Fail(Throwable(result.message))
		}
	    }
	}
    }

    /* To send uploadID from media uploader to accounts BE */
    fun saveProfilePicture(uploadId: String) {
	viewModelScope.launch(dispatcher.io) {
	    try {
		val res = saveProfilePictureUseCase(
		    mapOf(PARAM_UPLOAD_ID to uploadId)
		)
		withContext(dispatcher.main) {
		    if (res.data.errorMessage.isEmpty() &&
			res.data.innerData.isSuccess == 1 &&
			res.data.innerData.imageUrl.isNotEmpty()
		    ) {
			_saveImageProfileResponse.value = Success(res.data.innerData.imageUrl)
		    } else {
			_saveImageProfileResponse.value =
			    Fail(Throwable(res.data.errorMessage.first()))
		    }
		}
	    } catch (e: Exception) {
		withContext(dispatcher.main) {
		    _saveImageProfileResponse.value = Fail(e)
		}
	    }
	}
    }

    companion object {
	private val SOURCE_ID = "tPxBYm"
    }
}