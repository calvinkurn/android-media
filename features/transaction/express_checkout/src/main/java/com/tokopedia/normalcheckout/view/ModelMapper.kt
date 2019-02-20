package com.tokopedia.normalcheckout.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.constant.MAX_QUANTITY
import com.tokopedia.expresscheckout.data.entity.response.atc.Message
import com.tokopedia.expresscheckout.domain.model.atc.*
import com.tokopedia.expresscheckout.domain.model.profile.ProfileModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.product.detail.common.data.model.ProductInfo

object ModelMapper {

    fun convertToModels(productInfo: ProductInfo, noteString: String?,
                        quantity: Int = 0): ArrayList<Visitable<*>> {
        val dataList: ArrayList<Visitable<*>> = ArrayList()
        dataList.add(convertToQuantityViewModel(productInfo, quantity))
//        if (variantViewModelList.isNotEmpty()) {
//            dataList.addAll(variantViewModelList)
//        }
        dataList.add(convertToNoteViewModel(noteString))
        return dataList
    }

    fun convertToModels(atcResponseModel: AtcResponseModel, productData: ProductData?): ArrayList<Visitable<*>> {
        val dataList: ArrayList<Visitable<*>> = ArrayList()

        val variantViewModelList = ArrayList<TypeVariantViewModel>()
        val productVariantDataModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels
        if (productVariantDataModels != null && productVariantDataModels.isNotEmpty()) {
            val variantCombinationValidation = validateVariantCombination(productVariantDataModels[0])
            val variantChildrenValidation = validateVariantChildren(productVariantDataModels[0])
            val hasVariant = variantCombinationValidation && variantChildrenValidation
            val children = productVariantDataModels[0].childModels
            val variantModels = productVariantDataModels[0].variantModels
            if (hasVariant && variantModels != null) {
                for (variantModel: VariantModel in variantModels) {
                    if (children != null) {
                        variantViewModelList.add(convertToTypeVariantViewModel(variantModel, children))
                    }
                }
            }
        }
        val productViewModel = convertToProductViewModel(atcResponseModel, variantViewModelList)
        dataList.add(productViewModel)
        dataList.add(convertToQuantityViewModel(atcResponseModel, productViewModel))
        if (variantViewModelList.isNotEmpty()) {
            dataList.addAll(variantViewModelList)
        }
        dataList.add(convertToNoteViewModel(atcResponseModel))
        val summaryViewModel = convertToSummaryViewModel(atcResponseModel)
        dataList.add(summaryViewModel)

        return dataList
    }

    fun convertToNoteViewModel(noteString: String?): NoteViewModel {
        val noteViewModel = NoteViewModel()
        noteViewModel.noteCharMax = 144
        if (noteString != null && noteString.isNotEmpty()) {
            noteViewModel.note = noteString
        }
        return noteViewModel
    }


    fun convertToNoteViewModel(atcResponseModel: AtcResponseModel): NoteViewModel {
        val noteViewModel = NoteViewModel()
        noteViewModel.noteCharMax = atcResponseModel.atcDataModel?.maxCharNote ?: 144
        noteViewModel.note = ""

        return noteViewModel
    }

    fun convertToSummaryViewModel(atcResponseModel: AtcResponseModel): SummaryViewModel {
        val summaryViewModel = SummaryViewModel(null)
        val productModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
        summaryViewModel.itemPrice = productModel?.productQuantity?.times(productModel?.productPrice?.toLong()
                ?: 0)?.toLong() ?: 0

        return summaryViewModel
    }

    fun convertToProductViewModel(atcResponseModel: AtcResponseModel,
                                  typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel {
        val productModel: ProductModel? = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
        val productViewModel = ProductViewModel()
        productViewModel.parentId = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productId
                ?: 0
        productViewModel.productImageUrl = productModel?.productImageSrc200Square ?: ""
        productViewModel.productName = productModel?.productName ?: ""
        productViewModel.minOrderQuantity = productModel?.productMinOrder ?: 0
        if (productModel != null) {
            if (productModel.productInvenageValue > 0) {
                productViewModel.maxOrderQuantity = productModel.productInvenageValue
            } else {
                productViewModel.maxOrderQuantity = atcResponseModel.atcDataModel?.maxQuantity
                        ?: MAX_QUANTITY
            }
        }
        productViewModel.productPrice = productModel?.productPrice ?: 0
        val productChildList = ArrayList<ProductChild>()
        var hasSelectedDefaultVariant = false
        if (typeVariantViewModels.size > 0) {
            val childrenModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.childModels
            if (childrenModel != null) {
                for (childModel: ChildModel in childrenModel) {
                    val productChild = ProductChild()
                    productChild.productId = childModel.productId
                    productChild.productName = childModel.name ?: ""
                    productChild.isAvailable = childModel.isBuyable ?: false
                    productChild.productPrice = childModel.price
                    productChild.stockWording = childModel.stockWording ?: ""
                    productChild.stock = childModel.stock
                    productChild.minOrder = childModel.minOrder
                    productChild.maxOrder = if (childModel.maxOrder != 0) childModel.maxOrder else childModel.stock
                    productChild.optionsId = childModel.optionIds ?: ArrayList()
                    val productVariantDataModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)
                    if (productVariantDataModel?.defaultChild == childModel.productId &&
                            productChild.isAvailable && !hasSelectedDefaultVariant) {
                        productChild.isSelected = true
                        hasSelectedDefaultVariant = true
                        val defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        val optionIds = childModel.optionIds
                        if (optionIds != null) {
                            for (optionId: Int in optionIds) {
                                val variantModels = productVariantDataModel.variantModels
                                if (variantModels != null) {
                                    for (variantModel: VariantModel in variantModels) {
                                        val optionModels = variantModel.optionModels
                                        if (optionModels != null) {
                                            for (optionModel: OptionModel in optionModels) {
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
                        productViewModel.selectedVariantOptionsIdMap = defaultVariantIdOptionMap
                    } else {
                        productChild.isSelected = false
                    }
                    productChildList.add(productChild)
                }
            }
        }
        productViewModel.productChildrenList = productChildList

        if (productChildList.isNotEmpty()) {
            if (!hasSelectedDefaultVariant) {
                for (productChild: ProductChild in productViewModel.productChildrenList) {
                    if (productChild.isAvailable) {
                        productChild.isSelected = true
                        val defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        for (optionId: Int in productChild.optionsId) {
                            val variantModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.variantModels
                            if (variantModels != null) {
                                for (variantModel: VariantModel in variantModels) {
                                    val optionModels = variantModel.optionModels
                                    if (optionModels != null) {
                                        for (optionModel: OptionModel in optionModels) {
                                            if (optionId == optionModel.id) {
                                                defaultVariantIdOptionMap[variantModel.productVariantId
                                                        ?: 0] = optionId
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        productViewModel.selectedVariantOptionsIdMap = defaultVariantIdOptionMap
                        break
                    }
                }
            }
            var firstVariantId = 0
            var firstOptionId = 0
            for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
                if (firstVariantId == 0 && firstOptionId == 0) {
                    firstVariantId = key
                    firstOptionId = value
                }
            }

            for (variantTypeViewModel: TypeVariantViewModel in typeVariantViewModels) {
                if (variantTypeViewModel.variantId != firstVariantId) {
                    for (optionViewModel: OptionVariantViewModel in variantTypeViewModel.variantOptions) {

                        // Get other variant type selected option id
                        val otherVariantSelectedOptionIds = ArrayList<Int>()
                        for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
                            if (key != firstVariantId && key != variantTypeViewModel.variantId) {
                                otherVariantSelectedOptionIds.add(value)
                            }
                        }

                        // Look for available child
                        var hasAvailableChild = false
                        for (productChild: ProductChild in productViewModel.productChildrenList) {
                            hasAvailableChild = checkChildAvailable(productChild, optionViewModel.optionId, firstOptionId, otherVariantSelectedOptionIds)
                            if (hasAvailableChild) break
                        }

                        // Set option id state with checking result
                        if (!hasAvailableChild) {
                            optionViewModel.hasAvailableChild = false
                            optionViewModel.currentState == optionViewModel.STATE_NOT_AVAILABLE
                        } else if (optionViewModel.currentState != optionViewModel.STATE_SELECTED) {
                            optionViewModel.hasAvailableChild = true
                            optionViewModel.currentState == optionViewModel.STATE_NOT_SELECTED
                        }
                    }
                }
            }
        }

        return productViewModel
    }

    private fun checkChildAvailable(productChild: ProductChild,
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

    fun convertToProfileViewModel(atcResponseModel: AtcResponseModel): ProfileViewModel {
        val userProfileModel: ProfileModel? = atcResponseModel.atcDataModel?.userProfileModelDefaultModel
        val profileViewModel = ProfileViewModel()
        profileViewModel.profileId = userProfileModel?.id ?: 0
        profileViewModel.addressId = userProfileModel?.addressModel?.addressId ?: 0
        profileViewModel.districtName = userProfileModel?.addressModel?.districtName ?: ""
        profileViewModel.cityName = userProfileModel?.addressModel?.cityName ?: ""
        profileViewModel.addressTitle = userProfileModel?.addressModel?.addressName ?: ""
        profileViewModel.addressDetail = userProfileModel?.addressModel?.addressStreet ?: ""
        profileViewModel.paymentOptionImageUrl = userProfileModel?.paymentModel?.image ?: ""
        profileViewModel.paymentDetail = userProfileModel?.paymentModel?.gatewayCode ?: ""
        profileViewModel.shippingDuration = userProfileModel?.shipmentModel?.serviceDuration ?: ""
        profileViewModel.shippingDurationId = userProfileModel?.shipmentModel?.serviceId ?: 0
        profileViewModel.isDefaultProfileCheckboxChecked = false
        profileViewModel.isDurationError = false
        profileViewModel.isCourierError = false
        profileViewModel.isEditable = false
        profileViewModel.isSelected = true
        profileViewModel.isShowDefaultProfileCheckBox = false
        profileViewModel.isStateHasChangedProfile = false
        profileViewModel.isStateHasRemovedProfile = false

        return profileViewModel
    }

    fun convertToQuantityViewModel(atcResponseModel: AtcResponseModel,
                                   productViewModel: ProductViewModel): QuantityViewModel {
        val quantityViewModel = QuantityViewModel()
        val messagesModel = atcResponseModel.atcDataModel?.messagesModel
        quantityViewModel.errorFieldBetween = messagesModel?.get(Message.ERROR_FIELD_BETWEEN) ?: ""
        quantityViewModel.errorFieldMaxChar = messagesModel?.get(Message.ERROR_FIELD_MAX_CHAR) ?: ""
        quantityViewModel.errorFieldRequired = messagesModel?.get(Message.ERROR_FIELD_REQUIRED)
                ?: ""
        quantityViewModel.errorProductAvailableStock = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK)
                ?: ""
        quantityViewModel.errorProductAvailableStockDetail = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL)
                ?: ""
        quantityViewModel.errorProductMaxQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MAX_QUANTITY)
                ?: ""
        quantityViewModel.errorProductMinQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MIN_QUANTITY)
                ?: ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = productViewModel.maxOrderQuantity
        quantityViewModel.minOrderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.orderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.stockWording = ""

        return quantityViewModel
    }

    fun convertToQuantityViewModel(productInfo: ProductInfo, quantity: Int = 0): QuantityViewModel {
        val quantityViewModel = QuantityViewModel()
        quantityViewModel.errorProductMaxQuantity = ""
        quantityViewModel.errorProductMinQuantity = ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = productInfo.basic.maxOrder
        quantityViewModel.minOrderQuantity = productInfo.basic.minOrder
        quantityViewModel.orderQuantity = if (quantity > 0) quantity else productInfo.basic.minOrder
        quantityViewModel.stockWording = ""

        return quantityViewModel
    }

    fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel {
        val typeVariantViewModel = TypeVariantViewModel(null)

        val optionVariantViewModels = ArrayList<OptionVariantViewModel>()
        val optionModels = variantModel.optionModels
        if (optionModels != null) {
            for (optionModel: OptionModel in optionModels) {
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

    fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel {
        val optionVariantViewModel = OptionVariantViewModel(null)
        optionVariantViewModel.variantId = variantId
        optionVariantViewModel.optionId = optionModel.id
        optionVariantViewModel.variantHex = optionModel.hex ?: ""
        optionVariantViewModel.variantName = optionModel.value ?: ""

        var hasAvailableChild = false
        for (childModel: ChildModel in childrenModel) {
            if (childModel.isBuyable == true && childModel.optionIds?.contains(optionVariantViewModel.optionId) == true) {
                hasAvailableChild = true
                break
            }
        }
        optionVariantViewModel.hasAvailableChild = hasAvailableChild
        if (!hasAvailableChild) {
            optionVariantViewModel.currentState = optionVariantViewModel.STATE_NOT_AVAILABLE
        }

        return optionVariantViewModel
    }

    private fun validateVariantCombination(productVariantDataModel: ProductVariantDataModel): Boolean {
        val variantModels = productVariantDataModel.variantModels
        if (variantModels != null) {
            val variantOptionSizeList: ArrayList<Int> = ArrayList()
            for (variantModel: VariantModel in variantModels) {
                val optionModels = variantModel.optionModels
                variantOptionSizeList.add(optionModels?.size ?: 0)
            }

            var variantCombinationSize = 1
            for (optionSize: Int in variantOptionSizeList) {
                variantCombinationSize *= optionSize
            }

            return variantCombinationSize == productVariantDataModel.childModels?.size
        }

        return false
    }

    private fun validateVariantChildren(productVariantDataModel: ProductVariantDataModel): Boolean {

        val childModels = productVariantDataModel.childModels
        if (childModels != null) {
            for (childModel: ChildModel in childModels) {
                if (childModel.optionIds?.size != productVariantDataModel.variantModels?.size) {
                    return false
                }
            }

            var hasValidVariant = false
            for (childModel: ChildModel in childModels) {
                if (childModel.isBuyable == true) {
                    hasValidVariant = true
                    break
                }
            }

            return hasValidVariant
        }
        return false
    }

}