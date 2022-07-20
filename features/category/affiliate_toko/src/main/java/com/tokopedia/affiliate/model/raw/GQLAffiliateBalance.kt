package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Balance: String = """query getAffiliateBalance(){
  getAffiliateBalance() {
    Data {
      Status             
      Error {            
        ErrorType        
        Message          
        CtaText          
        CtaLink{
          DesktopURL     
          AndroidURL     
          IosURL          
          MobileURL      
        }
      }
      Amount              
      AmountFormatted    
      WalletStatus       
    }
  }
}""".trimIndent()