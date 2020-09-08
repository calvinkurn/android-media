package com.tokopedia.tradein.raw

const val GQL_GET_SHOP_INFO: String = """query getShopInfoShopShipment(${'$'}shopIds: [Int!]!, ${'$'}fields: [String!]!) {
           shopInfoByID(input: {
             shopIDs: ${'$'}shopIds,
             fields: ${'$'}fields
           }) {
             result {
               shipmentInfo {
                 isAvailable
                 code
                 shipmentID
                 image
                 name
                 isPickup
                 maxAddFee
                 awbStatus
               }
             }
             error {
               message
             }
           }
         }
"""