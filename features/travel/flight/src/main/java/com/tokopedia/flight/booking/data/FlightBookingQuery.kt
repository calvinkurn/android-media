package com.tokopedia.flight.booking.data

/**
 * @author by furqan on 08/10/2020
 */
object FlightBookingQuery {
    val QUERY_ADD_TO_CART = """
        mutation addtocart(${'$'}param: FlightAddtoCartArgs!) {
          flightAddToCart(input:${'$'}param) {
            id
            meta {
                requestID
            }
          }
        }
    """.trimIndent()

    val QUERY_GET_CART = """
        query getCart(${'$'}cartID:String!) {
            flightCart(cartID:${'$'}cartID) {
              meta {
                needRefresh
                refreshTime
                maxRetry
              }
              included {
                id
                type
                attributes {
                  name
                  shortName
                  logo
                  city
                }
              }
              data {
                id
                status
                newPrice {
                  id
                  fare {
                    adult
                    child
                    infant
                    adultNumeric
                    childNumeric
                    infantNumeric
                  }
                }
                flight {
                  email
                  phone
                  contactName
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
                  isDomestic
                  repriceTime
                  adult
                  child
                  infant
                  class
                  priceDetail {
                      label
                      price
                      priceNumeric
                  }
                  mandatoryDOB
                  amenityOptions {
                    id
                    journeyID
                    arrivalAirportID
                    departureAirportID
                    key
                    type
                    description
                    items {
                      id
                      price
                      priceNumeric
                      description
                      currency
                    }
                  }
                  insuranceOptions {
                    id
                    name
                    description
                    totalPriceNumeric
                    defaultChecked
                    tncAggrement
                    tncURL
                    benefits {
                      title
                      description
                      icon
                    }
                  }
                  journeys {
                    id
                    searchTerm
                    departureAirportID
                    departureTime
                    departureTerminal
                    arrivalAirportID
                    arrivalTime
                    arrivalTerminal
                    totalTransit
                    totalStop
                    addDayArrival
                    duration
                    durationMinute
                    durationLong
                    searchTerm
                    fare {
                      adult
                      child
                      infant
                      adultNumeric
                      childNumeric
                      infantNumeric
                    }
                    routes {
                      operatingAirlineID
                      departureAirportID
                      departureTime
                      arrivalAirportID
                      arrivalTime
                      airlineID
                      flightNumber
                      duration
                      layover
                      refundable
                      departureTerminal
                      arrivalTerminal
                      stop
                      stopDetail {
                        code
                        city
                      }
                      infos {
                        label
                        value
                      }
                      amenities {
                        icon
                        label
                      }
                    }
                  }
                  passengers {
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
                      departureAirportID
                      arrivalAirportID
                      type
                      price
                      priceNumeric
                      detail
                      journeyID
                      itemID
                      key
                    }
                  }
                  insurances {
                    id
                    paidAmount
                    paidAmountNumeric
                    title
                    tagLine
                  }
                }
                voucher {
                  enableVoucher
                  isCouponActive
                  defaultPromo
                  autoApply {
                    success
                    code
                    isCoupon
                    discountAmount
                    discountPrice
                    discountedAmount
                    discountedPrice
                    titleDescription
                    messageSuccess
                    promoID
                  }
                }
              }
            }
        }
    """.trimIndent()

    val QUERY_CHECK_VOUCHER = """
        query flightVoucher(${'$'}cartID: String!, ${'$'}voucherCode: String!){
            flightVoucher(cartID: ${'$'}cartID, voucherCode: ${'$'}voucherCode) {
                voucherCode
                UserID
                DiscountAmount
                DiscountAmountPlain
                CashbackAmount
                CashbackAmountPlain
                DiscountedPrice
                DiscountedPricePlain
                Message
                VoucherAmount
                TitleDescription
                IsCoupon
            }
        }
    """.trimIndent()

    val QUERY_VERIFY_CART = """
        mutation VerifyFlight(${'$'}data: FlightVerifyArgs!) {
          flightVerify(input:${'$'}data) {
            data {
              cartItems {
                productID
                quantity
                metaData {
                  cartID
                  invoiceID
                }
                configuration {
                  price
                }
                newPrice {
                  id
                  fare {
                    adult
                    child
                    infant
                    adultNumeric
                    childNumeric
                    infantNumeric
                  }
                }
                oldPrice
                oldPriceNumeric
                priceDetail {
                  label
                  price
                  priceNumeric
                }
              }
              promo {
                code
                discount
                discountNumeric
                cashback
                cashbackNumeric
                message
              }
            }
            meta {
              needRefresh
              refreshTime
              maxRetry
            }
          }
        }
    """.trimIndent()

    val QUERY_CHECKOUT_CART = """
        mutation CheckoutFlight(${'$'}data: FlightCheckoutArgs!) {
          flightCheckout(input:${'$'}data) {
            id
            redirectURL
            callbackURLSuccess
            callbackURLFailed
            queryString
            parameter {
              merchantCode
              profileCode
              transactionID
              transactionCode
              transactionDate
              customerName
              customerEmail
              amount
              currency
              itemsName
              itemsQuantity
              itemsPrice
              signature
              language
              userDefinedValue
              nid
              state
              fee
              paymentsAmount
              paymentsName
              pid
              customerMsisdn
            }
            thanksURL
          }
        }
    """.trimIndent()
}