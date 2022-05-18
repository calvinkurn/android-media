package com.tokopedia.product.addedit.variant.data.constant

object GetVariantCombinationQueryConstant {
    const val BASE_QUERY = """
        query GetVariantCategoryCombination(%1s) {
            getVariantCategoryCombination(%2s) {
                %3s
            }
        }
    """

    const val OPERATION_PARAM_BY_CATEGORY = "${'$'}categoryID: Int!, ${'$'}productVariants: String, ${'$'}type: String!"
    const val OPERATION_PARAM_ALL_VARIANT = "${'$'}categoryID: Int!, ${'$'}allVariants: String"

    const val QUERY_PARAM_BY_CATEGORY = "categoryID: ${'$'}categoryID, productVariants: ${'$'}productVariants, type: ${'$'}type"
    const val QUERY_PARAM_ALL_VARIANT = "categoryID: ${'$'}categoryID, allVariants: ${'$'}allVariants"

    const val QUERY_DATA_BY_CATEGORY = """ 
        data {
            variantIDCombinations
            variantDetails {
                VariantID
                HasUnit
                Identifier
                Name
                Status
                IsPrimary
                Units {
                    VariantUnitID
                    Status
                    UnitName
                    UnitShortName
                    UnitValues {
                        VariantUnitValueID
                        Status
                        Value
                        EquivalentValueID
                        EnglishValue
                        Hex
                        Icon
                    }
                }
            }
        }
    """

    const val QUERY_DATA_ALL_VARIANT = """ 
        data{
            allVariants {
                VariantID
                Name
            }
        }
    """
}