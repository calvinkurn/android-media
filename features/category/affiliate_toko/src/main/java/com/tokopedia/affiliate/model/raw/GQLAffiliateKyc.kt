package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Kyc: String = """query kycProjectInfo(${"$"}projectId:Int!) {
      kycProjectInfo(projectID:${"$"}projectId) {
        Status
        StatusName
        Message
        IsAllowToRegister
        TypeList {
          TypeID
          Status
          StatusName
          IsAllowToUpload
        }
        IsSelfie
      }
    }
""".trimIndent()