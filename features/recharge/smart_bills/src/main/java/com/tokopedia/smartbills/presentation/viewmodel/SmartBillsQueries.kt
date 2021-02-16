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

    val compound by lazy {
        """{
                "rechargeStatementBills": {
                  "total": 52510,
                  "totalText": "Rp 52.510",
                  "month": 2,
                  "monthText": "February",
                  "dateRangeText": "1 - 15 February",
                  "isOngoing": true,
                  "summaries": [
                    {
                      "categoryID": 8,
                      "categoryName": "Internet & TV Kabel",
                      "total": 10,
                      "totalText": "Rp 10",
                      "percentage": 0.019043991342186928
                    },
                    {
                      "categoryID": 5,
                      "categoryName": "Air PDAM",
                      "total": 52500,
                      "totalText": "Rp 52.500",
                      "percentage": 99.98095703125
                    }
                  ],
                  "bills": [
                    {
                      "index": 0,
                      "productID": 393,
                      "productName": "AETRA JAKARTA",
                      "categoryID": 5,
                      "categoryName": "Air PDAM",
                      "operatorID": 29,
                      "operatorName": "AETRA JAKARTA",
                      "clientNumber": "13111516111",
                      "amount": 52500,
                      "amountText": "Rp 52.500",
                      "iconURL": "https://ecs7.tokopedia.net/img/recharge/operator/aetra.png",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [
                        {
                          "name": "product_id",
                          "value": "393"
                        },
                        {
                          "name": "client_number",
                          "value": "13111516111"
                        }
                      ],
                      "billName": "",
                      "isChecked": true,
                      "DueDate": "",
                      "DueMessage": {
                          "Type": 1,
                          "Text": "Bayar Sekarang"
                        },
                        "DueDateLabel": {
                          "Type": 3,
                          "Text": "14 Februari"
                        }
                    },
                    {
                      "index": 1,
                      "productID": 1249,
                      "productName": "First Media",
                      "categoryID": 8,
                      "categoryName": "Internet & TV Kabel",
                      "operatorID": 243,
                      "operatorName": "First Media",
                      "clientNumber": "11122111",
                      "amount": 10,
                      "amountText": "Rp 10",
                      "iconURL": "https://ecs7.tokopedia.net/img/recharge/operator/firstmedia.png",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [
                        {
                          "name": "product_id",
                          "value": "1249"
                        },
                        {
                          "name": "client_number",
                          "value": "11122111"
                        }
                      ],
                      "billName": "",
                      "isChecked": true,
                      "DueDate": "",
                      "DueMessage": {
                          "Type": 1,
                          "Text": "Bayar Sebelum"
                        },
                        "DueDateLabel": {
                          "Type": 2,
                          "Text": "16 Februari"
                        }
                    }
                  ]
                }
              }
        """.trimIndent()
    }
}