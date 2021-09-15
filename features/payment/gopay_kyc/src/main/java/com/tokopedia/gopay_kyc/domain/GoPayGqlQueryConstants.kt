package com.tokopedia.gopay_kyc.domain

const val GQL_CHECK_KYC_STATUS = """
    query walletappCheckStatusKYC(${'$'} partnerCode: String!){
        walletappCheckStatusKYC(partnerCode: ${'$'} partnerCode) {
        	code
        	data {
        		is_eligible_kyc
        		wallet_level_str
        	}
        }
    }
"""

const val GQL_INITIATE_KYC = """
    mutation initiateKYC(${'$'} kycType: String!, ${'$'} partnerCode: String!){
    initiateKYC( kycType: ${'$'}kycType, partnerCode: ${'$'}partnerCode){
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
"""

const val GQL_SUBMIT_KYC = """
    mutation submitKYC(kycRequestID: Int!, partnerCode: String!) {
    submitKYC(kycRequestID: ${'$'}requestID, partnerCode: ${'$'}partnerCode){
	    code
    	data {
		    kyc_request_id
        }
    }
}
"""