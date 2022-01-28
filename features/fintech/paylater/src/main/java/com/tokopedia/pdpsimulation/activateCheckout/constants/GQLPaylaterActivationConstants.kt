package com.tokopedia.pdpsimulation.activateCheckout.constants




const val GQL_PAYLATER_ACTIVATION = """
    query optimized_checkout(${'$'}request: PaylaterOptimizedCheckOutRequest!) {
    paylater_getOptimizedCheckout(request: ${'$'}request) {
      data {
      gateway_id
      gateway_code
      gateway_name
      subtitle
      subtitle2
      light_img_url
      dark_img_url
      disable
      reason_long
      reason_short
      detail {
        tenure
        chip_title
        monthly_installment
        description
        installment_details {
          header
          content {
            title
            value
            type
          }
        }
      }
    }
    footer
    }
}
"""


const val GQL_GET_PRODUCT_DETAIL =
    """query GetProductV3(${'$'}productID: String!, ${'$'}options:OptionV3!){
  getProductV3(productID: ${'$'}productID, options: ${'$'}options) {
    productName
    url
    price
    variant{
   selections{
    options{
      value
    }
  }
    products{
      productID
      combination
    }
  }
     pictures{
      urlThumbnail
    }
  }
}"""