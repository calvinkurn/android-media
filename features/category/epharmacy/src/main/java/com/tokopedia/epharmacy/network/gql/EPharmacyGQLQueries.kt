package com.tokopedia.epharmacy.network.gql

const val GQL_FETCH_ORDER_DETAILS_QUERY = """
    query GetEpharmacyOrderDetails(${'$'}order_id: Int!) {
    getEpharmacyOrderDetails(order_id: ${'$'}order_id) {
      form {
          shopId
          shopName
          paymentDate
          invoiceRefNum
          orderPdf
          products {
            productId
            name
            quantity
            isEthicalDrug
            productImage
          }
          prescriptionImages {
            prescriptionId
            rejectReason
            expiredAt
            status
            data
          }
          epharmacyButton {
            text
            key
            type
            uri
          }
          epharmacyTicker {
            text
          }
          
      } 
    }
}
"""