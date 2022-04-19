package com.tokopedia.gopay.kyc.domain

const val GQL_CHECK_KYC_STATUS = """
    query walletappCheckStatusKYC(${'$'}partnerCode: String!) {
  	walletappCheckStatusKYC(partnerCode: ${'$'}partnerCode) {
        code
        data {
        is_eligible_kyc
        message
        } 
	}
}
"""

const val GQL_INITIATE_KYC = """
    mutation walletappInitiateKYC(${'$'}kycType: String!, ${'$'}partnerCode: String!){
    walletappInitiateKYC(kycType: ${'$'}kycType, partnerCode: ${'$'}partnerCode){
	code
	data {
		kyc_request_id
		docs {
	        document_id
            document_type
	        document_url
            }
        }
    }
}
"""

const val GQL_SUBMIT_KYC = """
    mutation walletappSubmitKYC(${'$'}kycRequestID: String!, ${'$'}partnerCode: String!) {
    walletappSubmitKYC(kycRequestID: ${'$'}kycRequestID, partnerCode: ${'$'}partnerCode){
	    code
    }
}
"""