package com.tokopedia.profilecompletion.settingprofile.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
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
    private val uploadProfilePictureUseCase : UploadProfilePictureUseCase,
    private val userSession: UserSessionInterface,
    private val rawQueries: Map<String, String>,
    dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    val userProfileInfo = MutableLiveData<Result<ProfileCompletionData>>()
    val uploadProfilePictureResponse = MutableLiveData<Result<UploadProfileImageModel>>()

    fun getUserProfileInfo(){
        val rawQuery = rawQueries[ProfileCompletionQueriesConstant.QUERY_PROFILE_COMPLETION]
        if(!rawQuery.isNullOrEmpty()){
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                CacheType.CACHE_FIRST)
            graphqlCacheStrategy.setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5)
            userProfileInfoUseCase.run {
//                setCacheStrategy(graphqlCacheStrategy.build())
                setGraphqlQuery(rawQuery)
                execute(
                    {
                        it.profileCompletionData.apply {
                            this.profilePicture = userSession.profilePicture
                            userProfileInfo.value = Success(this)
                        }
                    },{
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
                uploadProfilePictureResponse.value = Success(result)
            }
        })
    }
}