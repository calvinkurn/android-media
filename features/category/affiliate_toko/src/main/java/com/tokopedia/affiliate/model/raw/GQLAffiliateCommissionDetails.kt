package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Commission: String = """query getAffiliateCommissionDetail(${"$"}transactionID: String! ){
 getAffiliateCommissionDetail(transactionID: ${"$"}transactionID) {
    Data {
      Status             
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
      CardDetail {
        ProductID        
        OrderID                 
        ShopName
        ShopBadge
        CardTitle
        CardPrice
        CardPriceFormatted
        Image {
          DesktopURL      
          MobileURL      
          AndroidURL      
          IosURL         
        }
      }
      DetailTitle             
      Detail {
        TextType
        DetailType
        TextSize           
        DetailTitle             
        DetailTooltip          
        DetailDescription
        AdvancedTooltip {
          TooltipType           
          TextType              
          TextStyle             
          TextSize              
          TooltipText           
        }     
      }
      CommissionType
      PageType
      AdditionalQueryKey
      Notes                  
      CreatedAt               
      CreatedAtFormatted      
      UpdatedAt                
      UpdatedAtFormatted      
    }
  }
}""".trimIndent()