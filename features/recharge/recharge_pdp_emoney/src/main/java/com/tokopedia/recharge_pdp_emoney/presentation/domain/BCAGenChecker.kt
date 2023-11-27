package com.tokopedia.recharge_pdp_emoney.presentation.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recharge_pdp_emoney.presentation.domain.BCAGenChecker.BCA_GEN_CHECKER_OPERATION_NAME
import com.tokopedia.recharge_pdp_emoney.presentation.domain.BCAGenChecker.BCA_GEN_CHECKER_QUERY

@GqlQuery(BCA_GEN_CHECKER_OPERATION_NAME, BCA_GEN_CHECKER_QUERY)
object BCAGenChecker {
    const val BCA_GEN_CHECKER_OPERATION_NAME = "BCAGenCheckerQuery"
    const val BCA_GEN_CHECKER_QUERY = """
        query $BCA_GEN_CHECKER_OPERATION_NAME(${'$'}input: DigiPersoGetPersonalizedItemsRequest!){
            digiPersoGetPersonalizedItems(input: ${'$'}input) {
                items {
    	             id
                     label1
                     label2
                }
            }
        }
    """
}
