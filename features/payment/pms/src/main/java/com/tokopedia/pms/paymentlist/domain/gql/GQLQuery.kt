package com.tokopedia.pms.paymentlist.domain.gql

const val GQL_PAYMENT_LIST_QUERY = """query paymentList(${'$'}cursor : String!){
    paymentList(lang: "ID", cursor:${'$'}cursor, perPage:10) {
        last_cursor
        has_next_page
        payment_list {
          transaction_id
          transaction_date
          transaction_expire
          transaction_expire_unix
          merchant_code
          payment_amount
          invoice_url
          product_name
          product_img
          gateway_name
          gateway_img
          payment_code
          is_va
          is_klikbca
          bank_img
          user_bank_account {
            acc_no
            acc_name
            bank_id
          }
          dest_bank_account {
            acc_no
            acc_name
            bank_id
          }
          show_upload_button
          show_edit_transfer_button
          show_edit_klikbca_button
          show_cancel_button
          show_help_page
          ticker_message
          app_link
        }
    }
}
"""

const val GQL_GET_CANCEL_QUERY =
    """query cancelDetail(${'$'}transactionID: String!, ${'$'}merchantCode: String!) {
  cancelDetail(transactionID: ${'$'}transactionID, merchantCode: ${'$'}merchantCode) {
      success
      hasRefund
      refundCCAmount
      refundWalletAmount
      refundMessage
    }
}
"""

const val GQL_CANCEL_PAYMENT_MUTATION =
    """mutation cancelPayment(${'$'}transactionID: String!, ${'$'}merchantCode: String!){
  cancelPayment(transactionID: ${'$'}transactionID, merchantCode: ${'$'}merchantCode) {
    success
    message
  }
}
"""