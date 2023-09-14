package com.tokopedia.oldcatalog.model.raw.gql

const val GQL_CATALOG_REIMAGINE_QUERY = """query catalogGetDetailModular(${'$'}catalog_id: String!,${'$'}comparison_id: String!,${'$'}user_id: String!, ${'$'}device: String!, ${'$'}preferVersion: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id,comparison_id: ${'$'}comparison_id,user_id: ${'$'}user_id, device: ${'$'}device, preferVersion: ${'$'}preferVersion) {
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
    layout{
      name
      type
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
        ... on CatalogCompCharacteristic {
          characteristic {
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
      }
    }
  }
}
"""
