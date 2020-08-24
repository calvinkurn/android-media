package com.tokopedia.recentview.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.recentview.data.entity.Badge
import com.tokopedia.recentview.data.entity.Label
import com.tokopedia.recentview.data.entity.ProductItem
import com.tokopedia.recentview.data.entity.RecentViewData
import com.tokopedia.recentview.data.query.RecentViewQuery
import com.tokopedia.recentview.domain.model.RecentViewBadgeDomain
import com.tokopedia.recentview.domain.model.RecentViewLabelDomain
import com.tokopedia.recentview.domain.model.RecentViewProductDomain
import com.tokopedia.recentview.domain.model.RecentViewShopDomain
import com.tokopedia.recentview.view.viewmodel.LabelsViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.util.*

/**
 * @author by yoasfs on 13/08/20
 */
class RecentViewUseCase (
        private val graphqlRepository: GraphqlRepository
): UseCase<ArrayList<RecentViewDetailProductViewModel>>() {

    private val params = RequestParams.create()

    private val CASHBACK = "Cashback"
    private val OFFICIAL_STORE = "Official Store"

    override suspend fun executeOnBackground(): ArrayList<RecentViewDetailProductViewModel> {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val request = GraphqlRequest(
                RecentViewQuery.getQuery(),
                RecentViewData::class.java,
                params.parameters
        )
        val response = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        response.getError(RecentViewData::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    throw Throwable(it[0].message)
                }
            }
        }

        val data = response.getData<RecentViewData>(RecentViewData::class.java)
        return convertToViewModel(data)
    }

    fun getParam(loginID: String) {
        params.parameters.clear()
        params.putInt(GetRecentViewUseCase.PARAM_USER_ID, loginID.toInt())
    }

    private fun convertToViewModel(productItemData: RecentViewData): ArrayList<RecentViewDetailProductViewModel> {
        val listProduct = ArrayList<RecentViewDetailProductViewModel>()
        var position = 1
        for (domain in productItemData.list) {
            listProduct.add(RecentViewDetailProductViewModel(domain.id.toInt(),
                    domain.name,
                    domain.price,
                    domain.imgUri,
                    convertLabels(domain.labels),
                    false,
                    domain.wishlist,
                    if (domain.rating != null) domain.rating.toInt() else "0".toInt(),
                    domain.isGold != null && domain.isGold == "1",
                    convertToIsOfficial(domain.badges),
                    domain.shop,
                    domain.shop_location,
                    position
            ))
            position++
        }
        return listProduct
    }

    private fun convertLabels(labels: List<Label>): List<LabelsViewModel>? {
        val labelsViewModels: MutableList<LabelsViewModel> = ArrayList()
        for (labelDomain in labels) {
            labelsViewModels.add(LabelsViewModel(labelDomain.title,
                    labelDomain.color))
        }
        return labelsViewModels
    }

    private fun convertToIsOfficial(badges: List<Badge>): Boolean {
        if (!badges.isEmpty()) {
            for (domain in badges) {
                if (domain.title.contains(OFFICIAL_STORE)) {
                    return true
                }
            }
        }
        return false
    }

}