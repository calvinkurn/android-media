package com.tokopedia.tokofood.feature.merchant.common.util

import com.tokopedia.imageassets.ImageUrl

import android.app.Activity
import android.content.Context
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantShareComponent
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel

class MerchantShareComponentUtil(
    val context: Context?
) {

    private fun getLinkerDataMapper(merchantShareComponent: MerchantShareComponent): LinkerShareData {
        val linkerData = LinkerData().apply {
            id = merchantShareComponent.id
            name = merchantShareComponent.pageName
            uri = merchantShareComponent.shareLinkUrl
            description = merchantShareComponent.txtDesc
            ogDescription = merchantShareComponent.ogDesc
            isThrowOnError = true
            type = LinkerData.FOOD_TYPE
            deepLink = merchantShareComponent.shareLinkDeepLink
        }
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    fun createShareRequest(
        shareModel: ShareModel,
        merchantShareComponent: MerchantShareComponent,
        activity: Activity?,
        view: View?,
        onSuccess: () -> Unit = {}
    ) {
        val linkerShareData = getLinkerDataMapper(merchantShareComponent)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotBlank() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(Int.ZERO, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = String.format(
                        SHARE_STR_FMT,
                        merchantShareComponent.previewTitle,
                        linkerShareData?.shareUri
                    )
                    SharingUtil.executeShareIntent(
                        shareModel,
                        linkerShareData,
                        activity,
                        view,
                        shareString
                    )
                    onSuccess.invoke()
                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )
    }

    fun getMerchantShareComponent(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?,
        merchantId: String
    ): MerchantShareComponent {
        return MerchantShareComponent(
            id = merchantId,
            pageName = ShareComponentConstants.TOKOFOOD,
            previewTitle = tokoFoodMerchantProfile?.name.orEmpty(),
            previewThumbnail = tokoFoodMerchantProfile?.imageURL ?: MERCHANT_THUMBNAIL_URL,
            txtDesc = context?.getString(
                com.tokopedia.tokofood.R.string.desc_merchant_page_share,
                tokoFoodMerchantProfile?.name.orEmpty()
            ).orEmpty(),
            shareLinkUrl = ShareComponentConstants.Merchant.MERCHANT_URL,
            shareLinkDeepLink = UriUtil.buildUri(
                ApplinkConst.TokoFood.MERCHANT, mapOf(
                    "{"+ShareComponentConstants.Merchant.MERCHANT_ID+"}" to merchantId
                )
            ),
            ogTitle = "${tokoFoodMerchantProfile?.name.orEmpty()}, ${tokoFoodMerchantProfile?.address.orEmpty()} | ${ShareComponentConstants.TOKOPEDIA}",
            ogDesc = context?.getString(
                com.tokopedia.tokofood.R.string.og_desc_merchant_page_share,
                tokoFoodMerchantProfile?.name.orEmpty()
            ).orEmpty(),
            ogImage = tokoFoodMerchantProfile?.imageURL ?: MERCHANT_THUMBNAIL_URL
        )
    }

    fun getFoodShareComponent(
        tokoFoodMerchantProfile: TokoFoodMerchantProfile?,
        productUiModel: ProductUiModel,
        merchantId: String
    ): MerchantShareComponent {
        return MerchantShareComponent(
            id = productUiModel.id,
            pageName = ShareComponentConstants.TOKOFOOD,
            previewTitle = productUiModel.name,
            previewThumbnail = productUiModel.imageURL,
            txtDesc = context?.getString(
                com.tokopedia.tokofood.R.string.desc_merchant_food_share,
                productUiModel.name,
                tokoFoodMerchantProfile?.name.orEmpty(),
                productUiModel.priceFmt
            ).orEmpty(),
            shareLinkUrl = ShareComponentConstants.Merchant.MERCHANT_URL,
            shareLinkDeepLink = TokoFoodMerchantUiModelMapper.getMerchantFoodAppLink(
                merchantId,
                productUiModel.id
            ),
            ogTitle = """"${productUiModel.name}" ${tokoFoodMerchantProfile?.name.orEmpty()} | ${ShareComponentConstants.TOKOPEDIA}""",
            ogDesc = context?.getString(
                com.tokopedia.tokofood.R.string.og_desc_merchant_food_share,
                productUiModel.name,
                tokoFoodMerchantProfile?.name.orEmpty()
            ).orEmpty(),
            ogImage = productUiModel.imageURL
        )
    }

    companion object {
        const val SHARE_STR_FMT = "%s %s"
        const val MERCHANT_THUMBNAIL_URL = ImageUrl.MERCHANT_THUMBNAIL_URL
    }
}