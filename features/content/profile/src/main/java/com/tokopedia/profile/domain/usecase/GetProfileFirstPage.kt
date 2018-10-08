package com.tokopedia.profile.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profile.data.pojo.profileheader.Profile
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.profile.view.viewmodel.ProfilePostViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileFirstPage @Inject constructor(val getProfileHeaderUseCase: GetProfileHeaderUseCase,
                                              val getProfilePostUseCase: GetProfilePostUseCase)
    : UseCase<ProfileFirstPageViewModel>() {

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ProfileFirstPageViewModel>? {
        val userId: String = requestParams?.getString(GetProfileHeaderUseCase.PARAM_USER_ID, "")
                ?: ""
        return Observable.zip(
                getHeader(userId),
                getPost()) { header: ProfileHeaderViewModel, posts: List<ProfilePostViewModel> ->
            ProfileFirstPageViewModel(header, posts)
        }
    }

    private fun getHeader(userId: String): Observable<ProfileHeaderViewModel> {
        return getProfileHeaderUseCase
                .createObservable(GetProfileHeaderUseCase.createRequestParams(userId))
                .map(convertToHeader())
    }

    private fun getPost(): Observable<List<ProfilePostViewModel>> {
        //TODO milhamj
        val posts: ArrayList<ProfilePostViewModel> = ArrayList()
        var imageList: ArrayList<String> = ArrayList()
        imageList.add("https://kucingpedia.com/wp-content/uploads/2017/11/cara-mengobati-kucing-yang-demam.jpg")
        imageList.add("http://www.redefiningamber.com/wp-content/uploads/2017/10/Gambar-Kucing-1.jpg")
        posts.add(
                ProfilePostViewModel(
                        "Sadam Hussein",
                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
                        "1 dekade yang lalu",
                        "",
                        0,
                        imageList,
                        false,
                        false
                )
        )

        imageList = ArrayList()
        imageList.add("http://www.mitratoday.com/wp-content/uploads/2018/04/0071d163288bf4141d4982af160ead8a.jpg")
        posts.add(
                ProfilePostViewModel(
                        "Sadam Hussein",
                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
                        "3 abad tahun yang lalu",
                        "Main gak ya?",
                        0,
                        imageList,
                        false,
                        true
                )
        )

        imageList = ArrayList()
        imageList.add("https://www.biologicaldiversity.org/publications/earthonline/eeo_images/826-840/828/RedWolf_AmeliaBeadish_Flickr.jpg")
        imageList.add("http://www.untamedscience.com/science/wp-content/uploads/2016/08/redwolf.jpg")
        imageList.add("https://onncg8dr7k-flywheel.netdna-ssl.com/wp-content/uploads/2016/07/10977060066_2533315a09_o_FIX.jpg")
        posts.add(
                ProfilePostViewModel(
                        "Sadam Hussein",
                        "https://ca.slack-edge.com/T038RGMSP-U7RNUK482-a8a9c33ca937-1024",
                        "5 miliar tahun yang lalu",
                        "Yuk main yuk",
                        0,
                        imageList,
                        false,
                        true
                )
        )
        return Observable.just(posts)
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
                    0,
                    profile.isKol,
                    profile.isFollowed,
                    false
            )
        }
    }
}