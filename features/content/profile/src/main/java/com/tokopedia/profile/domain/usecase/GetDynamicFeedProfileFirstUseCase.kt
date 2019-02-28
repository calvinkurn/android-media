package com.tokopedia.profile.domain.usecase

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profile.data.pojo.profileheader.Profile
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderError
import com.tokopedia.profile.view.viewmodel.DynamicFeedProfileViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by yfsx on 15/02/19.
 */
class GetDynamicFeedProfileFirstUseCase
@Inject constructor(@ApplicationContext val context: Context,
                    val getDynamicFeedUseCase: GetDynamicFeedUseCase,
                    val getProfileHeaderUseCase: GetProfileHeaderUseCase,
                    val userSessionInterface: UserSessionInterface)
    : UseCase<DynamicFeedProfileViewModel>() {
    var userId = 0;

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedProfileViewModel> {
        userId = requestParams!!.getInt(GetProfileHeaderUseCase.PARAM_USER_ID_TARGET, 0)
        return Observable.zip(
                getProfileHeader(userId),
                getDynamicFeed(requestParams)
        ) { header, feed -> DynamicFeedProfileViewModel(header, feed)
        }
    }


    private fun getDynamicFeed(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    private fun getProfileHeader(userId: Int): Observable<ProfileHeaderViewModel> {
       return getProfileHeaderUseCase.createObservable(GetProfileHeaderUseCase.createRequestParams(userId))
                .map(convertToHeader())
                .subscribeOn(Schedulers.io())
    }

    private fun convertToHeader(): Func1<GraphqlResponse, ProfileHeaderViewModel> {
        return Func1 { graphqlResponse ->
            val data: ProfileHeaderData = graphqlResponse.getData(ProfileHeaderData::class.java)
            val error: ProfileHeaderError = data.bymeProfileHeader.profileHeaderError

            if (TextUtils.isEmpty(error.message).not()) {
                throw MessageErrorException(error.message)
            }

            val profile: Profile = data.bymeProfileHeader.profile
            ProfileHeaderViewModel(
                    profile.name,
                    profile.avatar,
                    profile.totalFollower.formatted,
                    profile.totalFollowing.formatted,
                    profile.affiliateName,
                    profile.link,
                    userId,
                    profile.isKol,
                    profile.isAffiliate,
                    profile.isShowAffiliateContent,
                    profile.isFollowed,
                    userId.toString() == userSessionInterface.userId
            )
        }
    }
    companion object {
        fun createRequestParams(targetUserId: Int): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createProfileFeedRequestParams(targetUserId.toString(), "")
            requestParams.putInt(GetProfileHeaderUseCase.PARAM_USER_ID_TARGET, targetUserId)
            return requestParams
        }
    }
}