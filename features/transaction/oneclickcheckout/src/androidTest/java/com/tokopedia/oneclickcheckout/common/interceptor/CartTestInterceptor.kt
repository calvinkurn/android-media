package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CartTestInterceptor : BaseOccInterceptor() {

    var customGetOccCartResponsePath: String? = null
    var customGetOccCartThrowable: IOException? = null

    var customUpdateCartOccResponsePath: String? = null
    var customUpdateCartOccThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_OCC_CART_QUERY)) {
            if (customGetOccCartThrowable != null) {
                throw customGetOccCartThrowable!!
            } else if (customGetOccCartResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetOccCartResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_OCC_CART_PAGE_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(UPDATE_CART_OCC_QUERY)) {
            if (customUpdateCartOccThrowable != null) {
                throw customUpdateCartOccThrowable!!
            } else if (customUpdateCartOccResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customUpdateCartOccResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(UPDATE_CART_OCC_SUCCESS_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetOccCartResponsePath = null
        customGetOccCartThrowable = null
        customUpdateCartOccResponsePath = null
        customUpdateCartOccThrowable = null
    }
}

const val GET_OCC_CART_QUERY = "get_occ_cart_page"
const val UPDATE_CART_OCC_QUERY = "update_cart_occ"

const val GET_OCC_CART_PAGE_DEFAULT_RESPONSE_PATH = "cart/get_occ_cart_page_default_response.json"
const val GET_OCC_CART_PAGE_NO_PROFILE_RESPONSE_PATH = "cart/get_occ_cart_page_no_profile_response.json"
const val GET_OCC_CART_PAGE_LAST_APPLY_RESPONSE_PATH = "cart/get_occ_cart_page_last_apply_response.json"
const val GET_OCC_CART_PAGE_LAST_APPLY_WITH_LOW_MAXIMUM_PAYMENT_RESPONSE_PATH = "cart/get_occ_cart_page_last_apply_with_low_maximum_payment_response.json"

const val GET_OCC_CART_PAGE_CREDIT_CARD_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_EXPIRED_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_expired_response.json"
const val GET_OCC_CART_PAGE_MULTIPLE_CREDIT_CARD_DELETED_RESPONSE_PATH = "cart/get_occ_cart_page_multiple_credit_card_deleted_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_ERROR_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_error_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_MIN_AMOUNT_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_min_amount_response.json"

const val GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_revamp_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_EXPIRED_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_expired_revamp_response.json"
const val GET_OCC_CART_PAGE_MULTIPLE_CREDIT_CARD_DELETED_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_multiple_credit_card_deleted_revamp_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_ERROR_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_error_revamp_response.json"
const val GET_OCC_CART_PAGE_CREDIT_CARD_MIN_AMOUNT_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_credit_card_min_amount_revamp_response.json"

const val GET_OCC_CART_PAGE_DEBIT_CARD_RESPONSE_PATH = "cart/get_occ_cart_page_debit_card_response.json"

const val GET_OCC_CART_PAGE_DEBIT_CARD_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_debit_card_revamp_response.json"

const val GET_OCC_CART_PAGE_SLASH_PRICE_RESPONSE_PATH = "cart/get_occ_cart_page_slash_price_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_LOW_WALLET_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_low_wallet_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_TOP_UP_WALLET_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_top_up_wallet_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_NO_PHONE_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_no_phone_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATION_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_activation_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATED_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_activated_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ERROR_TICKER_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_error_ticker_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ERROR_TICKER_DISABLE_BUTTON_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_error_ticker_disable_button_response.json"

const val GET_OCC_CART_PAGE_SLASH_PRICE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_slash_price_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_LOW_WALLET_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_low_wallet_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_TOP_UP_WALLET_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_top_up_wallet_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_NO_PHONE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_no_phone_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATION_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_activation_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATED_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_activated_revamp_response.json"
const val GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ERROR_TICKER_DISABLE_BUTTON_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_campaign_ovo_only_error_ticker_disable_button_revamp_response.json"

const val GET_OCC_CART_PAGE_OVO_NO_PHONE_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_no_phone_response.json"
const val GET_OCC_CART_PAGE_OVO_ACTIVATION_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_activation_response.json"
const val GET_OCC_CART_PAGE_OVO_ACTIVATED_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_activated_response.json"
const val GET_OCC_CART_PAGE_OVO_LOW_WALLET_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_low_wallet_response.json"
const val GET_OCC_CART_PAGE_OVO_TOP_UP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_top_up_response.json"

const val GET_OCC_CART_PAGE_OVO_NO_PHONE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_no_phone_revamp_response.json"
const val GET_OCC_CART_PAGE_OVO_ACTIVATION_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_activation_revamp_response.json"
const val GET_OCC_CART_PAGE_OVO_ACTIVATED_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_activated_revamp_response.json"
const val GET_OCC_CART_PAGE_OVO_LOW_WALLET_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_low_wallet_revamp_response.json"
const val GET_OCC_CART_PAGE_OVO_TOP_UP_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_ovo_top_up_revamp_response.json"

const val GET_OCC_CART_PAGE_NO_PROFILE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_no_profile_revamp_response.json"
const val GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_one_profile_revamp_response.json"
const val GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH = "cart/get_occ_cart_page_many_profile_revamp_response.json"

const val GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_BOE_RESPONSE_PATH = "cart/get_occ_cart_page_one_profile_revamp_with_boe_response.json"

const val GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_RECOMMENDATION_SPID_RESPONSE_PATH = "cart/get_occ_cart_page_one_profile_revamp_with_recommendation_spid_response.json"

const val GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_ADDRESS_2_RESPONSE_PATH = "cart/get_occ_cart_page_one_profile_revamp_with_address_2_response.json"

const val GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_PAYMENT_2_RESPONSE_PATH = "cart/get_occ_cart_page_one_profile_revamp_with_payment_2_response.json"

const val GET_OCC_CART_PAGE_REMOVE_PROFILE_PRE_RESPONSE_PATH = "cart/get_occ_cart_page_remove_profile_pre_response.json"

const val UPDATE_CART_OCC_SUCCESS_RESPONSE_PATH = "cart/update_cart_occ_success_response.json"
const val UPDATE_CART_OCC_DIALOG_PROMPT_RESPONSE_PATH = "cart/update_cart_occ_dialog_prompt_response.json"