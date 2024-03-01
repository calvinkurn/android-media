package com.tokopedia.product.addedit.specification.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.product.addedit.R

enum class CertificationOnboardingEnum(val categoryId: String, @StringRes val onboardingMessageRes: Int) {
    ONBOARDING_ELECTRONIC("", R.string.message_onboarding_postel_number),
    ONBOARDING_ELECTRONIC_PHONE("", R.string.message_onboarding_postel_number),
    ONBOARDING_ELECTRONIC_COMPUTER("", R.string.message_onboarding_postel_number),

    ONBOARDING_KITCHEN("", R.string.message_onboarding_sni),
    ONBOARDING_KIDS_FASHION("", R.string.message_onboarding_sni),
    ONBOARDING_TOYS("", R.string.message_onboarding_sni),
    ONBOARDING_SPORT("", R.string.message_onboarding_sni),
    ONBOARDING_AUTOMOTIVE("", R.string.message_onboarding_sni),
    ONBOARDING_TOOLS("", R.string.message_onboarding_sni),
    ONBOARDING_HOME_APPLIANCE("", R.string.message_onboarding_sni),

    ONBOARDING_BEAUTY("", R.string.message_onboarding_bpom),
    ONBOARDING_FOOD_AND_BEVERAGE("", R.string.message_onboarding_bpom),
    ONBOARDING_BODY_CARE("", R.string.message_onboarding_bpom),

    ONBOARDING_PET_CARE("", R.string.message_onboarding_izin_edar_number),

    ONBOARDING_ELECTRONIC_GENERAL("", R.string.message_onboarding_sni_or_postel),
    ONBOARDING_HEALTH("", R.string.message_onboarding_health)
}
