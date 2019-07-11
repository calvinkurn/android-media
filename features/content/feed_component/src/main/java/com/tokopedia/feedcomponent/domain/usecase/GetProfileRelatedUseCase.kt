package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.ProfileRelatedQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Provider

/**
 * Use case to get list of related other profiles given the activityId
 * ActivityId may be blank
 *
 * Example case: when user see the blank KOL profile, it will show other peoples' profile in list
 */
class GetProfileRelatedUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                   graphqlUseCaseProvider: Provider<GraphqlUseCase>)
    : UseCase<FeedPostRelated>() {

    val graphqlUseCase: GraphqlUseCase = graphqlUseCaseProvider.get()

    init {
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<FeedPostRelated> {
        val query: String = GraphqlHelper.loadRawString(
            context.resources,
            R.raw.query_profile_related
        )

        val graphqlRequest = GraphqlRequest(query, ProfileRelatedQuery::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
//        return graphqlUseCase.createObservable(requestParams)
//            .map {
//                val data: FeedPostRelated = (it.getData(ProfileRelatedQuery::class.java)
//                    as ProfileRelatedQuery).feedPostRelated
//                data
//            }
        return Observable.just(Gson().fromJson(
            """
                {"data":[{"id":"54814983","content":{"header":{"avatar":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2017/12/16/25686039/25686039_244788be-69af-4b33-8b1d-4c7694aa34ee.jpg","avatarTitle":"Roemah Inspirasi","avatarBadge":"https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/icon_kol_badge.png"},"body":{"media":[{"id":"","type":"image","applink":"tokopedia://content/54814983","thumbnail":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/21/25686039/25686039_eb5d5ebc-9804-4daf-ae82-dca8a798b5c1.png","thumbnailLarge":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/21/25686039/25686039_eb5d5ebc-9804-4daf-ae82-dca8a798b5c1.png"}],"caption":{"text":"Dapatkan produk mantap stainless straw disini, Toppers! Yuk mulai gaya hidup ramah lingkungan sekarang!","applink":"tokopedia://content/54814983"}},"tracking":{"authorID":"25686039"}}},{"id":"58595225","content":{"header":{"avatar":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2019/4/10/4937388/4937388_985a48c9-5fe0-4ae8-acd8-71c37a6905fe","avatarTitle":"Faiz Sadad","avatarBadge":"https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/icon_kol_badge.png"},"body":{"media":[{"id":"","type":"image","applink":"tokopedia://content/58595225","thumbnail":"https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/23/33033823/33033823_26378a4f-5cef-46ab-8838-b76704cf2073_1280_1706.jpg","thumbnailLarge":"https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/23/33033823/33033823_26378a4f-5cef-46ab-8838-b76704cf2073_1280_1706.jpg"}],"caption":{"text":"","applink":"tokopedia://content/58595225"}},"tracking":{"authorID":"4937388"}}},{"id":"55089708","content":{"header":{"avatar":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2019/4/20/22127319/22127319_4bfedb50-6175-40b9-a2a3-b23eca27c300","avatarTitle":"Muhammad Arief","avatarBadge":"https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/icon_kol_badge.png"},"body":{"media":[{"id":"","type":"image","applink":"tokopedia://content/55089708","thumbnail":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/24/22127319/22127319_6c76054c-8f8e-4ac0-a80c-8236c45dd73d.png","thumbnailLarge":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/24/22127319/22127319_6c76054c-8f8e-4ac0-a80c-8236c45dd73d.png"}],"caption":{"text":"Guys kalo gue riding pastinya selalu butuh jaket dan sarung tangan, nah bagi kalian yang mau samaan kaya gue langsung click item dibawah ini ya!!","applink":"tokopedia://content/55089708"}},"tracking":{"authorID":"22127319"}}},{"id":"54386226","content":{"header":{"avatar":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2019/3/30/51615730/51615730_fb540b40-656b-4003-a68c-e202b42f5895.jpeg","avatarTitle":"Feed Tokopedia","avatarBadge":"https://ecs7.tokopedia.net/assets-tokopedia-lite/prod/icon_kol_badge.png"},"body":{"media":[{"id":"","type":"image","applink":"tokopedia://content/54386226","thumbnail":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/18/51615730/51615730_64215d7f-2f7f-42e1-b57e-3e506a8aec9c.png","thumbnailLarge":"https://ecs7.tokopedia.net/img/cache/700/attachment/2019/6/18/51615730/51615730_64215d7f-2f7f-42e1-b57e-3e506a8aec9c.png"}],"caption":{"text":"Ternyata cafe 24 jam menjadi tempat pilihan untuk mengerjakan tugas bagi sebagian besar mahasiswa. Kalau kamu lebih suka ngerjain tugas kuliah di mana sih, Toppers? Komen di bawah, dong!\n\nSupaya ngerjain tugasnya lebih lancar, cek perlengkapan bekerja & belajar di sini.","applink":"tokopedia://content/54386226"}},"tracking":{"authorID":"51615730"}}}],"meta":{"totalItems":6}}
            """.trimIndent(),
            FeedPostRelated::class.java))
    }

    companion object {
        const val PARAM_ACTIVITY_ID = "activityID"
        @JvmOverloads
        fun createRequestParams(activityId: String?):
            RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_ACTIVITY_ID, activityId ?: "")
            return requestParams
        }
    }
}