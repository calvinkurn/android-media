package com.tokopedia.home.beranda.helper.benchmark

//Please refer to this https://developer.android.com/topic/performance/tracing/command-line

//Viewholders
const val TRACE_ON_BIND_BANNER_VIEWHOLDER = "onBind.BannerViewHolder"
const val TRACE_ON_BIND_DYNAMIC_ICON_VIEWHOLDER = "onBind.DynamicIconSectionViewHolder"
const val TRACE_ON_BIND_OVO_VIEWHOLDER = "onBind.OvoViewHolder"
const val TRACE_ON_BIND_OVO_CUSTOMVIEW = "onBind.OvoCustomView"
const val TRACE_ON_BIND_DYNAMIC_CHANNEL = "onBind.dynamicChannel_"
const val TRACE_ON_BIND_HEADER_OVO = "onBind.HomeHeaderOvoViewHolder"
const val TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW = "onBind.BalanceWidgetView"

//HomeCachedDataSource class
const val TRACE_GET_CACHED_DATA_SOURCE = "HomeCachedDataSource.getCachedHomeData"
const val TRACE_SAVE_TO_DATABASE = "HomeCachedDataSource.saveToDatabase"

//Converters
const val TRACE_STRING_TO_HOME_DATA = "Converters.StringtoHomeData"
const val TRACE_HOME_DATA_TO_STRING = "Converters.HomeDataToString"

//HomeMapper
const val TRACE_MAP_TO_HOME_VIEWMODEL = "HomeDataMapper.mapToHomeViewModel"
const val TRACE_MAP_TO_HOME_VIEWMODEL_REVAMP = "HomeDataMapper.mapToHomeRevampViewModel"

//HomeFragment
const val TRACE_INFLATE_HOME_FRAGMENT = "inflate.HomeFragment"
