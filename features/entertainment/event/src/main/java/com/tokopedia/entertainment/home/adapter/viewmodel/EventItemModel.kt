package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.kotlin.model.ImpressHolder

data class EventItemModel(var produkId: Int,
                          var rating: Int,
                          var imageUrl: String,
                          var title : String,
                          var location: String,
                          var price: String,
                          var date: String,
                          var isLiked: Boolean,
                          var appUrl: String,
                          var seoURL: String
): ImpressHolder()