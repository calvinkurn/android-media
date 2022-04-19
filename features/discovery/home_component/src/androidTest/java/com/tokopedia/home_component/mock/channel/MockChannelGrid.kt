package com.tokopedia.home_component.mock.channel

import com.tokopedia.home_component.model.*

object MockChannelGrid {
    fun get(): List<ChannelGrid> {
        val channelGrid = ChannelGrid(
                id = "1",
                warehouseId = "1",
                minOrder = 1,
                price = "Rp 389.350",
                imageUrl = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2020/3/22/1579059/1579059_86946c79-26f2-40da-a7c3-a6286936345f_700_700",
                name = "Mock Product",
                applink = "tokopedia://home",
                url = "https://www.tokopedia.com/",
                discount = "35% OFF",
                slashedPrice = "Rp 599.000",
                label = "Label",
                soldPercentage = 50,
                attribution = "",
                impression = "",
                cashback = "",
                productClickUrl = "",
                isTopads = true,
                productViewCountFormatted = "500",
                isOutOfStock = false,
                isFreeOngkirActive = true,
                shopId = "",
                labelGroup = listOf(
                        LabelGroup("Bisa Tukar Tambah", "offers", "lightGrey")
                ),
                hasBuyButton = true,
                rating = 5,
                countReview = 1000,
                backColor = "",
                benefit = ChannelBenefit(
                        type = "Mulai Dari",
                        value = "Rp 50k"
                )
        )
        val gridList = mutableListOf<ChannelGrid>()
        for (i in 0..20) {
            gridList.add(channelGrid)
        }
        return gridList
    }
}