package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Withdrawal_Detail : String = """query getAffiliateWithdrawalDetail(${"$"}TransactionID: String!){
  getAffiliateWithdrawalDetail(TransactionID: ${"$"}TransactionID) {
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
      Ticker {
        TickerTitle               
        TickerDescription        
      }
      WithdrawalAmount           
      WithdrawalAmountFormatted  
      BankName                   
      AccountName               
      WithdrawalFee             
      WithdrawalFeeFormatted     
      FinalTransferred            
      FinalTransferredFormatted   
      Notes                      
      Label {
        LabelType                 
        LabelText                
      }
      CreatedAt                
      CreatedAtFormatted        
      UpdatedAt                 
      UpdatedAtFormatted        
      Detail {
        DetailTitle            
        DetailDescription      
        DetailStatus            
        CreatedAt             
        CreatedAtFormatted    
      }
    }
  }
}""".trimIndent()
