package com.tokopedia.oldcatalog.model.raw.gql

const val GQL_CATALOG_REIMAGINE_QUERY = """query catalogGetDetailModular(${'$'}catalog_id: String!,${'$'}comparison_id: String!,${'$'}user_id: String!, ${'$'}device: String!, ${'$'}preferVersion: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id,comparison_id: ${'$'}comparison_id,user_id: ${'$'}user_id, device: ${'$'}device, preferVersion: ${'$'}preferVersion) {
    version
    globalStyle {
      darkMode
      presetKey
      bgColor
      primaryColor
      secondaryColor
    }
    basicInfo{
      id
      departmentId
      name
      brand
      tag
      description
      shortDescription
      url
      mobileUrl
      productSortingStatus
      catalogImage {
        imageUrl
        isPrimary
      }
      marketPrice {
        min
        max
        minFmt
        maxFmt
        date
        name
      }
      longDescription {
        title
        description
      }
    }
    comparisonInfo{
      id
      name
      brand
      url
      catalogImage {
        imageUrl
        isPrimary
      }
      marketPrice {
        min
        max
        minFmt
        maxFmt
        date
        name
      }
      fullSpec {
        name
        icon
        row {
          key
          value
        }
      }
      topSpec {
        key
        value
        icon
      }
    }
    components{
      id
      name
      type
      sticky
      data{
        ... on CatalogModularVideo{
          url
          type
          thumbnail
          author
          title
          videoId
        }
        ... on CatalogModularTopSpec {
          key
          value
          icon
        }
        ... on CatalogModularSpecification {
          name
          icon
          row{
            key
            value
          }
        }
        ... on CatalogModularRecommendation{
          id
          name
          brand
          catalogImage {
            imageUrl
            isPrimary
          }
          marketPrice {
            min
            max
            minFmt
            maxFmt
            date
            name
          }
          topSpec {
            key
            value
            icon
          }
          fullSpec {
            name
            icon
            row {
              key
              value
            }
          }
        }
        ... on CatalogModularComparisonNew {
          spec_list {
            title
            sub_card {
              sub_title
              left_data
              right_data
            }
          }
          compared_data {
            id
            name
            brand
            url
            catalogImage {
              imageUrl
              isPrimary
            }
            marketPrice {
              min
              max
              minFmt
              maxFmt
              date
              name
            }
          }
        }
        ... on CatalogModularProductReview{
          avgRating
          totalHelpfulReview
          reviews {
            rating
            informativeScore
            reviewerName
            reviewDate
            reviewText
            reviewImageUrl
            reviewId
            productUrl
          }
        }
        ... on CatalogLibraryEntrypointResponse {
          category_name
          category_identifier
          catalog_count
          catalogs {
            id
            name
            brand
            brand_id
            categoryID
            imageUrl
            url
            mobileUrl
            applink
            marketPrice {
              min
              max
              minFmt
              maxFmt
            }
          }
        }
      }
    }
    layout{
      name
      type
      position
      data {
        ... on CatalogCompHero {
          hero {
            name
            brandLogoUrl
            heroSlide {
              videoUrl
              imageUrl
              subtitle
            }
          }
          style {
            isHidden
            isPremium
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompFeatureTop {
          topFeature {
            iconUrl
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompTrustmaker {
          trustmaker {
            imageUrl
            title
            subtitle
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompBannerSingle {
          singleBanner {
            imageUrl
          }
          style {
            isHidden
            bannerRatio
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompBannerDouble {
          doubleBanner {
            imageUrl
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompPanelImage {
          imagePanel {
            imageUrl
            title
            subtitle
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompNavigation {
          navigation {
            title
            eligibleNames
          }
          style {
            isHidden
          }
        }
        ... on CatalogCompSliderImage {
          imageSlider {
            imageUrl
            title
            subtitle
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompText {
          text {
            title
            subtitle
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompReviewExpert {
          expertReview {
            name
            title
            imageUrl
            review
            videoUrl
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
                ... on CatalogCompComparison {
          section {
            title
          }
          comparison {
            id
            name
            url
            mobileUrl
            applink
            catalogImage {
              imageUrl
              isPrimary
            }
            marketPrice {
              max
              min
              minFmt
              maxFmt
              date
              name
            }
            fullSpec {
              name
              icon
              row {
                key
                value
                flags
              }
            }
            topSpec {
              key
              value
              icon
            }
          }
          style {
            isHidden
          }
        }
        ... on CatalogCompFeatureSupport {
          section {
            title
          }
          supportFeature {
            iconUrl
            title
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompAccordion {
          section {
            title
          }
          accordion {
            title
            desc
          }
          style {
            isHidden
          }
          navInfo {
            title
          }
        }
        ... on CatalogCompInfoColumn {
          section {
            title
          }
          infoColumn {
            name
            row {
              key
              value
            }
          }
          style {
            isHidden
            columnType
          }
        }
        ... on CatalogCompVideo {
          video {
            url
            type
            thumbnail
            author
            title
            videoId
          }
          style {
            isHidden
          }
        }
        ... on CatalogCompCtaPrice {
          basicInfo {
            name
            marketPrice {
              minFmt
              maxFmt
            }
          }
          style {
            isHidden
            isSticky
          }
        }
        ... on CatalogCompTopSellerCard {
          topSeller {
            productID
            warehouseID
            isVariant
            stock {
              soldPercentage
              isHidden
              wording
            }
            shop {
              id
              name
              city
              badge
              stats {
                chatEta
                orderProcessEta
              }
            }
            price {
              text
              original
            }
            credibility {
              rating
              sold
              ratingCount
            }
            mediaUrl {
              image300
            }
            delivery {
              eta
            }
            additionalService {
              name
            }
            paymentOption {
              iconUrl
              title
              desc
            }
            labelGroups {
              position
              title
              url
              styles {
                key
                value
              }
            }
          }
          style {
            isHidden
          }
        }
        ... on CatalogCompTopSellerHanging {
          topSeller {
            productID
            warehouseID
            isVariant
            shop {
              id
              name
              badge
            }
            price {
              text
              original
            }
            credibility {
              rating
              sold
            }
          }
          style {
            isHidden
            isSticky
          }
        }
        ... on CatalogCompReviewBuyer {
          section {
            title
          }
          buyerReviewSummary {
            avgRating
            totalHelpfulReview
          }
          buyerReviewList {
            rating
            informativeScore
            reviewerName
            reviewDate
            reviewText
            reviewImageUrl
            reviewId
			productId
			productIdString
			productUrl
			reviewerStamp
			reviewerProfilePicture
			productVariantName
			shopID
			shopName
			shopUrl
			shopBadge
            imageAttachments {
              fullsizeUrl
              thumbnailUrl
              attachmentID
            }
			userStats {
              key
              count
            }
          }
          style {
            isHidden
            maxDisplay
          }
        }
      }
    }
  }
}
"""
