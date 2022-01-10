package com.tokopedia.pdp.fintech.constants

const val GQL_GET_PRODUCT_DETAIL_V3 =
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