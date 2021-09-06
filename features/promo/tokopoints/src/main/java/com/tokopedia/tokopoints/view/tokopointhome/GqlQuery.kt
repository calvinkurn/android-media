package com.tokopedia.tokopoints.view.tokopointhome

const val TP_HOMEPAGE_SECTION = """
query  tokopointsHomepage(${'$'}apiVersion: String!) {
    tokopointsHomepage(apiVersion: ${'$'}apiVersion) {
    sectionContent {
      layoutType
      sectionTitle
      sectionSubTitle
      backgroundImgURLMobile
      countdownAttr {
        showTimer
        activeCountDown
        expiredCountDown
      }
      cta {
        icon
        text
        url
        appLink
        type
      }
      layoutCategoryAttr {
        categoryTokopointsList {
          id
          iconImageURL
          text
          url
          appLink
          isNewCategory
        }
      }
      layoutCouponAttr {
        couponList {
          id
          promoID
          code
          imageURL
          imageURLMobile
          redirectURL
          redirectAppLink
          usage {
            activeCountdown
            expiredCountdown
            text
            usageStr
          }
          minimumUsage
          minimumUsageLabel
        }
      }
      layoutBannerAttr {
        bannerType
        imageList {
          imageURL
          imageURLMobile
          redirectURL
          redirectAppLink
          title
          subTitle
          inBannerTitle
          inBannerSubTitle
        }
      }
      layoutCatalogAttr {
        catalogList {
          id
          promoID
          expired
          points
          title
          subtitle
          expiredLabel
          expiredStr
          isDisabled
          disableErrorMessage
          thumbnailURL
          thumbnailURLMobile
          imageURL
          imageUrlMobile
          slug
          baseCode
          pointsStr
          isGift
          upperTextDesc
          isDisabledButton
          catalogType
          pointsSlash
          pointsSlashStr
          discountPercentage
          discountPercentageStr
          isShowTukarButton
          quotaPercentage
        }
        countdownInfo{
          isShown
          type
          label
          countdownUnix
          countdownStr
          textColor
          backgroundColor
        }
      }
      layoutTickerAttr {
        tickerList {
          id
          type
          metadata {
            image {
              url
            }
            text {
              content
              color
              format
            }
            link {
              url
              applink
            }
          }
        }
      }
       layoutTopAdsAttr {
       jsonTopAdsDisplayParam
      }
      layoutMerchantCouponAttr {
        topAdsJsonParam
        CatalogMVCWithProductsList {
           shopInfo {
            id
            name
            iconUrl
            url
            appLink
            shopStatusIconURL
           }
        title
        maximumBenefitAmountStr
        subtitle
        adInfo {
            AdID
            AdViewUrl
            AdClickUrl
           }
        products {
        id
        imageURL
        name
        redirectURL
        redirectAppLink
        benefitLabel
        category {
             id
             name
             rootID
             rootName
               }
            }
         }
       }
    }
  }
  }

"""