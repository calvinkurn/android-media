package com.tokopedia.tkpd.tkpdreputation.createreputation

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
}