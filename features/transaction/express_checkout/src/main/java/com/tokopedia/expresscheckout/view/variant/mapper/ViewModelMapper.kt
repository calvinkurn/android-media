package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.data.entity.response.atc.Message
import com.tokopedia.expresscheckout.domain.model.atc.*
import com.tokopedia.expresscheckout.domain.model.profile.ProfileModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ViewModelMapper : DataMapper {

    override fun convertToViewModels(atcResponseModel: AtcResponseModel, productData: ProductData): ArrayList<Visitable<*>> {
        var dataList: ArrayList<Visitable<*>> = ArrayList()

        var variantViewModelList = ArrayList<TypeVariantViewModel>()
        var productVariantDataModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels
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

        if (atcResponseModel.atcDataModel?.userProfileModelDefaultModel != null) {
            dataList.add(convertToProfileViewModel(atcResponseModel))
        }
        var productViewModel = convertToProductViewModel(atcResponseModel, variantViewModelList)
        dataList.add(productViewModel)
        dataList.add(convertToQuantityViewModel(atcResponseModel, productViewModel))
        if (variantViewModelList.isNotEmpty()) {
            dataList.addAll(variantViewModelList)
        }
        dataList.add(convertToNoteViewModel(atcResponseModel))
        var summaryViewModel = convertToSummaryViewModel(atcResponseModel)
        if (productData.insurance.insuranceType != InsuranceConstant.INSURANCE_TYPE_NO) {
            dataList.add(convertToInsuranceViewModel(atcResponseModel, productData, summaryViewModel))
        }
        if (atcResponseModel.atcDataModel?.userProfileModelDefaultModel != null) {
            dataList.add(summaryViewModel)
        }

        return dataList
    }

    override fun convertToNoteViewModel(atcResponseModel: AtcResponseModel): NoteViewModel {
        var noteViewModel = NoteViewModel()
        noteViewModel.noteCharMax = atcResponseModel.atcDataModel?.maxCharNote ?: 144
        noteViewModel.note = ""

        return noteViewModel
    }

    override fun convertToProductViewModel(atcResponseModel: AtcResponseModel,
                                           typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel {
        val productModel: ProductModel? = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
        var productViewModel = ProductViewModel()
        productViewModel.productImageUrl = productModel?.productImageSrc200Square ?: ""
        productViewModel.productName = productModel?.productName ?: ""
        productViewModel.minOrderQuantity = productModel?.productMinOrder ?: 0
        if (productModel != null) {
            if (productModel.productInvenageValue > 0) {
                productViewModel.maxOrderQuantity = productModel.productInvenageValue
            } else {
                productViewModel.maxOrderQuantity = atcResponseModel.atcDataModel?.maxQuantity ?: 10000
            }
        }
        productViewModel.productPrice = productModel?.productPrice ?: 0
        var productChildList = ArrayList<ProductChild>()
        var hasSelectedDefaultVariant = false
        if (typeVariantViewModels.size > 0) {
            var childrenModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.childModels
            if (childrenModel != null) {
                for (childModel: ChildModel in childrenModel) {
                    var productChild = ProductChild()
                    productChild.productId = childModel.productId
                    productChild.productName = childModel.name ?: ""
                    productChild.isAvailable = childModel.isBuyable ?: false
                    productChild.productPrice = childModel.price
                    productChild.stockWording = childModel.stockWording ?: ""
                    productChild.minOrder = childModel.minOrder
                    productChild.maxOrder = childModel.maxOrder
                    productChild.optionsId = childModel.optionIds ?: ArrayList()
                    var productVariantDataModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)
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
                            var variantModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productVariantDataModels?.get(0)?.variantModels
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

    override fun convertToProfileViewModel(atcResponseModel: AtcResponseModel): ProfileViewModel {
        val userProfileModel: ProfileModel? = atcResponseModel.atcDataModel?.userProfileModelDefaultModel
        var profileViewModel = ProfileViewModel()
        profileViewModel.addressId = userProfileModel?.addressModel?.addressId ?: 0
        profileViewModel.addressTitle = userProfileModel?.addressModel?.addressName ?: ""
        profileViewModel.addressDetail = userProfileModel?.addressModel?.addressStreet ?: ""
        profileViewModel.paymentOptionImageUrl = userProfileModel?.paymentModel?.image ?: ""
        profileViewModel.paymentDetail = userProfileModel?.paymentModel?.gatewayCode ?: ""
        profileViewModel.shippingDuration = userProfileModel?.shipmentModel?.serviceDuration ?: ""
        profileViewModel.isDefaultProfileCheckboxChecked = false
        profileViewModel.isDurationError = false
        profileViewModel.isEditable = false
        profileViewModel.isSelected = false
        profileViewModel.isShowDefaultProfileCheckBox = false
        profileViewModel.isStateHasChangedProfile = false
        profileViewModel.isStateHasRemovedProfile = false

        return profileViewModel
    }

    override fun convertToQuantityViewModel(atcResponseModel: AtcResponseModel,
                                            productViewModel: ProductViewModel): QuantityViewModel {
        var quantityViewModel = QuantityViewModel()
        var messagesModel = atcResponseModel.atcDataModel?.messagesModel
        quantityViewModel.errorFieldBetween = messagesModel?.get(Message.ERROR_FIELD_BETWEEN) ?: ""
        quantityViewModel.errorFieldMaxChar = messagesModel?.get(Message.ERROR_FIELD_MAX_CHAR) ?: ""
        quantityViewModel.errorFieldRequired = messagesModel?.get(Message.ERROR_FIELD_REQUIRED) ?: ""
        quantityViewModel.errorProductAvailableStock = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK) ?: ""
        quantityViewModel.errorProductAvailableStockDetail = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL) ?: ""
        quantityViewModel.errorProductMaxQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MAX_QUANTITY) ?: ""
        quantityViewModel.errorProductMinQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MIN_QUANTITY) ?: ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = productViewModel.maxOrderQuantity
        quantityViewModel.minOrderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.orderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.stockWording = ""

        return quantityViewModel
    }

    override fun convertToSummaryViewModel(atcResponseModel: AtcResponseModel): SummaryViewModel {
        var summaryViewModel = SummaryViewModel(null)
        summaryViewModel.itemPrice = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productPrice ?: 0

        return summaryViewModel
    }

    override fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel {
        var typeVariantViewModel = TypeVariantViewModel(null)

        var optionVariantViewModels = ArrayList<OptionVariantViewModel>()
        var optionModels = variantModel.optionModels
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

    override fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel {
        var optionVariantViewModel = OptionVariantViewModel(null)
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

    override fun convertToInsuranceViewModel(atcResponseModel: AtcResponseModel,
                                             productData: ProductData,
                                             summaryViewModel: SummaryViewModel): InsuranceViewModel {
        var insuranceViewModel = InsuranceViewModel()
        insuranceViewModel.insuranceLongInfo = productData.insurance.insuranceUsedInfo
        insuranceViewModel.insurancePrice = productData.insurance.insurancePrice
        insuranceViewModel.insuranceType = productData.insurance.insuranceType
        insuranceViewModel.insuranceUsedDefault = productData.insurance.insuranceUsedDefault
        insuranceViewModel.shippingId = productData.shipperId
        insuranceViewModel.spId = productData.shipperProductId
        insuranceViewModel.isChecked =
                productData.insurance.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES ||
                productData.insurance.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST

        summaryViewModel.isUseInsurance = insuranceViewModel.isChecked
        summaryViewModel.shippingPrice = productData.price.price
        summaryViewModel.insurancePrice = productData.insurance.insurancePrice
        summaryViewModel.insuranceInfo = productData.insurance.insuranceUsedInfo

        return insuranceViewModel
    }

}