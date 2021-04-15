package com.tokopedia.hotel.common.util

object HotelGqlQuery {
    val PROPERTY_REVIEW = """
        query propertyReview(${'$'}data:PropertyReviewRequest!){
          propertyReview(input:${'$'}data){
            item{
              id
              propertyId
              reviewerId
              name
              score
              headline
              pros
              cons
              createTime
              country
            }
            totalReview
            totalReviewByCountry
            averageScoreReview
            hasNext
            headline
          }
        }
    """.trimIndent()

    val PROPERTY_SEARCH = """
        query PropertySearch(${'$'}data:PropertySearchRequest!){
            propertySearch(input:${'$'}data){
              propertyList {
                id
                name
                type
                address
                roomPrice{
                  price
                  priceAmount
                  totalPrice
                  totalPriceAmount
                  extraCharges{
                    netPrice
                    netPriceAmount
                    extraChargeInfo{
                      name
                      excluded
                      type
                      price
                      priceAmount
                    }
                  }
                  deals {
                    tagging
                    price
                    priceAmount
                  }
                }
                roomAvailability
                image {
                  isLogoPhoto
                  urlSquare60
                  mainPhoto
                  urlOriginal
                  urlMax300
                }
                star
                features
                review {
                  reviewScore
                  reviewDescription
                }
                location {
                  cityName
                  description
                  latitude
                  longitude
                }
                isDirectPayment
                safetyBadge {
                    show
                    title
                    content
                    icon {
                      dark
                      light
                    }
                  }
              }
              propertyDisplayInfo{
                filter{
                  filterPrice {
                    minPrice
                    maxPrice
                  }
                  filterAccomodation {
                    ID
                    name
                    displayName
                  }
                  filterPreferences {
                    ID
                    name
                    displayName
                    type
                  }
                  filterStar{
                    stars
                  }
                  filterReview {
                    minReview
                    maxReview
                  }
                }
                sort {
                  name
                  displayName
                }
              }
              filters {
                      type
                      name
                      displayName
                      options
                      image {
                          light
                          dark
                      }
                  }
              quickFilter {
                   name
                   displayName
                   values
                   selected
               }
            }
        }
    """.trimIndent()

    val DESTINATION_SEARCH = """
        query suggestion(${'$'}data: PropertySuggestionSearchParam!) {
          propertySearchSuggestion(input:${'$'}data) {
            data {
              id
              type
              typeID
              tag
              icon
              iconURL
              name
              location
              hotelCount
              searchType
              searchID
            }
            meta {
              keyword
              totalSuggestion
            }
          }
        }
    """.trimIndent()

    val GET_CART = """
        query PropertyCart(${'$'}data: PropertyGetCartRequest!) {
          status
          propertyGetCart(input:${'$'}data) {
            cartID
            cart {
              adult
              checkIn
              checkOut
              rooms {
                roomID
                numOfRooms
              }
              guestName
              specialRequest
              totalPrice
              totalPriceAmount
              currency
              localTotalPrice
              localTotalPriceAmount
              localCurrency
              fares {
                type
                description
                priceAmount
                price
                localPriceAmount
                localPrice
              }
              contact {
                name
                email
                phone
                phoneCode
              }
              roomContent
            }
            property {
              propertyID
              name
              type
              address
              propertyImage {
                urlSquare60
                urlOriginal
                urlMax300
              }
              star
              paymentNote
              checkInFrom
              checkInTo
              checkOutFrom
              checkOutTo
              isDirectPayment
              isDirectPaymentString
              room {
                roomID
                isBreakFastIncluded
                maxOccupancy
                roomName
                isNeedDeposit
                isRefundAble
                isCCRequired
                paymentTerms {
                  name
                  cancellationDescription
                  prepaymentDescription
                }
                roomPolicies {
                  content
                  policyType
                }
                cancellationPolicies {
                  title
                  content
                  isClickable
                  details{
                    shortTitle
                    longTitle
                    shortDesc
                    longDesc
                    isActive
                }
                }
              }
            }
            appliedVoucher {
                  code
                  discount
                  discountAmount
                  cashback
                  cashbackAmount
                  message
                  titleDescription
                  isCoupon
            }
          }
        }
    """.trimIndent()

    val DEFAULT_HOMEPAGE_PARAMETER = """
        query propertyDefaultHome{
            propertyDefaultHome{
                data {
                    label
                    searchType
                    searchID
                    checkIn
                    checkOut
                    totalRoom
                    totalGuest
                }
                meta {
                    source
                }
            }
        }
    """.trimIndent()

    val HOME_PROMO = """
        query getBanner {
         travelBanner(languageID: 0, countryID: "ID", deviceID: 4, instanceID: 4) {
           id
           attributes {
             description
             linkURL
             imageURL
             promoCode
           }
         }
        }
    """.trimIndent()

    val PROPERTY_DETAIL = """
        query propertyDetail(${'$'}data:PropertyDetailRequest!){
        propertyDetail(input:${'$'}data){
          property {
            id
            regionId
            districtId
            typeId
            typeName
            name
            slug
            address
            zipCode
            email
            phoneNumber
            latitude
            longitude
            locationImageStatic
            images {
              isLogoPhoto
              urlSquare60
              mainPhoto
              urlOriginal
              urlMax300
            }
            isClosed
            checkinFrom
            checkinTo
            checkinInfo
            checkoutFrom
            checkoutTo
            checkoutInfo
            star
            description
            importantInformation
            welcomeMessage
            licenseNumber
            chainId
            currency
            boost
            cityId
          }
          city {
            id
            name
            countryName
          }
          region {
            id
            name
            countryName
          }
          district {
            id
            cityId
            latitude
            longitude
            name
          }
          facility {
            groupName
            groupIconUrl
            item {
              id
              name
              icon
              availability
              iconUrl
            }
          }
          mainFacility {
            id
            name
            icon
            iconUrl
            availability
          }
          propertyPolicy {
            name
            content
            icon
            iconUrl
            propertyPolicyId
          }
          safetyBadge {
            show
            title
            content
            icon {
              dark
              light
            }
          }
        }
        }
    """.trimIndent()

    val ORDER_DETAILS = """
        query OrderDetailsQuery(${'$'}orderCategoryStr: String,${'$'}orderId: String) {
          status
          orderDetails(orderCategoryStr:${'$'}orderCategoryStr, orderId:${'$'}orderId) {
            status {
              status
              statusText
              iconUrl
              textColor
              backgroundColor
              fontSize
            }
            title {
              label
              value
            }
            invoice {
              invoiceRefNum
              invoiceUrl
            }
            conditionalInfo{
              title
            }
            conditionalInfoBottom{
              title
            }
            actionButtons {
              label
              buttonType
              uri
              weight
              uriWeb
            }
            payMethod {
              label
              value
            }
            pricing {
              label
              value
              textColor
              backgroundColor
              imageUrl
            }
            paymentsData {
              id
              label
              value
              textColor
              backgroundColor
              imageUrl
            }
            contactUs {
              helpText
              helpUrl
            }
            hotelTransportDetails{
              paymentType
              isShowEVoucher
              guestDetail{
                title
                content
              }
              propertyDetail{
                propertyCountry
                appLink
                bookingKey{
                  title
                  content
                }
                image{
                  urlMax360
                  urlSquare60
                  urlOriginal
                }
                checkInOut{
                  title
                  content
                  checkInOut{
                    day
                    date
                    time
                  }
                }
                stayLength{
                  title
                  content
                }
                propertyInfo{
                  types
                  name
                  address
                  starRating
                }
                room{
                  title
                  content
                  amenities{
                    content
                  }
                }
                specialRequest{
                  title
                  content
                }
                extraInfo {
                  content
                  uri
                  uriWeb
                  isClickable
                  title
                  shortDesc
                  longDesc
                 }
              }
              cancellationPolicies{
                title
                content
                isClickable
                policies{
                    shortTitle
                    longTitle
                    shortDesc
                    longDesc
                    active
                }
              }
              contactInfo{
                number
              }
            }
          }
        }
    """.trimIndent()

    val RECENT_SEARCH_DATA = """
        query{
          TravelCollectiveRecentSearches(product:HOTEL, platform: HOTELPAGE){
            items {
              product
              title
              subtitle
              prefix
              prefixStyling
              value
              webURL
              appURL
              imageURL
            }
            meta {
              title
              webURL
              appURL
            }
          }
        }
    """.trimIndent()

    val PROPERTY_ROOM_LIST = """
        query PropertySearchRoom(${'$'}data:PropertySearchRoomRequest!){
          propertySearchRoom(input:${'$'}data){
            propertyID
            rooms{
              ID
              available
              roomQtyRequired
              breakfastInfo{
                isBreakFastIncluded
                mealPlan
                breakFast
                icon
                iconUrl
              }
              occupancyInfo{
                maxOccupancy
                maxFreeChild
                occupancyText
              }
              depositInfo{
                isNeedDeposit
                depositText
              }
              refundInfo{
                isRefundAble
                refundStatus
                icon
                iconUrl
              }
              creditcardInfo{
                isCCRequired
                isCCDomesticRequired
                header
                info
              }
              numberRoomLeft
              roomPrice{
                price
                priceAmount
                totalPrice
                totalPriceAmount
                extraCharges{
                  netPrice
                  netPriceAmount
                  extraChargeInfo{
                    name
                    excluded
                    type
                    price
                    priceAmount
                  }
                }
                deals {
                  tagging
                  price
                  priceAmount
                }
              }
              roomPolicy{
                class
                content
              }
              cancelPolicy{
                subHeader
                content
              }
              refundableUntil
              roomInfo{
                description
                name
                bathRoomCount
                size
                maxGuest
                facility{
                  name
                  icon
                  iconUrl
                }
                mainFacility{
                  name
                  icon
                  iconUrl
                }
                roomImages{
                  urlMax300
                  urlOriginal
                  urlSquare
                }
                maxPrice
                minPrice
              }
              bedInfo
              taxes
              extraBedInfo{
                IsFreeExtraBed
                Content
              }
              isDirectPayment
              isDirectPaymentString
            }
            isAddressRequired
            isCvCRequired
            isDirectPayment
            isEnabled
            deals {
              tagging
              price
              priceAmount
            }
          }
        }
    """.trimIndent()

    val TOKOPOINTS_SUM_COUPON = """
        query {
          tokopointsSumCoupon (categoryID: 51) {
            sumCoupon
            sumCouponStr
            sumCouponUnitOpt
          }
        }
    """.trimIndent()
}