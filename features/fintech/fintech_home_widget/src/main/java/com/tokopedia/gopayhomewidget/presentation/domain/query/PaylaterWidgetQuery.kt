package com.tokopedia.gopayhomewidget.presentation.domain.query

const val GQL_QUERY_PAYLATER_WIDGET_DATA = """query paylater_getHomeWidget(${'$'}req: PaylaterGetHomeWidgetRequest!) {
  	paylater_getHomeWidget(request: ${'$'}req) {
      show
      image_dark
      image_light
      description
      button {
       button_name
       cta_type
       web_url
       apps_url
      }
    }
}"""