package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.Shop
import com.tokopedia.purchase_platform.features.express_checkout.data.constant.MAX_QUANTITY
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.atc.Message
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.*
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetOccCartUseCase @Inject constructor(@ApplicationContext val context: Context, val graphqlUseCase: GraphqlUseCase<GetOccCartGqlResponse>): UseCase<OrderData>() {

    override suspend fun executeOnBackground(): OrderData {
        graphqlUseCase.setTypeClass(GetOccCartGqlResponse::class.java)
        val graphqlRequest = GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_get_occ)
        graphqlUseCase.setGraphqlQuery(graphqlRequest)
        val response = graphqlUseCase.executeOnBackground()
        val orderCart = OrderCart()
        if (response.response.data.cartList.isNotEmpty()) {
            val cart = response.response.data.cartList[0]
            val orderProduct = generateOrderProduct(cart.product)
            orderCart.product = orderProduct
            orderCart.shop = generateOrderShop(cart.shop)
        }
        return OrderData(orderCart, response.response.data.profileResponse)
    }

    private fun generateOrderShop(shop: Shop): OrderShop {
        return OrderShop().apply {
            shopId = shop.shopId
            userId = shop.userId
            shopName = shop.shopName
            shopImage = shop.shopImage
            shopUrl = shop.shopUrl
            shopStatus = shop.shopStatus
            isGold = shop.isGold
            isGoldBadge = shop.isGoldBadge
            isOfficial = shop.isOfficial
            isFreeReturns = shop.isFreeReturns
            addressId = shop.addressId
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            districtId = shop.districtId
            districtName = shop.districtName
            origin = shop.origin
            addressStreet = shop.addressStreet
            provinceId = shop.provinceId
            cityId = shop.cityId
            cityName = shop.cityName
        }
    }

    private fun generateOrderProduct(product: ProductDataResponse): OrderProduct {
        val orderProduct = OrderProduct()
        orderProduct.apply {
            parentId = product.parentId
            productName = product.productName
            productPrice = product.productPrice
            productImageUrl = product.productImage.imageSrc
            maxOrderQuantity = product.productMaxOrder
            minOrderQuantity = product.productMinOrder
            originalPrice = product.productOriginalPrice
        }
        mapVariant(orderProduct, product)
        mapQuantity(orderProduct, product)
        return orderProduct
    }

    private fun mapQuantity(orderProduct: OrderProduct, product: ProductDataResponse) {
        val quantityViewModel = QuantityUiModel()
//        quantityViewModel.errorFieldBetween = messagesModel?.get(Message.ERROR_FIELD_BETWEEN) ?: ""
//        quantityViewModel.errorFieldMaxChar = messagesModel?.get(Message.ERROR_FIELD_MAX_CHAR) ?: ""
//        quantityViewModel.errorFieldRequired = messagesModel?.get(Message.ERROR_FIELD_REQUIRED) ?: ""
//        quantityViewModel.errorProductAvailableStock = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK) ?: ""
//        quantityViewModel.errorProductAvailableStockDetail = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL) ?: ""
//        quantityViewModel.errorProductMaxQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MAX_QUANTITY) ?: ""
//        quantityViewModel.errorProductMinQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MIN_QUANTITY) ?: ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = product.productMaxOrder
        quantityViewModel.minOrderQuantity = product.productMinOrder
        quantityViewModel.orderQuantity = product.productMinOrder
        quantityViewModel.stockWording = ""
        orderProduct.quantity = quantityViewModel
    }

    private fun mapVariant(orderProduct: OrderProduct, product: ProductDataResponse) {

        val variantViewModelList = ArrayList<TypeVariantUiModel>()
        val productVariantDataModels = product.productVariant
        if (productVariantDataModels != null) {
            val variantCombinationValidation = validateVariantCombination(productVariantDataModels)
            val variantChildrenValidation = validateVariantChildren(productVariantDataModels)
            val hasVariant = variantCombinationValidation && variantChildrenValidation
            val children = productVariantDataModels.children
            val variantModels = productVariantDataModels.variant
            if (hasVariant && variantModels.isNotEmpty()) {
                for (variantModel: VariantResponse in variantModels) {
                    if (children != null) {
                        variantViewModelList.add(convertToTypeVariantViewModel(variantModel, children))
                    }
                }
            }
        }

        if (product != null) {
            if (product.productInvenageValue > 0) {
                orderProduct.maxOrderQuantity = product.productInvenageValue
            } else {
                orderProduct.maxOrderQuantity = product.productMaxOrder ?: MAX_QUANTITY
            }
        }
        orderProduct.productPrice = product?.productPrice ?: 0
        val productChildList = ArrayList<OrderProductChild>()
        var hasSelectedDefaultVariant = false
        if (variantViewModelList.size > 0) {
            val childrenModel = product.productVariant.children
            if (childrenModel.isNotEmpty()) {
                for (childModel: ChildResponse in childrenModel) {
                    val productChild = OrderProductChild()
                    productChild.productId = childModel.productId
                    productChild.productName = childModel.name ?: ""
                    productChild.isAvailable = childModel.isBuyable ?: false
                    productChild.productPrice = childModel.price
                    productChild.stockWording = childModel.stockWording ?: ""
                    productChild.stock = childModel.stock
                    productChild.minOrder = childModel.minOrder
                    productChild.maxOrder = childModel.stock
                    productChild.optionsId = childModel.optionIds ?: ArrayList()
                    val productVariantDataModel = product.productVariant
                    if (productVariantDataModel?.defaultChild == childModel.productId &&
                            productChild.isAvailable && !hasSelectedDefaultVariant) {
                        productChild.isSelected = true
                        hasSelectedDefaultVariant = true
                        val defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        val optionIds = childModel.optionIds
                        if (optionIds.isNotEmpty()) {
                            for (optionId: Int in optionIds) {
                                val variantModels = productVariantDataModel.variant
                                if (variantModels.isNotEmpty()) {
                                    for (variantModel: VariantResponse in variantModels) {
                                        val optionModels = variantModel.options
                                        if (optionModels.isNotEmpty()) {
                                            for (optionModel: OptionResponse in optionModels) {
                                                if (optionId == optionModel.id) {
                                                    defaultVariantIdOptionMap[variantModel.productVariantId
                                                            ?: 0] = optionId
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        orderProduct.selectedVariantOptionsIdMap = defaultVariantIdOptionMap
                    } else {
                        productChild.isSelected = false
                    }
                    productChildList.add(productChild)
                }
            }
        }
        val arrayList = ArrayList<VariantUiModel>()
        for (typeVariantUiModel in variantViewModelList) {
            arrayList.add(typeVariantUiModel)
        }
        orderProduct.typeVariantList = arrayList
        orderProduct.productChildrenList = productChildList

        if (productChildList.isNotEmpty()) {
            if (!hasSelectedDefaultVariant) {
                for (productChild: OrderProductChild in orderProduct.productChildrenList) {
                    if (productChild.isAvailable) {
                        productChild.isSelected = true
                        val defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        for (optionId: Int in productChild.optionsId) {
                            val variantModels = product.productVariant.variant
                            if (variantModels.isNotEmpty()) {
                                for (variantModel: VariantResponse in variantModels) {
                                    val optionModels = variantModel.options
                                    if (optionModels.isNotEmpty()) {
                                        for (optionModel: OptionResponse in optionModels) {
                                            if (optionId == optionModel.id) {
                                                defaultVariantIdOptionMap[variantModel.productVariantId
                                                        ?: 0] = optionId
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        orderProduct.selectedVariantOptionsIdMap = defaultVariantIdOptionMap
                        break
                    }
                }
            }
            var firstVariantId = 0
            var firstOptionId = 0
            for ((key, value) in orderProduct.selectedVariantOptionsIdMap) {
                if (firstVariantId == 0 && firstOptionId == 0) {
                    firstVariantId = key
                    firstOptionId = value
                }
            }

            for (variantTypeUiModel: TypeVariantUiModel in variantViewModelList) {
                if (variantTypeUiModel.variantId != firstVariantId) {
                    for (optionUiModel: OptionVariantUiModel in variantTypeUiModel.variantOptions) {

                        // Get other variant type selected option id
                        val otherVariantSelectedOptionIds = ArrayList<Int>()
                        for ((key, value) in orderProduct.selectedVariantOptionsIdMap) {
                            if (key != firstVariantId && key != variantTypeUiModel.variantId) {
                                otherVariantSelectedOptionIds.add(value)
                            }
                        }

                        // Look for available child
                        var hasAvailableChild = false
                        for (productChild: OrderProductChild in orderProduct.productChildrenList) {
                            hasAvailableChild = checkChildAvailable(productChild, optionUiModel.optionId, firstOptionId, otherVariantSelectedOptionIds)
                            if (hasAvailableChild) break
                        }

                        // Set option id state with checking result
                        if (!hasAvailableChild) {
                            optionUiModel.hasAvailableChild = false
                            optionUiModel.currentState == OptionVariantUiModel.STATE_NOT_AVAILABLE
                        } else if (optionUiModel.currentState != OptionVariantUiModel.STATE_SELECTED) {
                            optionUiModel.hasAvailableChild = true
                            optionUiModel.currentState == OptionVariantUiModel.STATE_NOT_SELECTED
                        }
                    }
                }
            }
        }
    }

    private fun checkChildAvailable(productChild: OrderProductChild,
                                    optionViewModelId: Int,
                                    currentChangedOptionId: Int,
                                    otherVariantSelectedOptionIds: ArrayList<Int>): Boolean {

        // Check is child with newly selected option id, other variant selected option ids,
        // and current looping variant option id is available
        var otherSelectedOptionIdCount = 0
        for (optionId: Int in otherVariantSelectedOptionIds) {
            if (optionId in productChild.optionsId) {
                otherSelectedOptionIdCount++
            }
        }

        val otherSelectedOptionIdCountEqual = otherSelectedOptionIdCount == otherVariantSelectedOptionIds.size
        val currentChangedOptionIdAvailable = currentChangedOptionId in productChild.optionsId
        val optionViewModelIdAvailable = optionViewModelId in productChild.optionsId

        return productChild.isAvailable && currentChangedOptionIdAvailable && optionViewModelIdAvailable && otherSelectedOptionIdCountEqual
    }

    private fun validateVariantCombination(productVariantDataModel: ProductVariantResponse): Boolean {
        val variantModels = productVariantDataModel.variant
        if (variantModels.isNotEmpty()) {
            val variantOptionSizeList: ArrayList<Int> = ArrayList()
            for (variantModel: VariantResponse in variantModels) {
                val optionModels = variantModel.options
                variantOptionSizeList.add(optionModels?.size ?: 0)
            }

            var variantCombinationSize = 1
            for (optionSize: Int in variantOptionSizeList) {
                variantCombinationSize *= optionSize
            }

            return variantCombinationSize == productVariantDataModel.children?.size
        }

        return false
    }

    private fun validateVariantChildren(productVariantDataModel: ProductVariantResponse): Boolean {

        val childModels = productVariantDataModel.children
        if (childModels.isNotEmpty()) {
            for (childModel: ChildResponse in childModels) {
                if (childModel.optionIds?.size != productVariantDataModel.variant?.size) {
                    return false
                }
            }

            var hasValidVariant = false
            for (childModel: ChildResponse in childModels) {
                if (childModel.isBuyable) {
                    hasValidVariant = true
                    break
                }
            }

            return hasValidVariant
        }
        return false
    }

    fun convertToTypeVariantViewModel(variantModel: VariantResponse, childrenModel: List<ChildResponse>): TypeVariantUiModel {
        val typeVariantViewModel = TypeVariantUiModel(null)

        val optionVariantViewModels = ArrayList<OptionVariantUiModel>()
        val optionModels = variantModel.options
        if (optionModels != null) {
            for (optionModel: OptionResponse in optionModels) {
                optionVariantViewModels.add(
                        convertToOptionVariantViewModel(optionModel, variantModel.productVariantId
                                ?: 0, childrenModel)
                )
            }
        }
        typeVariantViewModel.variantId = variantModel.productVariantId ?: 0
        typeVariantViewModel.variantOptions = optionVariantViewModels
        typeVariantViewModel.variantName = variantModel.variantName ?: ""

        return typeVariantViewModel
    }

    fun convertToOptionVariantViewModel(optionModel: OptionResponse, variantId: Int, childrenModel: List<ChildResponse>): OptionVariantUiModel {
        val optionVariantViewModel = OptionVariantUiModel(null)
        optionVariantViewModel.variantId = variantId
        optionVariantViewModel.optionId = optionModel.id ?: 0
        optionVariantViewModel.variantHex = optionModel.hex ?: ""
        optionVariantViewModel.variantName = optionModel.value ?: ""

        var hasAvailableChild = false
        for (childModel: ChildResponse in childrenModel) {
            if (childModel.isBuyable && childModel.optionIds?.contains(optionVariantViewModel.optionId)) {
                hasAvailableChild = true
                break
            }
        }
        optionVariantViewModel.hasAvailableChild = hasAvailableChild
        if (!hasAvailableChild) {
            optionVariantViewModel.currentState = OptionVariantUiModel.STATE_NOT_AVAILABLE
        }

        return optionVariantViewModel
    }
}