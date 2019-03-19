package com.tokopedia.profile.domain.usecase

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profile.R
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliateQuotaData
import com.tokopedia.profile.data.pojo.profileheader.Profile
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderError
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileUseCase.Companion.SOURCE_ID
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
                    val getAffiliateQuotaUseCase: GetAffiliateQuotaUseCase,
                    val userSessionInterface: UserSessionInterface)
    : UseCase<DynamicFeedProfileViewModel>() {
    var userId = 0;

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedProfileViewModel> {
        userId = requestParams!!.getInt(GetProfileHeaderUseCase.PARAM_USER_ID_TARGET, 0)
        return Observable.zip(
                getProfileHeader(userId),
                getQuota(),
                getDynamicFeed(requestParams)
        ) { header, quota, feed -> DynamicFeedProfileViewModel(header, feed, quota)
        }
    }


    private fun getDynamicFeed(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    private fun getQuota(): Observable<AffiliatePostQuota> {
        return if (userId.toString() == userSessionInterface.userId) {
            getAffiliateQuotaUseCase
                    .createObservable(RequestParams.EMPTY)
                    .map(convertToPostQuota())
                    .subscribeOn(Schedulers.io())
        } else {
            Observable.just(AffiliatePostQuota())
        }
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

    private fun convertToPostQuota(): Func1<GraphqlResponse, AffiliatePostQuota> {
        return Func1 { graphqlResponse ->
            val data: AffiliateQuotaData? = graphqlResponse.getData(AffiliateQuotaData::class.java)
            data?.affiliatePostQuota ?: AffiliatePostQuota()
        }
    }

    companion object {
        fun createRequestParams(selfUserId: String, targetUserId: String): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(selfUserId, "", GetDynamicFeedUseCase.SOURCE_PROFILE)
            requestParams.putInt(GetProfileHeaderUseCase.PARAM_USER_ID_TARGET, targetUserId.toInt())
            requestParams.putString(SOURCE_ID, targetUserId)
            return requestParams
        }
    }
}