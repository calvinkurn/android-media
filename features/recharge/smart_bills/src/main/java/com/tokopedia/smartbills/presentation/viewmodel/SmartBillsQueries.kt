package com.tokopedia.smartbills.presentation.viewmodel

object SmartBillsQueries {
    val STATEMENT_MONTHS_QUERY by lazy {
        """
            query rechargeStatementMonths(${'$'}limit: Int!){
              rechargeStatementMonths(Limit: ${'$'}limit) {
                index: Index
                label: Label
                month: Month
                year: Year
                isOngoing: IsOngoing
              }
            }
        """.trimIndent()
    }

    val GET_CATALOG_ADD_BILLS by lazy {
        """
            query rechargeCatalog(${'$'}platformID: Int!){ 
              rechargeCatalogMenu(platformID:${'$'}platformID){ 
                id 
                name 
                label 
                icon 
                style 
                app_link 
                slug_name 
                }
               }
        """.trimIndent()
    }

    val GET_NOMINAL_TELCO by lazy {
        """query rechargeCatalog(${'$'}menuID: Int!, ${'$'}platformID: Int!, ${'$'}operator: String!, ${'$'}clientNumber: String!){
            rechargeCatalogProductInputMultiTab(menuID: ${'$'}menuID, platformID: ${'$'}platformID, operator: ${'$'}operator, clientNumber: [${'$'}clientNumber]) {
            productInputs {
                label
                needEnquiry
                isShowingProduct
                enquiryFields {
                    id
                    param_name
                    name
                }
                product {
                    name
                    text
                    placeholder
                    validations {
                        rule
                    }
                    dataCollections {
                        name
                        products {
                            id
                            attributes {
                                price
                                detail
                                info
                                product_labels
                                promo {
                                    id
                                }
                                desc
                            }
                        }
                    }
                }
                filterTagComponents{
                    name
                    text
                    param_name
                    data_collections {
                        key
                        value
                    }
                }
            }
        }
        }"""
    }

    val STATEMENT_BILLS_QUERY by lazy {
        """
            query rechargeStatementBills(${'$'}month: Int!, ${'$'}year: Int!, ${'$'}source: Int){
              rechargeStatementBills(
                Month: ${'$'}month
                Year: ${'$'}year
                Source: ${'$'}source
              ){
                total: Total
                totalText: TotalText
                month: Month
                monthText: MonthText
                dateRangeText: DateRangeText
                isOngoing: IsOngoing
                summaries: Summaries {
                  categoryID: CategoryID
                  categoryName: CategoryName
                  total: Total
                  totalText: TotalText
                  percentage:Percentage
                }
                bills: Bills{
                  Flag
                  index: Index
                  productID: ProductID
                  productName: ProductName
                  categoryID: CategoryID
                  categoryName: CategoryName
                  operatorID:OperatorID
                  operatorName: OperatorName
                  clientNumber: ClientNumber
                  amount: Amount
                  amountText: AmountText
                  iconURL: IconURL
                  date: Date
                  dateText: DateText
                  disabled: Disabled
                  disabledText: DisabledText
                  checkoutFields: CheckoutFields {
                    name: Name
                    value: Value
                  }
                  billName: BillName
                  isChecked: IsChecked
                  DueDate
                  DueMessage{
                     Type
                     Text
                  }
                  DueDateLabel{
                     Type
                     Text
                  }
                }
              }
            }
        """.trimIndent()
    }

    val STATEMENT_BILLS_QUERY_CLUSTERING by lazy {
        """
        query SBMList(${'$'}month: Int!, ${'$'}year: Int!, ${'$'}source: Int) {
        rechargeSBMList(Month: ${'$'}month, Year: ${'$'}year, Source: ${'$'}source) {
        userID: UserID
        total: Total
        totalText: TotalText
        month: Month
        monthText: MonthText
        dateRangeText: DateRangeText
        isOngoing: IsOngoing
        summaries: Summaries {
            categoryID: CategoryID
            categoryName: CategoryName
            total: Total
            totalText: TotalText
            percentage:Percentage
        }
        sections: Sections {
            title: Title
            type: Type
            text: Text
            bills: Bills {
                  flag: Flag
                  index: Index
                  UUID
                  productID: ProductID
                  productName: ProductName
                  categoryID: CategoryID
                  categoryName: CategoryName
                  operatorID:OperatorID
                  operatorName: OperatorName
                  clientNumber: ClientNumber
                  amount: Amount
                  amountText: AmountText
                  iconURL: IconURL
                  date: Date
                  dateText: DateText
                  disabled: Disabled
                  disabledText: DisabledText
                  checkoutFields: CheckoutFields {
                    name: Name
                    value: Value
                  }
                  billName: BillName
                  isChecked: IsChecked
                  DueDate
                  DueMessage{
                     Type
                     Text
                  }
                  DueDateLabel{
                     Type
                     Text
                  }
            }
        }
    }
    }""".trimIndent()

    }

    val GET_SBM_RELOAD_ACTION_QUERY by lazy {
        """
        query GetSBM(${'$'}uuids:[String],${'$'}month: Int!, ${'$'}year: Int!, ${'$'}source: Int){
        RechargeMultipleSBMBill(
        UUIDs:${'$'}uuids,
        Month:${'$'}month,
        Year:${'$'}year,
        Source:${'$'}source,
        ){
        userID: UserID
        bills: Bills{
                  UUID
                  index: Index
                  productID: ProductID
                  productName: ProductName
                  categoryID: CategoryID
                  categoryName: CategoryName
                  operatorID:OperatorID
                  operatorName: OperatorName
                  clientNumber: ClientNumber
                  amount: Amount
                  amountText: AmountText
                  iconURL: IconURL
                  date: Date
                  dateText: DateText
                  disabled: Disabled
                  disabledText: DisabledText
                  checkoutFields: CheckoutFields {
                    name: Name
                    value: Value
                  }
                  sections: Section{
                     title: Title
                     type: Type
                     text: Text
                  }
        }
    }
    }
    """
    }
}