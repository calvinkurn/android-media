package com.tokopedia.recentview.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
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
        return convertToViewModel(getProductsFromResponse(data))
    }

    fun getParam(loginID: String?) {
        params.parameters.clear()
        params.putString(GetRecentViewUseCase.PARAM_USER_ID, loginID)
    }

    private fun getProductsFromResponse(productItemData: RecentViewData): List<RecentViewProductDomain> {
        val results: MutableList<RecentViewProductDomain> = ArrayList()
        for (productItem in productItemData.list) {
            results.add(RecentViewProductDomain(
                    getShopFromResponse(productItem),
                    productItem.getId(),
                    productItem.getName(),
                    productItem.getPrice(),
                    productItem.getImgUri(), productItem.getIsNewGold().toString(),
                    getProductLabelFromResponse(productItem.getLabels()),
                    productItem.getRating(),
                    productItem.getReviewCount(),
                    productItem.getFree_return(),
                    getProductBadgeFromResponse(productItem),
                    productItem.getWholesale(),
                    productItem.getPreorder(),
                    productItem.wishlist))
        }
        return results
    }

    private fun getProductLabelFromResponse(labels: List<Label>): List<RecentViewLabelDomain>? {
        val list: MutableList<RecentViewLabelDomain> = ArrayList()
        for (label in labels) {
            list.add(RecentViewLabelDomain(label.title, label.color))
        }
        return list
    }

    private fun getShopFromResponse(productItem: ProductItem): RecentViewShopDomain? {
        return RecentViewShopDomain(productItem.shopId.toString(),
                productItem.getShop(),
                productItem.getIsGold(),
                productItem.getLuckyShop(),
                productItem.getShop_location())
    }


    private fun getProductBadgeFromResponse(productItem: ProductItem): List<RecentViewBadgeDomain>? {
        val badgeList: MutableList<RecentViewBadgeDomain> = ArrayList()
        for (badgeResponse in productItem.getBadges()) {
            badgeList.add(RecentViewBadgeDomain(badgeResponse.title, badgeResponse.imageUrl))
        }
        return badgeList
    }

    private fun convertToViewModel(recentViewProductDomains: List<RecentViewProductDomain>): ArrayList<RecentViewDetailProductViewModel> {
        val listProduct = ArrayList<RecentViewDetailProductViewModel>()
        var position = 1
        for (domain in recentViewProductDomains) {
            listProduct.add(RecentViewDetailProductViewModel(domain.id.toInt(),
                    domain.name,
                    domain.price,
                    domain.imgUri,
                    convertLabels(domain.labels),
                    domain.freeReturn != null && domain.freeReturn == "1",
                    domain.wishlist,
                    if (domain.rating != null) domain.rating.toInt() else "0".toInt(),
                    domain.isGold != null && domain.isGold == "1",
                    convertToIsOfficial(domain.badges),
                    domain.shop.name,
                    domain.shop.location,
                    position
            ))
            position++
        }
        return listProduct
    }

    private fun convertLabels(labels: List<RecentViewLabelDomain>): List<LabelsViewModel>? {
        val labelsViewModels: MutableList<LabelsViewModel> = ArrayList()
        for (labelDomain in labels) {
            labelsViewModels.add(LabelsViewModel(labelDomain.title,
                    labelDomain.color))
        }
        return labelsViewModels
    }

    private fun convertToIsOfficial(badges: List<RecentViewBadgeDomain>): Boolean {
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