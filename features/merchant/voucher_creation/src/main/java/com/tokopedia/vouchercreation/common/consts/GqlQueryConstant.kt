package com.tokopedia.vouchercreation.common.consts

object GqlQueryConstant {

    const val GET_INIT_VOUCHER_ELIGIBILITY_QUERY =
        "query InitiateVoucher(\$action: String, \$targetBuyer: Int, \$couponType: String){\n" +
                "\tgetInitiateVoucherPage(Action: \$action, TargetBuyer: \$targetBuyer ,CouponType: \$couponType){\n" +
                "\t\theader{\n" +
                "          process_time\n" +
                "          messages\n" +
                "          reason\n" +
                "          error_code\n" +
                "        }\n" +
                "        data{\n" +
                "          is_eligible\n" +
                "        }\n" +
                "    }\n" +
                "}"

    const val GET_COUPON_DETAIL_QUERY = """
            query GetVoucherDataById (${'$'}voucher_id: Int!, ${'$'}source: String!) {
                merchantPromotionGetMVDataByID(voucher_id: ${'$'}voucher_id, source: ${'$'}source) {                
                    header {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data {
                      voucher_id
                      voucher_name
                      voucher_type
                      voucher_image
                      voucher_image_square
                      voucher_image_portrait
                      voucher_status
                      voucher_discount_type
                      voucher_discount_amt
                      voucher_discount_amt_max
                      voucher_minimum_amt
                      voucher_quota
                      voucher_start_time
                      voucher_finish_time
                      voucher_code
                      create_time
                      update_time
                      is_public
                      voucher_type_formatted
                      voucher_discount_type_formatted
                      voucher_discount_amt_formatted
                      confirmed_global_quota
                      booked_global_quota
                      is_vps
                      package_name
                      is_subsidy
                      tnc
                      galadriel_voucher_id
                      product_ids {
                         parent_product_id
                         child_product_id
                      }
                      applink
                      weblink
                    }                    
                }
            }
        """

    const val INITIATE_COUPON_PRODUCT_QUERY = "query InitiateVoucher(\$action: String, \$targetBuyer: Int, \$couponType: String, \$isVoucherProduct: Int){\n" +
            "\tgetInitiateVoucherPage(Action: \$action, TargetBuyer: \$targetBuyer ,CouponType: \$couponType, IsVoucherProduct: \$isVoucherProduct){\n" +
            "\t\theader{\n" +
            "          process_time\n" +
            "          messages\n" +
            "          reason\n" +
            "          error_code\n" +
            "        }\n" +
            "        data{\n" +
            "          shop_id\n" +
            "          token\n" +
            "          user_id\n" +
            "          access_token\n" +
            "          upload_app_url\n" +
            "          img_banner_base\n" +
            "          img_banner_ig_post\n" +
            "          img_banner_ig_story\n" +
            "          img_banner_label_gratis_ongkir\n" +
            "          img_banner_label_cashback\n" +
            "          img_banner_label_cashback_hingga\n" +
            "          prefix_voucher_code\n" +
            "          is_eligible\n" +
            "          max_product\n" +
            "        }\n" +
            "    }\n" +
            "}"
}