package com.tokopedia.salam.umrah.common.util

object UmrahQuery {

    fun umrahHomePageBanner()="""
        {
         umrahBanners(params: {service: "UMRAHCATALOG", limit: 20, location: "homepage"}){
            id
            service
            location
            imageUrl
            webUrl
            applinkUrl
            weight
          }
        }
    """.trimIndent()

    fun umrahHomePageCategory()="""
        {
          umrahCategories(params: {limit: 5}) {
            id
            title
            description
            coverImageURL
            slugName
            startingPrice
            products {
              id
              availableSeat
              travelAgent {
                id
              }
              title
              description
              originalPrice
              downPaymentPrice
              departureCity {
                id
                name
                countryCode
              }
              arrivalCity {
                id
                name
                countryCode
              }
              returnFromCity {
                id
                name
                countryCode
              }
              transitCity {
                id
                name
                countryCode
              }
              departureDate
              returningDate
              durationDays
              hotels {
                id
                imageUrl
                name
                rating
                poiDistances
              }
              airlines {
                id
                name
                logoUrl
                type
              }
              facilities
              nonFacilities
              terms
              itineraries {
                day
                description
              }
              variant {
                id
                name
                price
              }
            }
          }
        }
    """.trimIndent()

    fun umrahHomePageFeatured()="""
        query UmrahCategoryProducts {
          umrahCategories(params: {flags: CATEGORY_FEATURED_ON_HOMEPAGE }) {
            id
            title
            products {
              id
              banner
              slugName
              travelAgent {
                id
                name
                logoUrl
              }
              title
              slashPrice
              originalPrice
              departureDate
              returningDate
              durationDays
              hotels {
                id
                imageUrl
                name
                rating
              }
              airlines {
                id
                name
                type
              }
            }
          }
        }
    """.trimIndent()

    fun umrahHomePageSearchParam()="""
        {
          umrahSearchParameter{
            departureCities{
              defaultOption
              options{
                displayText
                query
              }
            }
            departurePeriods{
              defaultOption
              options{
                displayText
                query
              }
            }
            sortMethods{
              defaultOption
              options{
                displayText
                query
              }
            }
            priceRangeLimit{
              minimum
              maximum
            }
            priceRangeOptions{
              defaultOption
              options{
                rangeDisplayText
                minimum
                maximum
              }
            }
            durationDaysRangeLimit{
              minimum
              maximum
            }
          }
        }
    """.trimIndent()

    fun umrahSearchProduct()="""
        query UmrahSearchProducts(${'$'}params: UmrahSearchProductInput!){
          umrahSearchProducts(params:${'$'}params) {
            ui{
              travelDates
              hotelStars
            }
            id
            banner
            slugName
            availableSeat
            travelAgent{
              id
            }
            title
            description
            originalPrice
            slashPrice
            downPaymentPrice
            departureCity{
              id
              name
              countryCode
            }
            arrivalCity{
              id
              name
              countryCode
            }
            returnFromCity{
              id
              name
              countryCode
            }
            transitCity {
              id
              name
              countryCode
            }
            departureDate
            returningDate
            durationDays
            hotels{
              id
              imageUrl
              name
              rating
              poiDistances
            }
            airlines{
              id
              name
              logoUrl
              type
            }
            facilities
            nonFacilities
            terms
            itineraries{
              day
              description
            }
            variant{
              id
              name
              price
            }
          }
        }
    """.trimIndent()

    fun umrahSayaList()="""
        query umrahWidgetUmrahSayaList {
          umrahWidgetUmrahSayaList{
            subHeader
            header
            nextActionText
            mainButton{
              text
              link
            }
          }
        }
    """.trimIndent()

    fun umrahTravelAgentGallery()="""
        query umrahGalleries(${'$'}params: UmrahGalleriesInput!){
          umrahGalleries(params:${'$'}params){
            id
            title
            subTitle
            description
            type
            createdAt
            medias{
              title
              source
              type
              thumbnail
            }
          }
        }
    """.trimIndent()

    fun umrahTravelbySlugName()="""
        query umrahTravelAgentBySlug(${'$'}slugName:String! ){
          umrahTravelAgentBySlug(slugName : ${'$'}slugName ){
            id
            slugName
            name
            location
            address
            phone
            email
            logoUrl
            permissionOfHajj
            permissionOfUmrah
            pilgrimsPerYear
            establishedSince
            description
            products{
                id
                title
                banner
                slugName
                originalPrice
                slashPrice
                downPaymentPrice
                durationDays
                departureDate
                returningDate
                ui{
                      travelDates
                      travelDurations
                      hotelStars
                      variant
                      transitCity
                      availableSeat
                    }
                airlines{
                      name
                      date
                      departureCity{
                        name
                      }
                      arrivalCity{
                        name
                      }
                      logoUrl
                    }
            }
            ui {
                establishedSince
                pilgrimsPerYear
            }
          }
        }
    """.trimIndent()

    fun umrahCommonTravelAgent()="""
        query umrahTravelAgents(${'$'}params : UmrahTravelAgentsInput!){
          umrahTravelAgents(params:${'$'}params){
            id
            name
            location
            address
            phone
            email
            logoUrl
            permissionOfHajj
            permissionOfUmrah
            pilgrimsPerYear
            establishedSince
            slugName
            ui {
                establishedSince
                pilgrimsPerYear
            }
          }
        }
    """.trimIndent()

    fun myUmrahByOrderId()="""
        query umrahWidgetUmrahSayaByOrderUUID(${'$'}orderId : ID!) {
          status
          umrahWidgetUmrahSayaByOrderUUID(params: {orderUUID:${'$'}orderId}){
            subHeader
            header
            nextActionText
            mainButton{
              text
              link
            }
          }
        }
    """.trimIndent()

    fun umrahOrderDetail()="""
        query OrderDetailsQuery(${'$'}orderCategoryStr: String, ${'$'}orderId : String) {
          orderDetails(orderCategoryStr: ${'$'}orderCategoryStr, orderId: ${'$'}orderId )  {
              invoice {
                  invoiceRefNum
                  invoiceUrl
              }
              conditionalInfo {
                           text
                           url
                           title
              }
              helpLink
              beforeOrderId
              title {
                  imageUrl
                  value
                  label
                  backgroundColor
                  textColor
              }
              actionButtons {
                  control
                  body {
                      body
                      appURL
                      webURL
                      method
                  }
                  name
                  weight
                  uri
                  buttonType
                  label
                  header
                  uriWeb
                  value
                  key
                  mappingUri
                  method
              }
              detail {
                  imageUrl
                  value
                  label
                  textColor
              }
              payMethod {
                  value
                  label
              }
              pricing {
                  imageUrl
                  value
                  label
                  backgroundColor
                  textColor
              }
              paymentsData {
                  imageUrl
                  value
                  label
                  backgroundColor
                  textColor
              }
              status {
                  status
                  statusText
                  iconUrl
                  statusLabel
                  fontSize
                  backgroundColor
                  textColor
              }
              contactUs {
                  helpUrl
                  helpText
              }
              items {
                  title
                  imageUrl
                  productUrl
                  metaData
              }
              passenger {
                  id
                  name
              }
              promoCode
          }
        }
    """.trimIndent()

    fun umrahPDP()="""
        query UmrahProduct(${'$'}slugName : String!){
          umrahProduct(slugName: ${'$'}slugName ){
            ui{
              travelDates
              travelDurations
              hotelStars
              variant
              transitCity
              availableSeat
            }
            id
            availableSeat
            title
            banner
            slugName
            originalPrice
            slashPrice
            downPaymentPrice
            travelAgent{
              name
              logoUrl
              permissionOfUmrah
              establishedSince
              pilgrimsPerYear
              slugName
              ui{
                  establishedSince
                  pilgrimsPerYear
               }
            }
            durationDays
            departureDate
            returningDate
            hotels{
              name
              checkIn
              checkOut
              imageUrl
              rating
              ui{
                distance
                stayDates
              }
            }
            airlines{
              name
              date
              departureCity{
                name
              }
              arrivalCity{
                name
              }
              logoUrl
            }
            itineraries{
              day
              description
            }
            featuredFacilities {
              iconUrl
              header
            }
            facilities
            nonFacilities
            transitCity{
              name
            }
            variant{
                id
                name
                description
                price
            }
            faq{
              allContentsLink
              contents{
                header
                snippet
                link
              }
            }
            terms
          }
        }
    """.trimIndent()

    fun umrahCheckoutTnc()="""
        query umrahTermsConditions(${'$'}params : UmrahTermsConditionsInput!){
          umrahTermsConditions(params: ${'$'}params ){
            header
            content
          }
        }
    """.trimIndent()

    fun umrahCheckoutGeneral()="""
        mutation checkout_general(${'$'}params : CheckoutGeneralParams) {
          checkout_general(params: ${'$'}params ) {
            header {
              process_time
              reason
              error_code
              messages
            }
            data {
              success
              error
              error_state
              message
              data {
                redirect_url
                callback_url
                query_string
                payment_type
                product_list {
                  id
                  name
                  quantity
                  price
                }
                parameter {
                  merchant_code
                  profile_code
                  customer_id
                  customer_name
                  customer_email
                  customer_msisdn
                  transaction_id
                  transaction_date
                  gateway_code
                  pid
                  nid
                  user_defined_value
                  amount
                  currency
                  language
                  signature
                  payment_metadata
                  merchant_type
                  back_url
                }
              }
            }
          }
        }
    """.trimIndent()

    fun umrahCheckoutPaymentOption()="""
        query umrahPaymentOptions(${'$'}params : UmrahPaymentOptionsInput!){
          umrahPaymentOptions(params:${'$'}params ){
            paymentOptions{
              price
              title
              desc
              defaultOption
              schemes {
                title
                price
                firstDueDate
                terms{
                  price
                  type
                  dueDate
                }
              }
            }
            defaultOption
          }
        }
    """.trimIndent()

    fun umrahCheckoutSummary()="""
        query umrahCheckoutSummary(${'$'}params : UmrahCheckoutSummaryInput!){
          umrahCheckoutSummary(params: ${'$'}params ){
            checkoutDetails{
              header
              subHeader
              amount
              __typename
            }
            total
          }
        }
    """.trimIndent()
}