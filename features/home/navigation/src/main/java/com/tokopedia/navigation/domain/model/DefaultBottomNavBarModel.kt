package com.tokopedia.navigation.domain.model

import com.tokopedia.navigation.presentation.model.BottomNavDiscoType
import com.tokopedia.navigation.presentation.model.BottomNavFeedType
import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation.presentation.model.BottomNavOfficialStoreId
import com.tokopedia.navigation.presentation.model.BottomNavTransactionType
import com.tokopedia.navigation.presentation.model.BottomNavWishlistType
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Key
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Type
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Variant
import com.tokopedia.navigation_common.ui.BottomNavBarJumper
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import com.tokopedia.navigation.R as navigationR

internal val defaultBottomNavModel = listOf(
    BottomNavBarUiModel(
        id = 1,
        title = "Home",
        type = BottomNavHomeType,
        jumper = BottomNavBarJumper(
            id = 1,
            title = "Buat Kamu",
            assets = mapOf(
                (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_enabled),
                (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_enabled),

                (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_home_to_thumb),
                (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_thumb_to_home),
                (Key.AnimIdle + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_thumb_idle),
                (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_home_to_thumb),
                (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_thumb_to_home),
                (Key.AnimIdle + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_thumb_idle),
            )
        ),
        assets = mapOf(
            (Key.ImageActive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_active),
            (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_enabled),
            (Key.ImageActive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_active),
            (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_home_enabled),

            (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_home),
            (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_home_to_enabled),
            (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_home_dark),
            (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_home_to_enabled_dark),
        ),
        discoId = DiscoId.Empty,
        queryParams = "",
    ),
    BottomNavBarUiModel(
        id = 2,
        title = "Feed",
        type = BottomNavFeedType,
        jumper = null,
        assets = mapOf(
            (Key.ImageActive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_feed_active),
            (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_feed_enabled),
            (Key.ImageActive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_feed_active),
            (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_feed_enabled),

            (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_feed),
            (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_feed_to_enabled),
            (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_feed_dark),
            (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_feed_to_enabled_dark),
        ),
        discoId = DiscoId.Empty,
        queryParams = "",
    ),
    BottomNavBarUiModel(
        id = 3,
        title = "Official Store",
        type = BottomNavDiscoType,
        jumper = null,
        assets = mapOf(
            (Key.ImageActive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_os_active),
            (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_os_enabled),
            (Key.ImageActive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_os_active),
            (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_os_enabled),

            (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_official),
            (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_os_to_enabled),
            (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_official_dark),
            (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_os_to_enabled_dark),
        ),
        discoId = BottomNavOfficialStoreId.discoId,
        queryParams = "ref=bottomnavigation"
    ),
    BottomNavBarUiModel(
        id = 4,
        title = "Wishlist",
        type = BottomNavWishlistType,
        jumper = null,
        assets = mapOf(
            (Key.ImageActive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_wishlist_active),
            (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_wishlist_enabled),
            (Key.ImageActive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_wishlist_active),
            (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_wishlist_enabled),

            (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_wishlist),
            (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_wishlist_to_enabled),
            (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_wishlist_dark),
            (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_wishlist_to_enabled_dark),
        ),
        discoId = DiscoId.Empty,
        queryParams = ""
    ),
    BottomNavBarUiModel(
        id = 5,
        title = "Transaksi",
        type = BottomNavTransactionType,
        jumper = null,
        assets = mapOf(
            (Key.ImageActive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_uoh_active),
            (Key.ImageInactive + Variant.Light) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_uoh_enabled),
            (Key.ImageActive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_uoh_active),
            (Key.ImageInactive + Variant.Dark) to Type.ImageRes(navigationR.drawable.ic_bottom_nav_uoh_enabled),

            (Key.AnimActive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_transaction),
            (Key.AnimInactive + Variant.Light) to Type.LottieRes(navigationR.raw.bottom_nav_transaction_to_enabled),
            (Key.AnimActive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_transaction_dark),
            (Key.AnimInactive + Variant.Dark) to Type.LottieRes(navigationR.raw.bottom_nav_transaction_to_enabled_dark),
        ),
        discoId = DiscoId.Empty,
        queryParams = ""
    ),
)

