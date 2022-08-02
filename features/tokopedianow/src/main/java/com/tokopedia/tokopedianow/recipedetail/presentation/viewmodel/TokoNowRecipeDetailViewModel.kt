package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.model.MediaItemUiModel
import com.tokopedia.tokopedianow.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.updateProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.LoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.IngredientSectionTitle
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.InstructionSectionTitle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getAddressUseCase: GetChosenAddressWarehouseLocUseCase,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val GET_ADDRESS_SOURCE = "tokonow"

        private const val INVALID_SHOP_ID = 0L
        private const val OOC_WAREHOUSE_ID = 0L
        private const val CHANGE_QUANTITY_DELAY = 500L
    }

    val layoutList: LiveData<Result<List<Visitable<*>>>>
        get() = _layoutList
    val recipeInfo: LiveData<Result<RecipeInfoUiModel>>
        get() = _recipeInfo
    val addItemToCart: LiveData<Result<AddToCartDataModel>>
        get() = _addItemToCart
    val removeCartItem: LiveData<Result<Pair<String, String>>>
        get() = _removeCartItem
    val updateCartItem: LiveData<Result<UpdateCartV2Data>>
        get() = _updateCartItem
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart

    private val _layoutList = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _recipeInfo = MutableLiveData<Result<RecipeInfoUiModel>>()
    private val _addItemToCart = MutableLiveData<Result<AddToCartDataModel>>()
    private val _removeCartItem = MutableLiveData<Result<Pair<String, String>>>()
    private val _updateCartItem = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()

    private var recipeId: String = ""
    private val layoutItemList = mutableListOf<Visitable<*>>()
    private var miniCartData: MiniCartSimplifiedData? = null

    private var changeQuantityJob: Job? = null
    private var getMiniCartJob: Job? = null

    fun checkAddressData() {
        val shopId = addressData.getShopId()

        if (shopId == INVALID_SHOP_ID) {
            getAddress()
        } else {
            getRecipe()
        }
    }

    fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        changeQuantityJob?.cancel()
        launchWithDelay(block = {
            val miniCartItem = getMiniCartItem(productId)
            val cartId = miniCartItem?.cartId.orEmpty()
            val notes = miniCartItem?.notes.orEmpty()

            when {
                miniCartItem == null -> addItemToCart(productId, shopId, quantity)
                quantity.isZero() -> deleteCartItem(productId, cartId)
                else -> updateCartItem(cartId, quantity, notes)
            }
        }, {

        }, CHANGE_QUANTITY_DELAY).let {
            changeQuantityJob = it
        }
    }

    fun deleteCartItem(productId: String, cartId: String) {
        deleteCartUseCase.setParams(cartIdList = listOf(cartId))
        deleteCartUseCase.execute({
            val message = it.data.message.joinToString(separator = ", ")
            val data = Pair(productId, message)
            _removeCartItem.postValue(Success(data))
        }, {
            _removeCartItem.postValue(Fail(it))
        })
    }

    fun getMiniCart() {
        val shopIds = listOf(getShopId())

        if(shouldGetMiniCart(shopIds)) {
            getMiniCartJob?.cancel()
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopIds, MiniCartSource.TokonowRecipe)
                getMiniCartUseCase.execute({
                    val showMiniCart = it.isShowMiniCartWidget
                    val outOfCoverage = addressData.isOutOfCoverage()
                    val data = it.copy(isShowMiniCartWidget = showMiniCart && !outOfCoverage)
                    setProductAddToCartQuantity(it)
                    _miniCart.postValue(Success(data))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            _layoutList.postValue(Success(layoutItemList))
        }) {}
    }

    fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun onAddressChanged() {
        showLoading()
        updateAddressData()
        checkAddressData()
    }

    fun showLoading() {
        layoutItemList.clear()
        layoutItemList.add(LoadingUiModel)
        _layoutList.postValue(Success(layoutItemList))
    }

    fun updateAddressData() = addressData.updateLocalData()

    fun getShopId(): String = addressData.getShopId().toString()

    fun setRecipeId(recipeId: String) {
        this.recipeId = recipeId
    }

    private fun getRecipe() {
        launchCatchError(block = {
            // Temporary Hardcode Data
            val mediaSlider = MediaSliderUiModel(
                items = listOf(
                    MediaItemUiModel(
                        id = "1",
                        url = "https://matamu.net/wp-content/uploads/2020/03/Foto-Landscpae-Gunung-dan-Padang-Rumput.jpg",
                        thumbnailUrl = "https://matamu.net/wp-content/uploads/2020/03/Foto-Landscpae-Gunung-dan-Padang-Rumput.jpg",
                        type = MediaType.IMAGE
                    ),
                    MediaItemUiModel(
                        id = "2",
                        url = "https://store.sirclo.com/blog/wp-content/uploads/2020/06/4.-cara-mencari-modal-usaha.jpg",
                        thumbnailUrl = "https://store.sirclo.com/blog/wp-content/uploads/2020/06/4.-cara-mencari-modal-usaha.jpg",
                        type = MediaType.IMAGE
                    ),
                    MediaItemUiModel(
                        id = "3",
                        url = "https://ecs7.tokopedia.net/assets/media/careers/recruitment-process.mp4",
                        thumbnailUrl = "https://img.freepik.com/premium-vector/meadows-landscape-with-mountains-hill_104785-943.jpg?w=2000",
                        duration = "02:30",
                        type = MediaType.VIDEO
                    )
                )
            )
            val recipeInfo = RecipeInfoUiModel(
                title = "Bubur Kacang Hijau",
                portion = 1,
                duration = 15,
                labels = listOf("Snack", "Santan", "Manis", "Kacang", "Hijau"),
                thumbnail = "https://matamu.net/wp-content/uploads/2020/03/Foto-Landscpae-Gunung-dan-Padang-Rumput.jpg"
            )

            val ingredients = listOf(
                IngredientUiModel("Kacang Hijau Muda, dikupas", 500, "gram"),
                IngredientUiModel("Garam", 1, "sendok teh"),
                IngredientUiModel("Gula pasir", 4, "sendok makan"),
                IngredientUiModel("Pandan Segar", 2, "ikat"),
                IngredientUiModel("Perisa pandan ", 1, "sendok teh"),
                IngredientUiModel("Santan segar, dicairkan", 2, "cup", true)
            )

            val instruction = InstructionUiModel(
                "<ol>\n" +
                    "<li>&nbsp;Beli bahannya</li>\n" +
                    "<li>&nbsp;Persiapkan alatnya</li>\n" +
                    "<li>&nbsp;Dimasak</li>\n" +
                    "<li>&nbsp;Disajikan</li>\n" +
                    "</ol>"
            )

            val products = listOf(
                RecipeProductUiModel(
                    id = "1",
                    shopId = "100",
                    name = "Kacang Hijau Curah",
                    quantity = 0,
                    stock = 5,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://utils.api.stdlib.com/automicon/codeworks.png",
                    slashedPrice = "Rp30.000",
                    discountPercentage = "50%"
                ),
                RecipeProductUiModel(
                    id = "2",
                    shopId = "101",
                    name = "Garam",
                    quantity = 0,
                    stock = 0,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://www.iconsdb.com/icons/preview/guacamole-green/square-xxl.png"
                ),
                RecipeProductUiModel(
                    id = "3",
                    shopId = "102",
                    name = "Gula",
                    quantity = 0,
                    stock = 2,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://www.iconsdb.com/icons/preview/pink/square-xxl.png"
                ),
                RecipeProductUiModel(
                    id = "4",
                    shopId = "103",
                    name = "Daun Salam",
                    quantity = 0,
                    stock = 2,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://www.iconsdb.com/icons/preview/pink/square-xxl.png"
                )
            )

            val totalPrice = "Rp60.000"
            val buyAllProductItem = BuyAllProductUiModel(totalPrice, products)
            val isOutOfCoverage = addressData.isOutOfCoverage()

            val ingredientTabItems = mutableListOf<Visitable<*>>().apply {
                if(isOutOfCoverage) {
                    add(OutOfCoverageUiModel)
                } else {
                    add(buyAllProductItem)
                    addAll(products)
                }
            }

            val instructionTabItems = mutableListOf<Visitable<*>>().apply {
                add(IngredientSectionTitle)
                addAll(ingredients)
                add(InstructionSectionTitle)
                add(instruction)
            }

            val recipeTab = RecipeTabUiModel(
                IngredientTabUiModel(ingredientTabItems),
                InstructionTabUiModel(instructionTabItems)
            )

            layoutItemList.clear()
            layoutItemList.add(mediaSlider)
            layoutItemList.add(recipeInfo)
            layoutItemList.add(recipeTab)

            // Temporary for testing
            val miniCart = MiniCartSimplifiedData(
                miniCartItems = mapOf(
                    MiniCartItemKey(id = "1") to MiniCartItem.MiniCartItemProduct(
                        productId = "1",
                        quantity = 2
                    ),
                    MiniCartItemKey(id = "3") to MiniCartItem.MiniCartItemProduct(
                        productId = "3",
                        quantity = 3
                    )
                )
            )
            setProductAddToCartQuantity(miniCart)
            hideLoading()

            _recipeInfo.postValue(Success(recipeInfo))
            _layoutList.postValue(Success(layoutItemList))
        }) {
            hideLoading()
        }
    }

    private fun getAddress() {
        getAddressUseCase.getStateChosenAddress( {
            addressData.updateAddressData(it)
            checkAddressData()
        },{

        }, GET_ADDRESS_SOURCE)
    }

    private fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _addItemToCart.postValue(Success(it))
        }, {
            _addItemToCart.postValue(Fail(it))
        })
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun updateCartItem(cartId: String, quantity: Int, notes: String) {
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = quantity,
            notes = notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _updateCartItem.postValue(Success(it))
        }, {
            _updateCartItem.postValue(Fail(it))
        })
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        layoutItemList.updateProductQuantity(miniCart)
    }

    private fun setMiniCartData(miniCart: MiniCartSimplifiedData) {
        miniCartData = miniCart
    }

    private fun shouldGetMiniCart(shopId: List<String>): Boolean {
        val warehouseId = addressData.getWarehouseId()
        val outOfCoverage = warehouseId != OOC_WAREHOUSE_ID
        return !shopId.isNullOrEmpty() && outOfCoverage && userSession.isLoggedIn
    }

    private fun hideLoading() {
        layoutItemList.removeFirst { it is LoadingUiModel }
        _layoutList.postValue(Success(layoutItemList))
    }
}