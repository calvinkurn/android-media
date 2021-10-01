package com.tokopedia.digital_deals.view.viewmodel


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_deals.data.ItemMapResponse
import com.tokopedia.digital_deals.view.model.cart.CartItem
import com.tokopedia.digital_deals.view.model.cart.CartItems
import com.tokopedia.digital_deals.view.model.cart.Configuration
import com.tokopedia.digital_deals.view.model.cart.MetaData
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsCheckoutViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.io) {


      fun verifytoCartItemMapper(itemMap: ItemMapResponse, dealsDetailsResponse: DealsDetailsResponse, promoCode: String = ""): JsonObject {
          val config = Configuration().apply {
              price = itemMap.price
          }

          val metaData = MetaData().apply {
              entityCategoryId = itemMap.categoryId.toInt()
              entityProductId = itemMap.productId.toInt()
              totalTicketCount = itemMap.quantity
              totalTicketPrice = itemMap.totalPrice
              entityStartTime = ""
          }

          val cartItems = arrayListOf<CartItem>()
          val cartItem = CartItem().apply {
               setMetaData(metaData)
               configuration = config
               quantity = itemMap.quantity
               productId = dealsDetailsResponse.catalog.digitalProductId
          }

          cartItems.add(cartItem)

          val cart = CartItems().apply {
              setCartItems(cartItems)
              promocode = promoCode
          }

          val jsonElement = JsonParser().parse(Gson().toJson(cart))
          return jsonElement.asJsonObject
      }
}