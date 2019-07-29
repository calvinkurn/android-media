package com.tokopedia.profilecompletion.settingprofile.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.data.SubmitProfilePictureData
import com.tokopedia.profilecompletion.settingprofile.data.UploadProfilePictureResult
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileInfoData
import com.tokopedia.profilecompletion.settingprofile.domain.UploadProfilePictureUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 */

class ProfileInfoViewModel @Inject constructor(
        private val userProfileInfoUseCase: GraphqlUseCase<UserProfileInfoData>,
        private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
        private val submitProfilePictureUseCase: GraphqlUseCase<SubmitProfilePictureData>,
        private val userSession: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val userProfileInfo = MutableLiveData<Result<ProfileCompletionData>>()
    val uploadProfilePictureResponse = MutableLiveData<Result<UploadProfilePictureResult>>()

    fun getUserProfileInfo() {
        val rawQuery = rawQueries[ProfileCompletionQueriesConstant.QUERY_PROFILE_COMPLETION]
        if (!rawQuery.isNullOrEmpty()) {
            userProfileInfoUseCase.run {
                setGraphqlQuery(rawQuery)
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

    fun uploadProfilePicture(imagePath: String) {

        uploadProfilePictureUseCase.execute(
                UploadProfilePictureUseCase.createRequestParams(imagePath),
                object : Subscriber<UploadProfileImageModel>() {

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        uploadProfilePictureResponse.value = Fail(e)
                    }

                    override fun onNext(result: UploadProfileImageModel) {

                        if (result.data.picObj.isNotBlank()) {
                            rawQueries[ProfileCompletionQueriesConstant.MUTATION_CHANGE_PICTURE]?.let { query ->

                                val params = mapOf(ProfileCompletionQueriesConstant.PARAM_PIC_OBJ to result.data.picObj)

                                submitProfilePictureUseCase.setTypeClass(SubmitProfilePictureData::class.java)
                                submitProfilePictureUseCase.setRequestParams(params)
                                submitProfilePictureUseCase.setGraphqlQuery(query)

                                submitProfilePictureUseCase.execute(
                                        onSuccessChangePicture(result),
                                        onErrorChangePicture()
                                )
                            }
                        } else {
                            uploadProfilePictureResponse.value = Fail(java.lang.RuntimeException())
                        }
                    }
                })
    }

    private fun onErrorChangePicture(): (Throwable) -> Unit {
        return {
            uploadProfilePictureResponse.value = Fail(it)
        }
    }

    private fun onSuccessChangePicture(result: UploadProfileImageModel): (SubmitProfilePictureData) -> Unit {
        return {
            when {
                it.changePictureData.isSuccess == 1 -> uploadProfilePictureResponse.value = Success(UploadProfilePictureResult(result, it))
                it.changePictureData.error.isNotBlank() -> uploadProfilePictureResponse.value = Fail(MessageErrorException(it.changePictureData.error))
                else -> uploadProfilePictureResponse.value = Fail(RuntimeException())
            }
        }
    }
}