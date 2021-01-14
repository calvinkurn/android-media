package com.tokopedia.topads.common.data.raw

/**
 * Created by Pika on 26/11/20.
 */

const val GROUP_REQUEST = """query topAdsGetPromo(${'$'}shopID: String!, ${'$'}adID: String!) { 
            topAdsGetPromo(shopID: ${'$'}shopID, adID: ${'$'}adID) {
              data { 
                adID 
                adType 
                groupID 
                shopID 
                itemID 
                status 
                priceBid 
                priceDaily 
                adStartDate 
                adStartTime 
                adEndDate 
                adEndTime 
                adImage 
                adTitle 
              } 
              errors { 
                code 
                detail 
                title 
              } 
             } 
            }""""