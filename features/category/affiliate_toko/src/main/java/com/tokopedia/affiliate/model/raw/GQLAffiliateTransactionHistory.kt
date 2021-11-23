package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Transaction_History: String = """{ getAffiliateTransactionHistory(Filter: {StartDate: "", EndDate: "", Page: 0, Limit: 20}) {
    Data {
        Status
        Error {
            ErrorType
            Message
            CtaText
            CtaLink {
                DesktopURL
                AndroidURL
                IosURL
                MobileURL
            }
        }
        StartDate
        EndDate
        HasNext
        Page
        Limit
        Transaction {
            Title
            TransactionID
            WithdrawalID
            Amount
            AmountFormatted
            TransactionType
            TransactionStatus
            Label {
                LabelType
                LabelText
            }
            Notes
            CreatedAt
            CreatedAtFormatted
            UpdatedAt
            UpdatedAtFormatted
        }
    }
}
}
""".trimIndent()