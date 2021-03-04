package com.tokopedia.tradein.raw

const val GQL_KYC_STATUS: String = """query kycStatus(${'$'}projectID:Int){
  kycStatus(projectID:${'$'}projectID){
    Message
    Detail{
      IsSuccess
      UserId
      ProjectId
      Status
      StatusName
      CreateTime
      CreateBy
      UpdateTime
    }
  }
}"""