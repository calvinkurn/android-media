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

    val compound by lazy {
        """
            {
                "rechargeStatementBills": {
                  "total": 435000,
                  "totalText": "Rp 435.000",
                  "month": 0,
                  "monthText": "",
                  "dateRangeText": "",
                  "isOngoing": false,
                  "summaries": [],
                  "bills": [
                    {
                      "Flag": true,
                      "index": 0,
                      "productID": 5,
                      "productName": "",
                      "categoryID": 0,
                      "categoryName": "AIR",
                      "operatorID": 1,
                      "operatorName": "OKE",
                      "clientNumber": "87878473874738",
                      "amount": 30000,
                      "amountText": "Rp 30.000",
                      "iconURL": "",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [],
                      "billName": "pdambill",
                      "isChecked": true,
                      "DueDate": "",
                      "DueMessage": {
                        "Type": 0,
                        "Text": ""
                      },
                      "DueDateLabel": {
                        "Type": 0,
                        "Text": ""
                      }
                    },
                    {
                      "Flag": false,
                      "index": 1,
                      "productID": 8,
                      "productName": "HHHA",
                      "categoryID": 0,
                      "categoryName": "ASURANSI",
                      "operatorID": 2,
                      "operatorName": "OKE",
                      "clientNumber": "",
                      "amount": 40000,
                      "amountText": "Rp 40.000",
                      "iconURL": "",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [],
                      "billName": "assurancebill",
                      "isChecked": true,
                      "DueDate": "",
                      "DueMessage": {
                        "Type": 0,
                        "Text": ""
                      },
                      "DueDateLabel": {
                        "Type": 0,
                        "Text": ""
                      }
                    },
                    {
                      "Flag": true,
                      "index": 2,
                      "productID": 5139,
                      "productName": "",
                      "categoryID": 0,
                      "categoryName": "BPJS",
                      "operatorID": 1856,
                      "operatorName": "OKOK",
                      "clientNumber": "748738478",
                      "amount": 55000,
                      "amountText": "Rp 55.000",
                      "iconURL": "",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [],
                      "billName": "bpjsdenda",
                      "isChecked": false,
                      "DueDate": "",
                      "DueMessage": {
                        "Type": 0,
                        "Text": ""
                      },
                      "DueDateLabel": {
                        "Type": 0,
                        "Text": ""
                      }
                    },
                    {
                      "Flag": false,
                      "index": 3,
                      "productID": 205,
                      "productName": "",
                      "categoryID": 0,
                      "categoryName": "BPJS",
                      "operatorID": 107,
                      "operatorName": "OKE",
                      "clientNumber": "87878473874738",
                      "amount": 155000,
                      "amountText": "Rp 155.000",
                      "iconURL": "",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [],
                      "billName": "bpjs kesehatan satu",
                      "isChecked": false,
                      "DueDate": "2021-02-20",
                      "DueMessage": {
                        "Type": 1,
                        "Text": "Bayar Sekarang"
                      },
                      "DueDateLabel": {
                        "Type": 3,
                        "Text": "20 Februari"
                      }
                    },
                    {
                      "Flag": false,
                      "index": 4,
                      "productID": 205,
                      "productName": "",
                      "categoryID": 0,
                      "categoryName": "BPJS",
                      "operatorID": 107,
                      "operatorName": "AIR",
                      "clientNumber": "876487648",
                      "amount": 155000,
                      "amountText": "Rp 155.000",
                      "iconURL": "",
                      "date": "",
                      "dateText": "",
                      "disabled": false,
                      "disabledText": "",
                      "checkoutFields": [],
                      "billName": "bpjs kesehatan dua",
                      "isChecked": true,
                      "DueDate": "2021-02-10",
                      "DueMessage": {
                        "Type": 1,
                        "Text": "Bayar Sekarang"
                      },
                      "DueDateLabel": {
                        "Type": 3,
                        "Text": "10 Februari"
                      }
                    }
                  ]
                }
            }
        """.trimIndent()
    }
}