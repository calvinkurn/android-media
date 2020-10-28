package com.tokopedia.recentview.data.mapper

import com.tokopedia.recentview.data.entity.Badge
import com.tokopedia.recentview.data.entity.Label
import com.tokopedia.recentview.data.entity.RecentViewData
import com.tokopedia.recentview.view.viewmodel.LabelsViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel
import java.util.ArrayList

/**
 * @author by yoasfs on 25/08/20
 */
class RecentViewMapper {
    private val CASHBACK = "Cashback"
    private val OFFICIAL_STORE = "Official Store"

    fun convertToViewModel(productItemData: RecentViewData): ArrayList<RecentViewDetailProductViewModel> {
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
                    if (domain.rating.isNotEmpty()) domain.rating.toInt() else "0".toInt(),
                    domain.isNewGold == 1,
                    convertToIsOfficial(domain.badges),
                    domain.shop,
                    domain.shop_location,
                    position
            ))
            position++
        }
        return listProduct
    }

    fun convertLabels(labels: List<Label>): List<LabelsViewModel>? {
        val labelsViewModels: MutableList<LabelsViewModel> = ArrayList()
        for (labelDomain in labels) {
            labelsViewModels.add(LabelsViewModel(labelDomain.title,
                    labelDomain.color))
        }
        return labelsViewModels
    }

    fun convertToIsOfficial(badges: List<Badge>): Boolean {
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