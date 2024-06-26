package com.tokopedia.flight.orderdetail.data

import com.tokopedia.flight.orderdetail.data.GetOrderETicketQuery.QUERY_GET_ORDER_E_TICKET
import com.tokopedia.flight.orderdetail.data.GetOrderInvoiceQuery.QUERY_GET_ORDER_INVOICE
import com.tokopedia.flight.orderdetail.data.OrderDetailQuery.QUERY_ORDER_DETAIL
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 14/10/2020
 */

@GqlQuery("QueryGetOrderETicket", QUERY_GET_ORDER_E_TICKET)
internal object GetOrderETicketQuery{
    const val QUERY_GET_ORDER_E_TICKET = """
        query FlightGetETicket(${'$'}data: FlightETicketArgs!) {
            flightGetETicket(input:${'$'}data) {
                data
            }
        }
    """
}

@GqlQuery("QueryGetOrderInvoice", QUERY_GET_ORDER_INVOICE)
internal object GetOrderInvoiceQuery{
    const val QUERY_GET_ORDER_INVOICE = """
        query FlightGetInvoice(${'$'}data: FlightInvoiceArgs!) {
            flightGetInvoice(input:${'$'}data) {
                data
            }
        }
    """
}

@GqlQuery("QueryOrderDetail", QUERY_ORDER_DETAIL)
internal object OrderDetailQuery{
    const val QUERY_ORDER_DETAIL = """
        query GetOrderDetail(${'$'}data:GetOrderDetailArgs!) {
          flightGetOrderDetail(input:${'$'}data) {
            data {
              omsID
              createTime
              status
              statusString
              flight {
                id
                invoiceID
                contactName
                email
                phone
                countryID
                totalAdult
                totalAdultNumeric
                totalChild
                totalChildNumeric
                totalInfant
                totalInfantNumeric
                totalPrice
                totalPriceNumeric
                currency
                pdf
                isDomestic
                mandatoryDOB
                classText
                contactUsURL
                hasEticket
                payment {
                  id
                  status
                  statusStr
                  gatewayName
                  gatewayIcon
                  paymentDate
                  expireOn
                  transactionCode
                  promoCode
                  adminFeeAmt
                  adminFeeAmtStr
                  voucherAmt
                  voucherAmtStr
                  saldoAmt
                  saldoAmtStr
                  totalAmt
                  totalAmtString
                  needToPayAmt
                  needToPayAmtStr
                  additionalInfo
                  manualTransfer{
                    uniqueCode
                    accountBankName
                    accountBranch
                    accountNo
                    accountName
                    total
                  }
                }
                journeys {
                  id
                  status
                  departureID
                  departureTime
                  departureAirportName
                  departureCityName
                  arrivalID
                  arrivalTime
                  arrivalAirportName
                  arrivalCityName
                  totalTransit
                  totalStop
                  addDayArrival
                  duration
                  durationMinute
                  fare {
                    adultNumeric
                    childNumeric
                    infantNumeric
                  }
                  routes{
                    departureID
                    departureTime
                    departureAirportName
                    departureCityName
                    arrivalID
                    arrivalTime
                    arrivalAirportName
                    arrivalCityName
                    pnr
                    airlineID
                    airlineName
                    airlineLogo
                    operatorAirlineID
                    flightNumber
                    duration
                    durationMinute
                    layover
                    layoverMinute
                    refundable
                    departureTerminal
                    arrivalTerminal
                    stop
                    carrier
                    stopDetails{
                      code
                      city
                    }
                    ticketNumbers{
                      passengerID
                      ticketNumber
                    }
                    freeAmenities{
                      cabinBaggage{
                        isUpTo
                        unit
                        value
                      }
                      freeBaggage{
                        isUpTo
                        unit
                        value
                      }
                      meal
                      usbPort
                      wifi
                      others{
                        type
                        available
                      }
                    }
                  }
                  webCheckIn{
                    title
                    subTitle
                    startTime
                    endTime
                    iconURL
                    appURL
                    webURL
                  }
                }
                passengers{
                  id
                  type
                  title
                  firstName
                  lastName
                  dob
                  nationality
                  passportNo
                  passportCountry
                  passportExpiry
                  amenities {
                    departureID
                    arrivalID
                    type
                    price
                    priceNumeric
                    detail
                  }
                  cancelStatus {
                    status
                    statusStr
                    statusType
                  }
                }
                actionButtons {
                  id
                  label
                  buttonType
                  uri
                  uriWeb
                  mappingURL
                  method
                  weight
                }
                conditionalInfo {
                  type
                  title
                  text
                  color {
                    border
                    background
                  }
                }
                insurances {
                  id
                  title
                  tagline
                  paidAmount
                  paidAmountNumeric
                }
                cancellations {
                  cancelID
                  cancelDetail {
                    journeyID
                    passengerID
                    refundedGateway
                    refundedTime
                  }
                  createTime
                  updateTime
                  estimatedRefund
                  estimatedRefundNumeric
                  realRefund
                  realRefundNumeric
                  status
                  statusStr
                  statusType
                  refundInfo
                  refundDetail {
                    topInfo {
                      key
                      value
                    }
                    middleInfo {
                      title
                      content {
                        key
                        value
                      }
                    }
                    bottomInfo {
                      key
                      value
                    }
                    notes {
                      key
                      value
                    }
                  }
                }
              }
            }
            error {
              id
              status
              title
            }
          }
        }
    """
}
