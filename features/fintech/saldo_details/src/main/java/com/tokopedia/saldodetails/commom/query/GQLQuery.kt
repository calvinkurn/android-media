package com.tokopedia.saldodetails.commom.query


private const val TRANSACTION_PROPERTIES = """
            note
            amount
            type_description
            withdrawal_status_string
            withdrawal_status_color
            create_time
            have_detail
            detail_type
            deposit_id
            withdrawal_id
"""

const val GQL_ALL_TYPE_TRANSACTION = """
    query DepositActivityQuery(${'$'}page: Int!, ${'$'}maxRows: Int!, ${'$'}dateFrom: String!, ${'$'}dateTo: String!) {
    allDepositHistory: depositActivity(page: ${'$'}page, maxRows: ${'$'}maxRows, dateFrom: ${'$'}dateFrom, dateTo: ${'$'}dateTo, saldoType: 2) {
        message
        have_error
        have_next_page
        deposit_history_list {
                $TRANSACTION_PROPERTIES
        }
    }

    buyerDepositHistory: depositActivity(page: ${'$'}page, maxRows: ${'$'}maxRows, dateFrom: ${'$'}dateFrom, dateTo: ${'$'}dateTo, saldoType: 0) {
        message
        have_error
        have_next_page
        deposit_history_list {
                $TRANSACTION_PROPERTIES
        }
    }

    sellerDepositHistory: depositActivity(page: ${'$'}page, maxRows: ${'$'}maxRows, dateFrom: ${'$'}dateFrom, dateTo: ${'$'}dateTo, saldoType: 1) {
        message
        have_error
        have_next_page
        deposit_history_list {
                $TRANSACTION_PROPERTIES
        }
    }
}   
"""


const val GQL_LOAD_TYPE_TRANSACTION_LIST = """
    query DepositActivityQuery(${'$'}page: Int!, ${'$'}maxRows: Int!, ${'$'}dateFrom: String!, ${'$'}dateTo: String!, ${'$'}saldoType: Int!) {
    allDepositHistory: depositActivity(page: ${'$'}page, maxRows: ${'$'}maxRows, dateFrom: ${'$'}dateFrom, dateTo: ${'$'}dateTo, saldoType: ${'$'}saldoType) {
        message
        have_error
        have_next_page
        deposit_history_list {
                $TRANSACTION_PROPERTIES
        }
    }
}
"""

const val GQL_PENJUALAN_TRANSACTION_LIST = """
    query MidasHistoryInvList(${'$'}params : midasHistoryInvListParam!) {
        midasHistoryInvList(params: ${'$'}params) {
            success
            have_next
            message_status
            content{
              summary_id
              amount
              desc
              create_time
              invoice
              order_id
            }
            public_message_desc
            public_message_title
        }
}   
"""

const val GQL_WITHDRAWAL_DETAIL =
    """query RichieGetWithdrawalInfo(${'$'}withdrawalID: String) {
        RichieGetWithdrawalInfo(withdrawalID: ${'$'}withdrawalID) {
            data {
                is_success
                withdrawal_info {
                    withdrawal_id
                    status
                    status_string
                    status_color
                    amount
                    fee
                    transferred_amount
                    create_time
                    bank_name
                    acc_no
                    acc_name
                    history {
                      title
                      description
                      create_time
                    }
                }
            }
            message_error
        }
        }"""

const val GQL_DEPOSIT_HISTORY_INVOICE = """
    query MidasDepositHistoryInvoiceDetail(${'$'}summaryID: String!) {
         MidasDepositHistoryInvoiceDetail(summaryID: ${'$'}summaryID) {
            is_success
            deposit_history {
                total_amount
                create_time
                invoice_no
                invoice_url
                order_url_android
                detail {
                  type_description
                  amount
                  is_late
                  last_update_deposit
                }
            }
        }
    }"""
