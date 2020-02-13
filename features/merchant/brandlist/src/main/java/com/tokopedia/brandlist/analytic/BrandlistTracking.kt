package com.tokopedia.brandlist.analytic

class BrandlistTracking(context: Context) {
  
  private var trackingQueue = TrackingQueue(context)

  private val EVENT = "event"
  private val EVENT_CATEGORY = "eventCategory"
  private val EVENT_ACTION = "eventAction"
  private val EVENT_LABEL = "eventLabel"

  private val ECOMMERCE = "ecommerce"
  private val CLICK = "click"
  private val IMPRESSION = "impression"
  private val ECOMMERCE_IMPRESSIONS = "impressions"
  private val ECOMMERCE_CURRENCY_CODE = "currencyCode"

  private val PROMO_CLICK = "promoClick"
  private val PROMO_VIEW = "promoView"
  private val PROMOTIONS = "promotions"

  private val EVENT_VALUE = "clickOSAllBrands"
  private val EVENT_CATEGORY_VALUE = "official store all brands page"

  fun clickSearchBox() {
    val data = DataLayer.mapOf(
      EVENT, EVENT_VALUE,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click search - {search result/no search result}"
      EVENT_LABEL, "{keyword}"
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickBrandOnSearchBox() {
    val data = DataLayer.mapOf(
      EVENT, EVENT_VALUE,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - shop - {optional parameter} - {login/non login}"
      EVENT_LABEL, "{shop_id} - {keyword}"
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickCategory() {
    val data = DataLayer.mapOf(
      EVENT, EVENT_VALUE,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{current category tab}}",
      EVENT_ACTION, "click all brands page category tab"
      EVENT_LABEL, "{{clicked category tab}}"
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickBrandPilihan() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_CLICK,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - shop - brand of choice list - {login/non login}"
      EVENT_LABEL, "{shopId}",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - brand of choice list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun impressionBrandPilihan() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_VIEW,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "impression - shop - brand of choice list - {login/non login}"
      EVENT_LABEL, "shop impression",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - brand of choice list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickLihatSemua() {
    val data = DataLayer.mapOf(
      EVENT, EVENT_VALUE,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - brand pilihan - view all"
      EVENT_LABEL, "{login/non login}"
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickBrandPopular() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_CLICK,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - shop - popular brand list - {login/non login}"
      EVENT_LABEL, "{shop_id}",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - popular brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun impressionBrandPopular() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_VIEW,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "impression - shop - popular brand list - {login/non login}"
      EVENT_LABEL, "shop impression",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - popular brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickBrandBaruTokopedia() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_CLICK,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - shop - new brand list - {login/non login}"
      EVENT_LABEL, "{shop id}",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - new brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun impressionBrandBaru() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_VIEW,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "impression - shop - new brand list - {login/non login}"
      EVENT_LABEL, "shop impression",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - new brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun clickBrand() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_CLICK,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "click - shop - all brand list - {search result/not search result} - {login/non login}"
      EVENT_LABEL, "{shop id} - {search result/ not search result} - {keyword}",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - {search result/ not search result} - {keyword/NaN} - all brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

  fun impressionBrand() {
    val data = DataLayer.mapOf(
      EVENT, PROMO_VIEW,
      EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - {{category tab}}",
      EVENT_ACTION, "impression - shop - all brand list - {login/non login}"
      EVENT_LABEL, "{search result/ not search result} - {keyword}",
      ECOMMERCE, DataLayer.mapOf(
        PROMO_CLICK, DataLayer.mapOf(
          PROMOTIONS, DataLayer.listOf(
            DataLayer.mapOf(
              "id", "{{shopId}}",
              "name", "/officialstore/brand/{category tab name} - {search result/ not search result} - {keyword/NaN} - all brand list",
              "position", "{{shoplogo_position}}"
              "creative", "{{shop_name}}"
              "creative_url", "{{image_url}}"
            )
          )
        )
      )
    )
    trackingQueue.putEETracking(data as HashMap<String, Any>)
  }

}