package com.tokopedia.hotel.common.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.hotel.common.util.HotelCancelVoucherQuery.HOTEL_CANCEL_VOUCHER
import com.tokopedia.hotel.common.util.HotelDestinationSearchQuery.DESTINATION_SEARCH
import com.tokopedia.hotel.common.util.HotelGetCancellationQuery.GET_CANCELLATION
import com.tokopedia.hotel.common.util.HotelGetCartQuery.GET_CART
import com.tokopedia.hotel.common.util.HotelGetPopularPropertyQuery.GET_POPULAR_PROPERTY_QUERY
import com.tokopedia.hotel.common.util.HotelGetRecentSearchQuery.GET_HOTEL_RECENT_SEARCH_QUERY
import com.tokopedia.hotel.common.util.HotelHomepageDefaultParameterQuery.DEFAULT_HOMEPAGE_PARAMETER
import com.tokopedia.hotel.common.util.HotelNearbyLandmarksQuery.HOTEL_NEARBY_LANDMARKS
import com.tokopedia.hotel.common.util.HotelOrderDetailQuery.ORDER_DETAILS
import com.tokopedia.hotel.common.util.HotelPropertyDetailQuery.PROPERTY_DETAIL
import com.tokopedia.hotel.common.util.HotelPropertyReviewQuery.PROPERTY_REVIEW
import com.tokopedia.hotel.common.util.HotelPropertyRoomListQuery.PROPERTY_ROOM_LIST
import com.tokopedia.hotel.common.util.HotelPropertySearchQuery.PROPERTY_SEARCH
import com.tokopedia.hotel.common.util.HotelRecentSearchDataQuery.RECENT_SEARCH_DATA
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst

@GqlQuery("QueryHotelPropertyReview", PROPERTY_REVIEW)
internal object HotelPropertyReviewQuery {
    const val PROPERTY_REVIEW = """
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
    """
}

@GqlQuery("QueryHotelPropertySearch", PROPERTY_SEARCH)
internal object HotelPropertySearchQuery {
    const val PROPERTY_SEARCH = """
        query PropertySearch(${'$'}data:PropertySearchRequest!) {
        propertySearch(input: ${'$'}data){
        propertyList {
            id
            name
            type
            address
            roomPrice {
                price
                priceAmount
                totalPrice
                totalPriceAmount
                extraCharges {
                    netPrice
                    netPriceAmount
                    extraChargeInfo {
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
        propertyDisplayInfo {
            filter {
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
                filterStar {
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
        source
    }
    }
    """
}

@GqlQuery("QueryHotelDestinationSearch", DESTINATION_SEARCH)
internal object HotelDestinationSearchQuery {
    const val DESTINATION_SEARCH = """
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
            source
          }
        }
    """
}

@GqlQuery("QueryHotelGetCart", GET_CART)
internal object HotelGetCartQuery {
    const val GET_CART = """
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
    """
}

@GqlQuery("QueryHotelHomepageDefaultParameter", DEFAULT_HOMEPAGE_PARAMETER)
internal object HotelHomepageDefaultParameterQuery {
    const val DEFAULT_HOMEPAGE_PARAMETER = """
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
    """
}

@GqlQuery("QueryHotelPropertyDetail", PROPERTY_DETAIL)
internal object HotelPropertyDetailQuery {
    const val PROPERTY_DETAIL = """
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
    """
}

@GqlQuery("QueryHotelOrderDetail", ORDER_DETAILS)
internal object HotelOrderDetailQuery {
    const val ORDER_DETAILS = """
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
              contactButtonWording
              tickerContactHotel
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
            agent {
              name
              logo
            }
          }
        }
    """
}

@GqlQuery("QueryHotelRecentSearchData", RECENT_SEARCH_DATA)
internal object HotelRecentSearchDataQuery {
    const val RECENT_SEARCH_DATA = """
        query TravelCollectiveRecentSearches() {
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
    """
}

@GqlQuery("QueryHotelPropertyRoomList", PROPERTY_ROOM_LIST)
internal object HotelPropertyRoomListQuery {
    const val PROPERTY_ROOM_LIST = """
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
    """
}

@GqlQuery("QueryHotelNearbyLandmarks", HOTEL_NEARBY_LANDMARKS)
internal object HotelNearbyLandmarksQuery {
    const val HOTEL_NEARBY_LANDMARKS = """
        query propertySearchPlace(${'$'}data :PropertySearchPlaceRequest!){
          propertySearchPlace(input:${'$'}data){
            result{
              type
              header
              icon
              places{
                name
                icon
                geoLocation{
                  latitude
                  longitude
                }
                distance
              }
            }
            information
          }
        }
    """
}

@GqlQuery("QueryHotelGetPopularProperty", GET_POPULAR_PROPERTY_QUERY)
internal object HotelGetPopularPropertyQuery {
    const val GET_POPULAR_PROPERTY_QUERY = """
        query {
          propertyPopular {
            name
            destinationID
            type
            subLocation
            image
            metaDescription
            searchID
          }
        }
    """
}

@GqlQuery("QueryHotelGetRecentSearch", GET_HOTEL_RECENT_SEARCH_QUERY)
internal object HotelGetRecentSearchQuery {
    const val GET_HOTEL_RECENT_SEARCH_QUERY = """
        query propertyRecentSearch(${'$'}id: Int!){
         status
         travelRecentSearch(dataType:HOTEL, userID:${'$'}id){
           UUID
           property {
             type
             value
             ID
             location{
               district
               region
               city
               country
             }
           }
           startTime
           endTime
           lastSearch
           customer {
             adult
             child
             class
             infant
             room
           }
         }
        }
    """
}

@GqlQuery("QueryHotelGetCancellation", GET_CANCELLATION)
internal object HotelGetCancellationQuery {
    const val GET_CANCELLATION = """
        query getCancellation(${'$'}data: PropertyGetCancellationRequest!){
          propertyGetCancellation(input: ${'$'}data        ){
            data {
                cancelCartID
                cancelCartExpiry
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
                    checkInOut{
                        title
                        checkInOut {
                          day
                          date
                          time
                        }
                    }
                    stayLength
                    isDirectPayment
                    room {
                        isBreakFastIncluded
                        maxOccupancy
                        roomName
                        roomContent
                        isRefundAble
                        isCCRequired
                    }
                }
                cancelPolicy {
                    title
                    policy {
                        title
                        desc
                        active
                        feeInLocalCurrency {
                            amountStr
                            amount
                            currency
                        }
                        fee {
                            amountStr
                            amount
                            currency
                        }
                        styling
                    }
                }
                cancelInfo {
                    desc
                    isClickable
                    longDesc {
                        title
                        desc
                    }
                }
                payment {
                    title
                    detail {
                        title
                        amount
                    }
                    summary {
                        title
                        amount
                    }
                    footer {
                        desc
                        links
                    }
                }
                reasons {
                    id
                    title
                    freeText
                }
                footer{
                    desc
                    links
                }
                confirmationButton {
                    title
                    desc
                }
          }
            meta{
              invoiceID
            }
            content {
              success
              title
              desc
                actionButton {
                   label
                   buttonType
                   URI
                   URIWeb
                }
            }
          }
        }
    """
}

@GqlQuery("QueryHotelCancelVoucher", HOTEL_CANCEL_VOUCHER)
internal object HotelCancelVoucherQuery {
    const val HOTEL_CANCEL_VOUCHER = PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CANCEL_VOUCHER
}
