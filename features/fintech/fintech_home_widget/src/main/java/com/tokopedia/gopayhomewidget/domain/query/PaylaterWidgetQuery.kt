package com.tokopedia.gopayhomewidget.domain.query

const val GQL_QUERY_PAYLATER_WIDGET_DATA =
        """query paylaterGetHomeWidget(${'$'}request: PaylaterGetHomeWidgetRequest!) {
  	paylater_getHomeWidget(request: ${'$'}request) {
      show
      image_dark
      image_light
      description
      case_type
      button {
       button_name
       cta_type
       web_url
       apps_url
      }
    }
}"""


const val GQL_QUERY_PAYLATER_WIDGET_CLOSE =
        """query  {
  	paylater_closeHomeWidget() {
     success
    }
}"""