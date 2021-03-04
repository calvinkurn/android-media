package com.tokopedia.recentview.data.mapper

import com.tokopedia.recentview.data.entity.Badge
import com.tokopedia.recentview.data.entity.Label
import com.tokopedia.recentview.data.entity.RecentViewData
import com.tokopedia.recentview.view.viewmodel.LabelsDataModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import java.util.*

/**
 * @author by yoasfs on 25/08/20
 */
class RecentViewMapper {
    companion object{
        private const val OFFICIAL_STORE = "Official Store"
    }

    fun convertToViewModel(productItemData: RecentViewData): ArrayList<RecentViewDetailProductDataModel> {
        val listProduct = ArrayList<RecentViewDetailProductDataModel>()
        var position = 1
        for (domain in productItemData.list) {
            listProduct.add(RecentViewDetailProductDataModel(
                    productId = domain.id,
                    name = domain.name,
                    price = domain.price,
                    imageSource =  domain.imgUri,
                    labels = convertLabels(domain.labels) ?: listOf(),
                    isFreeReturn = false,
                    isWishlist = domain.wishlist,
                    productLink = domain.productUrl,
                    rating = if (domain.rating.isNotEmpty()) domain.rating.toInt() else "0".toInt(),
                    isGold = domain.isNewGold == 1,
                    isOfficial = convertToIsOfficial(domain.badges),
                    shopName = domain.shop,
                    shopLocation = domain.shop_location,
                    positionForRecentViewTracking = position
            ))
            position++
        }
        return listProduct
    }

    private fun convertLabels(labels: List<Label>): List<LabelsDataModel>? {
        val labelsDataModels: MutableList<LabelsDataModel> = ArrayList()
        for (labelDomain in labels) {
            labelsDataModels.add(LabelsDataModel(labelDomain.title,
                    labelDomain.color))
        }
        return labelsDataModels
    }

    private fun convertToIsOfficial(badges: List<Badge>): Boolean {
        if (badges.isNotEmpty()) {
            for (domain in badges) {
                if (domain.title.contains(OFFICIAL_STORE)) {
                    return true
                }
            }
        }
        return false
    }
}