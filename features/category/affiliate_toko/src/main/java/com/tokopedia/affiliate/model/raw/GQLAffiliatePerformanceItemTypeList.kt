package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Performance_Item_Type_List: String = """query getItemTypeList() {
  getItemTypeList() {
    Data {
      ItemTypes{
        PageType
        Name
        Order
      }
      CtaText
      CtaLink {
        DesktopURL
        MobileURL
        AndroidURL
        IosURL
      }
      Error {
        ErrorType
        Message
        CtaText
        CtaLink {
          DesktopURL
          MobileURL
          AndroidURL
          IosURL
        }
      }
    }
  }
}
""".trimIndent()