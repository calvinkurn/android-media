package com.tokopedia.review.feature.createreputation

/**
 * Created By @ilhamsuaib on 2020-01-03
 */
object Query {

    const val GET_REPUTATION_FORM_QUERY = "query productrevGetForm(\$reputationId:Int!,\$productId:Int!){\n" +
            "  productrevGetForm(reputationID:\$reputationId, productID:\$productId){\n" +
            "    reputationID\n" +
            "    orderID\n" +
            "    validToReview\n" +
            "    productData{\n" +
            "      productID\n" +
            "      productName\n" +
            "      productStatus\n" +
            "      productPageURL\n" +
            "      productImageURL\n" +
            "    }\n" +
            "    shopData{\n" +
            "      shopID\n" +
            "      shopOpen\n" +
            "    }\n" +
            "    userData{\n" +
            "      userID\n" +
            "      userName\n" +
            "      userStatus\n" +
            "    }\n" +
            "  }\n" +
            "}"

    const val GET_PRODUCT_INCENTIVE_OVO = "query getProductRevIncentiveOvo{\n" +
            "\tproductrevIncentiveOvo{\n" +
            "    ticker{\n" +
            "      title\n" +
            "      subtitle\n" +
            "    }\n" +
            "    title\n" +
            "    subtitle\n" +
            "    description\n" +
            "    numbered_list\n" +
            "    cta_text\n" +
            "  }\n" +
            "}\n"
}