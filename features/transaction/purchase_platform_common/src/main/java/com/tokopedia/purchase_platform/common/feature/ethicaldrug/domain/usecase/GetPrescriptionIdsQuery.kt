package com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase

const val GET_PRESCRIPTION_IDS_QUERY = """
    query GetEpharmacyCheckoutData(${'$'}checkout_id: String!, ${'$'}source: String!) {
        getEpharmacyCheckoutData(checkout_id: ${'$'}checkout_id, source: ${'$'}source) {
            data {
                checkout_id
                prescription_images {
                    prescription_id
                }
            }
        }
    }"""
