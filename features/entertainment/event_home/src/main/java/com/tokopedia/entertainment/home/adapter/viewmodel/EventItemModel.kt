package com.tokopedia.entertainment.home.adapter.viewmodel

data class EventItemModel(var produkId: String,
                          var rating: String,
                          var imageUrl: String,
                          var title : String,
                          var location: String,
                          var price: String,
                          var date: String,
                          var isLiked: Boolean,
                          var appUrl: String)