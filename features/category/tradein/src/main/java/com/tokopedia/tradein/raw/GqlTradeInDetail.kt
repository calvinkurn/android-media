package com.tokopedia.tradein.raw

const val GQL_TRADE_IN_DETAIL: String = """query getTradeInDetailData(${"$"}input: Input){
    getTradeInDetailData(input: ${"$"}input) {
        bannerUrl
        productPriceFmt
        activePromo {
            code
            title
            subTitle
        }
        logisticMessage
        logisticOptions {
            isDiagnosed
            isAvailable
            isPrefered
            is3PL
            discountPercentageFmt
            title
            estimationPriceFmt
            diagnosticPriceFmt
            finalPriceFmt
            subTitle
            expiryTime
            diagnosticReview
        }
        deviceAttribute {
            ram
            imei
            brand
            grade
            model
            storage
            model_id
        }
    }
}
"""