package com.tokopedia.salam.umrah.homepage.data
/**
 * @author by firman on 14/10/19
 */

data class UmrahHomepageBottomSheetData (val list:List<UmrohHomepageBottomSheetwithType>)
data class UmrohHomepageBottomSheetwithType(val displayText: String,
                                            val query: String,
                                            val priceMin: Int,
                                            val priceMax: Int,
                                            val type:String,
                                            val index: Int)
