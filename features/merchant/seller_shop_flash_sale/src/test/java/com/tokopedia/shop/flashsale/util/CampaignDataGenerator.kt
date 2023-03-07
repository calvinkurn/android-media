package com.tokopedia.shop.flashsale.util

import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction.Submit
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction.Update
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner.Shop as BannerShop
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner.Product as BannerProduct
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase.Param
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel.PackageInfo
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel.ThematicInfo
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel.ProductSummary
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC.TncRequest
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.Product
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.ProductMapData
import com.tokopedia.shop.flashsale.domain.entity.ShopInfo
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignCreationEligibility
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import java.util.Date

object CampaignDataGenerator {
    fun generateCampaignUiModel(
        isUniqueBuyer: Boolean = true,
        isCampaignRuleSubmit: Boolean = true,
        isCampaignRelation: Boolean = true,
        thematicParticipation: Boolean = true,
        isCancellable: Boolean = true,
        status: CampaignStatus = CampaignStatus.READY,
        relatedCampaigns: List<RelatedCampaign> = emptyList(),
        upcomingDate: Date = Date()
    ) = CampaignUiModel(
        campaignId = 1001,
        campaignName = "Flash Sale Toko",
        endDateFormatted = "",
        endTime = "",
        isCancellable = isCancellable,
        isShareable = true,
        notifyMeCount = 0,
        startDateFormatted = "",
        startTime = "",
        status = status,
        thematicParticipation = thematicParticipation,
        summary = ProductSummary(0, 0, 0, 0, 0, 0),
        startDate = Date(),
        endDate = Date(),
        gradientColor = Gradient("", "", true),
        useUpcomingWidget = true,
        upcomingDate = upcomingDate,
        paymentType = PaymentType.REGULAR,
        isUniqueBuyer = isUniqueBuyer,
        isCampaignRelation = isCampaignRelation,
        relatedCampaigns = relatedCampaigns,
        isCampaignRuleSubmit = isCampaignRuleSubmit,
        relativeTimeDifferenceInMinute = 100,
        thematicInfo = ThematicInfo(0, 0, "", 0, ""),
        reviewStartDate = Date(),
        reviewEndDate = Date(),
        packageInfo = PackageInfo(
            packageId = 0,
            packageName = "VPS"
        )
    )

    fun generateRelatedCampaigns(campaignId: Long = 1001): List<RelatedCampaign> = listOf(
        RelatedCampaign(campaignId, "Flash Sale Toko Kelontong")
    )

    fun generateCampaignCreationParam(
        paymentType: PaymentType?,
        isTNCConfirmed: Boolean,
        isUniqueBuyer: Boolean?,
        isCampaignRelation: Boolean?,
        relatedCampaigns: List<RelatedCampaign>? = null,
        campaignData: CampaignUiModel = generateCampaignUiModel(),
        isUpdate: Boolean = true
    ): Param = Param(
        action = if (isUpdate) Update(campaignData.campaignId) else Submit(campaignData.campaignId),
        campaignName = campaignData.campaignName,
        scheduledStart = campaignData.startDate.removeTimeZone(),
        scheduledEnd = campaignData.endDate.removeTimeZone(),
        teaserDate = campaignData.upcomingDate.removeTimeZone(),
        firstColor = campaignData.gradientColor.first,
        secondColor = campaignData.gradientColor.second,
        campaignRelation = when {
            isUniqueBuyer == true -> emptyList()
            isCampaignRelation == false -> relatedCampaigns?.map { it.id }
                ?.plus(campaignData.campaignId) ?: emptyList()
            else -> listOf(campaignData.campaignId)
        },
        paymentType = paymentType ?: campaignData.paymentType,
        isCampaignRuleSubmit = isTNCConfirmed,
        showTeaser = campaignData.useUpcomingWidget,
        packageId = campaignData.packageInfo.packageId
    )

    fun generateCampaignCreationResult(
        campaignId: Long = generateCampaignUiModel().campaignId,
        isSuccess: Boolean = true,
        totalProductFailed: Int = 0,
        errorDescription: String = "Belum memenuhi kriteria",
        errorTitle: String = "Gagal diproses",
        errorMessage: String = "Produk ditolak",
    ): CampaignCreationResult = CampaignCreationResult(
        campaignId = campaignId,
        isSuccess = isSuccess,
        totalProductFailed = totalProductFailed,
        errorDescription = errorDescription,
        errorTitle = errorTitle,
        errorMessage = errorMessage
    )

    fun generateCampaignDetailMetaData(
        status: CampaignStatus = CampaignStatus.READY,
        isCancellable: Boolean = true,
        thematicParticipation: Boolean = true,
        campaignData: CampaignUiModel = generateCampaignUiModel(
            status = status,
            thematicParticipation = thematicParticipation,
            isCancellable = isCancellable
        ),
        productList: SellerCampaignProductList = generateProductList()
    ): CampaignDetailMeta = CampaignDetailMeta(
        campaign = campaignData,
        productList = productList
    )

    fun generateProductList(
        success: Boolean = false,
        errorMessage: List<String> = listOf(),
        totalProduct: Int = 0,
        totalProductSold: Int = 0,
        countAcceptedProduct: Int = 0,
        totalProductQty: Int = 0,
        totalIncome: Int = 0,
        totalIncomeFormatted: String = "",
        productFailedCount: Int = 0,
        isError: Boolean = false
    ): SellerCampaignProductList = SellerCampaignProductList(
        success = success,
        errorMessage = errorMessage,
        productList = if (isError) generateErrorProduct() else generateCompleteProduct(),
        totalProduct = totalProduct,
        totalProductSold = totalProductSold,
        countAcceptedProduct = countAcceptedProduct,
        totalProductQty = totalProductQty,
        totalIncome = totalIncome,
        totalIncomeFormatted = totalIncomeFormatted,
        productFailedCount = productFailedCount
    )

    fun generateCompleteProduct() = listOf(
        Product(
            productMapData = ProductMapData(
                originalPrice = 100000,
                discountedPrice = 80000,
                discountPercentage = 20,
                customStock = 90,
                originalCustomStock = 90,
                originalStock = 100,
                campaignSoldCount = 10,
                maxOrder = 90
            )
        )
    )

    fun generateErrorProduct() = listOf(
        Product(
            productMapData = ProductMapData(
                originalPrice = 100000,
                discountedPrice = 80000,
                discountPercentage = 20,
                customStock = 90,
                originalCustomStock = 90,
                originalStock = 100,
                maxOrder = 110
            )
        )
    )

    fun generateShareComponentMetaData(
        banner: CampaignBanner = generateCampaignBanner(),
        shopInfo: ShopInfo = ShopInfo(true, true),
    ): ShareComponentMetadata = ShareComponentMetadata(
        banner = banner,
        shop = shopInfo
    )

    fun generateCampaignBanner(
        campaignId: Long = 1001,
        campaignName: String = "Flash Sale Toko",
        products: List<BannerProduct> = listOf(
            BannerProduct(
                imageUrl = "https://images.tokopedia.net/img/cache/500-square/hDjmkQ/2022/9/26/894a8962-6c1c-411b-a8c9-5e50230c8529.jpg.webp?ect=4g",
                originalPrice = "Rp 100.000",
                discountedPrice = "Rp 80.000",
                discountPercentage = 20
            )
        ),
        maxDiscountPercentage: Int = 50,
        campaignStatusId: Int = 0,
        shop: BannerShop = BannerShop(
            name = "Flash Sale Toko",
            domain = "https://tokopedia/tokopedia",
            logo = "https://images.tokopedia.net/img/seller_no_logo_3.png",
            isGold = true,
            isOfficial = true,
            badgeImageUrl = "https://images.tokopedia.net/img/official_store/badge_os.png"
        ),
        startDate: Date = Date(),
        endDate: Date = Date()
    ): CampaignBanner = CampaignBanner(
        campaignId = campaignId,
        campaignName = campaignName,
        products = products,
        maxDiscountPercentage = maxDiscountPercentage,
        campaignStatusId = campaignStatusId,
        shop = shop,
        startDate = startDate,
        endDate = endDate
    )

    fun generateCampaignCreationEligibility(
        remainingQuota: Int = 10,
        isEligible: Boolean = true
    ): CampaignCreationEligibility =
        CampaignCreationEligibility(remainingQuota = remainingQuota, isEligible = isEligible)

    fun generateMerchantCampaignTNCRequest(
        campaignId: Long = 1001,
        isUniqueBuyer: Boolean = true,
        isCampaignRelation: Boolean = true,
        paymentType: PaymentType = PaymentType.REGULAR,
    ): TncRequest = TncRequest(
        campaignId = campaignId,
        isUniqueBuyer = isUniqueBuyer,
        isCampaignRelation = isCampaignRelation,
        paymentType = paymentType
    )
}

