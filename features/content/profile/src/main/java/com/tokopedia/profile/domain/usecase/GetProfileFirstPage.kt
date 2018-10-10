package com.tokopedia.profile.domain.usecase

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
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
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileFirstPage @Inject constructor(val getProfileHeaderUseCase: GetProfileHeaderUseCase,
                                              val getProfilePostUseCase: GetProfilePostUseCase,
                                              val getAffiliateQuotaUseCase: GetAffiliateQuotaUseCase,
                                              val userSession: UserSession)
    : UseCase<ProfileFirstPageViewModel>() {

    var userId = 0

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ProfileFirstPageViewModel>? {
        userId = requestParams?.getInt(GetProfileHeaderUseCase.PARAM_USER_ID, 0)
                ?: 0
        return Observable.zip(
                getHeader(userId),
                getPost(),
                getQuota()) { header: ProfileHeaderViewModel,
                              posts: List<KolPostViewModel>,
                              quota: AffiliatePostQuota ->
            ProfileFirstPageViewModel(header, posts, quota)
        }
    }

    private fun getHeader(userId: Int): Observable<ProfileHeaderViewModel> {
        return getProfileHeaderUseCase
                .createObservable(GetProfileHeaderUseCase.createRequestParams(userId))
                .map(convertToHeader())
    }

    private fun getPost(): Observable<List<KolPostViewModel>> {
        //TODO milhamj
        val posts: ArrayList<KolPostViewModel> = ArrayList()
//        var imageList: ArrayList<String> = ArrayList()
//        imageList.add("https://kucingpedia.com/wp-content/uploads/2017/11/cara-mengobati-kucing-yang-demam.jpg")
//        imageList.add("http://www.redefiningamber.com/wp-content/uploads/2017/10/Gambar-Kucing-1.jpg")
//        posts.add(
//                ProfilePostViewModel(
//                        "Sadam Hussein",
//                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
//                        "1 dekade yang lalu",
//                        "",
//                        0,
//                        imageList,
//                        false,
//                        false
//                )
//        )
//
//        imageList = ArrayList()
//        imageList.add("http://www.mitratoday.com/wp-content/uploads/2018/04/0071d163288bf4141d4982af160ead8a.jpg")
//        posts.add(
//                ProfilePostViewModel(
//                        "Sadam Hussein",
//                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
//                        "3 abad tahun yang lalu",
//                        "Main gak ya?",
//                        0,
//                        imageList,
//                        false,
//                        true
//                )
//        )
//
//        imageList = ArrayList()
//        imageList.add("https://www.biologicaldiversity.org/publications/earthonline/eeo_images/826-840/828/RedWolf_AmeliaBeadish_Flickr.jpg")
//        imageList.add("http://www.untamedscience.com/science/wp-content/uploads/2016/08/redwolf.jpg")
//        imageList.add("https://onncg8dr7k-flywheel.netdna-ssl.com/wp-content/uploads/2016/07/10977060066_2533315a09_o_FIX.jpg")
//        posts.add(
//                ProfilePostViewModel(
//                        "Sadam Hussein",
//                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
//                        "5 miliar tahun yang lalu",
//                        "Yuk main yuk",
//                        0,
//                        imageList,
//                        false,
//                        true
//                )
//        )
        return Observable.just(posts)
    }

    private fun getQuota(): Observable<AffiliatePostQuota> {
        return if (userId.toString() == userSession.userId) {
            getAffiliateQuotaUseCase.createObservable(RequestParams.EMPTY).map(convertToPostQuota())
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
                    profile.isFollowed,
                    userId.toString() == userSession.userId
            )
        }
    }

    private fun convertToPostQuota(): Func1<GraphqlResponse, AffiliatePostQuota> {
        return Func1 { graphqlResponse ->
            val data: AffiliateQuotaData = graphqlResponse.getData(AffiliateQuotaData::class.java)
                    ?: AffiliateQuotaData()
            data.affiliatePostQuota
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