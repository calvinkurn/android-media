package com.tokopedia.promocheckout.util

object PromoCheckoutQuery {

    fun promoCheckoutCatalogDetail()="""
    query hachikoCatalogDetail(${'$'}slug:String, ${'$'}catalog_id:Int , ${'$'}apiVersion: String){
        hachikoCatalogDetail(slug: ${'$'}slug, catalog_id: ${'$'}catalog_id , apiVersion: ${'$'}apiVersion) {
            id
            catalog_type
            expired
            points
            title
            sub_title
            expired_label
            expired_str
            is_disabled
            is_disabled_button
            disable_error_message
            upper_text_desc
            description
            overview
            how_to_use
            tnc
            icon
            thumbnail_url
            thumbnail_url_mobile
            image_url
            image_url_mobile
            thumbnail_v2_url
            thumbnail_v2_url_mobile
            image_v2_url
            image_v2_url_mobile
            quota
            is_gift
            cta
            points_str
            button_str
            points_slash
            points_slash_str
            discount_percentage
            discount_percentage_str
            minimumUsage
            minimumUsageLabel
        }
    }"""

    fun promoCheckoutDetailMarketPlace()="""
    query hachikoCouponDetail(${'$'}code : String!,${'$'}apiVersion : String){
        status
        hachikoCouponDetail(code:${'$'}code , apiVersion: ${'$'}apiVersion){
            id
            expired
            real_code
            minimum_usage
            minimum_usage_label
            points
            title
            catalog_title
            sub_title
            catalog_sub_title
            description
            overview
            how_to_use
            tnc
            icon
            thumbnail_url
            thumbnail_url_mobile
            image_url
            image_url_mobile
            thumbnail_v2_url
            thumbnail_v2_url_mobile
            image_v2_url
            image_v2_url_mobile
            quota
            is_gift
            cta
            cta_desktop
            usage {
                active_count_down
                expired_count_down
                text
                usage_str
                btn_usage {
                    text
                    url
                    applink
                    type
                }
            }
            swipe {
                need_swipe
                text
                note
                partner_code
                pin {
                    need_pin
                    text
                }
            }
        }
    }"""

    fun promoCheckoutExchangeCoupon()="""
            query
    {
        tokopointsCatalogHighlight {
            resultStatus{
                code
                message
                status
            }
            catalogList{
                id
                promoID
                catalogType
                expired
                points
                quota
                title
                subtitle
                thumbnailURL
                thumbnailURLMobile
                imageURL
                imageUrlMobile
                thumbnailV2URL
                thumbnailV2URLMobile
                imageV2URL
                imageV2URLMobile
                slug
                baseCode
                pointsStr
                isGift
                expiredLabel
                expiredStr
                isDisabled
                isDisabledButton
                disableErrorMessage
                upperTextDesc
                pointsSlash
                pointsSlashStr
                discountPercentage
                discountPercentageStr
                isShowTukarButton
                quotaPercentage
            }
            title
            subTitle
        }
    }"""

    fun promoCheckoutLastSeen()="""
            query rechargeLastSeenPromo(${'$'}categoryIDs: [Int]!){
        rechargePromoBanner(categoryIDs: ${'$'}categoryIDs) {
            id
            title
            subtitle
            promo_code
        }
    }"""

    fun promoCheckoutList()="""
            query tokopointsCouponList(${'$'}input : CouponListRequest!){
        status
        tokopointsCouponList(input: ${'$'}input){
            tokopointsCouponData{
                id
                promoID
                code
                expired
                title
                catalogTitle
                subTitle
                catalogSubTitle
                description
                icon
                imageUrl
                imageUrlMobile
                thumbnailUrl
                thumbnailUrlMobile
                minimum_usage
                minimum_usage_label
                imageV2Url
                imageV2UrlMobile
                thumbnailV2Url
                thumbnailV2UrlMobile
                cta
                ctaDesktop
                slug
                usage {
                    activeCountdown
                    expiredCountdown
                    text
                    usageStr
                    buttonUsage {
                        text
                        url
                        appLink
                        type
                    }
                }
            }
            tokopointsPaging{
                hasNext
            }
            tokopointsExtraInfo{
                infoHTML
                linkText
                linkUrl
            }
            tokopointsEmptyMessage{
                title
                subTitle
            }
        }
    }"""

    fun promoCheckoutPrevalidateCoupon() ="""
            mutation hachialidateRedeem(${'$'}catalog_id: Int, ${'$'}is_gift: Int, ${'$'}gift_user_id: Int, ${'$'}gift_email: String) {
        validateRedeem : hachikoValidateRedeem(catalog_id: ${'$'}catalog_id, is_gift: ${'$'}is_gift, gift_user_id: ${'$'}gift_user_id, gift_email: ${'$'}gift_email) {
            is_valid
            message_success
            message_title
        }
    }"""

    fun promoCheckoutRedeemCoupon() = """
            mutation hachikoRedeem(${'$'}catalog_id: Int, ${'$'}is_gift: Int, ${'$'}gift_user_id: Int, ${'$'}gift_email: String, ${'$'}notes: String) {
        hachikoRedeem(catalog_id: ${'$'}catalog_id, is_gift: ${'$'}is_gift, gift_user_id: ${'$'}gift_user_id, gift_email: ${'$'}gift_email, notes: ${'$'}notes) {
            coupons {
                id
                owner
                promo_id
                code
                title
                description
                cta
                cta_desktop
            }
            reward_points
        }
    }"""

    fun promoCurrentPoints() = """
        query UserPointsQuery {
          tokopoints {
            resultStatus {
              code
            }
            status {
              tier {
                nameDesc
                eggImageURL
              }
              points {
                reward
                rewardStr
                loyalty
                loyaltyStr
                loyaltyExpiryInfo
                rewardExpiryInfo
              }
            }
          }
        }

    """

}