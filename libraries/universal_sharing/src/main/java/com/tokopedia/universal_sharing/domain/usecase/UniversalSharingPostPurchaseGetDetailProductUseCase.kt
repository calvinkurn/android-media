package com.tokopedia.universal_sharing.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.universal_sharing.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalSharingPostPurchaseGetDetailProductUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) {

    private val productDetailFlow = MutableStateFlow<Result<String>>(
        Result.Loading
    )

    private fun graphqlQuery(): String = """
        query getProductV3(
          $$PRODUCT_ID: String!,
          $$OPTIONS: OptionV3!
        ) {     
          getProductV3(
            productID:$$PRODUCT_ID, 
            options:$$OPTIONS, 
            extraInfo: { aggregate: true }
          ) {         
            productID 
            productName 
            status 
            stock 
            priceCurrency 
            price 
            lastUpdatePrice 
            minOrder 
            maxOrder 
            description 
            weightUnit 
            weight 
            condition 
            mustInsurance 
            sku 
            hasDTStock 
            category{   
              id   
              name   
              title   
              isAdult   
              breadcrumbURL   
              detail{     
                id     
                name     
                breadcrumbURL     
                isAdult 
              }
            } 
            menus 
            pictures{   
              picID   
              description   
              filePath   
              fileName   
              width   
              height   
              urlOriginal   
              urlThumbnail   
              url300   
              status 
            } 
            position{   
              position   
              isSwap 
            } 
            preorder{   
              duration   
              timeUnit   
              isActive 
            } 
            shop {
              id
              name
              domain
              url
            } 
            wholesale{   
              minQty   
              price 
            } 
            cpl{   
              shipperServices
            } 
            campaign{   
              campaignID   
              campaignType  
              campaignTypeName 
              percentageAmount 
              originalPrice 
              discountedPrice  
              originalStock
              isActive 
            } 
            video{
              source 
              url 
            } 
            cashback {
              percentage
            } 
            txStats{ 
              itemSold
            } 
            variant{  
              products{
                productID
                status
                combination
                isPrimary
                price
                sku
                stock
                weight
                weightUnit
                hasDTStock
                isCampaign
                pictures{ 
                  picID 
                  description 
                  filePath 
                  fileName 
                  width   
                  height  
                  isFromIG
                  urlOriginal 
                  urlThumbnail  
                  url300
                  status
                }   
              }   
              selections{
                variantID 
                variantName
                unitName
                unitID
                unitName
                identifier
                options{ 
                  unitValueID
                  value
                  hexCode
                }   
              }   
              sizecharts{ 
                picID    
                description
                filePath  
                fileName   
                width   
                height    
                urlOriginal 
                urlThumbnail 
                url300   
                status   
              } 
            }      
          }     
        }
    """.trimIndent()

    fun observe() = productDetailFlow.asStateFlow()

    suspend fun getDetailProduct(productId: String) {
        withContext(dispatcher.io) {
            productDetailFlow.emit(Result.Loading)
            try {
                val params = generateParam(productId)
                val response = repository.request<Map<String, Any>, String>(
                    graphqlQuery(),
                    params
                )
                productDetailFlow.emit(Result.Success(response))
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                productDetailFlow.emit(Result.Error(throwable))
            }
        }
    }

    private fun generateParam(productId: String): Map<String, Any> {
        val optionMap = mutableMapOf(
            "basic" to true,
            "picture" to true,
            "variant" to true,
            "shop" to true,
            "campaign" to true
        )
        return mapOf(
            PRODUCT_ID to productId,
            OPTIONS to optionMap
        )
    }

    companion object {
        const val PRODUCT_ID = "productID"
        const val OPTIONS = "options"
    }
}
