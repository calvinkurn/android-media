package com.tokopedia.tokopedianow.recipedetail.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.model.MediaItemUiModel
import com.tokopedia.tokopedianow.common.util.NumberFormatter.formatFloatToString
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeProductResponse
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.IngredientSectionTitle
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.InstructionSectionTitle

object RecipeDetailMapper {

    private const val MAX_LABEL_COUNT = 3
    private const val TAKE_LABEL_COUNT = 4
    private const val PRODUCT_DEFAULT_QTY = 0
    private const val DEFAULT_ID = 1

    private const val MEDIA_TYPE_IMAGE = "Image"

    fun MutableList<Visitable<*>>.updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        val miniCartItems = miniCart.miniCartItems

        find { it is RecipeTabUiModel }?.let { item ->
            var recipeTab = item as RecipeTabUiModel
            var ingredientTab = recipeTab.ingredient

            val itemIndex = indexOf(recipeTab)
            val itemList = ingredientTab.items.toMutableList()
            val productList = itemList.filterIsInstance<RecipeProductUiModel>()

            miniCartItems.filter { it.value is MiniCartItem.MiniCartItemProduct }
                .forEach { miniCartItem ->
                    val miniCartProduct = miniCartItem.value as MiniCartItem.MiniCartItemProduct
                    productList.firstOrNull { it.id == miniCartProduct.productId }?.let {
                        val product = it.copy(quantity = miniCartProduct.quantity)
                        val index = itemList.indexOf(it)
                        itemList[index] = product
                    }
                }

            ingredientTab = ingredientTab.copy(items = itemList)
            recipeTab = recipeTab.copy(ingredient = ingredientTab)
            set(itemIndex, recipeTab)
        }
    }

    fun MutableList<Visitable<*>>.updateDeletedProductQuantity(miniCart: MiniCartSimplifiedData) {
        val miniCartProductIds = miniCart.miniCartItems
            .filter { it.value is MiniCartItem.MiniCartItemProduct }
            .map {
                val cartItem = it.value as MiniCartItem.MiniCartItemProduct
                cartItem.productId
            }

        find { it is RecipeTabUiModel }?.let { item ->
            var recipeTab = item as RecipeTabUiModel
            var ingredientTab = recipeTab.ingredient

            val itemIndex = indexOf(recipeTab)
            val itemList = ingredientTab.items.toMutableList()
            val productList = itemList.filterIsInstance<RecipeProductUiModel>()

            productList.forEach {
                if(it.id !in miniCartProductIds) {
                    val product = it.copy(quantity = PRODUCT_DEFAULT_QTY)
                    val index = itemList.indexOf(it)
                    itemList[index] = product
                }
            }

            ingredientTab = ingredientTab.copy(items = itemList)
            recipeTab = recipeTab.copy(ingredient = ingredientTab)
            set(itemIndex, recipeTab)
        }
    }

    fun MutableList<Visitable<*>>.removeLoadingItem() {
        removeFirst { it is RecipeDetailLoadingUiModel }
    }

    fun mapToMediaSlider(response: RecipeResponse): MediaSliderUiModel {
        val mediaItems = response.medias.mapIndexed { index, media ->
            MediaItemUiModel(
                url = media.url,
                thumbnailUrl = media.url,
                type = if(media.type == MEDIA_TYPE_IMAGE) {
                    MediaType.IMAGE
                } else {
                    MediaType.VIDEO
                },
                position = index + 1
            )
        }
        return MediaSliderUiModel(mediaItems)
    }

    fun mapToRecipeInfo(response: RecipeResponse): RecipeInfoUiModel {
        val thumbnail = response.images.first().urlOriginal
        val imageUrls = response.images.map { it.urlOriginal }
        val tags = response.tags.take(TAKE_LABEL_COUNT).mapIndexed { index, tag ->
            val position = index + 1
            if (position > MAX_LABEL_COUNT) {
                val otherLabelCount = (response.tags.count() - MAX_LABEL_COUNT).toString()
                TagUiModel(
                    tag = otherLabelCount,
                    shouldFormatTag = true,
                    shouldUseStaticBackgroundColor = false
                )
            } else {
                TagUiModel(
                    tag = tag.name,
                    shouldFormatTag = false,
                    shouldUseStaticBackgroundColor = false
                )
            }
        }

        return RecipeInfoUiModel(
            id = response.id,
            title = response.title,
            portion = response.portion,
            duration = response.duration.orZero(),
            tags = tags,
            thumbnail = thumbnail,
            imageUrls = imageUrls,
            shareUrl = response.url
        )
    }

    fun mapToRecipeTab(
        response: RecipeResponse,
        addressData: TokoNowLocalAddress
    ): RecipeTabUiModel {
        val isOutOfCoverage = addressData.isOutOfCoverage()
        val ingredientsResponse = response.ingredients

        val ingredients = ingredientsResponse.mapIndexed { index, ingredient ->
            val isLastItem = index == ingredientsResponse.count() - 1
            IngredientUiModel(
                ingredient.name,
                formatFloatToString(ingredient.quantity),
                ingredient.unit,
                isLastItem
            )
        }

        val instruction = InstructionUiModel(response.instruction)

        val products = response.products.mapIndexed { index, product ->
            mapToProductUiModel(index, product)
        }

        val ingredientTabItems = mutableListOf<Visitable<*>>().apply {
            if(isOutOfCoverage) {
                add(OutOfCoverageUiModel)
            } else {
                addAll(products)
            }
        }

        val instructionTabItems = mutableListOf<Visitable<*>>().apply {
            add(IngredientSectionTitle)
            addAll(ingredients)
            add(InstructionSectionTitle)
            add(instruction)
        }

        return RecipeTabUiModel(
            DEFAULT_ID,
            IngredientTabUiModel(ingredientTabItems),
            InstructionTabUiModel(instructionTabItems)
        )
    }

    private fun mapToProductUiModel(index: Int, product: RecipeProductResponse): RecipeProductUiModel {
        val position = index + 1
        val detail = product.detail

        val similarProducts = product.similarProducts?.mapIndexed { idx, similarProduct ->
            mapToProductUiModel(idx, similarProduct)
        }.orEmpty()

        return RecipeProductUiModel(
            id = product.id,
            shopId = detail.shopID,
            name = detail.name,
            stock = detail.stock,
            minOrder = detail.minOrder,
            maxOrder = detail.maxOrder,
            priceFmt = detail.fmtPrice,
            imageUrl = detail.imageUrl,
            slashedPrice = detail.slashedPrice,
            discountPercentage = formatFloatToString(detail.discountPercentage),
            categoryId = detail.categoryID,
            categoryName = detail.categoryName,
            similarProducts = similarProducts,
            position = position
        )
    }
}
