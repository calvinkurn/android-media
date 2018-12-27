package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.domain.model.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ViewModelMapper : DataMapper {

    override fun convertToViewModels(atcExpressCheckoutModel: AtcExpressCheckoutModel): ArrayList<Visitable<*>> {
        var dataList: ArrayList<Visitable<*>> = ArrayList()

        var variantViewModelList = ArrayList<TypeVariantViewModel>()
        var productVariantDataModels = atcExpressCheckoutModel.dataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels
        if (productVariantDataModels != null && productVariantDataModels.isNotEmpty()) {
            val variantCombinationValidation = validateVariantCombination(productVariantDataModels[0])
            val variantChildrenValidation = validateVariantChildren(productVariantDataModels[0])
            var hasVariant = variantCombinationValidation && variantChildrenValidation
            var children = productVariantDataModels[0].childModels
            var variantModels = productVariantDataModels[0].variantModels
            if (hasVariant && variantModels != null) {
                for (variantModel: VariantModel in variantModels) {
                    if (children != null) {
                        variantViewModelList.add(convertToTypeVariantViewModel(variantModel, children))
                    }
                }
            }
        }

        if (atcExpressCheckoutModel.dataModel?.userProfileModelDefaultModel != null) {
            dataList.add(convertToProfileViewModel(atcExpressCheckoutModel))
        }
        var checkoutVariantProductViewModel = convertToProductViewModel(atcExpressCheckoutModel, variantViewModelList)
        dataList.add(checkoutVariantProductViewModel)
        dataList.add(convertToQuantityViewModel(atcExpressCheckoutModel, checkoutVariantProductViewModel))
        if (variantViewModelList.isNotEmpty()) {
            dataList.addAll(variantViewModelList)
        }
        dataList.add(convertToNoteViewModel(atcExpressCheckoutModel))
        if (atcExpressCheckoutModel.dataModel?.userProfileModelDefaultModel != null) {
            dataList.add(convertToSummaryViewModel(atcExpressCheckoutModel))
        }

        return dataList
    }

    override fun convertToNoteViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): NoteViewModel {
        var checkoutVariantNoteViewModel = NoteViewModel()
        checkoutVariantNoteViewModel.noteCharMax = atcExpressCheckoutModel.dataModel?.maxCharNote ?: 144
        checkoutVariantNoteViewModel.note = ""

        return checkoutVariantNoteViewModel
    }

    override fun convertToProductViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel,
                                           typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel {
        val productModel: ProductModel? = atcExpressCheckoutModel.dataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
        var productViewModel = ProductViewModel()
        productViewModel.productImageUrl = productModel?.productImageSrc200Square ?: ""
        productViewModel.productName = productModel?.productName ?: ""
        productViewModel.minOrderQuantity = productModel?.productMinOrder ?: 0
        if (productModel != null) {
            if (productModel.productInvenageValue > 0) {
                productViewModel.maxOrderQuantity = productModel.productInvenageValue
            } else {
                productViewModel.maxOrderQuantity = atcExpressCheckoutModel.dataModel?.maxQuantity ?: 10000
            }
        }
        productViewModel.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(productModel?.productPrice
                ?: 0, false)
        var productChildList = ArrayList<ProductChild>()
        var hasSelectedDefaultVariant = false
        if (typeVariantViewModels.size > 0) {
            var childrenModel = atcExpressCheckoutModel.dataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.childModels
            if (childrenModel != null) {
                for (childModel: ChildModel in childrenModel) {
                    var productChild = ProductChild()
                    productChild.productId = childModel.productId
                    productChild.productName = childModel.name ?: ""
                    productChild.isAvailable = childModel.isBuyable ?: false
                    productChild.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(childModel.price, false)
                    productChild.stockWording = childModel.stockWording ?: ""
                    productChild.minOrder = childModel.minOrder
                    productChild.maxOrder = childModel.maxOrder
                    productChild.optionsId = childModel.optionIds ?: ArrayList()
                    var productVariantDataModel = atcExpressCheckoutModel.dataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)
                    if (productVariantDataModel?.defaultChild == childModel.productId &&
                            productChild.isAvailable && !hasSelectedDefaultVariant) {
                        productChild.isSelected = true
                        hasSelectedDefaultVariant = true
                        var defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        var optionIds = childModel.optionIds
                        if (optionIds != null) {
                            for (optionId: Int in optionIds) {
                                var variantModels = productVariantDataModel.variantModels
                                if (variantModels != null) {
                                    for (variantModel: VariantModel in variantModels) {
                                        var optionModels = variantModel.optionModels
                                        if (optionModels != null) {
                                            for (optionModel: OptionModel in optionModels) {
                                                if (optionId == optionModel.id) {
                                                    defaultVariantIdOptionMap.put(variantModel.productVariantId
                                                            ?: 0, optionId)
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
                        var defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                        for (optionId: Int in productChild.optionsId) {
                            var variantModels = atcExpressCheckoutModel.dataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.variantModels
                            if (variantModels != null) {
                                for (variantModel: VariantModel in variantModels) {
                                    var optionModels = variantModel.optionModels
                                    if (optionModels != null) {
                                        for (optionModel: OptionModel in optionModels) {
                                            if (optionId == optionModel.id) {
                                                defaultVariantIdOptionMap.put(variantModel.productVariantId
                                                        ?: 0, optionId)
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
                        var otherVariantSelectedOptionIds = ArrayList<Int>()
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

    fun checkChildAvailable(productChild: ProductChild,
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

    override fun convertToProfileViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): ProfileViewModel {
        val userProfileModel: UserProfileModel? = atcExpressCheckoutModel.dataModel?.userProfileModelDefaultModel
        var checkoutVariantProfileViewModel = ProfileViewModel()
        checkoutVariantProfileViewModel.addressTitle = userProfileModel?.receiverName ?: ""
        checkoutVariantProfileViewModel.addressDetail = userProfileModel?.addressStreet ?: ""
        checkoutVariantProfileViewModel.paymentOptionImageUrl = userProfileModel?.image ?: ""
        checkoutVariantProfileViewModel.paymentDetail = userProfileModel?.gatewayCode ?: ""

        return checkoutVariantProfileViewModel
    }

    override fun convertToQuantityViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel,
                                            productViewModel: ProductViewModel): QuantityViewModel {
        var checkoutVariantQuantityViewModel = QuantityViewModel()
        var messagesModel = atcExpressCheckoutModel.dataModel?.messagesModel
        checkoutVariantQuantityViewModel.errorFieldBetween = messagesModel?.errorFieldBetween ?: ""
        checkoutVariantQuantityViewModel.errorFieldMaxChar = messagesModel?.errorFieldMaxChar ?: ""
        checkoutVariantQuantityViewModel.errorFieldRequired = messagesModel?.errorFieldRequired ?: ""
        checkoutVariantQuantityViewModel.errorProductAvailableStock = messagesModel?.errorProductAvailableStock ?: ""
        checkoutVariantQuantityViewModel.errorProductAvailableStockDetail = messagesModel?.errorProductAvailableStockDetail ?: ""
        checkoutVariantQuantityViewModel.errorProductMaxQuantity = messagesModel?.errorProductMaxQuantity ?: ""
        checkoutVariantQuantityViewModel.errorProductMinQuantity = messagesModel?.errorProductMinQuantity ?: ""
        checkoutVariantQuantityViewModel.isStateError = false

        checkoutVariantQuantityViewModel.maxOrderQuantity = productViewModel.maxOrderQuantity
        checkoutVariantQuantityViewModel.minOrderQuantity = productViewModel.minOrderQuantity
        checkoutVariantQuantityViewModel.orderQuantity = productViewModel.minOrderQuantity
        checkoutVariantQuantityViewModel.stockWording = ""

        return checkoutVariantQuantityViewModel
    }

    override fun convertToSummaryViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): SummaryViewModel {
        var checkoutVariantSummaryViewModel = SummaryViewModel(null)

        return checkoutVariantSummaryViewModel
    }

    override fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel {
        var checkoutVariantTypeVariantViewModel = TypeVariantViewModel(null)

        var checkoutVariantOptionVariantViewModels = ArrayList<OptionVariantViewModel>()
        var optionModels = variantModel.optionModels
        if (optionModels != null) {
            for (optionModel: OptionModel in optionModels) {
                checkoutVariantOptionVariantViewModels.add(
                        convertToOptionVariantViewModel(optionModel, variantModel.productVariantId
                                ?: 0, childrenModel)
                )
            }
        }
        checkoutVariantTypeVariantViewModel.variantId = variantModel.productVariantId ?: 0
        checkoutVariantTypeVariantViewModel.variantOptions = checkoutVariantOptionVariantViewModels
        checkoutVariantTypeVariantViewModel.variantName = variantModel.variantName ?: ""

        return checkoutVariantTypeVariantViewModel
    }

    override fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel {
        var checkoutVariantOptionVariantViewModel = OptionVariantViewModel(null)
        checkoutVariantOptionVariantViewModel.variantId = variantId
        checkoutVariantOptionVariantViewModel.optionId = optionModel.id
        checkoutVariantOptionVariantViewModel.variantHex = optionModel.hex ?: ""
        checkoutVariantOptionVariantViewModel.variantName = optionModel.value ?: ""

        var hasAvailableChild = false
        for (childModel: ChildModel in childrenModel) {
            if (childModel.isBuyable == true && childModel.optionIds?.contains(checkoutVariantOptionVariantViewModel.optionId) == true) {
                hasAvailableChild = true
                break
            }
        }
        checkoutVariantOptionVariantViewModel.hasAvailableChild = hasAvailableChild
        if (!hasAvailableChild) {
            checkoutVariantOptionVariantViewModel.currentState = checkoutVariantOptionVariantViewModel.STATE_NOT_AVAILABLE
        }

        return checkoutVariantOptionVariantViewModel
    }

    fun validateVariantCombination(productVariantDataModel: ProductVariantDataModel): Boolean {
        var variantModels = productVariantDataModel.variantModels
        if (variantModels != null) {
            var variantOptionSizeList: ArrayList<Int> = ArrayList()
            for (variantModel: VariantModel in variantModels) {
                var optionModels = variantModel.optionModels
                variantOptionSizeList.add(optionModels?.size ?: 0)
            }

            var variantCombinationSize: Int = 1
            for (optionSize: Int in variantOptionSizeList) {
                variantCombinationSize *= optionSize
            }

            return variantCombinationSize == productVariantDataModel.childModels?.size
        }

        return false
    }

    fun validateVariantChildren(productVariantDataModel: ProductVariantDataModel): Boolean {

        var childModel = productVariantDataModel.childModels
        if (childModel != null) {
            for (childModel: ChildModel in childModel) {
                if (childModel.optionIds?.size != productVariantDataModel.variantModels?.size) {
                    return false
                }
            }

            var hasValidVariant = false
            for (childModel: ChildModel in childModel) {
                if (childModel.isBuyable == true) {
                    hasValidVariant = true
                    break
                }
            }

            return hasValidVariant
        }
        return false
    }

    override fun convertToInsuranceViewModel(): InsuranceViewModel {
        var insuranceViewModel = InsuranceViewModel()

        return insuranceViewModel
    }

}