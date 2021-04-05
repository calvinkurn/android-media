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

    val STATEMENT_BILLS_QUERY_CLUSTERING by lazy {
        """
        query SBMList(${'$'}month: Int!, ${'$'}year: Int!, ${'$'}source: Int) {
        rechargeSBMList(Month: ${'$'}month, Year: ${'$'}year, Source: ${'$'}source) {
        UserID
        Total
        TotalText
        Month
        MonthText
        DateRangeText
        IsOngoing
        Summaries {
            CategoryID
            CategoryName
            Total
            TotalText
            Percentage
        }
        Sections {
            Title
            Type
            Text
            Bills {
                Index
                UUID
                Notables
                IsSubscribed
                ProductID
                ProductName
                CategoryID
                CategoryName
                OperatorID
                OperatorName
                ClientNumber
                Amount
                AmountText
                IconURL
                Date
                DateText
                Disabled
                DisabledText
                BillName
                IsChecked
                DueDate
                DueMessage {
                    Type
                    Text
                }
                DueDateLabel {
                    Type
                    Text
                }
                CheckoutFields {
                    Name
                    Value
                }
            }
        }
    }
    }""".trimIndent()

    }

    val DUMMY_RESPONSE = """{
    "rechargeSBMList": {
      "UserID": 8940072,
      "Total": 380000,
      "TotalText": "Rp 380.000",
      "Month": 3,
      "MonthText": "March",
      "DateRangeText": "1 - 24 March",
      "IsOngoing": true,
      "Summaries": [
        {
          "CategoryID": 1,
          "CategoryName": "Pulsa",
          "Total": 52500,
          "TotalText": "",
          "Percentage": 13.815789222717285
        },
        {
          "CategoryID": 14,
          "CategoryName": "Gas PGN",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 48,
          "CategoryName": "OVO",
          "Total": 15000,
          "TotalText": "Rp 15.000",
          "Percentage": 3.9473683834075928
        },
        {
          "CategoryID": 3,
          "CategoryName": "Listrik PLN",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 31,
          "CategoryName": "M-Tix XXI",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 31,
          "CategoryName": "M-Tix XXI",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 3,
          "CategoryName": "Listrik PLN",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 3,
          "CategoryName": "Listrik PLN",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        },
        {
          "CategoryID": 3,
          "CategoryName": "Listrik PLN",
          "Total": 0,
          "TotalText": "Rp 0",
          "Percentage": 0
        }
      ],
      "Sections": [
        {
          "Title": "Sudah Dibayar",
          "Type": 1,
          "Text": "",
          "Bills": [
            {
              "Index": 0,
              "UUID": "uuid_sbm_8940072_31_0877171717711",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 31,
              "ProductName": "50.000",
              "CategoryID": 1,
              "CategoryName": "PAID",
              "OperatorID": 5,
              "OperatorName": "XL",
              "ClientNumber": "0877171717711",
              "Amount": 52500,
              "AmountText": "",
              "IconURL": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "31"
                },
                {
                  "Name": "client_number",
                  "Value": "0877171717711"
                }
              ]
            },
            {
              "Index": 1,
              "UUID": "uuid_sbm_8940072_938_081319695557",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 938,
              "ProductName": "Rp 15.000",
              "CategoryID": 48,
              "CategoryName": "OVO",
              "OperatorID": 464,
              "OperatorName": "OVO",
              "ClientNumber": "081319695557",
              "Amount": 15000,
              "AmountText": "Rp 15.000",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/uob.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "938"
                },
                {
                  "Name": "client_number",
                  "Value": "081319695557"
                }
              ]
            },
            {
              "Index": 2,
              "UUID": "uuid_sbm_8940072_195_081297896118",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 195,
              "ProductName": "Rp 100.000",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 99,
              "OperatorName": "Token Listrik",
              "ClientNumber": "081297896118",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "2021-04-03",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "3 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "195"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896118"
                }
              ]
            },
            {
              "Index": 3,
              "UUID": "uuid_sbm_8940072_703_081297896228",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "081297896228",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896228"
                }
              ]
            },
            {
              "Index": 4,
              "UUID": "uuid_sbm_8940072_703_18013828993",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "18013828993",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "18013828993"
                }
              ]
            },
            {
              "Index": 5,
              "UUID": "uuid_sbm_8940072_220_11111111111",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 220,
              "ProductName": "Tagihan Listrik",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 108,
              "OperatorName": "Tagihan Listrik",
              "ClientNumber": "11111111111",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": true,
              "DueDate": "2021-04-20",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "20 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "220"
                },
                {
                  "Name": "client_number",
                  "Value": "11111111111"
                }
              ]
            }
          ]
        },
{
          "Title": "Pilih Tagihan",
          "Type": 2,
          "Text": "",
          "Bills": [
            {
              "Index": 0,
              "UUID": "uuid_sbm_8940072_31_0877171717711",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 31,
              "ProductName": "50.000",
              "CategoryID": 1,
              "CategoryName": "MAIN",
              "OperatorID": 5,
              "OperatorName": "XL",
              "ClientNumber": "0877171717711",
              "Amount": 52500,
              "AmountText": "",
              "IconURL": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "31"
                },
                {
                  "Name": "client_number",
                  "Value": "0877171717711"
                }
              ]
            },
            {
              "Index": 1,
              "UUID": "uuid_sbm_8940072_938_081319695557",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 938,
              "ProductName": "Rp 15.000",
              "CategoryID": 48,
              "CategoryName": "OVO",
              "OperatorID": 464,
              "OperatorName": "OVO",
              "ClientNumber": "081319695557",
              "Amount": 15000,
              "AmountText": "Rp 15.000",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/uob.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "938"
                },
                {
                  "Name": "client_number",
                  "Value": "081319695557"
                }
              ]
            },
            {
              "Index": 2,
              "UUID": "uuid_sbm_8940072_195_081297896118",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 195,
              "ProductName": "Rp 100.000",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 99,
              "OperatorName": "Token Listrik",
              "ClientNumber": "081297896118",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "2021-04-03",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "3 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "195"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896118"
                }
              ]
            },
            {
              "Index": 3,
              "UUID": "uuid_sbm_8940072_703_081297896228",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "081297896228",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896228"
                }
              ]
            },
            {
              "Index": 4,
              "UUID": "uuid_sbm_8940072_703_18013828993",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "18013828993",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "18013828993"
                }
              ]
            },
            {
              "Index": 5,
              "UUID": "uuid_sbm_8940072_220_11111111111",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 220,
              "ProductName": "Tagihan Listrik",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 108,
              "OperatorName": "Tagihan Listrik",
              "ClientNumber": "11111111111",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": true,
              "DueDate": "2021-04-20",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "20 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "220"
                },
                {
                  "Name": "client_number",
                  "Value": "11111111111"
                }
              ]
            }
          ]
        },
{
          "Title": "Butuh Tindakan",
          "Type": 3,
          "Text": "",
          "Bills": [
            {
              "Index": 0,
              "UUID": "uuid_sbm_8940072_31_0877171717711",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 31,
              "ProductName": "50.000",
              "CategoryID": 1,
              "CategoryName": "ACTION",
              "OperatorID": 5,
              "OperatorName": "XL",
              "ClientNumber": "0877171717711",
              "Amount": 52500,
              "AmountText": "",
              "IconURL": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "31"
                },
                {
                  "Name": "client_number",
                  "Value": "0877171717711"
                }
              ]
            },
            {
              "Index": 1,
              "UUID": "uuid_sbm_8940072_938_081319695557",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 938,
              "ProductName": "Rp 15.000",
              "CategoryID": 48,
              "CategoryName": "OVO",
              "OperatorID": 464,
              "OperatorName": "OVO",
              "ClientNumber": "081319695557",
              "Amount": 15000,
              "AmountText": "Rp 15.000",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/uob.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "938"
                },
                {
                  "Name": "client_number",
                  "Value": "081319695557"
                }
              ]
            },
            {
              "Index": 2,
              "UUID": "uuid_sbm_8940072_195_081297896118",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 195,
              "ProductName": "Rp 100.000",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 99,
              "OperatorName": "Token Listrik",
              "ClientNumber": "081297896118",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "2021-04-03",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "3 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "195"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896118"
                }
              ]
            },
            {
              "Index": 3,
              "UUID": "uuid_sbm_8940072_703_081297896228",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "081297896228",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "081297896228"
                }
              ]
            },
            {
              "Index": 4,
              "UUID": "uuid_sbm_8940072_703_18013828993",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 703,
              "ProductName": "Rp 100.000",
              "CategoryID": 31,
              "CategoryName": "M-Tix XXI",
              "OperatorID": 404,
              "OperatorName": "Top Up M-Tix",
              "ClientNumber": "18013828993",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/xxi.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": false,
              "DueDate": "",
              "DueMessage": {
                "Type": 0,
                "Text": ""
              },
              "DueDateLabel": {
                "Type": 0,
                "Text": ""
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "703"
                },
                {
                  "Name": "client_number",
                  "Value": "18013828993"
                }
              ]
            },
            {
              "Index": 5,
              "UUID": "uuid_sbm_8940072_220_11111111111",
              "Notables": "",
              "IsSubscribed": false,
              "ProductID": 220,
              "ProductName": "Tagihan Listrik",
              "CategoryID": 3,
              "CategoryName": "Listrik PLN",
              "OperatorID": 108,
              "OperatorName": "Tagihan Listrik",
              "ClientNumber": "11111111111",
              "Amount": 0,
              "AmountText": "Rp 0",
              "IconURL": "https://ecs7.tokopedia.net/img/recharge/operator/pln.png",
              "Date": "",
              "DateText": "",
              "Disabled": false,
              "DisabledText": "",
              "BillName": "",
              "IsChecked": true,
              "DueDate": "2021-04-20",
              "DueMessage": {
                "Type": 1,
                "Text": "Bayar Sebelum:"
              },
              "DueDateLabel": {
                "Type": 4,
                "Text": "20 April"
              },
              "CheckoutFields": [
                {
                  "Name": "product_id",
                  "Value": "220"
                },
                {
                  "Name": "client_number",
                  "Value": "11111111111"
                }
              ]
            }
          ]
        }
      ]
    }
  }"""
}