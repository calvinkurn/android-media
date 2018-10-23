package com.tokopedia.profile.domain.usecase

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kol.feature.post.data.mapper.GetContentListMapper
import com.tokopedia.kol.feature.post.domain.model.ContentListDomain
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliateQuotaData
import com.tokopedia.profile.data.pojo.profileheader.Profile
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileFirstPage @Inject constructor(val getProfileHeaderUseCase: GetProfileHeaderUseCase,
                                              val getContentListUseCase: GetContentListUseCase,
                                              val getAffiliateQuotaUseCase: GetAffiliateQuotaUseCase,
                                              val getContentListMapper: GetContentListMapper,
                                              val userSession: UserSession)
    : UseCase<ProfileFirstPageViewModel>() {

    var userId = 0

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ProfileFirstPageViewModel>? {
        userId = requestParams!!.getInt(GetProfileHeaderUseCase.PARAM_USER_ID, 0)
        return Observable.zip(
                getHeader(userId),
                getPost(),
                getQuota()) { header: ProfileHeaderViewModel,
                              content: ContentListDomain,
                              quota: AffiliatePostQuota ->
            ProfileFirstPageViewModel(header, content.visitableList, quota, content.lastCursor)
        }
    }

    private fun getHeader(userId: Int): Observable<ProfileHeaderViewModel> {
        return getProfileHeaderUseCase
                .createObservable(GetProfileHeaderUseCase.createRequestParams(userId))
                .map(convertToHeader())
                .subscribeOn(Schedulers.io())
    }

    private fun getPost(): Observable<ContentListDomain> {
        return getContentListUseCase
                .createObservable(GetContentListUseCase.getProfileParams(userId, ""))
                .subscribeOn(Schedulers.io())
    }

    private fun getQuota(): Observable<AffiliatePostQuota> {
        return if (userId.toString() == userSession.userId) {
            getAffiliateQuotaUseCase
                    .createObservable(RequestParams.EMPTY)
                    .map(convertToPostQuota())
                    .subscribeOn(Schedulers.io())
        } else {
            Observable.just(AffiliatePostQuota())
        }
    }

    private fun convertToHeader(): Func1<GraphqlResponse, ProfileHeaderViewModel> {
        return Func1 { graphqlResponse ->
            val data: ProfileHeaderData = graphqlResponse.getData(ProfileHeaderData::class.java)
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
                    profile.isFollowed,
                    userId.toString() == userSession.userId
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
        fun createRequestParams(userId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(GetProfileHeaderUseCase.PARAM_USER_ID, userId)
            return requestParams
        }
    }
}