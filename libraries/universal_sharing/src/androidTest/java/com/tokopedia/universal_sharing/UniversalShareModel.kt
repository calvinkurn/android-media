package com.tokopedia.universal_sharing

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.swap
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.model.PersonalizedCampaignModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ChipProperties
import com.tokopedia.universal_sharing.view.model.LinkProperties

object UniversalShareModel {

    fun getShopBottomSheet(): UniversalShareBottomSheet {
        return UniversalShareBottomSheet().apply {
            setImageOnlySharingOption(true) // hide option to share via sms
            setMetaData(
                "Unilever Official Store",
                "https://images.tokopedia.net/img/cache/215-square/shops-1/2020/6/15/1854168/1854168_da5f48be-6075-4a68-a820-0eb6e6587e65.png"
            )
            enableDefaultShareIntent()
            val chips = ArrayList<ChipProperties>().apply {
                add(ChipProperties(1, "Halaman Toko", true, "", getLinkProperties("HomeTab")))
                add(ChipProperties(2, "Product", false, "", getLinkProperties("ProductTab")))
                add(ChipProperties(3, "Etalase", false, "", getLinkProperties("EtalaseTab")))
                add(ChipProperties(4, "Feed", false, "", getLinkProperties("FeedTab")))
                add(ChipProperties(5, "Review", false, "", getLinkProperties("ReviewTab")))
            }
            val targetIndex = Int.ZERO
            chips.swap(0, targetIndex)
            setChipList(chips)
        }
    }

    private fun getLinkProperties(tabName: String): LinkProperties {
        return LinkProperties(
            linkerType = LinkerData.SHOP_TYPE,
            ogTitle = "Unilever Official Store",
            ogImageUrl = String.EMPTY,
            ogDescription = "Jakarta",
            desktopUrl = "",
            id = "1854168",
            deeplink = Uri.parse(UriUtil.buildUri(ApplinkConst.SHOP, "1854168"))
                .buildUpon()
                .appendQueryParameter("tab", tabName).toString())
    }

    fun getPDPBottomSheetWith2ImagesOption(): UniversalShareBottomSheet {
        val imageList = ArrayList<String>().apply {
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/07beb08a-1d53-46d9-be79-460145c289c4.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/f7389697-944a-40ce-a29e-6e0608117453.jpg.webp")
        }
        return UniversalShareBottomSheet().apply {
            setMetaData(
                "Edifier W820NB - Active Noise Cancelling Bluetooth Stereo Headphones - Hitam",
                "https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/46f47190-d14a-476b-9ef4-ef7b3fb6777e.jpg.webp",
                "",
                imageList
            )
        }
    }

    fun getDefaultPDPBottomSheet(): UniversalShareBottomSheet {
        val imageList = ArrayList<String>().apply {
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/07beb08a-1d53-46d9-be79-460145c289c4.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/f7389697-944a-40ce-a29e-6e0608117453.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/0677a39a-7bc4-4557-bc3c-2bb9ef7c3406.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/8278f80d-e519-4f8b-9c32-cac53023393f.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/051104f7-343d-4013-95f5-bae311cde5e4.jpg.webp")
            add("https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/46f47190-d14a-476b-9ef4-ef7b3fb6777e.jpg.webp")
        }
        return UniversalShareBottomSheet().apply {
            setMetaData(
                "Edifier W820NB - Active Noise Cancelling Bluetooth Stereo Headphones - Hitam",
                "https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/46f47190-d14a-476b-9ef4-ef7b3fb6777e.jpg.webp",
                "",
                imageList
            )
            setPersonalizedCampaign(
                PersonalizedCampaignModel(
                    "Diskon Terbatas",
                    "",
                    "829000",
                    false,
                    50F,
                    0L,
                    1699093076
                )
            )
            getImageFromMedia(true)
            setMediaPageSourceId(ImageGeneratorConstants.ImageGeneratorSourceId.AB_TEST_PDP)
            enableDefaultShareIntent()
            setImageGeneratorParam(PdpParamModel())
            setLinkProperties(LinkProperties(
                linkerType = LinkerData.PRODUCT_TYPE,
                ogImageUrl = "https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/1/46f47190-d14a-476b-9ef4-ef7b3fb6777e.jpg.webp"
            ))
        }
    }
}
