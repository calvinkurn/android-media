package com.tokopedia.officialstore.official.presentation

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

class OfficialStoreConfig(private val remoteConfig: RemoteConfig) {
    fun isEligibleForDisableShopWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_SHOP_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableBestSellerWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_BEST_SELLER_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableMappingBanner(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_BANNERS)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableRemoveBestSellerWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_REMOVE_BEST_SELLER_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableRemoveShopWidget(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_REMOVE_SHOP_WIDGET)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableMappingBenefit(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_BENEFIT)
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun isEligibleForDisableMappingOfficialFeaturedShop(): Boolean {
        return try {
            return remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_OFFICIAL_STORE_MAPPING_OFFICIAL_FEATURED_SHOP)
                ?: false
        } catch (e: Exception) {
            false
        }
    }
}